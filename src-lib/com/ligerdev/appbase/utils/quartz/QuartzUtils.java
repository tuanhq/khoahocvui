package com.ligerdev.appbase.utils.quartz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class QuartzUtils {

	private static Logger logger = Log4jLoader.getLogger();
	private ArrayList<Hashtable<String, Object>> listData = new ArrayList<Hashtable<String,Object>>();
	private InitJobs initJobs = new InitJobs();
	private ArrayList<JobInfoBean> allListExp = new ArrayList<JobInfoBean>();
	
	public static boolean isFirstFireInDay(JobExecutionContext arg0){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date fireTime = arg0.getFireTime();
		Date nextTime = arg0.getTrigger().getFireTimeAfter(fireTime);
		while(df.format(fireTime).equals(nextTime)){
			nextTime = arg0.getTrigger().getFireTimeAfter(nextTime);
		}
		df = new SimpleDateFormat("HHmmss");
		return df.format(fireTime).equals(nextTime);
	} 
	
	public static boolean isLastFireInDay(JobExecutionContext arg0){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date fireTime = arg0.getFireTime();
		Date nextTime = arg0.getTrigger().getFireTimeAfter(fireTime);
		return !df.format(fireTime).equals(nextTime);
	}
	
	public static boolean isJobRunning(JobExecutionContext ctx, Job jobTmp) throws SchedulerException {
		@SuppressWarnings("unchecked")
		List<JobExecutionContext> jobs = ctx.getScheduler().getCurrentlyExecutingJobs();
		for (JobExecutionContext job : jobs) {
			if (job.getJobInstance() != jobTmp 
					&& job.getJobDetail().getJobClass().getName().equals(jobTmp.getClass().getName()) 
					&& job.getJobDetail().getFullName().equals(ctx.getJobDetail().getFullName())) {
				return true;
			}
		}
		return false;
	}
	
	public void put(String exp, Class clazz, JobValues ... values){
		ArrayList<String> listExp = new ArrayList<String>();
		listExp.add(exp);
		put(listExp, clazz, values);
	}
	
	public void put(ArrayList<String> listExp, Class clazz, JobValues ... values){
		allListExp.add(new JobInfoBean(listExp, clazz, values)); 
		put0(listExp, clazz, values);
	}
	
	private void put0(ArrayList<String> listExp, Class clazz, JobValues ... values){
		Hashtable<String, Object> data = new Hashtable<String, Object>();
		if(listExp == null || listExp.size() == 0){
			logger.info("Expression blank, class job = " + clazz.getName());
		} 
		if(listExp != null){
			data.put("listExp", listExp);
		}
		data.put("clazz", clazz);
		if(values != null && values.length > 0){
			data.put("values", values);
		}
		listData.add(data);
	}
	
	public void start() throws SchedulerException, ParseException{ 
		for(int i = 0; i < listData.size(); i ++){
			Hashtable<String, Object> data = listData.get(i);
			ArrayList<String> listExp = (ArrayList<String>) data.get("listExp");
			Class clazz = (Class) data.get("clazz");
			if(listExp == null || listExp.size() == 0){
				logger.info("Can not create schedule " + clazz.getName() + ", list exp is blank ...");
				continue;
			}
			JobValues[] values = (JobValues[]) data.get("values");
			initJobs.createSchedule(i + 1, listExp, clazz, values);
			String exps = "";
			for(String exp: listExp){
				exps += "|" + exp;
			}
			exps = exps.replaceFirst("|", "");
			logger.info("Created schedule: " + clazz.getName() + ", exps: " + exps); 
		}
	}
	
	public boolean hasInstanceRunning(){
		return initJobs.hasInstanceRunning();
	}
	
	public void restart() throws SchedulerException, ParseException{
		if(listData.size() > 0){
			initJobs.clearAllJobs();
			listData.clear();
			for(JobInfoBean bean: allListExp){
				put0(bean.getListExp(), bean.getClazz(), bean.getValues());
			}
			start();
		}
	}

	public static int translate(ArrayList<String> listStr, ArrayList<String> list) {
		if(listStr == null || listStr.size() == 0){
			if(list == null || list.size() == 0){
				return 1;
			}
			list.clear();
			return 0;
		}
		if(listStr.equals(list)){
			return 1;
		}
		list.clear();
		for(String s : listStr){
			list.add(s);
		}
		return 0;
	}
	
	public static void main(String[] args) {
		ArrayList<String> list1 = new ArrayList<String>();
		list1.add("A");
		list1.add("B");
		
		ArrayList<String> list2 = new ArrayList<String>();
		list2.add("A");
		list2.add("B");
		System.out.println(list1.equals(list2));
	}
}
