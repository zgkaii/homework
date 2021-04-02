package com.zgkaii.nio.demo.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.util.ReferenceCountUtil;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/03/26 16:37
 * @Description:
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 通知ChannelInboundHandler最后一次对channelRead()的调用是当前批量读取中的最后一条消息
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 对于每个传入的消息都要调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        long startTime = System.currentTimeMillis();
        try {
            //logger.info("channelRead流量接口请求开始，时间为{}", startTime);
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            String uri = fullRequest.uri();
            //logger.info("接收到的请求url为{}", uri);
//            handlerBody(fullRequest, ctx);
            if (uri.contains("/test")) {
                handlerTest(fullRequest, ctx, "Hello,Mr.Z!");
            } else {
                handlerTest(fullRequest, ctx, "Hello,Everyone!");
            }
        } finally {
            // 如果extends SimpleChannelInboundHandler 就不需要显示地release
            ReferenceCountUtil.release(msg);
        }
        // logger.info("channelRead流量接口请求结束，时间为{}", System.currentTimeMillis() - startTime);
    }

    private void handlerTest(FullHttpRequest fullRequest, ChannelHandlerContext ctx, String body) {
        // long startTime = System.currentTimeMillis();
        FullHttpResponse response = null;
        try {
            String value = body;

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());

        } catch (Exception e) {
            System.out.println("处理测试接口出错" + e.getMessage());
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (fullRequest != null) {
                if (!HttpHeaderUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
        }
    }

    /**
     * —在读取操作期间，有异常抛出时会调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 作业1
     */
    private void handlerBody(FullHttpRequest fullRequest, ChannelHandlerContext ctx) throws IOException {
        //long startTime = System.currentTimeMillis();
        FullHttpResponse response = null;
        try {
            // 调用HttpClientHelper 获取value值
            String value = HttpClientHelper.getAsString("http://localhost:8800");
            System.out.println("response: " + value);

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());

        } catch (Exception e) {
            System.out.println("处理测试接口出错" + e.getMessage());
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (fullRequest != null) {
                if (!HttpHeaderUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
        }
    }
}