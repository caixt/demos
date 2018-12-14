package com.github.cxt;

import org.junit.Test;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.github.cxt.MyDubboProvider.GoodsManager;
import com.github.cxt.MyDubboProvider.GoodsManagerImpl;

public class Main {

	
	@Test
	public void provider() throws InterruptedException{
		ApplicationConfig conf = new ApplicationConfig("simple-provider");
		
		ProtocolConfig protocolConfig = new ProtocolConfig("dubbo", 20001);
		protocolConfig.setThreads(10);
		
		
		ServiceConfig<GoodsManager> serviceConfig = new ServiceConfig<>();
		serviceConfig.setApplication(conf);
		serviceConfig.setProtocol(protocolConfig);
		
		serviceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
		serviceConfig.setInterface(GoodsManager.class);
		serviceConfig.setRef(new GoodsManagerImpl());
		
		serviceConfig.export();
		
		Thread.sleep(Long.MAX_VALUE);
	}
	
	
	@Test
	public void consumer(){
		ApplicationConfig conf = new ApplicationConfig("young-app");
		
		ReferenceConfig<GoodsManager> referenceConfig = new ReferenceConfig<>();
		referenceConfig.setApplication(conf);
		referenceConfig.setInterface(GoodsManager.class);
		referenceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
//		referenceConfig.setUrl("dubbo://127.0.0.1:20001");
		
		System.out.println(referenceConfig.get().test());
	}
}
