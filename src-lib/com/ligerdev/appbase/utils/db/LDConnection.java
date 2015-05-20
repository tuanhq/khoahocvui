package com.ligerdev.appbase.utils.db;

import java.sql.Connection;

class LDConnection {

	private Connection connection;
	private LDConnInfo connInfo;

	public LDConnection(String transid, Connection connection) {
		this.connection = connection;
		long now = System.currentTimeMillis();
		this.connInfo = new LDConnInfo(transid, now, now, now, true);
	}

	public Connection getConnection() {
		return connection;
	}

	public LDConnInfo getConnInfo() {
		return connInfo;
	}

	public void setLastPush(String transid, long currentTimeMillis) {
		connInfo.setLastPush(currentTimeMillis);
		
		if(connInfo.getTransid() == null){
			connInfo.setTransid(transid);
		}
		if(connInfo.getThrowable() == null){
			connInfo.createThrowable();
		}
	}

	public void setLastPull(String transid, long currentTimeMillis) {
		connInfo.setLastPull(currentTimeMillis);
		
		if(connInfo.getTransid() == null){
			connInfo.setTransid(transid);
		}
		if(connInfo.getThrowable() == null){
			connInfo.createThrowable();
		}
	} 
	
	public void setExtInfo(String transid){
		connInfo.setCreatedNew(false);
		connInfo.setTransid(transid);
		connInfo.createThrowable();
	}
}