package com.github.cxt.springboot2.entities;

import lombok.Getter;

@Getter
public class R {
	
	private Boolean success;
	
	private String msg;

	public static R success() {
		R r = new R();
		r.success = true;
		return r;
	}
	
	public static R fail(String msg) {
		R r = new R();
		r.success = false;
		r.msg = msg;
		return r;
	}
}
