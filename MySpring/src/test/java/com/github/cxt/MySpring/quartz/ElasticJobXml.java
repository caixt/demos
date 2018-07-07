package com.github.cxt.MySpring.quartz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ElasticJobXml.Context.class })
public class ElasticJobXml {

	@Test
	public void test() throws InterruptedException{
		Thread.sleep(Long.MAX_VALUE);
		
		
	}
	
	
	@ImportResource("classpath:/com/github/cxt/MySpring/quartz/elastic-job.xml")
	public static class Context{
		
	}
}
