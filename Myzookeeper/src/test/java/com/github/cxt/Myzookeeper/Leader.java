package com.github.cxt.Myzookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Leader {

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
	public void leaderLatch() throws Exception{
        // 选举Leader 启动  
        LeaderLatch latch = new LeaderLatch(client,"/leaderLatch");  
        latch.addListener(new LeaderLatchListener(){

            @Override
            public void isLeader() {
                System.out.println("I am Leader");
            }

            @Override
            public void notLeader() {
                System.out.println("I am not Leader");
            }}
        );
        latch.start();  
        latch.await();  
        
        System.out.println("我启动了");  
        Thread.currentThread().sleep(100000);  
        latch.close();  
        client.close();  
	}
	
	@Test
	public void leaderSelector() throws Exception{
		//类似轮询
		final LeaderSelector leaderSelector = new LeaderSelector(client, "/leaderSelector", new LeaderSelectorListenerAdapter(){  
			  
            @Override  
            public void takeLeadership(CuratorFramework client) throws Exception {  
                System.err.println("working...");  
                Thread.currentThread().sleep(3000);  
                System.err.println("end");  
            }
       
        });  
        leaderSelector.autoRequeue();  
        leaderSelector.start();  
        System.in.read();   
	}
}
