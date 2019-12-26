package com.github.cxt.MySpring.io.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.io.UnsupportedEncodingException;

public class ClientHandler extends ChannelInboundHandlerAdapter {
	
	ChannelHandlerContext ctx;
	/**
	 * tcp链路简历成功后调用
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
		sendMsg("客户端消息");
	}
	
	public boolean sendMsg(String msg){
		System.out.println("客户端发送消息："+msg);
		byte[] req = msg.getBytes();
		ByteBuf m = Unpooled.buffer(req.length);
		m.writeBytes(req);
		ChannelFuture future = ctx.writeAndFlush(m);
		boolean exit =  msg.equals("q")?false:true;
		if(!exit){
			future.addListener(ChannelFutureListener.CLOSE);
		}
		return exit;
	}
	
	/**
	 * 收到服务器消息后调用
	 * @throws UnsupportedEncodingException 
	 */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        try{
        	ByteBuf buf = (ByteBuf) msg;
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			String body = new String(req,"utf-8");
			System.out.println("服务器消息："+body);
        }finally{
        	ReferenceCountUtil.release(msg);
        }
    }
    /**
     * 发生异常时调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}