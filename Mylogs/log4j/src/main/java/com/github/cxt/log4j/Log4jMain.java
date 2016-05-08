package com.github.cxt.log4j;

import org.apache.log4j.Logger;

public class Log4jMain {
	

	public static void test() {
		Logger logger = Logger.getLogger(Log4jMain.class);
		logger.info("test log4j");
	}

}
