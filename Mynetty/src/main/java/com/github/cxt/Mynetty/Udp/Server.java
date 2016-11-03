package com.github.cxt.Mynetty.Udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;


public class Server {

    // 相比于TCP而言，UDP不存在客户端和服务端的实际链接，因此
    // 不需要为连接(ChannelPipeline)设置handler
    public void run(int port)throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(new ServerHandler());

            b.bind(port).sync().channel().closeFuture().await();
        }
        finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        new Server().run(11111);
    }
}
