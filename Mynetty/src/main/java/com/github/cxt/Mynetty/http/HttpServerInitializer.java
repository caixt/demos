package com.github.cxt.Mynetty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline pipeline = socketChannel.pipeline();
		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new HttpObjectAggregator(1024*1024*1024));
		pipeline.addLast(new HttpRequestHandler());
		
		
//		pipeline.addLast(new ChunkedWriteHandler());
//		pipeline.addLast(new HttpObjectAggregator(1024*1024*1024));
//		pipeline.addLast(new HttpBigFileRequestHandler());
	}

}
