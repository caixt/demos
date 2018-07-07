package com.github.cxt.MySpring.quartz;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ElasticJobAnnotation.Context.class })
public class ElasticJobAnnotation {

	@Test
	public void test() throws InterruptedException{
		Thread.sleep(Long.MAX_VALUE);
	}
	
	
	public static class Context{
		@Bean
	    DataSource dataSource(){
			BasicDataSource ds = new BasicDataSource();
	        ds.setDriverClassName("com.mysql.jdbc.Driver");
	        ds.setUrl("jdbc:mysql://127.0.0.1:3306/mybatis?useUnicode=true&amp;characterEncoding=UTF-8");
	        ds.setUsername("root");
	        ds.setPassword("xiantong");
	        return ds;
	    }
		
	    @Bean(initMethod = "init")
	    public ZookeeperRegistryCenter regCenter() {
	        return new ZookeeperRegistryCenter(new ZookeeperConfiguration("127.0.0.1:2181", "elastic-job-example-lite-spring"));
	    }
	    
	    @Bean
	    public JobEventConfiguration jobEventConfiguration(DataSource dataSource) {
	        return new JobEventRdbConfiguration(dataSource);
	    }
	    
	    @Bean(initMethod = "init")
	    public JobScheduler simpleJobScheduler(ZookeeperRegistryCenter regCenter, JobEventConfiguration jobEventConfiguration) {
	    	SimpleJob simpleJob = new SpringSimpleJob();
	    	LiteJobConfiguration conf = LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(JobCoreConfiguration.newBuilder(
	    			simpleJob.getClass().getName(), "0/5 * * * * ?", 1).build(), simpleJob.getClass().getCanonicalName())).overwrite(true).build();
	        return new SpringJobScheduler(simpleJob, regCenter, conf, jobEventConfiguration);
	    }
	    
	    @Bean(initMethod = "init")
	    public JobScheduler dataflowJobScheduler(ZookeeperRegistryCenter regCenter, JobEventConfiguration jobEventConfiguration) {
	    	DataflowJob<Integer> dataflowJob = new SpringDataflowJob();
	    	//streamingProcess为true会一直跑数据,除非不在返回数据。等待下一个调度
	    	LiteJobConfiguration conf = LiteJobConfiguration.newBuilder(new DataflowJobConfiguration(JobCoreConfiguration.newBuilder(
	    			 dataflowJob.getClass().getName(), "0/5 * * * * ?", 3).build(), dataflowJob.getClass().getCanonicalName(), false)).overwrite(true).build();
	    	return new SpringJobScheduler(dataflowJob, regCenter, conf, jobEventConfiguration);
	    }
	}
}
