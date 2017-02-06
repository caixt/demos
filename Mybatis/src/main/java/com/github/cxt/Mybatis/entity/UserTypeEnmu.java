package com.github.cxt.Mybatis.entity;

public enum UserTypeEnmu {
	CODE1(1),
	CODE2(2);
	
	public final static int TYPE = 2;
	
	private Integer val;
	
	private UserTypeEnmu(Integer val){
		this.val = val;
	}

	public Integer getVal() {
		return val;
	}
}
