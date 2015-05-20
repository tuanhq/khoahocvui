package com.vas.aps.cmd;

import java.util.Date;

import com.vas.aps.api.CmdResult;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.comms.AppUtils;
import com.vas.aps.comms.XmlConfigs;
import com.vas.aps.db.orm.MtHis;
import com.vas.aps.db.orm.Question;
import com.vas.aps.db.orm.Subscriber;
import com.vas.aps.tablecache.MessageFactory;
import com.vas.aps.tablecache.QuestionFactory;

public class LastQuestionCmd extends AbstractCmd {

	@Override
	public CmdResult execute0() throws Exception {
		Subscriber subs = baseDAO.getBeanByKey(mo.getTransId(), Subscriber.class, mo.getMsisdn());
		resultCmd.setSubscriber(subs);
		
		if (subs == null || subs.getStatus() == AppConstants.SUBS_STATUS_DEACTIVE) {
			logger.info( mo.getTransId() + ", msisdn = " + this.mo.getMsisdn() + ", subs is not activing");
			
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_REQUEST_WHEN_NOT_ACTIVE, subs);
			mainApp.getMtQueue().addLast(mt);
			
			resultCmd.addMt(mt);
			return resultCmd;
		}
		int answerTh = AppUtils.getAnsweredCount(subs, channel); 
		if (answerTh >= XmlConfigs.MAX_QUESTION_PER_CHANNEL) {
			logger.info(mo.getTransId() + ", over limit answer for a channel");
			
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_ANSWER_LAST_QUESTION, subs);
			mainApp.getMtQueue().addLast(mt);
			
			resultCmd.addMt(mt);
			return resultCmd;
		}
		Question question = QuestionFactory.getQuestionById(AppUtils.getLastQuesId(subs, channel));
		if (question == null) {
			if(AppConstants.CHANNEL_SMS.equalsIgnoreCase(channel)){
				logger.info( mo.getTransId() + ", do not have any question in day");
				MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_ANSWER_WHEN_NOT_RECEIVED_QUESTION, subs);
				mainApp.getMtQueue().addLast(mt);
				
				resultCmd.addMt(mt);
				return resultCmd;
				
			} else if(AppConstants.CHANNEL_WAP.equalsIgnoreCase(channel)){
				if(subs.getExpireTime().compareTo(new Date()) > 0){
					logger.info(mo.getTransId() + ", get lastQuestion by wap channel => reutrn first question");
					question = QuestionFactory.getQuestion(mo.getTransId(), subs, channel);
					baseDAO.updateBeanFields(mo.getTransId(), subs, true, "QUES_RECEIVED", "LAST_QUES_WAP");
				} else {
					logger.info(mo.getTransId() + ", get lastQuestion by wap channel, but this subs is not renew successfully => do not return question");
					MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_ANSWER_WHEN_NOT_RECEIVED_QUESTION, subs);
					mainApp.getMtQueue().addLast(mt);
					
					resultCmd.addMt(mt);
					return resultCmd;
				}
			} else {
				if(subs.getExpireTime().compareTo(new Date()) > 0){
					logger.info(mo.getTransId() + ", get lastQuestion by web channel => reutrn first question");
					question = QuestionFactory.getQuestion(mo.getTransId(), subs, channel);
					baseDAO.updateBeanFields(mo.getTransId(), subs, true, "QUES_RECEIVED", "LAST_QUES_WEB");
				} else {
					logger.info(mo.getTransId() + ", get lastQuestion by web channel, but this subs is not renew successfully => do not return question");
					MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_ANSWER_WHEN_NOT_RECEIVED_QUESTION, subs);
					mainApp.getMtQueue().addLast(mt);
					
					resultCmd.addMt(mt);
					return resultCmd;
				}
			}
		}
		String content = question.getContent();
		MtHis mt = new MtHis(mo.getTransId(), 0, mo.getMsisdn(), content, mo.getId(), "QT-" + question.getId(), null, null);
		mainApp.getMtQueue().addLast(mt);
		
		resultCmd.addMt(mt);
		return resultCmd;
	}

}
