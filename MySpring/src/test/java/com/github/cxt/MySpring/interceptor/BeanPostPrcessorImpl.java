package com.github.cxt.MySpring.interceptor;

import org.springframework.beans.BeansException;  
import org.springframework.beans.factory.config.BeanPostProcessor;  
  
public class BeanPostPrcessorImpl implements BeanPostProcessor {  
   
    // Bean 实例化之前执行该方法  
    public Object postProcessBeforeInitialization(Object bean, String beanName)  
           throws BeansException {  
       System.out.println( beanName + "开始实例化");  
       return bean;  
    }  
   
    // Bean 实例化之后执行该方法  
    public Object postProcessAfterInitialization(Object bean, String beanName)  
           throws BeansException {  
       System.out.println( beanName + "实例化完成");  
       return bean;  
    }  
}  