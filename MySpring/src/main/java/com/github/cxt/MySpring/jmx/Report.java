package com.github.cxt.MySpring.jmx;

import java.util.concurrent.atomic.AtomicLong;


public class Report implements ReportMBean{
	
	public Report(){
		initTime = System.currentTimeMillis();
		send = new AtomicLong();
	}
	
	//初始化时间
	private long initTime;

	//总次数
	private AtomicLong send;
	

	public void addCount(long add){
		send.addAndGet(add);
	}

	@Override
	public long getCount() {
		return send.get();
	}


	@Override
	public long getInitTime() {
		return initTime;
	}
}
