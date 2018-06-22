package com.github.cxt.MySpring.transaction.custom;

@Custom
public class CustomDemoImpl implements CustomDemoInterface{

	@Custom
	@Override
	public void test1() {
		System.out.println(CustomAnnotationInterceptor.getInfo());
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
