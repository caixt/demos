package com.github.cxt.MySpring.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public class JobTimer implements Job{

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap map = context.getJobDetail().getJobDataMap();
		String jobId = (String) map.get("jobId");
		System.out.println(jobId);
		 try {
			System.out.println(context.getScheduler().getContext().get("applicationContext"));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
