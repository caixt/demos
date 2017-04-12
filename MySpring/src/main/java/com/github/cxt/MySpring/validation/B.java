package com.github.cxt.MySpring.validation;

import org.hibernate.validator.constraints.NotBlank;

public class B {

	@NotBlank
	private String str;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
}
