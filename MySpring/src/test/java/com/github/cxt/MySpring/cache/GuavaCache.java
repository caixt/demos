package com.github.cxt.MySpring.cache;

import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class GuavaCache {

	public static void main(String[] args) throws Exception {
		 //CacheLoader.asyncReloading
		LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .concurrencyLevel(8)
                .expireAfterWrite(15, TimeUnit.SECONDS)
                .initialCapacity(2)
                .maximumSize(200)
                .recordStats()
                .removalListener(new RemovalListener<String, String>() {

					@Override
					public void onRemoval(RemovalNotification<String, String> notification) {
						System.out.println(notification);
					}
				})
                .build(new CacheLoader<String, String>(){

					@Override
					public String load(String key) throws Exception {
						System.out.println("!");
						return key + "!!!";
					}
                });
		
		String value = cache.get("aaa");
		System.out.println(value);
		value = cache.get("aaa");
		System.out.println(value);
		Thread.sleep(10000);
		value = cache.get("aaa");
		System.out.println(value);
		Thread.sleep(10000);
		value = cache.get("aaa");
		System.out.println(value);
	}
}
