package com.vas.aps.cmd;

import java.util.ArrayList;

import com.vas.aps.api.CmdResult;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.db.orm.MtHis;
import com.vas.aps.db.orm.Subscriber;
import com.vas.aps.tablecache.MessageFactory;

public class TopScoreCmd extends AbstractCmd {

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
		
		String content ="Danh sach top 10 thue bao co so diem cao nhat:";		
		String sql = "SELECT * FROM `subscriber` order by MONTH_SCORE desc,LAST_MODIFIED asc limit 10";  // có thể thêm điều kiện where thoải mái
		ArrayList<Subscriber> list = baseDAO.getListBySql(mo.getTransId(), Subscriber.class, sql, 0, 10);
		for(int i=0;i<list.size();i++){
			Subscriber sub = list.get(i);			
			content+= sub+":" +sub.getWeekScore();
			if(i<list.size()-1){
				content+=",";
			}
			
		}
		MtHis mt = MessageFactory.getMessage(mo, content, subs);
		mainApp.getMtQueue().addLast(mt);		
		resultCmd.addMt(mt);
		return resultCmd;
		
	}
}
