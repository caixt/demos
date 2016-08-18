package com.github.cxt.MySpring.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 一个计数信号量,相当于多少个资源。
 * @author 蔡仙通
 * @date:2014年4月11日 下午3:16:47
 */
public class SemaphoreTest {
	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final  Semaphore sp = new Semaphore(3);
		for(int i=0;i<4;i++){
			Runnable runnable = new Runnable(){
					public void run(){
						int n = 0;
						while(true){
							try {
								sp.acquire();
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							System.out.println("线程" + Thread.currentThread().getName() + 
									"进入，当前已有" + (3-sp.availablePermits()) + "个并发");
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							System.out.println("线程" + Thread.currentThread().getName() + 
									"即将离开");					
							sp.release();
							//下面代码有时候执行不准确，因为其没有和上面的代码合成原子单元
							System.out.println("线程" + Thread.currentThread().getName() + 
									"已离开，当前已有" + (3-sp.availablePermits()) + "个并发" +
									",运行了" + (++n) + "次");	
					}
				}
			};
			service.execute(runnable);			
		}
	}

}
