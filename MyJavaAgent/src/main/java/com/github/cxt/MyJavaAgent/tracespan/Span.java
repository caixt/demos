package com.github.cxt.MyJavaAgent.tracespan;

import java.util.LinkedList;
import java.util.List;

public class Span {
	
	private String id;
	
	private String name;
	
	private String desc;
	
	private int level;
	
	private long startTime;
	
	private long stopTime;
	
	private List<Span> childrens;
	
	private Span parent;
	
	private boolean success;
	
	public Span(String id, String name, String desc, Span parent) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.parent = parent;
		this.childrens = new LinkedList<>();
		if(this.parent == null){
			this.level = 0;
		}
		else {
			this.level = this.parent.level + 1;
		}
		this.startTime = this.stopTime = -1;
	}
	
	public void start(){
		this.startTime = System.currentTimeMillis();
	}
	
	public void stop(boolean success){
		this.stopTime = System.currentTimeMillis();
		this.success = success;
	}
	
	public void addChild(Span child){
		this.childrens.add(child);
	}

	public Span getParent() {
		return parent;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public int getLevel() {
		return level;
	}

	public List<Span> getChildrens() {
		return childrens;
	}

	public long getStopTime() {
		return stopTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public boolean isSuccess() {
		return success;
	}
	
	public StringBuilder info(){
		StringBuilder sb = new StringBuilder();
		sb.append("name:").append(this.name).append(",")
		.append("desc:").append(desc).append(",")
		.append("time:").append(stopTime - startTime).append(",")
		.append("success:").append(success);
		return sb;
	}
}
