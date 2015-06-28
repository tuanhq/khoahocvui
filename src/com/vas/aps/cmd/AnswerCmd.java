package com.vas.aps.cmd;

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
import com.vas.aps.db.orm.SubActiveDuplicate;
import com.vas.aps.db.orm.Subscriber;
import com.vas.aps.tablecache.MessageFactory;
import com.vas.aps.tablecache.QuestionFactory;

public class AnswerCmd extends AbstractCmd {

	@Override
	public CmdResult execute0() throws Exception {
		Subscriber subs = baseDAO.getBeanByKey(mo.getTransId(), Subscriber.class, mo.getMsisdn());
		resultCmd.setSubscriber(subs);
		
		if (subs == null || subs.getStatus() == AppConstants.SUBS_STATUS_DEACTIVE) {
			logger.info( mo.getTransId() + ", subs is not activing");
			
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_REQUEST_WHEN_NOT_ACTIVE, subs);
			mainApp.getMtQueue().addLast(mt);
			
			resultCmd.addMt(mt);
			return resultCmd;
		}
		int lastQuestionId = AppUtils.getLastQuesId(subs, channel);
		Question lastQuestion = QuestionFactory.getQuestionById(lastQuestionId);
		SubActiveDuplicate subActive = baseDAO.getBeanByKey(mo.getTransId(), SubActiveDuplicate.class, mo.getMsisdn());
		
		if (lastQuestion == null) {
			logger.info(mo.getTransId() +", do not have any question in day");
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_ANSWER_WHEN_NOT_RECEIVED_QUESTION, subs);
			mainApp.getMtQueue().addLast(mt);
			
			resultCmd.addMt(mt);
			return resultCmd;
		}
		String sql ="SELECT IFNULL(sum(number_question),0) FROM sub_buy_question WHERE DATE(date_modified) = CURDATE() and MSISDN = '"+mo.getMsisdn()+"'";
		Integer count = baseDAO.getFirstCell(mo.getTransId(), sql, Integer.class);
		int answerTh = AppUtils.getAnsweredCount(subs, channel) + 1; 
		if (answerTh >= XmlConfigs.MAX_QUESTION_PER_CHANNEL + count+ 1) {
			logger.info(mo.getTransId() + ", over limit answer for a channel");
			
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_ANSWER_LAST_QUESTION, subs);
			mainApp.getMtQueue().addLast(mt);
			
			resultCmd.addMt(mt);
			return resultCmd;
		}
		MtHis confirmAnswer = null;
		int scoreBonus = 0;
		
		if (this.mo.getContent().equalsIgnoreCase(lastQuestion.getResult())) {
			logger.info( mo.getTransId() + ", answer for question "  + lastQuestion.getId() + " is " + this.mo.getContent() + " => correct answer");
			
			scoreBonus = XmlConfigs.Score.ANSWER;
			if(subActive!=null && subActive.getIsEnable()==1){
				scoreBonus = XmlConfigs.Score.ANSWER*2;
			}
			AppUtils.addScore(subs, scoreBonus);
			
			String temp = lastQuestion.getConfirmCorrectMt();
			if (BaseUtils.isNotBlank(temp)) {
				confirmAnswer = new MtHis(mo.getTransId(), 0, mo.getMsisdn(), temp, mo.getId(), "CR-" + lastQuestion.getId(), null, null);
			} else {
				if(scoreBonus==XmlConfigs.Score.ANSWER*2){
					confirmAnswer = MessageFactory.getMessage(mo, AppConstants.MT_ANSWER_CORRECTX2, subs);
				}else{
				confirmAnswer = MessageFactory.getMessage(mo, AppConstants.MT_ANSWER_CORRECT, subs);
				}
				
				
			}
		} else {
			logger.info( mo.getTransId() + ", answer for question "  + lastQuestion.getId() + " is " + this.mo.getContent() + " => wrong answer");
			
			String temp = lastQuestion.getConfirmWrongMt();
			if (BaseUtils.isNotBlank(temp)) {
				confirmAnswer = new MtHis(mo.getTransId(), 0, mo.getMsisdn(), temp, mo.getId(), "WR-" + lastQuestion.getId(), null, null);
			} else {
				confirmAnswer = MessageFactory.getMessage(mo, AppConstants.MT_ANSWER_WRONG, subs);
			}
		}
		if(subActive!=null && subActive.getIsEnable()==1){
			subActive.setIsEnable(0);
			subActive.setDateModified(new Date());
			baseDAO.updateBean(mo.getTransId(), subActive);
		}
		mainApp.getMtQueue().addLast(confirmAnswer);
		resultCmd.addMt(confirmAnswer);
		
		if (answerTh >= XmlConfigs.MAX_QUESTION_PER_CHANNEL + count) {
			logger.info( mo.getTransId() + ", the answer is the last in day => do not send new quesion any more ...");
			if (answerTh == XmlConfigs.MAX_QUESTION_PER_CHANNEL + count) {
				int newCount = AppUtils.getAnsweredCount(subs, channel) + 1;
				AppUtils.setAnsweredCount(subs, channel, newCount);
			}
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_ANSWER_LAST_QUESTION, subs);
			mainApp.getMtDelay1Queue().addLast(mt);
			resultCmd.addMt(mt);
		} else {
			if(subs.getIgnoreQuestion() == 1){
				logger.info(mo.getTransId() + ", subs have denied receiving question daily => ignore return new question");
			} else {
				int newCount = AppUtils.getAnsweredCount(subs, channel) + 1;
				AppUtils.setAnsweredCount(subs, channel, newCount);				
				Question newQuestion = QuestionFactory.getQuestion(mo.getTransId(), subs, channel);
				String content = newQuestion.getContent();
				
				MtHis mt = new MtHis(mo.getTransId(), 0, mo.getMsisdn(), content, mo.getId(), "QT-" + newQuestion.getId(), null, null);
				mainApp.getMtDelay1Queue().addLast(mt);  
				resultCmd.addMt(mt);
			}
		}
		baseDAO.updateBean(mo.getTransId(), subs);
		
		CdrHis cdrHis = new CdrHis(0, mo.getMsisdn(), 0, AppConstants.CDR_REASON_ANSWER, null, mo.getTransId());
		mainApp.getCdrHisQueue().addLast(cdrHis);
		
		if(scoreBonus > 0){
			ScoreHis scoreHis = new ScoreHis(0, mo.getMsisdn(), scoreBonus, subs.getDayScore(), subs.getWeekScore(), 
					subs.getMonthScore(), subs.getTotalScore(), AppConstants.SCORE_HIS_REASON_ANSWER, null, mo.getTransId());
			mainApp.getScoreHisQueue().addLast(scoreHis);
		}
		return resultCmd;
	}
}
