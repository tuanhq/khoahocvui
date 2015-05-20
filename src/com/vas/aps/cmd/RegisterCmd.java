package com.vas.aps.cmd;

import java.util.Calendar;
import java.util.Date;

import com.ligerdev.appbase.utils.BaseUtils;
import com.vas.aps.api.CmdResult;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.comms.AppUtils;
import com.vas.aps.comms.XmlConfigs;
import com.vas.aps.db.orm.CdrHis;
import com.vas.aps.db.orm.MtHis;
import com.vas.aps.db.orm.Question;
import com.vas.aps.db.orm.ScoreHis;
import com.vas.aps.db.orm.Subscriber;
import com.vas.aps.tablecache.MessageFactory;
import com.vas.aps.tablecache.QuestionFactory;

public class RegisterCmd extends AbstractCmd {

	@Override
	public CmdResult execute0() throws Exception {
		Subscriber subs = baseDAO.getBeanByKey(mo.getTransId(), Subscriber.class, mo.getMsisdn());
		resultCmd.setSubscriber(subs);
		
		if (subs != null && subs.getStatus() == AppConstants.SUBS_STATUS_ACTIVE) {
			// mất đồng bộ với MPS (vì nếu đang active mà đk thì mps sẽ ko forward sang CP, trường hợp forward sang nghĩa là mất đồng bộ)
			// phản hồi giá trị như với TH giao dịch thành công
			
			logger.info(mo.getTransId() + ", subs is activing");
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_REGISTER_SECOND_TODAY, subs);
			mainApp.getMtQueue().addLast(mt);
			
			resultCmd.addMt(mt);
			return resultCmd;
		}
		if(subs == null){
			return fistRegister();
		}
		return reRegister(subs);
	}

	private CmdResult fistRegister() {
		
		logger.info(mo.getTransId() + ", subs not exist in DB => first register");
		Subscriber subs = new Subscriber();
		subs.setMsisdn(mo.getMsisdn());
		subs.setLastModified(new Date());
		subs.setActiveChannel(getChannel());
		subs.setStatus(AppConstants.SUBS_STATUS_ACTIVE);
		subs.setActiveTime(new Date());
		
		Date expireTime = BaseUtils.addTime(new Date(), Calendar.DATE, 1);
		expireTime = BaseUtils.truncDate(expireTime);
		expireTime = BaseUtils.addTime(expireTime, Calendar.SECOND, -1);
		subs.setExpireTime(expireTime);
		
		AppUtils.addScore(subs, XmlConfigs.Score.REGISTER);
		{
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_REGISTER_FIRST_MT_1, subs);
			mainApp.getMtQueue().addLast(mt);
			resultCmd.addMt(mt);
		}
		{
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_REGISTER_FIRST_MT_2, subs);
			mainApp.getMtDelay1Queue().addLast(mt);
			resultCmd.addMt(mt);
		}
		logger.info( mo.getTransId() + ", it's valid time to return question");
		Question question = QuestionFactory.getQuestion(mo.getTransId(), subs, channel);
		String content = question.getContent(); 
		
		MtHis mt = new MtHis(mo.getTransId(), 0, mo.getMsisdn(), content, mo.getId(), "QT-" + question.getId(), null, null);
		mainApp.getMtDelay2Queue().addLast(mt);
		resultCmd.addMt(mt);
		
		baseDAO.insertBean(mo.getTransId(), subs);
		
		ScoreHis scoreHis = new ScoreHis(0, mo.getMsisdn(), XmlConfigs.Score.REGISTER, subs.getDayScore(), subs.getWeekScore(), 
				subs.getMonthScore(), subs.getTotalScore(), AppConstants.SCORE_HIS_REASON_REGISTER, null, mo.getTransId());
		mainApp.getScoreHisQueue().addLast(scoreHis);
		
		CdrHis cdrHis = new CdrHis(0, mo.getMsisdn(), 0, AppConstants.CDR_REASON_REGISTER, null, mo.getTransId());
		mainApp.getCdrHisQueue().addLast(cdrHis);
		
		resultCmd.setSubscriber(subs);
		return resultCmd;
	}
	
	private CmdResult reRegister(Subscriber subs) {
		
		logger.info(mo.getTransId() + ", re-register");
		if(subs.getExpireTime().compareTo(new Date()) < 0){
			Date expireTime = BaseUtils.addTime(new Date(), Calendar.DATE, 1);
			expireTime = BaseUtils.truncDate(expireTime);
			expireTime = BaseUtils.addTime(expireTime, Calendar.SECOND, -1);
			subs.setExpireTime(expireTime);
		}
		subs.setIgnoreQuestion(0);
		subs.setActiveTime(new Date());
		subs.setStatus(AppConstants.SUBS_STATUS_ACTIVE);
		subs.setActiveChannel(getChannel());
		
		if(amount > 0){
			logger.info( mo.getTransId() + ", last register is another day, chargeFee = " + amount + " => add score for subs");
			AppUtils.addScore(subs, XmlConfigs.Score.REGISTER);
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_REGISTER_SECOND_ANOTHER_DAY, subs);
			mainApp.getMtQueue().addLast(mt);
			resultCmd.addMt(mt);
		} else {
			logger.info( mo.getTransId() + " re-register today, chargeFee = " + amount + " => do NOT add score for subs");
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_REGISTER_SECOND_TODAY, subs);
			mainApp.getMtQueue().addLast(mt);
			resultCmd.addMt(mt);
		}
		if (AppUtils.getAnsweredCount(subs, channel) < XmlConfigs.MAX_QUESTION_PER_CHANNEL) {
			logger.info(mo.getTransId() + ", answered today < " + XmlConfigs.MAX_QUESTION_PER_CHANNEL);
			Question question = QuestionFactory.getQuestionById(AppUtils.getLastQuesId(subs, channel));
			if (question == null) {
				question = QuestionFactory.getQuestion(mo.getTransId(), subs, channel);
			}
			String content = question.getContent();
			MtHis mt = new MtHis(mo.getTransId(), 0, mo.getMsisdn(), content, mo.getId(), "QT-" + question.getId(), null, null);
			mainApp.getMtDelay1Queue().addLast(mt);
			resultCmd.addMt(mt);
		} else {
			logger.info(mo.getTransId() + ", over max question for a package => do not return question anymore");
		}
		CdrHis cdrHis = new CdrHis(0, mo.getMsisdn(), amount, AppConstants.CDR_REASON_RE_REGISTER, null, mo.getTransId());
		mainApp.getCdrHisQueue().addLast(cdrHis);
		baseDAO.updateBean(mo.getTransId(), subs);
		
		if(amount > 0){
			ScoreHis scoreHis = new ScoreHis(0, mo.getMsisdn(), XmlConfigs.Score.REREGISTER, subs.getDayScore(), subs.getWeekScore(), 
					subs.getMonthScore(), subs.getTotalScore(), AppConstants.SCORE_HIS_REASON_RE_REGISTER, null, mo.getTransId());
			mainApp.getScoreHisQueue().addLast(scoreHis);
		}
		return resultCmd;
	}
}
