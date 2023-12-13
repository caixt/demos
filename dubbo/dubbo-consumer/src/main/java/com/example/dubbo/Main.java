package com.example.dubbo;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import com.alibaba.fastjson.JSON;
import com.example.dubbo.service.UserService;

public class Main {

	public static void main(String[] args) {
		ApplicationConfig conf = new ApplicationConfig("dubbo-consumer");
		RegistryConfig registryConfig = new RegistryConfig("multicast://224.5.6.7:1234");
		ReferenceConfig<UserService> referenceConfig = new ReferenceConfig<>();
		referenceConfig.setApplication(conf);
		referenceConfig.setInterface(UserService.class);
		referenceConfig.setTimeout(1000 * 30);
//		referenceConfig.setVersion("0.0.1");
		referenceConfig.setRegistry(registryConfig);
//		referenceConfig.setUrl("dubbo://127.0.0.1:8888");
		UserService userService =  referenceConfig.get();
    	
    	Object o = userService.getByUserId(1);
		System.out.println(JSON.toJSON(o));
	}
	

}


