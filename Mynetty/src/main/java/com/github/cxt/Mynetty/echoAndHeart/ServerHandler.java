package com.github.cxt.Mynetty.echoAndHeart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class ServerHandler extends ChannelInboundHandlerAdapter{
	
    //用于网络的读写操作
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg)
	        throws Exception{
	    ByteBuf buf = (ByteBuf)msg;
	    byte[]req = new byte[buf.readableBytes()];
	    buf.readBytes(req);
	    String body = new String(req, "UTF-8");
	    System.out.println(body);
	    ByteBuf resp = Unpooled.copiedBuffer(body.getBytes());
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

	private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
			.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
					CharsetUtil.UTF_8));  
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			String type = "";
			if (event.state() == IdleState.READER_IDLE) {
				type = "read idle";
			} else if (event.state() == IdleState.WRITER_IDLE) {
				type = "write idle";
			} else if (event.state() == IdleState.ALL_IDLE) {
				type = "all idle";
			}

			ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(
					ChannelFutureListener.CLOSE_ON_FAILURE);
 
			System.out.println( ctx.channel().remoteAddress()+"超时类型：" + type);
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}
}

