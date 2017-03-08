package com.github.cxt.MySpring.validation;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class Abc {
	@Max(value = 2, message="MAX NUB IS {value}")
	@NotNull
	@Name
	private String str1;
	@NotNull(groups=GroupA.class)
	private String str2;
//	@Valid
	
	public String getStr1() {
		return str1;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
	}
	public String getStr2() {
		return str2;
	}
	public void setStr2(String str2) {
		this.str2 = str2;
	}
}
