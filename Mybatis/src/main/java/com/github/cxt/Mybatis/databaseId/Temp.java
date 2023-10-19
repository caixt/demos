package com.github.cxt.Mybatis.databaseId;

import java.util.Date;

public class Temp {

	private Integer id;
	
	private Date x1;
	
	private String x2;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getX1() {
		return x1;
	}

	public void setX1(Date x1) {
		this.x1 = x1;
	}

	public String getX2() {
		return x2;
	}

	public void setX2(String x2) {
		this.x2 = x2;
	}

	@Override
	public String toString() {
		return "Temp [id=" + id + ", x1=" + x1 + ", x2=" + x2 + "]";
	}
}
