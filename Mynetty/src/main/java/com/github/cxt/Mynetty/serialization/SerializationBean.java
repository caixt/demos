package com.github.cxt.Mynetty.serialization;

import java.io.Serializable;

public class SerializationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3235432002462705915L;
	
	private int age;
	private String name;
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
