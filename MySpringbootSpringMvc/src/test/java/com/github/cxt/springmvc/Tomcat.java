package com.github.cxt.springmvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;

public class Tomcat {

	private static void initLog(){
		SLF4JBridgeHandler.removeHandlersForRootLogger();  
		SLF4JBridgeHandler.install();
	}
	
	@Test
	public void test() throws InterruptedException {
		initLog();
		
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		ServletContextInitializer initializer = new ServletContextInitializer() {
			
			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
				Dynamic d = servletContext.addServlet("demo", ServletDemo.class);
				d.addMapping("/*");
			}
		};
		
		EmbeddedServletContainer container = factory.getEmbeddedServletContainer(initializer);
		container.start();
		Thread.sleep(1000 * 60 * 10);
	}
}
