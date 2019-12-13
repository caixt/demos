package com.github.cxt.MySpring.base;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		//指定Bean定义信息；（Bean的类型，Bean。。。）
		RootBeanDefinition beanDefinition = new RootBeanDefinition(X2.class);
		//注册一个Bean，指定bean名
		registry.registerBeanDefinition("x2", beanDefinition);
	}

}
