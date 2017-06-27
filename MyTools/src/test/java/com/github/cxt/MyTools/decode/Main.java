package com.github.cxt.MyTools.decode;

import java.io.File;
import org.junit.Test;

public class Main {

	@Test
	public void test1() throws Exception{
		//bb747b3df3130fe1ca4afa93fb7d97c9
		System.out.println(Hash.getsum("ABCDEFG", "MD5"));
	}
	
	@Test
	public void test2() throws Exception{
		System.out.println(Hash.getsum(new File("pom.xml"), "MD5"));
	}
}
