package com.github.cxt.Mybatis.dds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicDataSourceHolder {

	private static Logger logger  = LoggerFactory.getLogger(DynamicDataSourceHolder.class);
	private static ThreadLocal<String> threadlocal = new ThreadLocal<>();
	public static final String DB_MASTER = "master";
	public static final String DB_SLAVE = "slave";
	
	public static String getRouteKey(){
		String routeKey = threadlocal.get();
		if(routeKey == null){
			routeKey = DB_MASTER;
		}
		return routeKey;
	}
	
	public static void setRouteKey(String routeKey){
		logger.info("切换到了{}数据源", routeKey);
		threadlocal.set(routeKey);
	}
	
}
