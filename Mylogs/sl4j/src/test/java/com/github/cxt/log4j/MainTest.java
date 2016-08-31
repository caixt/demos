package com.github.cxt.log4j;

import org.junit.Test;

import com.github.cxt.sl4j.Sl4jMain;

public class MainTest {
	

	@Test
	public void done() throws InterruptedException {
		while(true){
			Sl4jMain.test();
			Thread.sleep(1000);
		}
	}

}
