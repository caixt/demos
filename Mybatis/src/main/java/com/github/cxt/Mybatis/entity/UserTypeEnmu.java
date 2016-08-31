package com.github.cxt.Mybatis.entity;

public enum UserTypeEnmu {
	CODE1(1),
	CODE2(2);
	
	private Integer val;
	
	private UserTypeEnmu(Integer val){
		this.val = val;
	}

	public Integer getVal() {
		return val;
	}
}
