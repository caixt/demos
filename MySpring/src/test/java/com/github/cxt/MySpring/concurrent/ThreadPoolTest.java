package com.github.cxt.MySpring.concurrent;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ThreadPoolTest {

	@Test
	public void test1() {
		ExecutorService threadPool = Executors.newFixedThreadPool(3);
//		ExecutorService threadPool = Executors.newCachedThreadPool();
//		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		for(int i=1;i<=10;i++){
			final int task = i;
			threadPool.execute(new Runnable(){
				@Override
				public void run() {
					for(int j=1;j<=10;j++){
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println(Thread.currentThread().getName() + " is looping of " + j + " for  task of " + task);
					}
				}
			});
		}
		System.out.println("all of 10 tasks have committed! ");
		threadPool.shutdown();
		
		Executors.newScheduledThreadPool(3).scheduleAtFixedRate(
				new Runnable(){
					@Override
				public void run() {
					System.out.println("bombing!");
					
				}},
				6,
				2,
				TimeUnit.SECONDS);
		
		Executors.newScheduledThreadPool(3).schedule(new Runnable(){
					@Override
				public void run() {
					System.out.println("!" + new Date().toLocaleString());
					System.out.println("bombing!!!");
					
				}}, 2,
				TimeUnit.SECONDS);
	}
	
	
	@Test
	public void test2() throws InterruptedException {
    	ThreadPoolExecutor es = new ThreadPoolExecutor(3, 3,
    			1, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
    	es.submit(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(Long.MAX_VALUE);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		});
    	
    	for(int i = 0; i < 10; i++){
	    	es.submit(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(1000 * 3);
						System.out.println("end...");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			});
    	}
    	System.out.println(es.getTaskCount());
    	
    	Thread.sleep(2000);
    	es.setCorePoolSize(2);
    	es.setMaximumPoolSize(2);
    	for(int i = 0; i < 10; i++){
    		System.out.println(es.getActiveCount());
    		Thread.sleep(1000);
    	}
    	Thread.sleep(Long.MAX_VALUE);
	}

	
	
	@Test
	public void test3(){
		ThreadPoolExecutor es = new ThreadPoolExecutor(1,1,
	            0L, TimeUnit.MILLISECONDS,
	            new ArrayBlockingQueue<Runnable>(1),
	            new RejectedExecutionHandler() {
			
			        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			        	System.out.println("!?");
			                if (!executor.isShutdown()) {
			                        try {
			                                executor.getQueue().put(r);
			                        } catch (InterruptedException e) {
			                                // should not be interrupted
			                        }
			                }
			        }
		});
    	
    	for(int i = 0; i < 10; i++){
    		System.out.println(i);
	    	es.submit(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(Long.MAX_VALUE);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			});
    	}
    	System.out.println("!");
	}
}
