package com.vas.aps.comms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.vas.aps.db.orm.Subscriber;
import com.vas.aps.entity.MoSyntax;

public class AppUtils {
	
	protected static Logger logger = Log4jLoader.getLogger();
	
	private AppUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static String getCdrTblName(Date d){
		String tableName = BaseUtils.formatTime("yyyyMM", System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		
		int dateInt = cal.get(Calendar.DATE);
		if(dateInt < 10){
			tableName += "0X";
			
		} else if(dateInt < 20){
			tableName += "1X";
			
		} else {
			tableName += "2X";
		}
		return tableName;
	}
	
	public static void setLastQuesId(Subscriber subs, String channel, int lastQuesId){
		if(AppConstants.CHANNEL_SMS.equalsIgnoreCase(channel)){
			subs.setLastQuesSms(lastQuesId);
			
		} else if(AppConstants.CHANNEL_WAP.equalsIgnoreCase(channel)) {
			subs.setLastQuesWap(lastQuesId);
		} else {
			subs.setLastQuesWeb(lastQuesId);
		}
	}
	
	public static int getLastQuesId(Subscriber subs, String channel){
		if(AppConstants.CHANNEL_SMS.equalsIgnoreCase(channel)){
			return subs.getLastQuesSms();
			
		} else if(AppConstants.CHANNEL_WAP.equalsIgnoreCase(channel)) {
			return subs.getLastQuesWap();
		} else {
			return subs.getLastQuesWeb();
		}
	}
	
	public static void setAnsweredCount(Subscriber subs, String channel, int answeredCount){
		if(AppConstants.CHANNEL_SMS.equalsIgnoreCase(channel)){
			subs.setAnsweredSms(answeredCount);
			
		} else if(AppConstants.CHANNEL_WAP.equalsIgnoreCase(channel)) {
			subs.setAnsweredWap(answeredCount);
		} else {
			subs.setAnsweredWeb(answeredCount);
		}
	}
	
	public static int getAnsweredCount(Subscriber subs, String channel){
		if(AppConstants.CHANNEL_SMS.equalsIgnoreCase(channel)){
			return subs.getAnsweredSms();
			
		} else if(AppConstants.CHANNEL_WAP.equalsIgnoreCase(channel)) {
			return subs.getAnsweredWap();
		} else {
			return subs.getAnsweredWeb();
		}
	} 
	
	public static boolean isValidTime() {
		boolean temp = true;
		if(temp) {
			// return true;
		}
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		String now = df.format(new Date());
		if (now.compareTo("08:00") >= 0 && now.compareTo("24:00") <= 0) {
			return true;
		}
		return false;
	}
	
	public static void addScore(Subscriber subs, int score){
		try {
			subs.setDayScore(subs.getDayScore() + score);
			subs.setWeekScore(subs.getWeekScore() + score);
			subs.setMonthScore(subs.getMonthScore() + score);
			subs.setTotalScore(subs.getTotalScore() + score);
			
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
		}
	}
	
	public static void resetScore(Subscriber subs, int score){
		try {
			subs.setDayScore(score);
			subs.setWeekScore(score);
			subs.setMonthScore(score);
			subs.setTotalScore(score);
			
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
		}
	}
	
	public static boolean isMatch(MoSyntax moSyntax, String content, StringBuilder cmd) {
		ArrayList<String> list = moSyntax.getListSyntax();
		cmd.append(moSyntax.getCommand());
		
		for (int i = 0; list != null && i < list.size(); i++) {
			String tmp = list.get(i).trim();
			if (tmp.contains("*")) {
				
				if (tmp.startsWith("*") && tmp.endsWith("*")) {
					tmp = tmp.replace("*", "");
					if (content.contains(tmp)) {
						return true;
					}
				} else if (tmp.startsWith("*")) {
					tmp = tmp.replace("*", "");
					if (content.endsWith(tmp)) {
						return true;
					}
				} else if (tmp.endsWith("*")) {
					tmp = tmp.replace("*", "");
					if (content.startsWith(tmp)) {
						return true;
					}
				}
			} else if (tmp.equalsIgnoreCase(content)) {
				return true;
			}
		}
		cmd.setLength(0);
		return false;
	}
}
