package com.github.cxt.MySpring.transaction.custom;

import org.springframework.aop.framework.AopContext;

@Custom
public class CustomDemoImpl implements CustomDemoInterface{

	@Custom
	@Override
	public void test1() {
		System.out.println(CustomAnnotationInterceptor.getInfo());
		this.test2();
		CustomDemoInterface c = (CustomDemoInterface) AopContext.currentProxy();//代理类本身，可使得里面的注解生效
		System.out.println(this == c);
		c.test2();//由于localInfo的信息是覆盖的。所以不能放在前面执行
	}

	@Custom(name="haha")
	@Override
	public void test2() {
		System.out.println(CustomAnnotationInterceptor.getInfo());
	}

	@Override
	public void test3() {
		System.out.println(CustomAnnotationInterceptor.getInfo());
	}

	@Override
	public void test4() {
		System.out.println(CustomAnnotationInterceptor.getInfo());
	}
}
