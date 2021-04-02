package com.zgkaii.nio.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;

/**
 * @Author: Mr.Z
 * @DateTime: 2021/04/01 16:51
 * @Description:
 */
public class NettyHttpClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8804"));

    public static void main(String[] args) throws Exception {
        // 1.创建一个 NioEventLoopGroup 对象实例
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 2.创建客户端启动引导类
            Bootstrap b = new Bootstrap();
            // 3.指定线程组
            b.group(group)
                    // 4.指定 IO 模型
                    .channel(NioSocketChannel.class)
                    // 5.在创建Channel时，向ChannelPipeline中添加一个 EchoClientHandler 实例
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            // 添加解码器
                            p.addLast("codec", new HttpClientCodec());
                            p.addLast("decompressor",
                                    new HttpContentDecompressor());
                            p.addLast(new EchoClientHandler());
                        }
                    });
            // 6.连接到远程节点，阻塞等待直到连接完成
            ChannelFuture f = b.connect(HOST, PORT).sync();
            // 7.阻塞，直到Channel 关闭
            f.channel().closeFuture();
            System.out.println(f.isSuccess());
        } finally {
            // 8.关闭线程池并且释放所有的资源
            group.shutdownGracefully();
        }
    }
}
