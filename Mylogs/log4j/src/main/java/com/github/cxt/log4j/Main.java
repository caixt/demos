package com.github.cxt.log4j;

import org.apache.log4j.Logger;

public class Main {
	

	public static void done() {
		Logger logger = Logger.getLogger(Main.class);
		logger.info("test log4j");
	}

}
