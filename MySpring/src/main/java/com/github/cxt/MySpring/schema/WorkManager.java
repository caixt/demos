package com.github.cxt.MySpring.schema;

import org.springframework.beans.factory.annotation.Autowired;


public class WorkManager {
	
	@Autowired
	private Work work;


	@Override
	public String toString() {
		return "WorkManager [ work=" + work + "]";
	}
}
