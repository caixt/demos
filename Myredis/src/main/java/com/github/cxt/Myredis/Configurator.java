package com.github.cxt.Myredis;

import org.springframework.context.annotation.Bean;
import redis.clients.jedis.JedisPoolConfig;


public class Configurator {
	
    
	@Bean
	public JedisPoolCustom jedisPoolCustom(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setTestOnBorrow(true);
		return new JedisPoolCustom(poolConfig, "mymaster", "127.0.0.1", 6379, 3000, "admin", 0, false);
	}
	@Bean
	public RedisTemplate redisTemplate(){
		return new RedisTemplate();
	}
    
}
