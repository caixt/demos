package com.github.cxt.Myredis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClusterMain {

	public static void main(String[] args) throws IOException {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setTestOnBorrow(true);
		
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("10.1.240.231", 7001));
		nodes.add(new HostAndPort("10.1.240.231", 7002));
		nodes.add(new HostAndPort("10.1.240.231", 7003));
		nodes.add(new HostAndPort("10.1.240.231", 7004));
		nodes.add(new HostAndPort("10.1.240.231", 7005));
		nodes.add(new HostAndPort("10.1.240.231", 7006));
		
		JedisCluster jc = new JedisCluster(nodes, 6000, 1000, poolConfig);
		
		
		System.out.println(jc.get("a"));
		jc.close();
	}

}
