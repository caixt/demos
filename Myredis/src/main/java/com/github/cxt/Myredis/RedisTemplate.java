package com.github.cxt.Myredis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

public class RedisTemplate {

	private static Logger logger = LoggerFactory.getLogger(RedisTemplate.class);

	@Autowired(required = false)
	private JedisPoolCustom jedisPool;

	public Jedis getRedisClient() {
		try {
			Jedis jedis = jedisPool.getPool().getResource();
			return jedis;
		} catch (Exception e) {
			logger.error("getRedisClent error", e);
			throw e;
		}
	}

	public void returnResource(Jedis jedis) {
		jedis.close();
	}

}