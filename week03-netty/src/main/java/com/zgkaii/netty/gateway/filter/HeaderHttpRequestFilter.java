package com.zgkaii.netty.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class HeaderHttpRequestFilter implements HttpRequestFilter {
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        // 在请求的头里面添加author属性，Mr.Z
        fullRequest.headers().set("Author", "Mr.Z");
    }
}
