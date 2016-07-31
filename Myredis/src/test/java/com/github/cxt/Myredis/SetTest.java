package com.github.cxt.Myredis;

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
public class SetTest {
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
	public void testSadd(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.sadd(key, "a", "b", "c") == 3);
	}
	
	@Test
	public void testScard(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.sadd(key, "a", "b", "c") == 3);
		
		Assert.assertTrue(jedis.scard(key) == 3);
	}
	
	@Test
	public void testSdiff(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.sadd(key + "1", "a", "b", "c") == 3);
		Assert.assertTrue(jedis.sadd(key + "2", "b" ,"d") == 2);
		Assert.assertTrue(jedis.sadd(key + "3", "c" ,"e") == 2);
		Assert.assertTrue(jedis.sdiff(key + "1", key + "2", key + "3").size() == 1);
	}
	
	@Test
	public void testSdiffstore(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.sadd(key + "1", "a", "b", "c") == 3);
		Assert.assertTrue(jedis.sadd(key + "2", "b" ,"d") == 2);
		Assert.assertTrue(jedis.sadd(key + "3", "c" ,"e") == 2);
		Assert.assertTrue(jedis.sdiffstore(key + "new", key + "1", key + "2", key + "3") == 1);
	}
	
	
	@Test
	public void testSinter(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.sadd(key + "1", "a", "b", "c") == 3);
		Assert.assertTrue(jedis.sadd(key + "2", "b" ,"d") == 2);
		Assert.assertTrue(jedis.sadd(key + "3", "c" ,"e") == 2);
		Assert.assertTrue(jedis.sinter(key + "1", key + "2", key + "3").size() == 0);
	}
	
	@Test
	public void testSinterstore(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.sadd(key + "1", "a", "b", "c") == 3);
		Assert.assertTrue(jedis.sadd(key + "2", "b" ,"d") == 2);
		Assert.assertTrue(jedis.sadd(key + "3", "c" ,"e") == 2);
		Assert.assertTrue(jedis.sinterstore(key + "new", key + "1", key + "2", key + "3") == 0);
	}
	
	@Test
	public void testSmember(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.sadd(key, "a", "b", "c") == 3);
		Assert.assertTrue(jedis.smembers(key).size() ==  3);
	}
	
	@Test
	public void testSismember(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.sadd(key, "a", "b", "c") == 3);
		Assert.assertTrue(jedis.sismember(key, "a") == true);
		Assert.assertTrue(jedis.sismember(key, "d") == false);
	}
	
	
	@Test
	public void testSmove(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.sadd(key, "a", "b", "c") == 3);
		Assert.assertTrue(jedis.smove(key, key + "new", "a") == 1);
		Assert.assertTrue(jedis.smove(key, key + "new", "d") == 0);
	}
	
	@Test
	public void testSpop(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.sadd(key, "a", "b", "c") == 3);
		//随机
		System.out.println(jedis.spop(key));
		System.out.println(jedis.spop(key));
		System.out.println(jedis.spop(key));
	}
	
	@Test
	public void testSrandmember(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.sadd(key, "a", "b", "c") == 3);
		//随机,会重复
		System.out.println(jedis.srandmember(key));
		System.out.println(jedis.srandmember(key));
		System.out.println(jedis.srandmember(key));
	}
	
	@Test
	public void testSrem(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.sadd(key, "a", "b", "c") == 3);
		Assert.assertTrue(jedis.srem(key, "a", "d") == 1);
	}
	
	@Test
	public void testSunion(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.sadd(key + "1", "a", "b", "c") == 3);
		Assert.assertTrue(jedis.sadd(key + "2", "c", "d") == 2);
		Assert.assertTrue(jedis.sadd(key + "3", "e") == 1);
		
		Assert.assertTrue(jedis.sunion(key + "1", key + "2", key + "3").size() == 5);
	}
	
	@Test
	public void testSunionstore(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.sadd(key + "1", "a", "b", "c") == 3);
		Assert.assertTrue(jedis.sadd(key + "2", "c", "d") == 2);
		Assert.assertTrue(jedis.sadd(key + "3", "e") == 1);
		
		Assert.assertTrue(jedis.sunionstore(key + "new", key + "1", key + "2", key + "3") == 5);
	}
	
	
	@Test
	public void testScan(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
	}
	
}
