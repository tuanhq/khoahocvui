/**
 * 
 */
package com.vas.aps.cmd;


import java.util.Date;

import com.vas.aps.api.CmdResult;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.db.orm.CdrHis;
import com.vas.aps.db.orm.MtHis;
import com.vas.aps.db.orm.Question;
import com.vas.aps.db.orm.SubActiveDuplicate;
import com.vas.aps.db.orm.Subscriber;
import com.vas.aps.tablecache.MessageFactory;
import com.vas.aps.tablecache.QuestionFactory;

/**
 * @author tuanhq
 *
 */
public class ChangeQuestionCmd extends AbstractCmd {


	@Override
	protected CmdResult execute0() throws Exception {
		Subscriber subs = baseDAO.getBeanByKey(mo.getTransId(), Subscriber.class, mo.getMsisdn());
		resultCmd.setSubscriber(subs);		
		Question newQuestion = QuestionFactory.getQuestion(mo.getTransId(), subs, channel);
		String content = newQuestion.getContent();
		MtHis mt = new MtHis(mo.getTransId(), 0, mo.getMsisdn(), content, mo.getId(), "QT-" + newQuestion.getId(), null, null);
		mainApp.getMtQueue().addLast(mt);
		resultCmd.addMt(mt);
		baseDAO.updateBean(mo.getTransId(), subs);
		CdrHis cdrHis = new CdrHis(0, mo.getMsisdn(), 0, AppConstants.CDR_REASON_BUY_QUESTION_DOI, null, mo.getTransId());
		mainApp.getCdrHisQueue().addLast(cdrHis);
		return resultCmd;		
		
	}


}



