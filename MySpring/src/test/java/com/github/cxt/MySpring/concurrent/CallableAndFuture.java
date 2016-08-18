package com.github.cxt.MySpring.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * 主线程等待其他线程的返回结果类似于线程的join方法
 * @author 蔡仙通
 * @date:2014年4月11日 下午2:16:03
 */
public class CallableAndFuture {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService threadPool =  Executors.newSingleThreadExecutor();
		Future<String> future =
			threadPool.submit(
				new Callable<String>() {
					public String call() throws Exception {
						Thread.sleep(3000);
						return "hello";
					};
				}
		);
		threadPool.shutdown();
		System.out.println("等待结果");
		try {
			System.out.println("拿到结果：" + future.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ExecutorService threadPool2 =  Executors.newFixedThreadPool(10);
		CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(threadPool2);
		for(int i=1;i<=10;i++){
			final int seq = i;
			completionService.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					Thread.sleep(2000);
					System.out.println("!!!!!!!");
					return seq;
				}
			});
		}
		threadPool2.shutdown();
		for(int i=0;i<10;i++){
			try {
				System.out.println(completionService.take().get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}