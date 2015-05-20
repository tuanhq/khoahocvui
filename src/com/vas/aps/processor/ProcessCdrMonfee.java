package com.vas.aps.processor;

import java.util.Calendar;
import java.util.Date;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.textbase.SyncStrManager;
import com.ligerdev.appbase.utils.threads.AbsProcessor;
import com.ligerdev.appbase.utils.traffic.SendTraffic;
import com.vas.aps.MainAps;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.comms.AppUtils;
import com.vas.aps.comms.XmlConfigs;
import com.vas.aps.db.orm.ChargeReminder;
import com.vas.aps.db.orm.MtHis;
import com.vas.aps.db.orm.Question;
import com.vas.aps.db.orm.ScoreHis;
import com.vas.aps.db.orm.Subscriber;
import com.vas.aps.tablecache.QuestionFactory;

public class ProcessCdrMonfee extends AbsProcessor {

	private MainAps mainApp = MainAps.getInstance();
	private BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	protected static SyncStrManager syncStrManager = SyncStrManager.getInstance("AbstractCMD");
	
	@Override
	public void execute() {
		ChargeReminder obj = (ChargeReminder) mainApp.getChargeReminderQueue().removeFirst();
		SendTraffic.getInstance("ProcessCdrMonfee").limitTraffic(25);
		
		Object objSynch = syncStrManager.createSynchObject(obj.getMsisdn());
		synchronized (objSynch) {
			execute0(obj);
		}
	}
	
	public void execute0(ChargeReminder obj) {
		if(obj.getTransId() == null){
			obj.setTransId("inv");
		}
		if(obj.getType() == AppConstants.REMINDER_TYPE_RETURN_QUES){
			logger.info(obj.getTransId() + ", ScanCDR renew"); 
			
			if(AppUtils.isValidTime()){
				
				//add tra loi cau hoi neu gia han trong ngay
				addVsReturnQues(obj);
			} else {
				//add them diem neu gia han trong khoang thoi gian ko phu hop(coi nhu khong tra loi)
				addScoreForMonfee(obj);
			}
		} else if(obj.getType() == AppConstants.REMINDER_TYPE_ADD_SCORE){
			addScoreForCMS(obj);
			
		} else {
			logger.info(obj.getTransId() + ", wrong type of reminder => ignore");
		}
	}

	private void addScoreForMonfee(ChargeReminder obj) {
		Subscriber subs = baseDAO.getBeanByKey(obj.getTransId(), Subscriber.class, obj.getMsisdn());
		if(subs != null){
			AppUtils.addScore(subs, XmlConfigs.Score.RENEW);
			baseDAO.updateBeanFields(obj.getTransId(), subs, true, "DAY_SCORE", "WEEK_SCORE", "MONTH_SCORE", "TOTAL_SCORE");
			
			ScoreHis scoreHis = new ScoreHis(0, obj.getMsisdn(), obj.getAddScore(), subs.getDayScore(), subs.getWeekScore(), 
					subs.getMonthScore(), subs.getTotalScore(), AppConstants.SCORE_HIS_REASON_RENEW, null, obj.getTransId());
			mainApp.getScoreHisQueue().addLast(scoreHis);
		} else {
			logger.info(obj.getTransId() + ", subs is null => ignore");
		}
	}

	private void addScoreForCMS(ChargeReminder obj) {
		Subscriber subs = baseDAO.getBeanByKey(obj.getTransId(), Subscriber.class, obj.getMsisdn());
		if(subs == null){
			logger.info(obj.getTransId() + "; subs == null => return");
			return;
		}
		logger.info(obj.getTransId() + ", addScore(): " + obj.getAddScore() + " for " + obj.getMsisdn());
		
		AppUtils.addScore(subs, obj.getAddScore());
		baseDAO.updateBeanFields(obj.getTransId(), subs, true, "DAY_SCORE", "WEEK_SCORE", "MONTH_SCORE", "TOTAL_SCORE");
		
		ScoreHis scoreHis = new ScoreHis(0, obj.getMsisdn(), obj.getAddScore(), subs.getDayScore(), subs.getWeekScore(), 
				subs.getMonthScore(), subs.getTotalScore(), AppConstants.SCORE_HIS_REASON_CMS, null, obj.getTransId());
		mainApp.getScoreHisQueue().addLast(scoreHis);
	}

	private void addVsReturnQues(ChargeReminder obj) {
		Subscriber subs = baseDAO.getBeanByKey(obj.getTransId(), Subscriber.class, obj.getMsisdn());
		if(subs == null){
			logger.info(obj.getTransId() + ", subs == null => return");
			return;
		}
		if(subs.getStatus() == AppConstants.SUBS_STATUS_DEACTIVE){
			logger.info(obj.getTransId() + ", subs unregistered => return");
			return;
		}
		logger.info(obj.getTransId() + ", returnQues(): " + obj.getAddScore() + " for " + obj.getMsisdn());
		AppUtils.addScore(subs, obj.getAddScore());

		Date expireTime = BaseUtils.addTime(new Date(), Calendar.DATE, 1);
		expireTime = BaseUtils.truncDate(expireTime);
		expireTime = BaseUtils.addTime(expireTime, Calendar.SECOND, -1);
		subs.setExpireTime(expireTime);
		
		if(subs.getIgnoreQuestion() == 1){
			logger.info(obj.getTransId() + ", subs have denied receiving question daily => ignore return new question");
			
		} else if(subs.getLastQuesSms() == 0){
			logger.info(obj.getTransId() + ", msisdn = " + subs.getMsisdn() + ", last quest id == 0 => return question");
			Question question = QuestionFactory.getQuestion(obj.getTransId(), subs, AppConstants.CHANNEL_SMS);
			String content = question.getContent();
			MtHis mt = new MtHis(obj.getTransId(), 0, obj.getMsisdn(), content, 0, "QT-" + question.getId(), null, null);
			mainApp.getMtQueue().addLast(mt);
		}
		ScoreHis scoreHis = new ScoreHis(0, obj.getMsisdn(), obj.getAddScore(), subs.getDayScore(), subs.getWeekScore(), 
				subs.getMonthScore(), subs.getTotalScore(), AppConstants.SCORE_HIS_REASON_RENEW, null, obj.getTransId());
		mainApp.getScoreHisQueue().addLast(scoreHis);
		
		baseDAO.updateBean(obj.getTransId(), subs);
		
	}

	@Override
	public int sleep() {
		return 0;
	}

	@Override
	public void exception(Throwable e) {
	}
}
