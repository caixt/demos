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
import redis.clients.jedis.JedisPubSub;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Configurator.class)
public class PubSubTest {
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
	public void testPsubscribe(){
        JedisPubSub listener = new JedisPubSub() {
            public void onPMessage(String pattern, String channel, String message) {
            	System.out.println(pattern + "!" + channel + "!" + message);
            	this.punsubscribe();
            }
            
            public void onPSubscribe(String pattern, int subscribedChannels) {
            	System.out.println("redis监听启动");
            }
        };
		jedis.psubscribe(listener, "test_test:*");

	}
	
	@Test
	public void testSubscribe(){
        JedisPubSub listener = new JedisPubSub() {
            public void onSubscribe(String pattern, int subscribedChannels) {
            	System.out.println("redis监听启动");
            }

			@Override
			public void onMessage(String channel, String message) {
	            System.out.println(channel + "!" + message);
	            this.unsubscribe();
			}
            
        };
		jedis.subscribe(listener, "test_test");

	}
	
	@Test
	public void testPublish(){
		jedis.publish("test_test:" + "id", "test_message");
		jedis.publish("test_test", "test_message");		
	}

}
