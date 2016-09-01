package com.github.cxt.MySpring.quartz;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/github/cxt/MySpring/quartz/spring-content.xml")
@ActiveProfiles("spring")
public class SpringQuartz {
	
	@Autowired
	private Scheduler scheduler;

	@Test
	public void test() throws SchedulerException, InterruptedException{
		String jobId = "abcd";
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