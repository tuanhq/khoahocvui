package com.vas.aps;

import java.util.Date;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.db.BaseDAO;

public class TruncateTables {

	private static BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	
	public static void main(String[] args) {
		// truncate to test
		{
			String sql = "truncate table MO_" + BaseUtils.formatTime("yyyyMM", new Date());
			baseDAO.execSql("truncateToTest", sql);
		}
		{
			String sql = "truncate table MT_" + BaseUtils.formatTime("yyyyMM", new Date());
			baseDAO.execSql("truncateToTest", sql);
		}
		{
			String sql = "truncate table CDR_" + BaseUtils.formatTime("yyyyMM", new Date());
			baseDAO.execSql("truncateToTest", sql);
		}
		{
			String sql = "truncate table SCORE_HIS";
			baseDAO.execSql("truncateToTest", sql);
		}
		{
			String sql = "truncate table SUBSCRIBER";
			baseDAO.execSql("truncateToTest", sql);
		}
		{
			String sql = "truncate table WINNER";
			baseDAO.execSql("truncateToTest", sql);
		}
		System.out.println("======== done ========"); 
		BaseDAO.closeAllConns();
		System.exit(-9);
	}
}
