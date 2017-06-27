package com.github.cxt.MySpring.hystrix;

import org.junit.Test;

public class Main {

	@Test
	public void test1() {
		for(int i = 0; i < 8; i++){
			try{
				System.out.println("-------------------------------------------------");
				HelloCommand command = new HelloCommand("gropy", "key");
				System.out.println(command.execute());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	@Test
	public void test2() throws InterruptedException {
		for(int i = 0; i < 8; i++){
			try{
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						HelloCommand command = new HelloCommand("gropy", "key1");
						System.out.println(command.execute());
						
					}
				}).start();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		Thread.sleep(1000);
		for(int i = 0; i < 5; i++){
			try{
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						HelloCommand command = new HelloCommand("gropy", "key2");
						System.out.println(command.execute());
						
					}
				}).start();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		Thread.sleep(30000);
	}

}
