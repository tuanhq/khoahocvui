package com.vas.aps.processor;

import java.util.Calendar;
import java.util.Date;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.threads.AbsProcessor;
import com.vas.aps.MainAps;
import com.vas.aps.comms.AppUtils;

public class CreateTblDynamic extends AbsProcessor {

	private MainAps mainApp = MainAps.getInstance();
	private BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN, "nolog");
	private static CreateTblDynamic instance = null;
	
	private CreateTblDynamic() {
		baseDAO.disableLogger(true);
	}
	
	public synchronized static CreateTblDynamic getInstance(){
		if(instance == null){
			instance = new CreateTblDynamic();
		}
		return instance;
	}
	
	@Override
	public void execute() {
		{
			String thisMonth = BaseUtils.formatTime("yyyyMM", System.currentTimeMillis());
			String nextMonth = BaseUtils.formatTime("yyyyMM", BaseUtils.addTime(new Date(), Calendar.MONTH, 1)); 
			
			String sqlCheck = "select 1 from MT_" + nextMonth;
			boolean checkExist = baseDAO.isValidSql("CheckTbl", sqlCheck);
			
			if(checkExist == false){
				String sqlCreate = "create table MT_" + nextMonth + " like MT_" + thisMonth;
				baseDAO.execSql("CreateTbl", sqlCreate);
				logger.info("################ " + sqlCreate);
			}
		}
		{
			String thisMonth = BaseUtils.formatTime("yyyyMM", System.currentTimeMillis());
			String nextMonth = BaseUtils.formatTime("yyyyMM", BaseUtils.addTime(new Date(), Calendar.MONTH, 1)); 
			
			String sqlCheck = "select 1 from MO_" + nextMonth;
			boolean checkExist = baseDAO.isValidSql("CheckTbl", sqlCheck);
			
			if(checkExist == false){
				String sqlCreate = "create table MO_" + nextMonth + " like MO_" + thisMonth;
				baseDAO.execSql("CreateTbl", sqlCreate);
				logger.info("################ " + sqlCreate);
			}
		}
		{
			Calendar cal = Calendar.getInstance();
			for(int i = 0; i < 6; i ++){
				cal.add(Calendar.DATE, 5);
				
				String nextName = AppUtils.getCdrTblName(cal.getTime());
				String sqlCheck = "select 1 from CDR_" + nextName; 
				boolean checkExist = baseDAO.isValidSql("CheckTbl", sqlCheck);
				
				if(checkExist == false){
					String sqlCreate = "create table CDR_" + nextName + " like CDR_" + AppUtils.getCdrTblName(new Date());
					baseDAO.execSql("CreateTbl", sqlCreate);
					logger.info("################ " + sqlCreate);
				}
			}
		}
	}

	@Override
	public int sleep() {
		return 1000 * 10;
	}

	@Override
	public void exception(Throwable e) {
	}
}
