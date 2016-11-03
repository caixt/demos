package com.github.cxt.Mynetty.protocol;

import java.nio.charset.Charset;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object obj)
			throws Exception {
        Channel incoming = ctx.channel();
		System.out.println("Server->Client:"+incoming.remoteAddress()+obj.toString());
		
		if(obj instanceof Msg) {
			Msg msg = (Msg)obj;
			System.out.println("Server->Client:"+incoming.remoteAddress()+msg.getBody());
		}
	}
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //ctx.flush();
    }
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 发送消息给服务器
		Msg msg = new Msg();
		Header header = new Header();
		header.setMagic((byte) 0x01);
		header.setMsgType(MsgType.EMGW_LOGIN_REQ);
		header.setReserve((short) 0);
		header.setSn((short) 0);
		String body = "测试信息..............";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 1; i++) {
			sb.append(body);
		}

		byte[] bodyBytes = sb.toString().getBytes(
				Charset.forName("utf-8"));
		int bodySize = bodyBytes.length;
		header.setLen(bodySize);

		msg.setHeader(header);
		msg.setBody(sb.toString());

		ctx.writeAndFlush(msg);
	}
 
}
