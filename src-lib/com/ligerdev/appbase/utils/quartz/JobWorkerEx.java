package com.ligerdev.appbase.utils.quartz;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class JobWorkerEx implements Job {

	private static Logger logger = Log4jLoader.getLogger();
	
	// @Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			if (QuartzUtils.isJobRunning(arg0, this)) { 
				logger.info("Have another executing instance of this job => return to do nothing");
				return;
			}
		} catch (SchedulerException e) {
			logger.info("Exception: " + e.getMessage(), e);
		}
		try {
			process(arg0);
		} catch (Throwable e) {
			logger.info("Exception: " + e.getMessage(), e);
		}
	}

	private void process(JobExecutionContext arg0) {
		
	}
}
