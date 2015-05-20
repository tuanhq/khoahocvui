package com.vas.aps.processor;

import java.util.Date;

import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.threads.AbsProcessor;
import com.vas.aps.MainAps;
import com.vas.aps.comms.AppUtils;
import com.vas.aps.db.orm.CdrHis;

public class SaveCdrHis extends AbsProcessor {

	private MainAps mainApp = MainAps.getInstance();
	private BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	
	@Override
	public void execute() {
		CdrHis bean = (CdrHis) mainApp.getCdrHisQueue().removeFirst();
		String transid = bean.getTransId();
		if(bean.getTransId() != null){
			bean.setTransId(bean.getTransId().split("@")[0]);
		}
		String tableName = "CDR_" + AppUtils.getCdrTblName(new Date());
		baseDAO.insertBean(transid, tableName, bean);
	}

	@Override
	public int sleep() {
		return 0;
	}

	@Override
	public void exception(Throwable e) {
	}

}
