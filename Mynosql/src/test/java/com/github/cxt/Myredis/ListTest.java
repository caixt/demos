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

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Configurator.class)
public class ListTest {
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
	public void testLpush(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.lpush(key, "a", "b", "c") == 3);
		
		Assert.assertTrue(toString(jedis.lrange(key, 0, -1)).equals("cba"));
	}
	
	@Test
	public void testLinsert(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.lpush(key, "a", "b", "c", "b", "d") == 5);
		Assert.assertTrue(jedis.linsert(key, LIST_POSITION.BEFORE, "b", "x") == 6);
		Assert.assertTrue(toString(jedis.lrange(key, 0, -1)).equals("dxbcba"));
	}
	
	@Test
	public void testLindex(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.lpush(key, "a", "b", "c") == 3);
		
		Assert.assertTrue(jedis.lindex(key, 0).equals("c"));
		Assert.assertTrue(jedis.lindex(key, 1).equals("b"));
		Assert.assertTrue(jedis.lindex(key, 2).equals("a"));
	}
	
	@Test
	public void testLlen(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.lpush(key, "a", "b", "c") == 3);
		
		Assert.assertTrue(jedis.llen(key) == 3);
	}
	
	@Test
	public void testLset(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.lpush(key, "a", "b", "c") == 3);
		
		Assert.assertTrue(jedis.lset(key, 1, "d").equals("OK"));
		Assert.assertTrue(toString(jedis.lrange(key, 0, -1)).equals("cda"));
	}
	
	@Test
	public void testLpop(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.lpush(key, "a", "b", "c") == 3);
		
		Assert.assertTrue(jedis.lpop(key).equals("c"));
		Assert.assertTrue(toString(jedis.lrange(key, 0, -1)).equals("ba"));
		
		Assert.assertTrue(jedis.lpop(key + "null") == null);
	}
	
	@Test
	public void testLrange(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.lpush(key, "a", "b", "c", "d") == 4);
		
		Assert.assertTrue(toString(jedis.lrange(key, 0, 2)).equals("dcb"));
	}
	
	
	@Test
	public void testLpushx(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		//不支持多个api有问题
		Assert.assertTrue(jedis.lpushx(key, "a") == 0);
		
		Assert.assertTrue(jedis.lpush(key, "d") == 1);
		Assert.assertTrue(jedis.lpushx(key, "a") == 2);
		Assert.assertTrue(jedis.lpushx(key, "a") == 3);
		
		Assert.assertTrue(toString(jedis.lrange(key, 0, 2)).equals("aad"));
	}
	
	@Test
	public void testLrem(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.lpush(key + "1", "a", "b", "c", "a", "d") == 5);
		Assert.assertTrue(toString(jedis.lrange(key + "1", 0, -1)).equals("dacba"));
		
		Assert.assertTrue(jedis.lpush(key + "2", "a", "b", "c", "a", "d") == 5);
		Assert.assertTrue(jedis.lrem(key + "2", 0, "a") == 2);
		Assert.assertTrue(toString(jedis.lrange(key + "2", 0, -1)).equals("dcb"));
		
		Assert.assertTrue(jedis.lpush(key + "3", "a", "b", "c", "a", "d") == 5);
		Assert.assertTrue(jedis.lrem(key + "3", 1, "a") == 1);
		Assert.assertTrue(toString(jedis.lrange(key + "3", 0, -1)).equals("dcba"));
		
		Assert.assertTrue(jedis.lpush(key + "4", "a", "b", "c", "a", "d") == 5);
		Assert.assertTrue(jedis.lrem(key + "4", -1, "a") == 1);
		Assert.assertTrue(toString(jedis.lrange(key + "4", 0, -1)).equals("dacb"));
	}
	
	@Test
	public void testLtrim(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.lpush(key, "a", "b", "c", "a", "d") == 5);
		Assert.assertTrue(toString(jedis.lrange(key, 0, -1)).equals("dacba"));
		Assert.assertTrue(jedis.ltrim(key, 1, 3).equals("OK"));
		Assert.assertTrue(toString(jedis.lrange(key, 0, -1)).equals("acb"));
	}
	
	@Test
	public void testRpop(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.lpush(key, "a", "b", "c") == 3);
		Assert.assertTrue(toString(jedis.lrange(key, 0, -1)).equals("cba"));
		
		Assert.assertTrue(jedis.rpop(key).equals("a"));
		Assert.assertTrue(jedis.rpop(key + "null") == null);
	}
	
	@Test
	public void testRpush(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.rpush(key, "a", "b", "c") == 3);
		
		Assert.assertTrue(toString(jedis.lrange(key, 0, -1)).equals("abc"));
	}
	
	@Test
	public void testRpushx(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		//不支持多个api有问题
		Assert.assertTrue(jedis.rpushx(key, "a") == 0);
		
		Assert.assertTrue(jedis.rpush(key, "d") == 1);
		Assert.assertTrue(jedis.rpushx(key, "a") == 2);
		Assert.assertTrue(jedis.rpushx(key, "a") == 3);
		
		Assert.assertTrue(toString(jedis.lrange(key, 0, 2)).equals("daa"));
	}
	
	
	@Test
	public void testRpoplpush(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.lpush(key, "a", "x", "x", "x", "c") == 5);
		
		Assert.assertTrue(toString(jedis.lrange(key, 0, -1)).equals("cxxxa"));
		
		Assert.assertTrue(jedis.rpoplpush(key, key).equals("a"));
		Assert.assertTrue(toString(jedis.lrange(key, 0, -1)).equals("acxxx"));
	}
	
	

	@Test
	public void testBLpop(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.lpush(key, "a", "b", "c") == 3);
		//单位秒
		Assert.assertTrue(jedis.blpop(1, key).get(1).equals("c"));
	}
	
	@Test
	public void testBRpop(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.lpush(key, "a", "b", "c") == 3);
		//单位秒
		Assert.assertTrue(jedis.brpop(1, key).get(1).equals("a"));
	}
	
	@Test
	public void testBRpoplpush(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.lpush(key, "a", "x", "x", "x", "c") == 5);
		
		Assert.assertTrue(toString(jedis.lrange(key, 0, -1)).equals("cxxxa"));
		//单位秒
		Assert.assertTrue(jedis.brpoplpush(key, key, 1).equals("a"));
		Assert.assertTrue(toString(jedis.lrange(key, 0, -1)).equals("acxxx"));
	}
	
	private String toString(List<String> lists){
		String result = "";
		for(String s : lists){
			result += s;
		}
		return result;
	}
}
