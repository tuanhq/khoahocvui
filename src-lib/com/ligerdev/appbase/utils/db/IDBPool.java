package com.ligerdev.appbase.utils.db;

import java.sql.Connection;

public interface IDBPool {

	String getDriver();
	String getUser();
	String getPassword();
	String getUrl();
	
	Connection getConnection() throws Exception;
	Connection getConnection(boolean autoCommit) throws Exception;
	Connection getConnection(Integer timeout) throws Exception;
	Connection getConnection(String transid) throws Exception;
	Connection getConnection(String transid, Integer timeout) throws Exception;
	Connection getConnection(boolean autoCommit, Integer timeout) throws Exception;
	Connection getConnection(String transid, boolean autoCommit) throws Exception;
	Connection getConnection(String transid, boolean autoCommit, Integer timeout) throws Exception;
	
	void releaseAll(Object ... objects);
	String getInfoByDateStr();
	String getInfoByHourStr(int hour); 

}
