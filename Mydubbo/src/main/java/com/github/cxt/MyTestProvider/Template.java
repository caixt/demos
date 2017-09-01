package com.github.cxt.MyTestProvider;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class Template<T extends Remote> {
	
	private String key = "goodsManager";
	private CuratorFramework client = null;
	
	private List<String> paths;
	private boolean startWatch = false;
	private Lock lock = new ReentrantLock();
	
	public Template(String zkUri) throws Exception {
		this.client = initZkCilent(zkUri);
	}


	private CuratorFramework initZkCilent(String zkUri) throws Exception {
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString(zkUri)  
		        .sessionTimeoutMs(30000)  
		        .connectionTimeoutMs(30000)  
		        .canBeReadOnly(false)  
		        .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE)) 
		        .namespace("mydubbo")
		        .defaultData(null)  
		        .build();
		client.start();
		client.blockUntilConnected();
		return client;
	}
	
	public void register(T t, int port) throws Exception{
		String key = "/" + this.key + "/providers";
		Stat stat = client.checkExists().forPath(key);
		if(stat == null){
			client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key);  
		}
		String url = "rmi://localhost:" + port + "/" + this.key;
        LocateRegistry.createRegistry(port); 
        Naming.bind(url, t); 
        client.create().withMode(CreateMode.EPHEMERAL).forPath(key + "/" + URLEncoder.encode(url, "UTF-8"));  
	}
	
	public void checkWatch() throws Exception{
		if(!startWatch){
			this.paths = new ArrayList<>(); 
			final String key = "/" + this.key + "/providers";
			Stat stat = client.checkExists().forPath(key);
			if(stat != null){
				PathChildrenCache childrenCache = new PathChildrenCache(client, key, true);
				childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
				childrenCache.getListenable().addListener(
						new PathChildrenCacheListener() {
							public void childEvent(CuratorFramework client,
									PathChildrenCacheEvent event) throws Exception {
								switch (event.getType()) {
								case CHILD_ADDED:{
									try{
										lock.lock();
										String path = event.getData().getPath().substring(key.length() + 1);
										System.out.println("CHILD_ADDED: " + event.getData().getPath());
										paths.add(path);
									}finally{
										lock.unlock();
									}
									break;
								}
								case CHILD_REMOVED:{
									try{
										lock.lock();
										String path = event.getData().getPath().substring(key.length() + 1);
										System.out.println("CHILD_REMOVED: " + event.getData().getPath());
										paths.remove(path);
									}finally{
										lock.unlock();
									}
									break;
								}
								default:
									break;
								}
							}
						});
			}
			startWatch = true;
		}
	}
	
	public T get() throws Exception{
		checkWatch();
		return realGet();
	}
	
	
	private T realGet() throws Exception{
		try{
			lock.lock();
			if(paths == null || paths.size() == 0){
				throw new RuntimeException("not find providers");
			}
			String url = paths.get(ThreadLocalRandom.current().nextInt(paths.size()));
			url = URLDecoder.decode(url, "UTF-8");
			return (T) Naming.lookup(url);
		}finally{
			lock.unlock();
		}
	}
}
