package com.github.cxt.Mynetty.StickyPacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter{
	
	private int counter;
    //用于网络的读写操作
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg)
	        throws Exception{
	    ByteBuf buf = (ByteBuf)msg;
	    byte[]req = new byte[buf.readableBytes()];
	    buf.readBytes(req);
	    String body = new String(req, "UTF-8");
	    System.out.println("the time server order : " + body + ";the counter is :" + (++counter));
	
	    String currentTime = "message.........................\r\n".equalsIgnoreCase(body)?
	    			System.currentTimeMillis() + "":"BAD ORDER";
	    currentTime += "\r\n";
	    ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
	    ctx.write(resp);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx)throws Exception{
	    ctx.flush();   // 它的作用是把消息发送队列中的消息写入SocketChannel中发送给对方
	    // 为了防止频繁的唤醒Selector进行消息发送，Netty的write方法，并不直接将消息写入SocketChannel中
	    // 调用write方法只是把待发送的消息发到缓冲区中，再调用flush，将发送缓冲区中的消息
	    // 全部写到SocketChannel中。
	}

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
		cause.printStackTrace();
	    ctx.close();
	}
}

