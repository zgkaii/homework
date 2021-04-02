package com.zgkaii.nio.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/02 00:26
 * @Description: 作业2
 **/
@ChannelHandler.Sharable// 标示一个ChannelHandler 可以被多个 Channel 安全地共享
public class EchoClientHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        FullHttpResponse response = (FullHttpResponse) msg;
        ByteBuf in = response.content();
        System.out.println("Received: " + in.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * —在读取操作期间，有异常抛出时会调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
