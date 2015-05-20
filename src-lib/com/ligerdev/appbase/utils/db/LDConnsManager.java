package com.ligerdev.appbase.utils.db;

import java.io.File;
import java.sql.Connection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.queues.MsgQueue;
import com.ligerdev.appbase.utils.queues.MsgQueueITF;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class LDConnsManager {

	private static Logger logger = Log4jLoader.getLogger();
	private LinkedList<LDConnection> listFree = new LinkedList<LDConnection>();
	private ConcurrentHashMap<Connection, LDConnection> listBusy = new ConcurrentHashMap<Connection, LDConnection>();
	
	private String poolName = null;
	private MsgQueueITF msgQueue = null;
	private LDPoolInfoManager infoMonitor = null;
	
	public LDConnsManager(String poolName) {
		this.poolName = poolName;
		this.msgQueue = new MsgQueue("pool_mnt_" + this.poolName);
		
		this.infoMonitor = new LDPoolInfoManager(poolName, msgQueue, listBusy);
		this.infoMonitor.start();
	}

	public void push(String transid, Connection conn) {
		LDConnection ldConn = listBusy.remove(conn);
		if (ldConn == null) {
			ldConn = new LDConnection(transid, conn);
		} 
		ldConn.setLastPush(transid, System.currentTimeMillis());
		try {
			msgQueue.addLast(ldConn.getConnInfo().clone());
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		}
		listFree.addLast(ldConn);
	}

	public Connection take(String transid) {
		if (listFree.size() == 0) {
			return null;
		}
		LDConnection ldConn = listFree.removeFirst();
		ldConn.setLastPull(transid, System.currentTimeMillis());
		ldConn.setExtInfo(transid);
		listBusy.put(ldConn.getConnection(), ldConn);
		return ldConn.getConnection();
	}

	public void delConnFromBusyList(String transid, Connection conn) {
		LDConnection ldConn = listBusy.remove(conn);
		if(ldConn != null){
			ldConn.setLastPush(transid, System.currentTimeMillis());
			ldConn.setExtInfo(transid);
			try {
				msgQueue.addLast(ldConn.getConnInfo().clone());
			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
	}
	
	public void addConnToBusyList(String transid, Connection conn) {
		LDConnection ldConn = new LDConnection(transid, conn);
		listBusy.put(conn, ldConn);
	}

	public int sizeFree() {
		return listFree.size();
	}

	public int sizeBusy() {
		return listBusy.size();
	}

	public int sizeTotal() {
		return listFree.size() + listBusy.size();
	}

	public void clearListBusy() {
		listBusy.clear();
	}
	
	public String getInfoByDateStr() {
		return infoMonitor.getInfoByDateStr();
	}
	
	public String getInfoByHourStr(int hour) {
		return infoMonitor.getInfoByHourStr(hour);
	}
	
}
