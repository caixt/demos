package com.github.cxt.Myzookeeper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.EnsurePath;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.google.common.collect.Lists;
//http://supben.iteye.com/blog/2094077
public class Base {
	
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
		
//        try {
//            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
//                    .connectString(url.getBackupAddress())
//                    .retryPolicy(new RetryNTimes(1, 1000))
//                    .connectionTimeoutMs(5000);
//            String authority = url.getAuthority();
//            if (authority != null && authority.length() > 0) {
//                builder = builder.authorization("digest", authority.getBytes());
//            }
//            client = builder.build();
//            client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
//                @Override
//                public void stateChanged(CuratorFramework client, ConnectionState state) {
//                    if (state == ConnectionState.LOST) {
//                        CuratorZookeeperClient.this.stateChanged(StateListener.DISCONNECTED);
//                    } else if (state == ConnectionState.CONNECTED) {
//                        CuratorZookeeperClient.this.stateChanged(StateListener.CONNECTED);
//                    } else if (state == ConnectionState.RECONNECTED) {
//                        CuratorZookeeperClient.this.stateChanged(StateListener.RECONNECTED);
//                    }
//                }
//            });
//            client.start();
//        } catch (Exception e) {
//            throw new IllegalStateException(e.getMessage(), e);
//        }
		/*以上是dubbo 2.5.7 后的curator的源码
		之前是  new RetryNTimes(Integer.MAX_VALUE, 1000)
      	如果zkserver被停止后几分钟后再正常启动 zkserver。
      	在过程中,zkclient会重新发起新的连接(会话id变化了)
      	zkserver正常启动后,zkclient会重新注册临时节点,但临时节点还存在，所以临时节点加不上去
      	但临时节点已经不属于当前的会话客户端,最终该临时节点会被删除
      	其他dubbo的客户端会监听到时间后发现没有生产者，所以报黑白名单问题
      	如果改成 new RetryNTimes(1, 1000) 就不会发起新的连接（老的会话会重新连接）
		*/
	}
	
	@After
	public void after(){
		CloseableUtils.closeQuietly(client);
	}
	
	
	@Test
	public void create() throws Exception {
		client.create().withMode(CreateMode.EPHEMERAL).forPath("/url_1", "1".getBytes());  
		client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/url_2", "2".getBytes());
		client.create().withMode(CreateMode.PERSISTENT).forPath("/url_3", "3".getBytes());  
		client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/url_4", "4".getBytes());
		
		
		EnsurePath ensure = new EnsurePath("/test/cai/test");  
        ensure.ensure(client.getZookeeperClient());  
	}
	
	@Test
	public void delete() throws Exception {
		client.delete().forPath("/url_3");
	}

	@Test
	public void checkExists() throws Exception {
		Stat stat = client.checkExists().forPath("/url_3");
		System.out.println(stat);
		
//		client.checkExists().inBackground(new BackgroundCallback() {
//			
//			public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
//				//回调类型
//				CuratorEventType curatorEventType=event.getType();
//				//节点路径
//				String path=event.getPath();
//				//节点列表
//				List<String> list=event.getChildren();
//				//获取上下文
//				Object context=event.getContext();
//				//获取返回碼  异步操作成功，返回0
//				int code=event.getResultCode();
//				//获取数据内容
//				byte[] data=event.getData();
//				
//				StringBuffer sb=new StringBuffer();
//				sb.append("curatorEventType="+curatorEventType).append("\n");
//				sb.append("path="+path).append("\n");
//				sb.append("list="+list).append("\n");
//				sb.append("context="+context).append("\n");
//				sb.append("data="+new String(data)).append("\n");
//				 
//				System.out.println(sb.toString());
//				
//			}
//		}).forPath("/url_3");
	}
	
	

	@Test
	public void test2() throws Exception{
        final CountDownLatch latch = new CountDownLatch(3);
        final List<String> paths = Lists.newArrayList();
        BackgroundCallback callback = new BackgroundCallback()
        {
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception
            {
                paths.add(event.getPath());
                latch.countDown();
            }
        };
        client.create().inBackground(callback).forPath("/one");
        client.create().inBackground(callback).forPath("/one/two");
        client.create().inBackground(callback).forPath("/one/two/three");

        latch.await();

        Assert.assertEquals(paths, Arrays.asList("/one", "/one/two", "/one/two/three"));
	  
	}
	
	@Test
	public void test3() throws Exception{
		CuratorTransaction transaction = client.inTransaction();  
		  
        Collection<CuratorTransactionResult> results = transaction
        		.create().forPath("/path", "some data".getBytes())
                .and().setData().forPath("/path", "other data".getBytes())
                //.and().delete().forPath("/yetanotherpath")  
                .and().commit();  

        for (CuratorTransactionResult result : results) {  
            System.out.println(result.getForPath() + " - " + result.getType());  
        }  
	}
}
