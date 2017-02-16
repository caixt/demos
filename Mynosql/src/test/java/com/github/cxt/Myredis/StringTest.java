package com.github.cxt.Myredis;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Configurator.class)
public class StringTest {
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
	public void testBitcount(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.setbit(key, 2, true) == false);
		Assert.assertTrue(jedis.setbit(key, 3, true) == false);
		Assert.assertTrue(jedis.bitcount(key) == 2l);
		//start end 不清楚
	}
	
	
	@Test
	public void testBitop(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		String key1 = key + "1";
		String key2 = key + "2";
		String newkey = key + "new";
		Assert.assertTrue(jedis.setbit(key1, 0, true) == false);
		Assert.assertTrue(jedis.setbit(key1, 3, true) == false);
		
		Assert.assertTrue(jedis.setbit(key2, 0, true) == false);
		Assert.assertTrue(jedis.setbit(key2, 1, true) == false);
		Assert.assertTrue(jedis.setbit(key2, 3, true) == false);
		
		Assert.assertTrue(jedis.bitop(BitOP.AND, newkey, key1, key2) == 1l);
		Assert.assertTrue(jedis.bitcount(newkey) == 2l);
	}
	
	@Test
	public void testAppend(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.append(key, "append") == 6);
		Assert.assertTrue(jedis.append(key, "append") == 12);
	}
	
	
	@Test
	public void testDecr(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "10").equals("OK"));
		Assert.assertTrue(jedis.decr(key) == 9L);
	}
	
	@Test
	public void testDecrBy(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "100").equals("OK"));
		Assert.assertTrue(jedis.decrBy(key, 20L) == 80L);
	}
	
	@Test
	public void testIncr(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "10").equals("OK"));
		Assert.assertTrue(jedis.incr(key) == 11L);
	}
	
	@Test
	public void testIncrBy(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "100").equals("OK"));
		Assert.assertTrue(jedis.incrBy(key, 20L) == 120L);
	}
	
	@Test
	public void testIncrByFloat(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "100").equals("OK"));
		Assert.assertTrue(jedis.incrByFloat(key, 23.4) == 123.4);
	}
	
	
	@Test
	public void testSet(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "set").equals("OK"));
	}
	
	@Test
	public void testSetNx(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.setnx(key, "setNx") == 1l);
		Assert.assertTrue(jedis.setnx(key, "setNx") == 0l);
	}
	
	@Test
	public void testSetEx() throws InterruptedException{
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		//Redis 2.4 版本中，过期时间的延迟在 1 秒钟之内，Redis 2.6 版本中，延迟被降低到 1 毫秒之内
		Assert.assertTrue(jedis.setex(key, 3, "aaa").equals("OK"));
		Thread.sleep(4000L);
		Assert.assertTrue(jedis.exists(key) ==  false);
	}
	
	@Test
	public void testGet(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "get").equals("OK"));
		Assert.assertTrue(jedis.get(key).equals("get"));
	}
	
	@Test
	public void testSetrange(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "hello world").equals("OK"));
		Assert.assertTrue(jedis.setrange(key, 6, "Redis") == 11L);
		Assert.assertTrue(jedis.get(key).equals("hello Redis"));
	}
	
	
	@Test
	public void testStrlen(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "strlen").equals("OK"));
		Assert.assertTrue(jedis.strlen(key) == 6L);
	}
	
	@Test
	public void testGetRange(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "ABCDEFG").equals("OK"));
		Assert.assertTrue(jedis.getrange(key, 3, 5).equals("DEF"));
	}
	
	@Test
	public void testGetSet(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "OLD").equals("OK"));
		Assert.assertTrue(jedis.getSet(key, "NEW").equals("OLD"));
	}
	
	
	@Test
	public void testMget(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key + "1", "A1").equals("OK"));
		Assert.assertTrue(jedis.set(key + "2", "A2").equals("OK"));
		List<String> values = jedis.mget(key + "1", key + "0", key + "2");
		Assert.assertTrue(values.get(0).equals("A1"));
		Assert.assertTrue(values.get(1) == null);
		Assert.assertTrue(values.get(2).equals("A2"));
	}
	
	@Test
	public void testMsetnx(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.msetnx(key + "1", "a1", key + "2", "a2") == 1l);
		Assert.assertTrue(jedis.msetnx(key + "2", "a1", key + "3", "a2") == 0l);
		Assert.assertTrue(!jedis.exists(key + "a3"));
	}
	
	
	@Test
	public void testPsetEx() throws InterruptedException{
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		//Redis 2.4 版本中，过期时间的延迟在 1 秒钟之内，Redis 2.6 版本中，延迟被降低到 1 毫秒之内
		Assert.assertTrue(jedis.psetex(key, 3000L, "aaa").equals("OK"));
		Thread.sleep(3100L);
		Assert.assertTrue(jedis.exists(key) ==  false);
	}
	
	@Test
	public void testMset(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.mset(key + "1", "a1", key + "2", "a2").equals("OK"));
		Assert.assertTrue(jedis.get(key + "1").equals("a1"));
	}
	
	
	@Test
	public void testSetBit(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.setbit(key, 10086, true) == false);
		Assert.assertTrue(jedis.setbit(key, 10086, true) == true);
		Assert.assertTrue(jedis.setbit(key, 10000, true) == false);
	}
	
	@Test
	public void testGetBit(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.setbit(key, 10086, true) == false);
		Assert.assertTrue(jedis.getbit(key, 10086) == true);
	}
	
	
	
}
