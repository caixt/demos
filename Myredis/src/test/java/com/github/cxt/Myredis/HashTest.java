package com.github.cxt.Myredis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Configurator.class)
public class HashTest {
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
	public void testHset(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.hset(key, "a", "A") == 1);
		Assert.assertTrue(jedis.hset(key, "b", "B") == 1);
		
		Assert.assertTrue(jedis.hset(key, "a", "a") == 0);
	}
	
	
	@Test
	public void testHget(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.hset(key, "a", "A") == 1);
		Assert.assertTrue(jedis.hset(key, "b", "B") == 1);
		
		Assert.assertTrue(jedis.hget(key, "a").equals("A"));
		Assert.assertTrue(jedis.hget(key, "c") == null);
	}
	
	@Test
	public void testHgetAll(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.hset(key, "a", "A") == 1);
		Assert.assertTrue(jedis.hset(key, "b", "B") == 1);
		
		Map<String, String> map = jedis.hgetAll(key);
		
		Assert.assertTrue(map.get("a").equals("A"));
		Assert.assertTrue(map.get("c") == null);
	}
	
	
	@Test
	public void testHdel(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.hset(key, "a", "A") == 1);
		Assert.assertTrue(jedis.hset(key, "b", "B") == 1);
		
		Assert.assertTrue(jedis.hdel(key, "a", "b", "c") == 2);
	}
	
	@Test
	public void testHexists(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.hset(key, "a", "A") == 1);
		Assert.assertTrue(jedis.hexists(key, "a") == true);
		Assert.assertTrue(jedis.hexists(key, "b") == false);
	}
	
	@Test
	public void testHsetnx(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.hsetnx(key, "a", "A") == 1);
		Assert.assertTrue(jedis.hsetnx(key, "b", "B") == 1);
		
		Assert.assertTrue(jedis.hsetnx(key, "a", "a") == 0);
	}
	
	@Test
	public void testHkeys(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.hset(key, "a", "A") == 1);
		Assert.assertTrue(jedis.hset(key, "b", "B") == 1);
		Set<String> values = jedis.hkeys(key);
		Assert.assertTrue(values.contains("a"));
		Assert.assertTrue(values.contains("b"));
	}
	
	@Test
	public void testHvals(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.hset(key, "a", "A") == 1);
		Assert.assertTrue(jedis.hset(key, "b", "B") == 1);
		List<String> values = jedis.hvals(key);
		Assert.assertTrue(values.get(0).equals("A"));
		Assert.assertTrue(values.get(1).equals("B"));
	}
	
	@Test
	public void testHscan(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();

	}
	
	
	@Test
	public void testHmset(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, String> map = new HashMap<>();
		map.put("a", "A");
		map.put("b", "B");
		Assert.assertTrue(jedis.hmset(key, map).equals("OK"));
	}
	
	@Test
	public void testHmget(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, String> map = new HashMap<>();
		map.put("a", "A");
		map.put("b", "B");
		Assert.assertTrue(jedis.hmset(key, map).equals("OK"));
		List<String> list = jedis.hmget(key, "a", "b", "c");
		Assert.assertTrue(list.get(0).equals("A"));
		Assert.assertTrue(list.get(1).equals("B"));
		Assert.assertTrue(list.get(2) == null);
	}
	
	@Test
	public void testHlen(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, String> map = new HashMap<>();
		map.put("a", "A");
		map.put("b", "B");
		Assert.assertTrue(jedis.hmset(key, map).equals("OK"));
		Assert.assertTrue(jedis.hlen(key) == 2L);
	}
	
	
	@Test
	public void testHincrBy(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.hset(key, "a", "100") == 1);
		Assert.assertTrue(jedis.hincrBy(key, "a", 10) == 110l);
	}
	
	@Test
	public void testHincrByFloat(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.hset(key, "a", "100") == 1);
		Assert.assertTrue(jedis.hincrByFloat(key, "a", 10.0) == 110);
	}
}
