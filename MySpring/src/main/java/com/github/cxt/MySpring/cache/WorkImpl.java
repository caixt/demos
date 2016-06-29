package com.github.cxt.MySpring.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class WorkImpl implements Work{

	@Cacheable(value="testCache", condition="#code.length() < 32")
	@Override
	public String readByCache(String code) {
		System.out.println("method1 " + code);
		return code + "!@#";
	}

	@CacheEvict(value="testCache")
	@Override
	public boolean update(String code) {
		return true;
	}

	@CachePut(value = "testCache")  
	@Override
	public String save(String code) {
		return code + "!@#";
	}

	@CacheEvict(value="testCache", allEntries=true)
	@Override
	public boolean cleanAll() {
		return true;
	}

//	@Caching(  
//	        cacheable = {  
//	                @Cacheable(value = "user", key = "#username")  
//	        },  
//	        put = {  
//	                @CachePut(value = "user", key = "#result.id", condition = "#result != null"),  
//	                @CachePut(value = "user", key = "#result.email", condition = "#result != null")  
//	        }  
//	)
	
}
