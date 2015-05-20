package com.vas.aps.processor;

import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.threads.AbsProcessor;
import com.vas.aps.MainAps;
import com.vas.aps.db.orm.ScoreHis;

public class SaveScoreHis extends AbsProcessor {

	private MainAps mainApp = MainAps.getInstance();
	private BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	
	@Override
	public void execute() {
		ScoreHis bean = (ScoreHis) mainApp.getScoreHisQueue().removeFirst();
		String transid = bean.getTransId();
		if(bean.getTransId() != null){
			bean.setTransId(bean.getTransId().split("@")[0]);
		}
		baseDAO.insertBean(transid, bean);
	}

	@Override
	public int sleep() {
		return 0;
	}

	@Override
	public void exception(Throwable e) {
	}

}
