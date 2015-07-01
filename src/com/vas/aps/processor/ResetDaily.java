package com.vas.aps.processor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.threads.AbsProcessor;
import com.vas.aps.MainAps;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.db.orm.Subscriber;
import com.vas.aps.db.orm.Winner;

public class ResetDaily extends AbsProcessor {

	private MainAps mainApp = MainAps.getInstance();
	private BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	private static ResetDaily instance = null;
	
	private ResetDaily() {
	}
	
	public synchronized static ResetDaily getInstance(){
		if(instance == null){
			instance = new ResetDaily();
		}
		return instance;
	}
	
	@Override
	public void execute() {
		if("00:00".equals(BaseUtils.formatTime("HH:mm", System.currentTimeMillis()))){
			int rs = saveWinner();
			if(rs > 0){
				resetSubs();
			}
			BaseUtils.sleep(2000);
		}
	}

	private int saveWinner() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String today = df.format(System.currentTimeMillis());
		int totalInsert = 0;
		
//		if(cal.get(Calendar.DAY_OF_WEEK) == 2){
//			String sql = "select * from SUBSCRIBER where STATUS = " + AppConstants.SUBS_STATUS_ACTIVE + " ORDER BY WEEK_SCORE desc";
//			ArrayList<Subscriber> listTop = baseDAO.getListBySql("GetTopWeekly", Subscriber.class, sql, 0, 5);
//			for(Subscriber subs : listTop){
//				Winner winner = new Winner(0, subs.getMsisdn(), subs.getDayScore(), subs.getWeekScore(), subs.getMonthScore(), 
//						subs.getTotalScore(), "WEEKLY", today, null);
//				totalInsert += baseDAO.insertBean("InsertWinner", winner);
//			}
//		}
		if(cal.get(Calendar.DATE) == 1){
			String sql = "select * from SUBSCRIBER where STATUS = " + AppConstants.SUBS_STATUS_ACTIVE + " ORDER BY MONTH_SCORE desc";
			ArrayList<Subscriber> listTop = baseDAO.getListBySql("GetTopMonthly", Subscriber.class, sql, 0, 5);
			for(Subscriber subs : listTop){
				Winner winner = new Winner(0, subs.getMsisdn(), subs.getDayScore(), subs.getWeekScore(), subs.getMonthScore(), 
						subs.getTotalScore(), "MONTHLY", today, null);
				totalInsert += baseDAO.insertBean("InsertWinner", winner);
			}
		}
		return totalInsert;
	}

	private void resetSubs() {
		String sql = "update SUBSCRIBER set LAST_QUES_SMS = 0, LAST_QUES_WAP = 0, " +
				"ANSWERED_SMS = 0, ANSWERED_WAP = 0, ANSWERED_WEB = 0, DAY_SCORE = 0";
		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.DAY_OF_WEEK) == 2){
			sql += ", WEEK_SCORE = 0";
		}
		if(cal.get(Calendar.DATE) == 1){
			sql += ", MONTH_SCORE = 0";
		}
		baseDAO.execSql("ResetDaily", sql);
	}

	@Override
	public int sleep() {
		return 500;
	}

	@Override
	public void exception(Throwable e) {
	}
}
