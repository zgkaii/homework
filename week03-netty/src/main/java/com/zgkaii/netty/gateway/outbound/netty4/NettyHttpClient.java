package com.zgkaii.netty.gateway.outbound.netty4;//package io.github.kimmking.gateway.outbound;

import com.zgkaii.netty.gateway.outbound.OutboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * 使用netty实现客户端
 */

/**
 * 使用Netty实现客户端
 *
 * @author monica
 */
public class NettyHttpClient implements OutboundHandler {

    private final static Logger logger = LoggerFactory.getLogger(NettyHttpClient.class);

    /**
     * 客户端要请求的url
     */
    private String backendUrl;

    /**
     * 来自浏览器的请求HttpInboundServer建立的Channel
     */
    private Channel serverChannel;

    /**
     * 网关内部的客户端和后端服务建立的Channel
     */
    private Channel clientChannel;

    public NettyHttpClient(String backendUrl) {
        this.backendUrl = backendUrl;
        String[] urlArray = backendUrl.split(":");
        connect(urlArray[0], Integer.parseInt(urlArray[1]));
    }

    /**
     * 初始化客户端后，连接上给定的后端服务
     *
     * @param host
     * @param port
     */
    private void connect(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                            pipeline.addLast(new HttpResponseDecoder());
                            // 客户端发送的是httpRequest，所以要使用HttpRequestEncoder进行编码
                            pipeline.addLast(new HttpRequestEncoder());
                            pipeline.addLast(new NettyHttpClientOutboundHandler());
                        }
                    });

            // start the client
            ChannelFuture f = b.connect(host, port).sync();
            logger.info("客户端已连接 {}:{}", host, port);
            this.clientChannel = f.channel();

            this.clientChannel.closeFuture().sync();
        } catch (Exception e) {
            logger.error("connect {} encountered exception", this.backendUrl, e);
        } finally {
            group.shutdownGracefully();
        }
    }

    @Override
    public void handle(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        try {
            this.serverChannel = ctx.channel();

            // 转发请求到后端服务
            URI uri = new URI(fullRequest.uri());
            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, uri.toASCIIString());
            request.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            request.headers().add(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
            this.clientChannel.writeAndFlush(request);


        } catch (Exception e) {
            logger.error("send request encountered exception, url:{}", fullRequest.uri(), e);
        }
    }
//
//    public static void main(String[] args) throws Exception {
//        NettyHttpClient client = new NettyHttpClient();
//        client.connect("127.0.0.1", 8844);
//    }
}