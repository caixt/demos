package com.github.cxt.springboot2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@SpringBootApplication
public class Springboot2Application implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(Springboot2Application.class, args);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {//?locale=zh_CN
		registry.addInterceptor(new LocaleChangeInterceptor()).addPathPatterns("/*");
	}
	
	@Bean
	public MessageSource messageSource() {//RequestContextFilter  Accept-Language: zh_CN
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("i18n/messages");
		return messageSource;
	}

	@Bean
	public LocaleResolver localeResolver() {
		return new SessionLocaleResolver();
	}
}
