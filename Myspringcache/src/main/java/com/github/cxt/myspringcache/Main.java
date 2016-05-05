package com.github.cxt.myspringcache;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		ApplicationContext ctx =  new ClassPathXmlApplicationContext("spring-context.xml");
		Work work = ctx.getBean(Work.class);
		String code = "aaa";
		System.out.println(work.save(code));
		System.out.println(work.readByCache(code));
		System.out.println(work.readByCache(code));
		System.out.println(work.update(code));
		System.out.println(work.readByCache(code));
		System.out.println(work.readByCache(code));
		System.out.println("-----------------------------");
		code = "bbb";
		System.out.println(work.readByCache(code));
		System.out.println(work.readByCache(code));
		System.out.println("-------------clean all ------");
		System.out.println(work.cleanAll());
		System.out.println(work.readByCache(code));
	}

}
