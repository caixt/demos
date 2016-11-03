package com.github.cxt.Mynetty.Udp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2016/1/21 0021.
 */
public class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private static final  String[] DICTIONARY={
            "hello world_1",
            "hello world_2",
            "hello world_3",
            "hello world_4",
            "hello world_5",
    };

    private String nextQuoto(){
        // 线程安全的随机类：ThreadLocalRandom
        int quoteId = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
        return DICTIONARY[quoteId];
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext,
                                   DatagramPacket msg) throws Exception {
        // 因为Netty对UDP进行了封装，所以接收到的是DatagramPacket对象。
        String req = msg.content().toString(CharsetUtil.UTF_8);
        System.out.println(req);

        if("message".equals(req)){
            channelHandlerContext.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
                    "结果："+nextQuoto(),CharsetUtil.UTF_8),msg.sender()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)throws Exception{
        ctx.close();
        cause.printStackTrace();
    }

}
