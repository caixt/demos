package com.github.cxt.kryo;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.esotericsoftware.kryo.serializers.VersionFieldSerializer;
import com.esotericsoftware.kryo.serializers.VersionFieldSerializer.Since;;

@DefaultSerializer(VersionFieldSerializer.class)
public class Demo2 {

	private String a;

	@Since(1)
	private Integer b;
	
	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public Integer getB() {
		return b;
	}

	public void setB(Integer b) {
		this.b = b;
	}

	@Override
	public String toString() {
		return "Demo [a=" + a + ", b=" + b + "]";
	}

//	public String getA() {
//		return a;
//	}
//
//	public void setA(String a) {
//		this.a = a;
//	}
//
//	@Override
//	public String toString() {
//		return "Demo2 [a=" + a + "]";
//	}
}
