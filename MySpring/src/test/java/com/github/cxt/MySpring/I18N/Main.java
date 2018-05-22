package com.github.cxt.MySpring.I18N;

import java.util.Date;
import java.util.Locale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Main.Context.class})
public class Main {

	@Autowired
	private MessageSource messageSource;
	
	@Test
	public void test(){
		System.out.println(String.format("%s", 1000));
		System.out.println(messageSource.getMessage("welcome-msg", new Object[]{String.format("%s", 1000), new Date()}, Locale.CHINA));
	}
	
	
	
	public static class Context {
		@Bean
		public MessageSource messageSource() {
			ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
			messageSource.setBasename("messages");
			return messageSource;
		}
	}
}
