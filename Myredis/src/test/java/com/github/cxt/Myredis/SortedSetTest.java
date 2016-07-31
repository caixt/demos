package com.github.cxt.Myredis;

import java.util.HashMap;
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
import redis.clients.jedis.ZParams;
import redis.clients.jedis.ZParams.Aggregate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Configurator.class)
public class SortedSetTest {
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
	public void testZadd(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key, map) == 3);
	}
	
	@Test
	public void testZcard(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key, map) == 3);
		
		Assert.assertTrue(jedis.zcard(key) == 3);
	}
	
	@Test
	public void testZcount(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key, map) == 3);
		
		Assert.assertTrue(jedis.zcount(key, 22, 33) == 2);
	}
	
	@Test
	public void testZincrby(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.zadd(key, 10, "a" ) == 1);
		Assert.assertTrue(jedis.zincrby(key, 20, "a") == 30);
	}
	
	@Test
	public void testZrange(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key, map) == 3);
		String[] s = jedis.zrange(key, 1, 2).toArray(new String[]{});
		Assert.assertTrue(s[0].equals("b"));
		Assert.assertTrue(s[1].equals("c"));
	}
	
	@Test
	public void testZrangeByScore(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key, map) == 3);
		String[] s = jedis.zrangeByScore(key, 22, 33).toArray(new String[]{});
		Assert.assertTrue(s[0].equals("b"));
		Assert.assertTrue(s[1].equals("c"));
	}
	
	@Test
	public void testZrank(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key, map) == 3);
		Assert.assertTrue(jedis.zrank(key, "a") == 0);
		Assert.assertTrue(jedis.zrank(key, "c") == 2);
	}
	
	@Test
	public void testZrevrank(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key, map) == 3);
		Assert.assertTrue(jedis.zrevrank(key, "a") == 2);
		Assert.assertTrue(jedis.zrevrank(key, "c") == 0);
	}
	
	
	@Test
	public void testZscore(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key, map) == 3);
		Assert.assertTrue(jedis.zscore(key, "a") == 11);
	}
	
	@Test
	public void testZrem(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key, map) == 3);
		Assert.assertTrue(jedis.zrem(key, "a", "d") == 1);
	}
	
	@Test
	public void testZremByRank(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key, map) == 3);
		Assert.assertTrue(jedis.zremrangeByRank(key, 2, 3) == 1);
	}
	
	@Test
	public void testZremByScore(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key, map) == 3);
		Assert.assertTrue(jedis.zremrangeByScore(key, 22, 33) == 2);
	}
	
	@Test
	public void testZrevrange(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key, map) == 3);
		String[] s = jedis.zrevrange(key, 1, 2).toArray(new String[]{});
		Assert.assertTrue(s[0].equals("b"));
		Assert.assertTrue(s[1].equals("a"));
	}
	
	
	@Test
	public void testZrevrangeByScore(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key, map) == 3);
		String[] s = jedis.zrevrangeByScore(key, 33, 22).toArray(new String[]{});
		Assert.assertTrue(s[0].equals("c"));
		Assert.assertTrue(s[1].equals("b"));
	}

	@Test
	public void testZunionstore(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key + "1", map) == 3);
		map = new HashMap<>();
		map.put("c", 30d);
		map.put("D", 44d);
		Assert.assertTrue(jedis.zadd(key + "2", map) == 2);
		
		Assert.assertTrue(jedis.zunionstore(key + "new1", key + "1", key + "2") == 4);
		Assert.assertTrue(jedis.zscore(key + "new1", "c") == 63);
		ZParams zpParams = new ZParams();
		zpParams.aggregate(Aggregate.MAX);
		Assert.assertTrue(jedis.zunionstore(key + "new", zpParams, key + "1", key + "2") == 4);
		Assert.assertTrue(jedis.zscore(key + "new", "c") == 33);
	}
	
	@Test
	public void testZinterstore(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Map<String, Double> map = new HashMap<>();
		map.put("a", 11d);
		map.put("b", 22d);
		map.put("c", 33d);
		Assert.assertTrue(jedis.zadd(key + "1", map) == 3);
		map = new HashMap<>();
		map.put("c", 30d);
		map.put("D", 44d);
		Assert.assertTrue(jedis.zadd(key + "2", map) == 2);
		
		Assert.assertTrue(jedis.zinterstore(key + "new1", key + "1", key + "2") == 1);
		Assert.assertTrue(jedis.zscore(key + "new1", "c") == 63);
		ZParams zpParams = new ZParams();
		zpParams.aggregate(Aggregate.MAX);
		Assert.assertTrue(jedis.zinterstore(key + "new", zpParams, key + "1", key + "2") == 1);
		Assert.assertTrue(jedis.zscore(key + "new", "c") == 33);
	}

//	
	@Test
	public void testScan(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
	}
	
}
