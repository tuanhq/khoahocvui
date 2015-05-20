package com.ligerdev.appbase.utils.quartz;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public  abstract class AbsJob implements Job{

	protected static Logger logger = Log4jLoader.getLogger();
	protected String transid = "inv";
	
	// @Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		if(isSingleInstance()){
			try {
				if(QuartzUtils.isJobRunning(ctx, this)){
					logger.info("There's another instance running => ignore");
					return;
				}
			} catch (SchedulerException e) {
				logger.info("Exception: " + e.getMessage());
			}
		} 
		try {
			execute0(ctx);
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
		}
	}

	protected abstract boolean isSingleInstance();
	protected abstract void execute0(JobExecutionContext ctx);
}
