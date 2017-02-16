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
public class BaseTest {
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
	public void testExists(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.exists(key) == false);
	}
	
	@Test
	public void testDel(){
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "test").equals("OK"));
		Assert.assertTrue(jedis.exists(key) == true);
		Assert.assertTrue(jedis.del(key) == 1L);
	}
	
	@Test
	public void testDump(){
		//序列号的值
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "hello, dumping world!").equals("OK"));
		System.out.println(jedis.dump(key));
	}
	
	@Test
	public void testExpire() throws InterruptedException{
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "test").equals("OK"));
		Assert.assertTrue(jedis.expire(key, 1) == 1);
		Thread.sleep(1200);
		Assert.assertTrue(jedis.exists(key) == false);
	}
	
	@Test
	public void testExpireAt() throws InterruptedException{
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "test").equals("OK"));
		Assert.assertTrue(jedis.expireAt(key, System.currentTimeMillis() /1000 + 1000 * 10) == 1);
//		Thread.sleep(1200);
//		Assert.assertTrue(jedis.exists(key) == false);
	}
	
	@Test
	public void tesTtl() throws InterruptedException{
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.ttl(key) == -2);
		Assert.assertTrue(jedis.set(key, "test").equals("OK"));
		Assert.assertTrue(jedis.ttl(key) == -1);
		Assert.assertTrue(jedis.expire(key, 10) == 1);
		Assert.assertTrue(jedis.ttl(key) == 10);
	}
	
	@Test
	public void testPersist() throws InterruptedException{
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.ttl(key) == -2);
		Assert.assertTrue(jedis.set(key, "test").equals("OK"));
		Assert.assertTrue(jedis.ttl(key) == -1);
		Assert.assertTrue(jedis.expire(key, 10) == 1);
		Assert.assertTrue(jedis.ttl(key) == 10);
		
		Assert.assertTrue(jedis.persist(key) == 1);
		Assert.assertTrue(jedis.ttl(key) == -1);
		Assert.assertTrue(jedis.persist(key) == 0);
	}
	
	
	@Test
	public void testPexpire() throws InterruptedException{
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "test").equals("OK"));
		Assert.assertTrue(jedis.pexpireAt(key, 1000) == 1);
		Thread.sleep(1200);
		Assert.assertTrue(jedis.exists(key) == false);
	}
	
	@Test
	public void testPexpireAt() throws InterruptedException{
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "test").equals("OK"));
		Assert.assertTrue(jedis.pexpireAt(key, System.currentTimeMillis() + 1000 * 10) == 1);
//		Thread.sleep(1200);
//		Assert.assertTrue(jedis.exists(key) == false);
	}
	
	@Test
	public void tesPttl() throws InterruptedException{
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.pttl(key) == -2);
		Assert.assertTrue(jedis.set(key, "test").equals("OK"));
		Assert.assertTrue(jedis.pttl(key) == -1);
		Assert.assertTrue(jedis.expire(key, 10) == 1);
		Thread.sleep(1200);
		long l = jedis.pttl(key);
		Assert.assertTrue(l > 0 && l < 10000);
	}
	
	@Test
	public void testKeys() throws InterruptedException{
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "test").equals("OK"));
		Assert.assertTrue(jedis.keys("com.github.cxt.Myredis.*").size() == 1);
	}
	
	@Test
	public void testMigrate() throws InterruptedException{
		//jedis.migrate(host, port, key, destinationDb, timeout);
	}
	
	@Test
	public void testMove() throws InterruptedException{
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "test").equals("OK"));
		Assert.assertTrue(jedis.move(key, 0) == 1l);
	}
	
	
	@Test
	public void testObject() throws InterruptedException{
		//基本用于调整
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "test").equals("OK"));
		System.out.println(jedis.objectRefcount(key));
		System.out.println(jedis.objectEncoding(key));
		System.out.println(jedis.objectIdletime(key));
	}
	
	@Test
	public void testType() throws InterruptedException{
		//基本用于调整
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		
		Assert.assertTrue(jedis.type(key + "none").equals("none"));
		
		Assert.assertTrue(jedis.set(key + "string", "test").equals("OK"));
		Assert.assertTrue(jedis.type(key + "string").equals("string"));
		
		Assert.assertTrue(jedis.hset(key + "hash", "key", "value") == 1);
		Assert.assertTrue(jedis.type(key + "hash").equals("hash"));
		
		Assert.assertTrue(jedis.lpush(key + "list", "value") == 1);
		Assert.assertTrue(jedis.type(key + "list").equals("list"));
		
		Assert.assertTrue(jedis.sadd(key + "set", "test") == 1);
		Assert.assertTrue(jedis.type(key + "set").equals("set"));
		
		Assert.assertTrue(jedis.zadd(key + "zset", 1000L, "test") == 1);
		Assert.assertTrue(jedis.type(key + "zset").equals("zset"));
	}
	
	
	@Test
	public void testRename() throws InterruptedException{
		//基本用于调整
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "test").equals("OK"));
		Assert.assertTrue(jedis.rename(key, key + "new").equals("OK"));
	}
	
	@Test
	public void testRenameNx() throws InterruptedException{
		//基本用于调整
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.set(key, "test").equals("OK"));
		Assert.assertTrue(jedis.set(key + "new", "test").equals("OK"));
		Assert.assertTrue(jedis.renamenx(key, key + "new") == 0);
		Assert.assertTrue(jedis.renamenx(key, key + "newnew") == 1);
	}
	
	
	@Test
	public void testRandomKey() throws InterruptedException{
		//基本用于调整
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
		Assert.assertTrue(jedis.randomKey() == null);
		
		
		Assert.assertTrue(jedis.set(key + "1", "test").equals("OK"));
		Assert.assertTrue(jedis.set(key + "2", "test").equals("OK"));
		Assert.assertTrue(jedis.set(key + "3", "test").equals("OK"));
		System.out.println(jedis.randomKey());
	}
	
	@Test
	public void testScan() throws InterruptedException{
		//基本用于调整
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
	}
	
	@Test
	public void testSort() throws InterruptedException{
		//基本用于调整
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
	}
	
	@Test
	public void testSortBy() throws InterruptedException{
		//基本用于调整
		String key = KEY_PREFIX + ":" + Thread.currentThread().getStackTrace()[1].getMethodName();
	}
}
