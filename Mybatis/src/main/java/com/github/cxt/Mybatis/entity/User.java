package com.github.cxt.Mybatis.entity;

import com.alibaba.fastjson.JSONObject;

public class User {
	private Long id;
	private String name;
	private Integer userType;
	private UserType type;
	private String uuid;
	private JSONObject info;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public UserType getType() {
		return type;
	}
	public void setType(UserType type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", userType=" + userType + ", type=" + type + "]";
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public JSONObject getInfo() {
		return info;
	}
	public void setInfo(JSONObject info) {
		this.info = info;
	}
}
