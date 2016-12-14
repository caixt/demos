package com.github.cxt.MydubboConsumer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.cxt.MyDubboProvider.GoodsManager;


public class Main {
	public static void main(String[] args) {
		// 初始化Spring
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"spring-consumer.xml");
		GoodsManager goodsManager = (GoodsManager) ctx.getBean("goodsService"); // 获取远程服务代理
		String goodsStr = goodsManager.test(); // 执行远程方法
		System.out.println(goodsStr);
	}
}
