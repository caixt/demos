package com.github.cxt.springboot2.lock;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LockManager {

	@Autowired
	private CuratorFramework curatorFramework;
	
	public Lock getLock(String key) {
		if(StringUtils.isBlank(key)) {
			throw new IllegalArgumentException("lock key empty");
		}
		if(!StringUtils.startsWith(key, "/")) {
			key = "/" + key;
		}
		return new Lock(curatorFramework, key);
	}
}
