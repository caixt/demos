package com.github.cxt.Mynetty.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
	
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        // 当客户端和服务端建立tcp成功之后，Netty的NIO线程会调用channelActive
        // 发送查询时间的指令给服务端。
        // 调用ChannelHandlerContext的writeAndFlush方法，将请求消息发送给服务端
        // 当服务端应答时，channelRead方法被调用
    	String s = "message...";
    	for(int i =0; i < 1; i++){
    		s += "message...";
    	}
    	byte[] req = s.getBytes();
        ByteBuf firstMessage=null;
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
        ChannelFuture future = ctx.writeAndFlush(firstMessage);
//        future.addListener(ChannelFutureListener.CLOSE);
    }

    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        ctx.close();
    }

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		byte[]req = new byte[msg.readableBytes()];
		msg.readBytes(req);
        String body = new String(req,"UTF-8");
        System.out.println(body);
		
	}
}
