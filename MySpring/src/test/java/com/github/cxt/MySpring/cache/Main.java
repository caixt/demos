package com.github.cxt.MySpring.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/github/cxt/MySpring/cache/spring-context-cache.xml")
public class Main {

	@Autowired
	private Work work;
	@Test
	public void test() {
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
