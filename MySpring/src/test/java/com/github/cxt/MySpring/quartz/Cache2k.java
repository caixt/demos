package com.github.cxt.MySpring.quartz;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.CacheEntry;
import org.cache2k.event.CacheEntryExpiredListener;
import org.cache2k.expiry.ExpiryPolicy;
import org.cache2k.expiry.ValueWithExpiryTime;
import org.junit.Test;


public class Cache2k {

	
	@Test
	public void test() throws InterruptedException{
		Cache<String, MyTask> expireCache = new Cache2kBuilder<String, MyTask>() {}
			.sharpExpiry(true)
			.eternal(false)
			.expiryPolicy(new ExpiryPolicy<String, MyTask>() {
				@Override
				public long calculateExpiryTime(String key, MyTask value,
						long loadTime, CacheEntry<String, MyTask> oldEntry) {
					return value.getCacheExpiryTime();
				}
			})
		    .addListener(new CacheEntryExpiredListener<String, MyTask>() {
				@Override
				public void onEntryExpired(Cache<String, MyTask> cache,
						CacheEntry<String, MyTask> task) {
					task.getValue().execute();
				}
			})
		    .build();
		expireCache.put("test", new MyTask() {
			
			@Override
			public long getCacheExpiryTime() {
				return System.currentTimeMillis() + 10000;
			}

			@Override
			public void execute() {
				System.out.println("我执行了1");
				
			}
		});
		
		expireCache.put("test", new MyTask() {
			
			@Override
			public long getCacheExpiryTime() {
				return System.currentTimeMillis() + 8000;
			}

			@Override
			public void execute() {
				System.out.println("我执行了2");
				
			}
		});
		
		Thread.sleep(1000 * 15);
	}
		
	
	public interface MyTask extends ValueWithExpiryTime{
		public void execute();
	}
}
