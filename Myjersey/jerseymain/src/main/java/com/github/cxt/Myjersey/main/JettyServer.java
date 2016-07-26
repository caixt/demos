package com.github.cxt.Myjersey.main;

import java.io.File;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;


public class JettyServer {
	private static final WebAppContext webContext = new WebAppContext();

	public static void main(String[] args) {
		webContext.setContextPath("/");
		webContext.setDescriptor("conf" + File.separator + "web.xml");
		webContext.setParentLoaderPriority(true);
		//setResourceBase 和 request.getServletContext().getRealPath("/"); 相关
		webContext.setResourceBase("");
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
