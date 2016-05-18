package com.github.cxt.MyWebEmbedded;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Hello world!
 *
 */
public class JettyMain 
{
	private static final WebAppContext webContext = new WebAppContext();

	public static void main(String[] args) {
		webContext.setContextPath("/");
		webContext.setDescriptor("WebRoot/WEB-INF/web.xml");
		webContext.setParentLoaderPriority(true);
		webContext.setResourceBase("WebRoot");
		Server server = new Server(8080);
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
