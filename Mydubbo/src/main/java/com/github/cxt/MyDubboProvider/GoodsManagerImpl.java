package com.github.cxt.MyDubboProvider;


public class GoodsManagerImpl implements GoodsManager{

	public String test() {
		System.out.println("!!!!!!!!!");
		//throw new RuntimeException("test3..........");
		return "result....";
	}
}
