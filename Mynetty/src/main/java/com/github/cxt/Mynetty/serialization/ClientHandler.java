package com.github.cxt.Mynetty.serialization;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object obj)
			throws Exception {
		if (obj instanceof SerializationBean) {
			SerializationBean user = (SerializationBean) obj;
			System.out.println("Client get msg form Server - name:"
					+ user.getName() + ";age:" + user.getAge());
		}

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		SerializationBean user = new SerializationBean();
		user.setAge(10);
		user.setName("waylau");
		ctx.writeAndFlush(user);
	}

}
