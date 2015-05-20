package com.vas.aps.cmd;

import com.vas.aps.api.CmdResult;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.db.orm.MtHis;
import com.vas.aps.db.orm.Subscriber;
import com.vas.aps.tablecache.MessageFactory;

public class ViewScoreCmd extends AbstractCmd {

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
		MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_VIEW_SCORE, subs);
		mainApp.getMtQueue().addLast(mt);
		
		resultCmd.addMt(mt);
		return resultCmd;
	}
}
