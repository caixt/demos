package com.github.cxt.MySpring.io.netty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@io.netty.channel.ChannelHandler.Sharable
public class ServerHandler  extends SimpleChannelInboundHandler<String> {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private ExecutorService executorService = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS,
			new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		executorService.submit(new Runnable() {
			
			@Override
			public void run() {
				logger.info("get Message: " + msg);
				String response = "server time:" + System.currentTimeMillis() + "," + msg;
				ctx.writeAndFlush(response);
//				try {
//					Thread.sleep(Long.MAX_VALUE);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}
		});
	}
}