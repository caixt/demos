package com.github.cxt.springboot2.entities;

import javax.validation.constraints.NotNull;

public class UserType {
    @NotNull
	private Integer id;
	private String code;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "UserType [id=" + id + ", code=" + code + "]";
	}
}
