package com.github.cxt.MyTestProvider;



public class Main {

	public static void main(String args[]) throws Exception { 
		Template<GoodsManager> template = new Template<>("127.0.0.1:2181");
		int port = 8886;
		GoodsManager goodsManager = new GoodsManagerImpl(port); 
		template.register(goodsManager, port);
    } 
}
