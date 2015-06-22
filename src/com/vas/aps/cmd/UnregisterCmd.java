package com.vas.aps.cmd;

import java.util.Date;

import com.vas.aps.api.CmdResult;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.comms.AppUtils;
import com.vas.aps.comms.XmlConfigs;
import com.vas.aps.db.orm.CdrHis;
import com.vas.aps.db.orm.MtHis;
import com.vas.aps.db.orm.Subscriber;
import com.vas.aps.mpsclient.SubsUpdateClient;
import com.vas.aps.mpsclient.UnreqResult;
import com.vas.aps.tablecache.MessageFactory;

public class UnregisterCmd extends AbstractCmd {

	@Override
	public CmdResult execute0() throws Exception {
		Subscriber subs = baseDAO.getBeanByKey(mo.getTransId(), Subscriber.class, mo.getMsisdn());
		resultCmd.setSubscriber(subs);
		
		if (subs == null || subs.getStatus() == AppConstants.SUBS_STATUS_DEACTIVE) {
			// mất đồng bộ với MPS, phản hồi giá trị như với TH giao dịch thành công
			logger.info( mo.getTransId() + ", subs is not activing");
			
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_UNREGISTER, subs);
			mainApp.getMtQueue().addLast(mt);
			
			resultCmd.addMt(mt);
			return resultCmd;
		}
		if(isMustCallMps()){
			UnreqResult resp = SubsUpdateClient.requestUnregister(mo.getTransId(), mo.getMsisdn());
			if(!resp.isSuccess()){
				MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_SYSTEM_ERROR, subs);
				mainApp.getMtQueue().addLast(mt);
				
				resultCmd.addMt(mt);
				resultCmd.setErrorCode("10");
				resultCmd.setErrorDesc("System error");
				return resultCmd;
			}
		}
		subs.setDeactiveTime(new Date());
		subs.setStatus(AppConstants.SUBS_STATUS_DEACTIVE);
		subs.setDeactiveChannel(getChannel());
		AppUtils.resetScore(subs, 0);
		baseDAO.updateBeanFields(mo.getTransId(), subs, true, "DEACTIVE_TIME", "STATUS", "DEACTIVE_CHANNEL","DAY_SCORE","WEEK_SCORE","MONTH_SCORE","TOTAL_SCORE");

		MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_UNREGISTER, subs);
		mainApp.getMtQueue().addLast(mt);
		logger.info( mo.getTransId() + ", unregister success");
		
		CdrHis cdrHis = new CdrHis(0, mo.getMsisdn(), 0, AppConstants.CDR_REASON_UNREGISTER, null, mo.getTransId());
		mainApp.getCdrHisQueue().addLast(cdrHis);
		
		resultCmd.addMt(mt);
		return resultCmd;
	}
}
