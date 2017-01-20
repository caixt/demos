package com.github.cxt.Myyaml;

import java.util.List;
import java.util.Map;

public class YamlData {
	private Integer num_integer;
	
	private Long num_long;
	
	private Float num_float;
	
	private Double num_double;

	private String message;
	
	private Map<String, Object> map;
	
	private List<Object> list;
	
	private YamlSubData subData;
	
	
	
	

	public Integer getNum_integer() {
		return num_integer;
	}

	public void setNum_integer(Integer num_integer) {
		this.num_integer = num_integer;
	}

	public Long getNum_long() {
		return num_long;
	}

	public void setNum_long(Long num_long) {
		this.num_long = num_long;
	}

	public Float getNum_float() {
		return num_float;
	}

	public void setNum_float(Float num_float) {
		this.num_float = num_float;
	}

	public Double getNum_double() {
		return num_double;
	}

	public void setNum_double(Double num_double) {
		this.num_double = num_double;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

	public YamlSubData getSubData() {
		return subData;
	}

	public void setSubData(YamlSubData subData) {
		this.subData = subData;
	}
	
}
