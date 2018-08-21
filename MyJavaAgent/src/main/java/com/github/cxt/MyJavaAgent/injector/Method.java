package com.github.cxt.MyJavaAgent.injector;

public class Method {

	private String name;
	private String descriptor;
	
	public Method(String name, String descriptor) {
		super();
		this.name = name;
		this.descriptor = descriptor;
	}
	
	public String getName() {
		return name;
	}

	public String getDescriptor() {
		return descriptor;
	}

}
