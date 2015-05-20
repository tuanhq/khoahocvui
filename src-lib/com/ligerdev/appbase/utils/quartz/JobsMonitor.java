package com.ligerdev.appbase.utils.quartz;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.Trigger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class JobsMonitor extends Thread {

	private static Logger logger = Log4jLoader.getLogger();
	private static int index = -1;
	
	private String lastStringWrote = null;
	private Scheduler scheduler = null;
	
	static {
		if(!new File(BaseUtils.getMyDir() + "./mnt").exists()){
			new File(BaseUtils.getMyDir() + "./mnt").mkdir();
		}
	}

	JobsMonitor(Scheduler scheduler) {
		this.scheduler = scheduler;
		index ++;
	}

	@Override
	public void run() {
		String threadName = Thread.currentThread().getName();
		logger.info(threadName + " is started ...");
		while (true) {
			try {
				process();
				Thread.sleep(5000);
			} catch (Throwable e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
	}

	private void process() {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String all = "";
			for (String groupName : scheduler.getJobGroupNames()) {
				for (String jobName : scheduler.getJobNames(groupName)) {
					Trigger[] triggers = scheduler.getTriggersOfJob(jobName, groupName);
					for (int i = 0; triggers != null && i < triggers.length; i++) {
						
						all += "JobGroupName = " + groupName 
								+ "\nJobName = " + jobName 
								+ "\nTriggerGroup = " + triggers[i].getGroup()
								+ "\nTriggerName = " + triggers[i].getName() + "\n\n";
						Date tmp = new Date();
						for(int k = 0; k < 15; k ++){
							tmp = triggers[i].getFireTimeAfter(tmp);
							if(tmp == null){
								continue;
							}
							String nextTimeStr = dateFormat.format(tmp);
							all += nextTimeStr + "\n";
						}
						all += ".....\n==================\n";
					}
					all += "\n";
					if(all.equals(lastStringWrote)){ 
						return;
					}
					BaseUtils.writeFile(BaseUtils.getMyDir() + "./mnt/jobs_mnt_" + index, all, false);
					lastStringWrote = all;
				}
			}
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
		}
	}
}
