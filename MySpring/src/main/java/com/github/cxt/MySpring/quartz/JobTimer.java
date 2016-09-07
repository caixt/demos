package com.github.cxt.MySpring.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobTimer implements Job{
	
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap map = context.getJobDetail().getJobDataMap();
		String jobId = (String) map.get("jobId");
		logger.info(jobId);
		 try {
			System.out.println(context.getScheduler().getContext().get("applicationContext"));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
