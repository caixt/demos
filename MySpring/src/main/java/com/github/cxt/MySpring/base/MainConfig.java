package com.github.cxt.MySpring.base;

import java.util.Properties;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@PropertySource(value={"classpath:com/github/cxt/MySpring/base/default.properties"}, ignoreResourceNotFound=false)//作用于ConfigurationClassPostProcessor,所以里面的spring.profiles.active有效
@Import(value={MyBeanPostProcessor.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})
public class MainConfig {
	
	
	@Profile("xx")
	@Conditional(ConditionIfAbsent.class)
	@Bean(initMethod="init", destroyMethod="destroyX")
	@Lazy
//	@Scope("prototype")
	public DemoBean demo(){
		return new DemoBean();
	}
	
	@Profile("xx")
	@Conditional(ConditionIfAbsent.class)
	@Bean(initMethod="init", destroyMethod="destroyX")
	public DemoBean demoXXX(){
		return new DemoBean();
	}
	
	@Bean
	@Lazy
	public DemoFactoryBean demoFactoryBean(){
		return new DemoFactoryBean();
	}
	
	@Bean
	public AwareBean awareBean(){
		return new AwareBean();
	}
	
	@Bean
	public BeanDefinitionRegistryPostProcessor beanDefinitionRegistryPostProcessor(){
		return new MyBeanDefinitionRegistryPostProcessor();
	}
	
	@Bean
	public PropertyPlaceholderConfigurer placeholderConfigurer(){
		//refresh#finishBeanFactoryInitialization
		PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
		Properties properties = new Properties();
		properties.setProperty("x1", "x1xxxxxxxxxxxxx");
		properties.setProperty("spring.profiles.active", "xx2222"); //对@Profile 主键不生效
		//propertyPlaceholderConfigurer.setPropertiesArray(propertiesArray);#PropertiesFactoryBean
		propertyPlaceholderConfigurer.setProperties(properties);
		return propertyPlaceholderConfigurer;
	}
	
}
