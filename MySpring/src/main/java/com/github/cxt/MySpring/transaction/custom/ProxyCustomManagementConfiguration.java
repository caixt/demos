package com.github.cxt.MySpring.transaction.custom;

import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;


@Configuration
public class ProxyCustomManagementConfiguration implements ImportAware{
	protected AnnotationAttributes enableTx;
	
	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		this.enableTx = AnnotationAttributes.fromMap(
				importMetadata.getAnnotationAttributes(EnableCustomManagement.class.getName(), false));
		if (this.enableTx == null) {
			throw new IllegalArgumentException(
					"@EnableTransactionManagement is not present on importing class " + importMetadata.getClassName());
		}
	}
	
	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public PointcutAdvisor customAdvisor(StaticMethodMatcherPointcut pointcut, CustomAnnotationInterceptor interceptor) {
		DefaultBeanFactoryPointcutAdvisor advisor = new DefaultBeanFactoryPointcutAdvisor();
		advisor.setPointcut(pointcut);
		advisor.setAdvice(interceptor);
		advisor.setOrder(this.enableTx.<Integer>getNumber("order"));
		return advisor;
	}
	
	@Bean
	public CustomAttributeSource customAttributeSource(){
		return new CustomAttributeSource();
	}
	
	@Bean
	public CustomAnnotationInterceptor customInterceptor(CustomAttributeSource customAttributeSource) {
		CustomAnnotationInterceptor interceptor = new CustomAnnotationInterceptor();
		interceptor.setCustomAttributeSource(customAttributeSource);
		return interceptor;
	}
	
	@Bean
	public StaticMethodMatcherPointcut staticMethodMatcherPointcut(CustomAttributeSource customAttributeSource){
		ProxyCustomStaticMethodMatcherPointcut p =  new ProxyCustomStaticMethodMatcherPointcut();
		p.setCustomAttributeSource(customAttributeSource);
		return p;
	}
}
