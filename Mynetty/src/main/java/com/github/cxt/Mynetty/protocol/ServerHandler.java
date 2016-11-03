package com.github.cxt.Mynetty.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {
 

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object obj)
			throws Exception {
        Channel incoming = ctx.channel();
		
		if(obj instanceof Msg) {
			Msg msg = (Msg)obj;
			System.out.println("Client->Server:"+incoming.remoteAddress()+msg.getBody());
			incoming.write(obj);
		}
	}
	
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
