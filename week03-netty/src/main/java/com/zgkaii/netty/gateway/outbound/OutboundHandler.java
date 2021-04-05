package com.zgkaii.netty.gateway.outbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/05 22:38
 * @Description: 不同的客户端实现该接口
 **/
public interface OutboundHandler {
    void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx);
}
