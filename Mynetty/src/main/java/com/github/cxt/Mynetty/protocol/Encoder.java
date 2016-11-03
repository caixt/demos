package com.github.cxt.Mynetty.protocol;

import java.nio.charset.Charset;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public class Encoder extends MessageToByteEncoder<Msg> {


	/* (non-Javadoc)
	 * @see io.netty.handler.codec.MessageToByteEncoder#encode(io.netty.channel.ChannelHandlerContext, java.lang.Object, io.netty.buffer.ByteBuf)
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, Msg msg,
			ByteBuf out) throws Exception {
		if (msg == null | msg.getHeader() == null) {
			throw new Exception("The encode message is null");
		}
		Header header = msg.getHeader();
		String body = msg.getBody();
		byte[] bodyBytes = body.getBytes(Charset.forName("utf-8"));
		int bodySize = bodyBytes.length;
		
		out.writeByte(header.getMagic());
		out.writeByte(header.getMsgType().getValue());
		out.writeShort(header.getReserve());
		out.writeShort(header.getSn());
		out.writeInt(bodySize);
		out.writeBytes(bodyBytes);
	}

}
