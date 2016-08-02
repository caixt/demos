package com.github.cxt.Myjersey.main;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class JettyServer {
	private static final WebAppContext webContext = new WebAppContext();
	
	private static Logger logger = LoggerFactory.getLogger(JettyServer.class);
	
	public static void main(String[] args) {
		logger.info("开始");
		
		webContext.setContextPath("/");
		webContext.setDescriptor("conf" + File.separator + "web.xml");
		webContext.setParentLoaderPriority(true);
		//setResourceBase 和 request.getServletContext().getRealPath("/"); 相关
		webContext.setResourceBase("html");
		Server server = new Server(8088);
		server.setHandler(webContext);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
