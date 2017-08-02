package com.github.cxt.MyWebEmbedded;

import java.io.File;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;

public class TomcatMain {

	private static String PROJECT_PATH = System.getProperty("user.dir");
	private static String WEB_APP_PATH = PROJECT_PATH + File.separatorChar
			+ "WebRoot";

	public static void main(String[] args) throws LifecycleException,
			ServletException {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);
		tomcat.setBaseDir(PROJECT_PATH);

		StandardServer server = (StandardServer) tomcat.getServer();
		AprLifecycleListener listener = new AprLifecycleListener();
		server.addLifecycleListener(listener);
		tomcat.addWebapp("/", WEB_APP_PATH);

		tomcat.start();
		tomcat.getServer().await();

	}

}
