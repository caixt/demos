package com.github.cxt.MySpring.base;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.StringValueResolver;

public class AwareBean implements ApplicationContextAware,BeanNameAware,EmbeddedValueResolverAware, BeanFactoryAware{

	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		System.out.println("${x1}:" + resolver.resolveStringValue("${x1}"));
	}

	@Override
	public void setBeanName(String name) {
		System.out.println("beanName:" + name);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("applicationContext:" + applicationContext);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("BeanFactory:" + beanFactory);
	}

	@Value("${x1}")
	private String xxx;

	@Autowired
	public void setValue(@Value("${x1}")String value) {
		System.out.println("${x1}" + "!" + xxx);
	}

}
