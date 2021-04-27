package com.github.cxt.Mynetty.sock5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.DefaultSocks5PasswordAuthRequest;
import io.netty.handler.codec.socksx.v5.DefaultSocks5PasswordAuthResponse;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthResponse;
import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthStatus;

public class Socks5PasswordAuthRequestHandler extends SimpleChannelInboundHandler<DefaultSocks5PasswordAuthRequest> {
	
	private static final Logger logger = LoggerFactory.getLogger(Socks5PasswordAuthRequestHandler.class);

	private String username;
	private String password;
	
	public Socks5PasswordAuthRequestHandler(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DefaultSocks5PasswordAuthRequest msg) throws Exception {
		logger.debug("用户名密码 : " + msg.username() + "," + msg.password());
		if(this.username.equals(msg.username()) && this.password.equals(msg.password())) {
//			ProxyChannelTrafficShapingHandler.username(ctx, msg.username());
			Socks5PasswordAuthResponse passwordAuthResponse = new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.SUCCESS);
			ctx.writeAndFlush(passwordAuthResponse);
		} else {
//			ProxyChannelTrafficShapingHandler.username(ctx, "unauthorized");
			Socks5PasswordAuthResponse passwordAuthResponse = new DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus.FAILURE);
			//发送鉴权失败消息，完成后关闭channel
			ctx.writeAndFlush(passwordAuthResponse).addListener(ChannelFutureListener.CLOSE);
		}
	}
}
