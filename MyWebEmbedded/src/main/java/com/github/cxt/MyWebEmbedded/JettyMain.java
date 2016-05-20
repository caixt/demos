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
		//./jre/bin/keytool -validity 365 -genkey -alias uyun -keyalg EC -keystore /opt/uyun.keystore -dname "CN=collector.uyun.cn,OU=broada,O=broada,L=Hangzhou,ST=Hangzhou,c=cn" -storepass uyuncollector -keypass uyuncollector

//		SslContextFactory sslContextFactory = new SslContextFactory();
//		sslContextFactory.setKeyStorePath("uyun.boltdog.keystore");
//		sslContextFactory.setKeyStorePassword("uyuncollector");
//		ServerConnector httpsConnector = new ServerConnector(server,
//		        new SslConnectionFactory(sslContextFactory,"http/1.1"),
//		        new HttpConnectionFactory());
//		httpsConnector.setPort(7000);
//		
//		server.addConnector(httpsConnector);
		
		
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
