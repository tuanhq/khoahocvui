/**
 * 
 */
package com.vas.aps.cmd;


import java.util.Date;

import com.vas.aps.api.CmdResult;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.db.orm.CdrHis;
import com.vas.aps.db.orm.MtHis;
import com.vas.aps.db.orm.SubActiveDuplicate;
import com.vas.aps.db.orm.Subscriber;
import com.vas.aps.tablecache.MessageFactory;

/**
 * @author tuanhq
 *
 */
public class ActiveDuplicateCmd extends AbstractCmd {


	@Override
	protected CmdResult execute0() throws Exception {
		Subscriber subs = baseDAO.getBeanByKey(mo.getTransId(), Subscriber.class, mo.getMsisdn());
		resultCmd.setSubscriber(subs);
		
//		if (subs == null || subs.getStatus() == AppConstants.SUBS_STATUS_DEACTIVE) {
//			logger.info( mo.getTransId() + ", subs is not activing");
//			
//			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_REQUEST_WHEN_NOT_ACTIVE, subs);
//			mainApp.getMtQueue().addLast(mt);
//			
//			resultCmd.addMt(mt);
//			return resultCmd;
//		}
		SubActiveDuplicate subActive = baseDAO.getBeanByKey(mo.getTransId(), SubActiveDuplicate.class, mo.getMsisdn());
		if (subActive !=null && subActive.getIsEnable() ==1 ){
			//gui mt la thue bao nay da active roi 
			{
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_X2_WHEN_ACTIVE_X2, subs);
			mainApp.getMtQueue().addLast(mt);			
			resultCmd.addMt(mt);
			return resultCmd;
			}
			
		}else{
			if(subActive!=null && subActive.getIsEnable()==0){
				subActive.setIsEnable(1);
				
				subActive.setDateModified(new Date());
				baseDAO.updateBean(mo.getTransId(), subActive);
			}else{
			subActive = new SubActiveDuplicate();
			subActive.setIsEnable(1);
			subActive.setMsisdn(mo.getMsisdn());
			subActive.setDateModified(new Date());
			baseDAO.insertBean(mo.getTransId(), subActive);
			}
			//khoi tao mt bao rang thue bao active thanh cong 
			{
			MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_X2_WHEN_SUCCESS, subs);
			mainApp.getMtQueue().addLast(mt);			
			resultCmd.addMt(mt);
			
			CdrHis cdrHis = new CdrHis(0, mo.getMsisdn(), 0, AppConstants.CDR_REASON_BUY_QUESTION_X2, null, mo.getTransId());
			mainApp.getCdrHisQueue().addLast(cdrHis);
			
			return resultCmd;
			}
		}
		
		
	}


}



