package com.github.cxt.Myredis;

import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Configurator.class)
public class TransactionTest {
	private String KEY_PREFIX = getClass().getName();
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	private Jedis jedis = null;

	@Before
	public void before(){
		jedis = redisTemplate.getRedisClient();
		Set<String> keys = jedis.keys(KEY_PREFIX + "*");
		if(keys.size() > 0){
			jedis.del(keys.toArray(new String[]{}));
		}
	}
	
	@After
	public void after(){
		jedis.close();
	}
	
	
	@Test
	public void testMulti(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Transaction transaction = jedis.multi();
		for(int i = 0; i < 10; i++){
			transaction.set(key + "_" + i, i + "");
		}
		
		List<?> result = transaction.exec();
		System.out.println(result);
	}
	
	@Test
	public void testDiscard(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Transaction transaction = jedis.multi();
		for(int i = 0; i < 10; i++){
			transaction.set(key + "_" + i, i + "");
		}
		
		System.out.println(transaction.discard());
	}
	
	
	//watch 相当于热关锁
	@Test
	public void testWatch(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		jedis.watch(key);
		Transaction transaction = jedis.multi();
		transaction.set("mykey", "a");
		List<?> result = transaction.exec();
		System.out.println(result);
	}
	
	@Test
	public void testUnWatch(){
		jedis.unwatch();
	}
	
	
	@Test
	public void test3Pipelined() {
	    Pipeline pipeline = jedis.pipelined();
	    for (int i = 0; i < 10; i++) {
	        pipeline.set("p" + i, "p" + i);
	    }
	    List<Object> results = pipeline.syncAndReturnAll();
	    System.out.println(results);
	}
}
