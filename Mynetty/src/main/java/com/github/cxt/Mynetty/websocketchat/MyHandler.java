package com.github.cxt.Mynetty.websocketchat;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import io.netty.util.ReferenceCountUtil;

public class MyHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		 if (msg instanceof FullHttpRequest) {
	         handleHttpRequest(ctx, (FullHttpRequest) msg);
	         ReferenceCountUtil.release(msg);
	     } else if (msg instanceof WebSocketFrame) {//如果是Websocket请求，则进行websocket操作
	         ctx.fireChannelRead(msg);
	     }
		
	}
	
	
	private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            String path = location.toURI() + "WebsocketChatClient.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate WebsocketChatClient.html", e);
        }
    }
	
    
    public void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
    	if ("/ws".equalsIgnoreCase(request.getUri())) {
            ctx.fireChannelRead(request.retain());                  //2
        }
    	
	    RandomAccessFile file = new RandomAccessFile(INDEX, "r");//4
	
	    HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
	    response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
	
	    boolean keepAlive = HttpHeaders.isKeepAlive(request);
	
	    if (keepAlive) {                                        //5
	        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
	        response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
	    }
	    ctx.write(response);                    //6
	
	    if (ctx.pipeline().get(SslHandler.class) == null) {     //7
	        ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
	    } else {
	        ctx.write(new ChunkedNioFile(file.getChannel()));
	    }
	    ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);           //8
	    if (!keepAlive) {
	        future.addListener(ChannelFutureListener.CLOSE);        //9
	    }
	    
	    file.close();
    }

}
