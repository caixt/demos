package com.github.cxt.Myredis;

import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Configurator.class)
public class ScriptTest {
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
	public void testEval(){
		System.out.println(jedis.eval("return {KEYS[1],KEYS[2],ARGV[1],ARGV[2]}", 2, "key1", "key2", "first", "second"));
		System.out.println(jedis.eval("return redis.call('set','foo','bar')", 0));
	}
	
	@Test
	public void testEvalsha(){
		String code = jedis.scriptLoad("return 'hello moto'");
		System.out.println(jedis.evalsha(code));
	}
	
	@Test
	public void testScriptLoad(){
		System.out.println(jedis.scriptLoad("return 'hello moto'"));
	}
	
	@Test
	public void testExists(){
		System.out.println(jedis.scriptExists("232fd51614574cf0867b83d384a5e898cfd24e5a"));
	}
	
	@Test
	public void testScriptKill(){
		//jedis.scriptKill();
	}
	
	@Test
	public void testScriptFlush(){
		System.out.println(jedis.scriptFlush());
	}
	
}
