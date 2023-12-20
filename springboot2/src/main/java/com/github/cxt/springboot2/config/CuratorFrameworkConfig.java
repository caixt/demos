package com.github.cxt.springboot2.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CuratorFrameworkConfig {
	
	@Value("${zk}")
	private String zkUrl;

	@Bean(initMethod = "start")
	public CuratorFramework createCurator() {
		String connectString = zkUrl;
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(connectString)
				.retryPolicy(new RetryNTimes(1, 1000))
				.connectionTimeoutMs(5 * 1000)
				.sessionTimeoutMs(60 * 1000)
				.build();
		return client;
	}
}
