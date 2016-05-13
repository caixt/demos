package com.github.cxt.Myyaml;

import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSONObject;


public class Main {
	public static void main(String[] args) {
		Yaml yaml = new Yaml();
		Object object = yaml.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("mysql_publichub.yml"));
		JSONObject json = (JSONObject)JSONObject.toJSON(object);
	    System.out.println(json);
	    
	    
	}
}
