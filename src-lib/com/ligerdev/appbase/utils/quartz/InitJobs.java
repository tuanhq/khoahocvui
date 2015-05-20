package com.ligerdev.appbase.utils.quartz;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class InitJobs {

	private Scheduler scheduler = null;
	private Logger logger = Log4jLoader.getLogger();

	InitJobs(){
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			JobsMonitor jobsMonitor = new JobsMonitor(scheduler);
			jobsMonitor.start();
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
		}
	}
	
	void createSchedule(int createdIndex, ArrayList<String> listExp, Class clazz, JobValues... values) throws SchedulerException, ParseException {
		for (int i = 0; listExp != null && i < listExp.size(); i++) {
			String exp = listExp.get(i);
			if (BaseUtils.isBlank(exp)) {
				continue;
			}
			String className = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
			String jobName = "JN_" + className + "_" + createdIndex;
			String jobGroup = "JG_" + className + "_" + createdIndex;
			String triggerName = "TN_" + (i + 1) + "_" + className + "_" + createdIndex;
			String triggerGroup = "TG_" + className + "_" + createdIndex;
			
			if (i == 0) {
				JobDetail job = new JobDetail(jobName, jobGroup, clazz);
				JobDataMap map = new JobDataMap();
				for (int k = 0; values != null && k < values.length; k++) {
					map.put(values[k].getKey(), values[k].getValue());
				}
				job.setJobDataMap(map);
				scheduler.addJob(job, true);
			}
			CronTrigger trigger = new CronTrigger(triggerName, triggerGroup, jobName, jobGroup, exp);
			scheduler.scheduleJob(trigger);
		}
	}
	
	boolean hasInstanceRunning(){
		try {
			return scheduler.getCurrentlyExecutingJobs() != null && scheduler.getCurrentlyExecutingJobs().size() > 0;
		} catch (SchedulerException e) {
			return false;
		}
	}

	void clearAllJobs() {
		try {
			List list = scheduler.getCurrentlyExecutingJobs();
			if (list != null && list.size() > 0) {
				logger.info("XmlConfig just be changed, have some job is executing, wait to its done to create new job...");
				while (list != null && list.size() > 0) {
					try {
						Thread.sleep(100);
						list = scheduler.getCurrentlyExecutingJobs();
					} catch (Exception e) {
						logger.info("Exception: " + e.getMessage(), e);
					}
				}
				logger.info("All job done, Clear all old jobs to start new by xml config...");
			}
			for (String groupName : scheduler.getJobGroupNames()) {
				for (String jobName : scheduler.getJobNames(groupName)) {
					Trigger[] triggers = scheduler.getTriggersOfJob(jobName, groupName);
					Date nextFireTime = triggers[0].getNextFireTime();
					logger.info("DELETE JOB: [jobName] : " + jobName + " [groupName] : " + groupName + " - " + nextFireTime);
					scheduler.deleteJob(jobName, groupName);
				}
			}
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
		}
	}
}
