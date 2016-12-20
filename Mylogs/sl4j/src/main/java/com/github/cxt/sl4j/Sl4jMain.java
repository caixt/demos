package com.github.cxt.sl4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sl4jMain {
	
	public static void test() {
		Logger logger = LoggerFactory.getLogger(Sl4jMain.class);
		logger.info("test sl4j");
	}
	
	
	public static void test2() {
		Logger logger = LoggerFactory.getLogger("com.github.cxt.sl4j.Test");
		logger.info("test sl4j");
	}

}
