package com.github.cxt.springmvc.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonView;

public class User {
	
	public interface IdView {};  
	@JsonView(IdView.class) 
	private String id;
	@Size(min=2, max=30)
	@NotBlank
	private String name;
	@Valid
	@NotNull
	private UserType type;
	@NotNull
	private JSONObject info;
	@Size(min=1)
	@NotNull
	private String[] classes;
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UserType getType() {
		return type;
	}
	public void setType(UserType type) {
		this.type = type;
	}
	public JSONObject getInfo() {
		return info;
	}
	public void setInfo(JSONObject info) {
		this.info = info;
	}
	public String[] getClasses() {
		return classes;
	}
	public void setClasses(String[] classes) {
		this.classes = classes;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
