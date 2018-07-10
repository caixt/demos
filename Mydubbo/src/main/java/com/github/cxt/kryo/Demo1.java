package com.github.cxt.kryo;

import com.esotericsoftware.kryo.DefaultSerializer;
import de.javakaffee.kryoserializers.CompatibleFieldSerializerReflectionFactorySupport;

@DefaultSerializer(CompatibleFieldSerializerReflectionFactorySupport.class)
public class Demo1 {

	private String a;
	
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
//		return "Demo1 [a=" + a + "]";
//	}

}
