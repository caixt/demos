package com.github.cxt.Myredis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

public class JedisPoolCustom {
	private JedisPoolConfig poolConfig;
	private Pool<Jedis> jedisPool;
	private String masterName;
	private String sentinels;
	private int timeout;
	private String password;
	private int database;
	private int port;
	private boolean redisSentinel;

	public JedisPoolCustom(JedisPoolConfig poolConfig, String masterName, String sentinels, int port, int timeout,
			String password, int database, boolean redisSentinel) {
		this.poolConfig = poolConfig;
		this.masterName = masterName;
		this.sentinels = sentinels;
		this.port = port;
		this.timeout = timeout;
		this.password = password;
		this.database = database;
		this.redisSentinel = redisSentinel;
	}

	public synchronized Pool<Jedis> getPool() {
		if (redisSentinel) {
			return getJedisSentinelPool();
		} else {
			return getJedisPool();
		}
	}

	private Pool<Jedis> getJedisSentinelPool() {
		if (jedisPool == null) {
			jedisPool = new JedisSentinelPool(masterName,
					getSentinelSet(sentinels), poolConfig, timeout, password, database);
		}
		return jedisPool;
	}

	private Pool<Jedis> getJedisPool() {
		if (jedisPool == null) {
			jedisPool = new JedisPool(poolConfig,
					sentinels, port, timeout, password, database);
		}
		return jedisPool;
	}

	private static Set<String> getSentinelSet(String sentinels) {
		Set<String> sentinelSet = new HashSet<String>();
		if (sentinels != null && !"".equals(sentinels.trim())) {
			sentinelSet = new HashSet<String>(Arrays.asList(sentinels.split(",")));
		}
		return sentinelSet;
	}

}
