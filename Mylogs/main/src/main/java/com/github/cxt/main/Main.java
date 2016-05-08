package com.github.cxt.main;

import com.github.cxt.log4j.Log4jMain;
import com.github.cxt.sl4j.Sl4jMain;

public class Main {

	public static void main(String[] args) {
		Log4jMain.test();
		Sl4jMain.test();
		
		org.jboss.logging.Logger log = org.jboss.logging.Logger.getLogger(Main.class);
		log.info("test");
	}

}
