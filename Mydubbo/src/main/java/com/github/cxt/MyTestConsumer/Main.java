package com.github.cxt.MyTestConsumer;

import com.github.cxt.MyTestProvider.Template;
import com.github.cxt.MyTestProvider.GoodsManager;

public class Main {

    public static void main(String args[]) throws Exception{ 
    	Template<GoodsManager> template = new Template<>("127.0.0.1:2181");
    	while(true){
    		try{
	    		GoodsManager goodsManager = template.get();
	    		System.out.println(goodsManager.test());
    		}catch(Exception e){
    			e.printStackTrace();
    		}finally {
    			Thread.sleep(3000);
			}
    	}
    } 
}
