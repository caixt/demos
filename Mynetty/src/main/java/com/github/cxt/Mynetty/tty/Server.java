package com.github.cxt.Mynetty.tty;

import java.util.concurrent.TimeUnit;

public class Server {

	public static void main(String[] args) throws Exception {
		//index.html的端口写死的
		NettyHttpTelnetBootstrap bootstrap = new NettyHttpTelnetBootstrap().setHost("localhost").setPort(8080);
	    bootstrap.start(new Shell()).get(10, TimeUnit.SECONDS);
	    System.out.println("Web server started on localhost:8080");
	    Thread.sleep(Integer.MAX_VALUE);
	}
}
