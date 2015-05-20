package com.vas.aps.processor;

import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.JobExecutionContext;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.db.IReadRs;
import com.ligerdev.appbase.utils.quartz.AbsJob;
import com.ligerdev.appbase.utils.textbase.ReqCountUtils;
import com.vas.aps.MainAps;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.db.orm.Subscriber;

public class LoadFirstQues extends AbsJob {

	private MainAps mainApp = MainAps.getInstance();
	private BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	
	@Override
	protected boolean isSingleInstance() {
		return true;
	}

	@Override
	protected void execute0(JobExecutionContext ctx) {
		logger.info("######## Load subs to return firstQuestion");
		
		String sql = "select * from SUBSCRIBER where status = " 
				+ AppConstants.SUBS_STATUS_ACTIVE 
				+ " AND LAST_QUES_SMS <= 0 AND EXPIRE_TIME > sysdate";
		
		final AtomicInteger counter = new AtomicInteger(0);
		baseDAO.handleSql("Load1stQues", sql, new IReadRs() {
			@Override
			public void notFound(String transid) {
				logger.info("Not found any records to return first question");
			}
			@Override
			public boolean found(String transid, int readIndex, ResultSet rs) {
				counter.incrementAndGet();
				Subscriber subs = baseDAO.readRs("ReadSubs", Subscriber.class, rs);
				mainApp.getFirstQuesQueue().addLast(subs);
				BaseUtils.sleep(mainApp.getFirstQuesQueue().size() / 1000);
				return true;
			}
		}, null, null);
		logger.info("Load1stQues: found " + counter.get() + " records to return first question");
	}
}
