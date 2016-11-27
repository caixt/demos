package com.github.cxt.Mynetty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * 处理 Http 请求
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> { //1
    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");

        boolean keepAlive = HttpHeaders.isKeepAlive(request);

        FullHttpResponse msg = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, 
        		HttpResponseStatus.OK, Unpooled.copiedBuffer("{}", CharsetUtil.UTF_8));
		msg.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json; charset=utf-8");
		msg.headers().set(HttpHeaders.Names.CONTENT_LENGTH, msg.content().readableBytes());

		// not keep-alive
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
