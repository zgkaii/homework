package com.zgkaii.netty.gateway.outbound.okhttp;

import com.zgkaii.netty.gateway.outbound.OutboundHandler;
import com.zgkaii.netty.gateway.outbound.httpclient4.NamedThreadFactory;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import okhttp3.*;
import org.apache.commons.codec.binary.StringUtils;

import java.io.IOException;
import java.util.concurrent.*;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 使用okhttp实现OutboundHandler
 */
public class OkhttpOutboundHandler implements OutboundHandler {
    /**
     * 使用的客户端
     */
    private OkHttpClient client;

    /**
     * 使用线程池请求
     */
    private ExecutorService proxyService;

    /**
     * 客户端要请求的url
     */
    private String backendUrl;

    public OkhttpOutboundHandler(String backendUrl) {
        this(2000, backendUrl);
    }

    public OkhttpOutboundHandler(long timeout, String backendUrl) {
        this.client = buildClient(timeout);
        this.backendUrl = backendUrl;

        long keepAliveTime = 1000;
        int queueSize = 2048;
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        this.proxyService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);
    }

    @Override
    public void handle(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        final String url = this.backendUrl + fullRequest.uri();
        proxyService.submit(() -> doRequest(fullRequest, ctx, url));
    }

    /**
     * 处理请求
     */
    public void doRequest(FullHttpRequest inbound, ChannelHandlerContext ctx, String url) {
        Request request = buildRequest(url);
        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("client request failed" + e);
                }

                @Override
                public void onResponse(Call call, Response endpointResponse) throws IOException {
                    try {
                        handleResponse(inbound, ctx, endpointResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
        } catch (Exception e) {
            System.out.println("client do request encountered error" + e);
        }
    }

    /**
     * 处理服务端的响应
     */
    private void handleResponse(final FullHttpRequest fullRequest,
                                final ChannelHandlerContext ctx,
                                final Response endpointResponse) throws Exception {
        FullHttpResponse response = null;
        try {
            // 处理body
            ResponseBody responseBody = endpointResponse.body();
            if (responseBody == null) {
                System.out.println("request failed, responseBody is null, codec: " + endpointResponse.code());
            }
            String responseBodyStr = responseBody == null ? "" : responseBody.string();
            Long contentLength = responseBody == null ? 0 : responseBody.contentLength();
            byte[] body = StringUtils.getBytesUtf8(responseBodyStr);

            // 封装响应数据包，正常响应返回200，非正常均返回302
            if (endpointResponse.code() == OK.code()) {
                response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
                response.headers().set("Content-Type", "application/json");
                response.headers().setInt("Content-Length", contentLength.intValue());
            } else {
                response = new DefaultFullHttpResponse(HTTP_1_1, FOUND, Unpooled.wrappedBuffer(body));
                response.headers().set("Content-Type", "application/json");
                response.headers().setInt("Content-Length", contentLength.intValue());
                System.out.println("request failed, response code: " + endpointResponse.code());
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(response);
                }
            }
            ctx.flush();
        }
    }

    /**
     * 创建客户端
     */
    private OkHttpClient buildClient(long timeout) {
        return new OkHttpClient().newBuilder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * 创建请求体
     */
    private Request buildRequest(String url) {
        return new Request.Builder().url(url).get().build();
    }
}
