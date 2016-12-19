package com.github.cxt.Mynetty.http;

import java.nio.charset.Charset;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * 处理 Http 请求
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> { //1
    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		ByteBuf buf = (ByteBuf)request.content();
	    byte[]req = new byte[buf.readableBytes()];
	    buf.readBytes(req);
	    String body = new String(req, HttpUtil.getCharset(request, Charset.forName("utf-8")));
	    System.out.println(body);
	    
		boolean keepAlive = HttpUtil.isKeepAlive(request);
        FullHttpResponse msg = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, 
        		HttpResponseStatus.OK, Unpooled.copiedBuffer("{}", CharsetUtil.UTF_8));
		msg.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
		msg.headers().set(HttpHeaderNames.CONTENT_LENGTH, msg.content().readableBytes());
		ChannelFuture future = ctx.writeAndFlush(msg);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);        //9
        }
    }

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
    	Channel incoming = ctx.channel();
		System.out.println("Client:"+incoming.remoteAddress()+"异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
	}
}
