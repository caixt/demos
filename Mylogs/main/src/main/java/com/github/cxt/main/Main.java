package com.github.cxt.main;


import org.slf4j.bridge.SLF4JBridgeHandler;
import com.github.cxt.log4j.Log4jMain;
import com.github.cxt.sl4j.Sl4jMain;

public class Main {
	
	private static final org.apache.commons.logging.Log commonsLog = org.apache.commons.logging.LogFactory.getLog(Main.class);  
    private static final org.apache.log4j.Logger log4jLogger = org.apache.log4j.Logger.getLogger(Main.class);  
    private static final org.apache.logging.log4j.Logger log4j2Logger = org.apache.logging.log4j.LogManager.getLogger(Main.class);  
    private static final java.util.logging.Logger jdkLogger = java.util.logging.Logger.getLogger(Main.class.getName());  
    private static final org.slf4j.Logger slf4jLogger = org.slf4j.LoggerFactory.getLogger(Main.class);  
    private static final org.jboss.logging.Logger jbossLog = org.jboss.logging.Logger.getLogger(Main.class);
    
    

	public static void main(String[] args) {
		initLong();
		
		Log4jMain.test();
		Sl4jMain.test();
		
		commonsLog.info("commonslog");
		log4jLogger.info("log4jLogger");
		log4j2Logger.info("log4j2Logger");
		jdkLogger.info("jdkLogger");
		slf4jLogger.info("slf4jLogger");
		jbossLog.info("jbossLog");
		
		
	}
	
	
	private static void initLong(){
		SLF4JBridgeHandler.removeHandlersForRootLogger();  
		SLF4JBridgeHandler.install();
	}
	
	
}
