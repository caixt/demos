package com.github.cxt.MySpring.quartz;

import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


public class Quartz {
	
	@Test
	public void test1() throws SchedulerException, InterruptedException{
		String jobId = "abcd";
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();  
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.start();
		JobDetail jobDetail = JobBuilder.newJob(JobTimer.class)
				.withIdentity("jobId", "group").build();
		JobDataMap map = jobDetail.getJobDataMap();
		map.put("jobId", jobId);
		scheduler.scheduleJob(jobDetail,
				TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
						.build());
		System.out.println(scheduler.checkExists(new JobKey("jobId", "group")));
		Thread.sleep(1000 * 60 * 2);
	}
	
	@Test
	public void tes2() throws SchedulerException, InterruptedException{
		String jobId = "abcd";
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();  
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.start();
		JobDetail jobDetail = JobBuilder.newJob(JobTimer.class)
				.withIdentity("jobId", "group").build();
		JobDataMap map = jobDetail.getJobDataMap();
		map.put("jobId", jobId);
		scheduler.scheduleJob(jobDetail, TriggerBuilder.newTrigger().startNow().withSchedule(
				SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build());
		System.out.println(scheduler.checkExists(new JobKey("jobId", "group")));
		Thread.sleep(1000 * 60 * 2);
	}
}