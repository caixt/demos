package com.github.cxt.MySpring.interceptor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;


public class BeanDefinitionRegistryPostProcessorImpl implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		MyClassPathBeanDefinitionScanner scaner = new MyClassPathBeanDefinitionScanner(registry);
		scaner.addFilter();
		scaner.scan("com.github.cxt.MySpring.interceptor");
	}
}
