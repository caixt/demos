package com.github.cxt.MySpring.regix;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

public class Main {
	
	@Test
	public void test0(){
		Pattern pattern = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
		Matcher matcher = pattern.matcher("http://www.baidu.com/test/aaa?b=1");
		while(matcher.find()){
			System.out.println(matcher.group(0));
			System.out.println(matcher.group(1));
			System.out.println(matcher.group(2));
		}
	}
	
	@Test
	public void test1(){
		Pattern pattern = Pattern.compile("href=\"(.+?)\"");
		Matcher matcher = pattern.matcher("<a href=\"index.html\" class=\"test\">主页</a>");
		while(matcher.find()){
		  System.out.println(matcher.group(1));
		}
	}
	
	@Test
	public void test2(){
		Pattern pattern = Pattern.compile("Windows(?:95|98|NT|2000)");
		Matcher matcher = pattern.matcher("Windows2000");
		while(matcher.find()){
			System.out.println(matcher.group(0));
		}
	}
	
	@Test
	public void test3(){
		Pattern pattern = Pattern.compile("Windows(?=95|98|NT|2000)");
		Matcher matcher = pattern.matcher("Windows2000");
		while(matcher.find()){
			System.out.println(matcher.group(0));
		}
	}
	
	@Test
	public void test4(){
		Pattern pattern = Pattern.compile("Windows(?!95|98|NT|2000)");
		Matcher matcher = pattern.matcher("Windows2001");
		while(matcher.find()){
			System.out.println(matcher.group(0));
		}
	}
	
	@Test
	public void test5(){
		Pattern pattern = Pattern.compile("(?<=95|98|NT|2000)Windows");
		Matcher matcher = pattern.matcher("2000Windows");
		while(matcher.find()){
			System.out.println(matcher.group(0));
		}
	}
	
	@Test
	public void test6(){
		Pattern pattern = Pattern.compile("(?<!95|98|NT|2000)Windows");
		Matcher matcher = pattern.matcher("2001Windows");
		while(matcher.find()){
			System.out.println(matcher.group(0));
		}
	}

	@Test
	public void test(){
		Pattern pattern = Pattern.compile("\\$\\{(\\w+?)\\}");
		Matcher matcher = pattern.matcher("aaa${abc}ddddd");
		while(matcher.find()){
			System.out.println(matcher.group(0));
			System.out.println(matcher.group(1));
		}
	}
	
//	@Test
//	public void test_1(){
//		Pattern pattern = Pattern.compile("\\$\\{(\\[\\d+\\]\\.\\w+|\\w+)\\}");
//		Matcher matcher = pattern.matcher("aaa${ba}aac${[2].bb}aaaaaa");
//		while(matcher.find()){
//			
//			System.out.println(matcher.group(0));
//			System.out.println(matcher.group(1));
//		}
//	}

//	@Test
//	public void test_1(){
//		Pattern pattern = Pattern.compile("\\$\\{(\\w+)\\}");
//		Matcher matcher = pattern.matcher("aaa${ba}aac${[2].bb}aaaaaa");
//		while(matcher.find()){
//			matcher.re
//			
//			System.out.println(matcher.group(0));
//			System.out.println(matcher.group(1));
//		}
//	}
}
