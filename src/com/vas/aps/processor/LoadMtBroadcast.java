package com.vas.aps.processor;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.db.IReadRsUpdatale;
import com.ligerdev.appbase.utils.threads.AbsProcessor;
import com.vas.aps.MainAps;
import com.vas.aps.db.orm.MtBroadcast;
import com.vas.aps.db.orm.MtHis;

public class LoadMtBroadcast extends AbsProcessor {

	private MainAps mainApp = MainAps.getInstance();
	private BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN, "nolog");
	private static LoadMtBroadcast instance = null;
	
	private LoadMtBroadcast() {
		baseDAO.disableLogger(true);
	}
	
	public synchronized static LoadMtBroadcast getInstance(){
		if(instance == null){
			instance = new LoadMtBroadcast();
		}
		return instance;
	}
	
	@Override
	public void execute() {
		String sql = "SELECT * FROM MT_BROADCAST WHERE STATUS = 1 AND (SEND_TIME IS NULL OR SEND_TIME <= now())";
		
		baseDAO.handleSqlUpdatable("LoadBroadcast", sql, new IReadRsUpdatale() {
			@Override
			public boolean whereRs(ResultSet rs) throws SQLException { 
				return rs.first();
			}
			@Override
			public void notFound(String transid) {
			}
			@Override
			public boolean found(String transid, int readIndex, ResultSet rs) throws SQLException {
				MtBroadcast mtBrc = baseDAO.readRs("readMt", MtBroadcast.class, rs);
				
				MtHis mt = new MtHis("BRC", 0, mtBrc.getMsisdn(), mtBrc.getContent(), 0, "BRC", null, null);
				mainApp.getMtBroadcastQueue().addLast(mt);
				
				rs.deleteRow();
				return true;
			}
		}, null, null);
	}

	@Override
	public int sleep() {
		return 1000;
	}

	@Override
	public void exception(Throwable e) {
	}
}
