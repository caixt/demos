package com.github.cxt.log4j;

import org.junit.Test;
import org.slf4j.MDC;

import com.github.cxt.sl4j.Sl4jMain;

public class MainTest {
	

	@Test
	public void done() throws InterruptedException {
		MDC.put("custom", "haha");
		while(true){
			Sl4jMain.test();
			Sl4jMain.test2();
			Thread.sleep(1000);
		}
	}

}
