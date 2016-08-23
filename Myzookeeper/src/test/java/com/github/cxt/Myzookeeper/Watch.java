package com.github.cxt.Myzookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Watch {

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
	public void nodeListener() throws Exception{
		final NodeCache nodeCache = new NodeCache(client, "/test", false);
		nodeCache.start();
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			
			public void nodeChanged() throws Exception {
				System.out.println("Node data is changed, new data: " + 
			            new String(nodeCache.getCurrentData().getData()));
			}
		});
	}
	
	@Test
	public void nodechildrenListener() throws Exception{
		final PathChildrenCache childrenCache = new PathChildrenCache(client, "/test", true);
	    childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
	    childrenCache.getListenable().addListener(
	      new PathChildrenCacheListener() {
	        public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
	            throws Exception {
	            switch (event.getType()) {
	            case CHILD_ADDED:
	              System.out.println("CHILD_ADDED: " + event.getData().getPath());
	              break;
	            case CHILD_REMOVED:
	              System.out.println("CHILD_REMOVED: " + event.getData().getPath());
	              break;
	            case CHILD_UPDATED:
	              System.out.println("CHILD_UPDATED: " + event.getData().getPath());
	              break;
	            default:
	              break;
	          }
	        }
	      }
	    );
	}
}
