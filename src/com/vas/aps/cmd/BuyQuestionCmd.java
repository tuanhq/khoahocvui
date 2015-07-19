/**
 * 
 */
package com.vas.aps.cmd;

import java.util.Date;

import com.vas.aps.api.CmdResult;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.comms.AppUtils;
import com.vas.aps.comms.XmlConfigs;
import com.vas.aps.db.orm.CdrHis;
import com.vas.aps.db.orm.MtHis;
import com.vas.aps.db.orm.Question;
import com.vas.aps.db.orm.SubBuyQuestion;
import com.vas.aps.db.orm.Subscriber;
import com.vas.aps.tablecache.MessageFactory;
import com.vas.aps.tablecache.QuestionFactory;


/**
 * @author tuanhq
 *
 */
public class BuyQuestionCmd extends AbstractCmd  {


	@Override
	protected CmdResult execute0() throws Exception {
		Subscriber subs = baseDAO.getBeanByKey(mo.getTransId(), Subscriber.class, mo.getMsisdn());
		resultCmd.setSubscriber(subs);
		
		if (subs == null || subs.getStatus() == AppConstants.SUBS_STATUS_DEACTIVE) {
			logger.info( mo.getTransId() + ", subs is not activing");
			
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_REQUEST_WHEN_NOT_ACTIVE, subs);
			mainApp.getMtQueue().addLast(mt);
			
			resultCmd.addMt(mt);
			return resultCmd;
		}
		int answerTh = AppUtils.getAnsweredCount(subs, channel);
		
		String sql ="SELECT IFNULL(sum(number_question),0) FROM sub_buy_question WHERE DATE(date_modified) = CURDATE() and MSISDN = '"+mo.getMsisdn()+"'";
		Integer count = baseDAO.getFirstCell(mo.getTransId(), sql, Integer.class);
		SubBuyQuestion subBuyQuestion = new SubBuyQuestion(mo.getMsisdn(), AppConstants.numberQuestionBuy, new Date());
		baseDAO.insertBean(mo.getTransId(), subBuyQuestion);
		{
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_MUA_WHEN_SUCCESS, subs);
			mainApp.getMtQueue().addLast(mt);
			resultCmd.addMt(mt);
			//tra ve cau hoi neu thue bao da tra loi het quota cau hoi
			if (answerTh == XmlConfigs.MAX_QUESTION_PER_CHANNEL + count) {
				Question newQuestion = QuestionFactory.getQuestion(mo.getTransId(), subs, channel);

				String content = newQuestion.getContent();
				{

					// MtHis mt1 = new MtHis(mo.getTransId(), 0, mo.getMsisdn(), content,
					// mo.getId(), "QT-" + question.getId(), null, null);
					MtHis mt1 = new MtHis(mo.getTransId(), 0, mo.getMsisdn(), content, mo.getId(), "QT-" + newQuestion.getId(), null, null);
					mainApp.getMtDelay1Queue().addLast(mt1);
					resultCmd.addMt(mt1);
				}
			}
			CdrHis cdrHis = new CdrHis(0, mo.getMsisdn(), 0, AppConstants.CDR_REASON_BUY_QUESTION_MUA, null, mo.getTransId());
			mainApp.getCdrHisQueue().addLast(cdrHis);
			
			
			return resultCmd;

		}
	}


}




