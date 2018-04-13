package com.github.cxt.MyWebEmbedded;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Hello world!
 *
 */
public class JettyMain 
{

	public static void main(String[] args) {

		WebAppContext webContext = new WebAppContext();
		webContext.setContextPath("/");
		webContext.setDescriptor("WebRoot/WEB-INF/web.xml");
		webContext.setParentLoaderPriority(true);
		webContext.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
		webContext.setResourceBase("WebRoot");
		Server server = new Server();
		server.setStopAtShutdown(true);
		
		HandlerCollection handlers = new HandlerCollection();
		handlers.setHandlers(new Handler[] { webContext });
		server.setHandler(handlers);
		
		try {
			HttpConfiguration http_config = new HttpConfiguration();
	        http_config.setSendServerVersion(false);
	        http_config.setSendDateHeader(false);
			ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory(http_config));
		    connector.setPort(8080);
		    server.setConnectors(new Connector[]{connector});
			server.start();
			server.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
