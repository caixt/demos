package com.github.cxt.MySpring.quartz;

import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


public class Quartz {
	
	@Test
	public void test() throws SchedulerException, InterruptedException{
		String jobId = "abcd";
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();  
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.start();
		JobDetail jobDetail = JobBuilder.newJob(JobTimer.class)
				.withIdentity(jobId).build();
		JobDataMap map = jobDetail.getJobDataMap();
		map.put("jobId", jobId);
		scheduler.scheduleJob(jobDetail,
				TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
						.build());
		
		Thread.sleep(1000 * 60);
	}
	
}