package com.github.cxt.Myzookeeper;

import java.util.Collection;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Lock {
	
	private CuratorFramework client = null;
	
	@Before
	public void before() throws InterruptedException{
		client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")  
		        .sessionTimeoutMs(30000)  
		        .connectionTimeoutMs(30000)  
		        .canBeReadOnly(false)  
		        .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE)) 
		        .namespace("test")
		        .defaultData(null)  
		        .build();
		client.start();
		client.blockUntilConnected();
	}
	
	@After
	public void after(){
		CloseableUtils.closeQuietly(client);
	}
	
	@Test
	public void lock() throws Exception {
		InterProcessLock lock = new InterProcessMutex(client, "/test/lock");
		lock.acquire();
		try{
			System.out.println("!!!!");
			Thread.sleep(30000);
		}finally{
			lock.release();
		}
	}
	
	
	@Test
	public void readlock() throws Exception {
		InterProcessReadWriteLock lock = new InterProcessReadWriteLock(client, "/test/lock");
		InterProcessLock readlock = lock.readLock();
		readlock.acquire();
		try{
			System.out.println("!!!!");
			Thread.sleep(3000);
		}finally{
			readlock.release();
		}
	}
	
	@Test
	public void writelock() throws Exception {
		InterProcessReadWriteLock lock = new InterProcessReadWriteLock(client, "/test/lock");
		InterProcessLock writelock = lock.writeLock();
		writelock.acquire();
		try{
			System.out.println("!!!!");
			Thread.sleep(30000);
		}finally{
			writelock.release();
		}
	}
	
	
	@Test
	public void semaphorelock() throws Exception {
		InterProcessSemaphoreV2 lock = new InterProcessSemaphoreV2(client, "/test/lock", 3);
		Collection<Lease> leases = lock.acquire(1);
		try{
			System.out.println("!!!!");
			Thread.sleep(30000);
		}finally{
			lock.returnAll(leases);
		}
	}
}
