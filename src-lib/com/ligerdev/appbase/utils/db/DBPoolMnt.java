package com.ligerdev.appbase.utils.db;

import java.io.RandomAccessFile;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.ligerdev.appbase.utils.threads.AbsApplication;

class DBPoolMnt implements IDBPool { 
	
	private static Logger logger = Log4jLoader.getLogger();
	private static String fileName = BaseUtils.getMyDir() + "./config/database.cfg";
	private static HashMap<String, DBPoolMnt> instanceList = new HashMap<String, DBPoolMnt>();
	
	private String poolName = null;
	private LDConnsManager connsManager = null; 
	
	private Integer maxConnection = null;
	private Integer minConnection = null;
	private String driver = null;
	private String url = null;
	private String user = null;
	private String password = null;
	private String sqlKeepConnection = null;
	private String urlWsMonitor = null;
	private WSPoolMonitor wsPoolMonitor = null;
	private boolean enableKeepConnection;

	static class SchemaName {
		static String MAIN = "main";
	}

	private DBPoolMnt(String poolName, boolean enableKeepConnection) {
		this.poolName = poolName;
		this.enableKeepConnection = enableKeepConnection;
	}

	private void keepConnections() {
		if (minConnection == null 
				|| BaseUtils.isBlank(sqlKeepConnection)
				|| sqlKeepConnection.startsWith("#")) { 
			return;
		}
		for (int i = 0; i < minConnection; i++) {
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				conn = getConnection();
				stmt = conn.prepareStatement(sqlKeepConnection);
				rs = stmt.executeQuery();

			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage());
			} finally {
				releaseAll(rs, stmt, conn);
			}
		}
	}

	private void buildPool() {
		this.connsManager = new LDConnsManager(poolName);
		if(enableKeepConnection){
			new Thread() {
				public void run() {
					while (AbsApplication.CONTINUES) {
						try {
							Thread.sleep(1000 * 60);
							keepConnections();
						} catch (Throwable e) {
							logger.info("Exception: " + e.getMessage());
						}
					}
				};
			}.start();
		}
		if(wsPoolMonitor == null && BaseUtils.isNotBlank(urlWsMonitor) && !urlWsMonitor.startsWith("#")){
			wsPoolMonitor = new WSPoolMonitor(this);
			Endpoint.publish(urlWsMonitor, wsPoolMonitor);
			logger.info("Published WS dbpool monitor: " + urlWsMonitor);
		}
		synchronized (connsManager) {
			while (connsManager.sizeFree() < minConnection) {
				try {
					Connection conn = createNewConnection("init");
					if (conn != null) {
						connsManager.push("init", conn);
					}
				} catch (Exception ex) {
					BaseUtils.sleep(5000);
				}
			}
			logger.info("Init successfully connection pool, poolsize = " + connsManager.sizeTotal());
		}
	}
	
	public Connection getConnection() throws Exception {
		return getConnection("inv", true, null);
	}
	
	public Connection getConnection(boolean autoCommit) throws Exception {
		return getConnection("inv", autoCommit, null);
	}
	
	public Connection getConnection(Integer timeout) throws Exception {
		return getConnection("inv", true, timeout);
	}
	
	public Connection getConnection(String transid) throws Exception {
		return getConnection(transid, true, null);
	}
	
	public Connection getConnection(String transid, Integer timeout) throws Exception {
		return getConnection(transid, true, timeout);
	}
	
	public Connection getConnection(boolean autoCommit, Integer timeout) throws Exception {
		return getConnection("inv", autoCommit, timeout);
	}
	
	public Connection getConnection(String transid, boolean autoCommit) throws Exception {
		return getConnection(transid, autoCommit, null);
	}
	
	public Connection getConnection(String transid, boolean autoCommit, Integer timeout) throws Exception {
		synchronized (connsManager) {
			Connection conn = (Connection) connsManager.take(transid);
			if (conn != null) {
				try {
					if(conn.isClosed()){
						connsManager.delConnFromBusyList(transid, conn);
					} else {
						conn.setAutoCommit(autoCommit);
						return conn;
					}
				} catch (Exception e) {
					connsManager.delConnFromBusyList(transid, conn);
					try {
						conn.close();
					} catch (Exception e2) {
					}
				}
			}  
			int totalConn = connsManager.sizeTotal();
			if(totalConn < maxConnection){
				try {
					if(logger.isDebugEnabled()){
						logger.debug(transid + ", Pool is empty => create new connection");
					}
					conn = createNewConnection(transid);
					conn.setAutoCommit(autoCommit);
					connsManager.addConnToBusyList(transid, conn);
					
					if(logger.isDebugEnabled()){
						logger.debug(transid + ", create new conn successfully");
					}
					return conn;
					
				} catch (Exception e) {
					throw e;
				}
			} else {
				if(timeout != null){
					if(logger.isDebugEnabled()){
						logger.debug(transid + ", over limit connections, maxConfig=" 
								+ totalConn + " => wait " + timeout + "ms to retry");
					}
					try {
						connsManager.wait(timeout);
					} catch (Exception e) {
					}
					conn = (Connection) connsManager.take(transid);
					
				} else {
					if(logger.isDebugEnabled()){
						logger.debug(transid + ", over limit connections, maxConfig=" 
								+ totalConn + " => wait to retry");
					}
					while(conn == null){
						try {
							connsManager.wait();
						} catch (Exception e) {
						}
						conn = (Connection) connsManager.take(transid);
					}
				}
				try {
					if(conn == null || conn.isClosed()){
						if(conn != null){
							connsManager.delConnFromBusyList(transid, conn);
						}
						throw new Exception("can not get connection due to over limit connections, maxConfig=" + totalConn);
						
					}  else {
						conn.setAutoCommit(autoCommit);
						return conn;
					}
				} catch (Exception e) {
					throw e;
				}
			}
		}
	}

	void putConnection(String transid, Connection conn) {
		synchronized(connsManager){
			try {
				if (conn == null || conn.isClosed()) {
					return;
				}
				if (connsManager.sizeFree() >= minConnection) {
					connsManager.delConnFromBusyList(transid, conn);
					conn.close();
				} else {
					connsManager.push(transid, conn);
					connsManager.notify();
				}
			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
	}

	void closeConnsInPool() {
		synchronized (connsManager) {
			if(connsManager.sizeFree() > 0){
				logger.info("Close connections in pool: " + poolName);
			}
			while (connsManager.sizeFree() > 0) {
				Connection conn = connsManager.take("inv");
				try {
					if (conn != null && !conn.isClosed()) {
						conn.close();
					}
				} catch (Exception e) {
					logger.info("release: Cannot close connection! (maybe closed?)");
				}
			}
			connsManager.clearListBusy(); 
		}
	}

	private Connection createNewConnection(String transid) throws SQLException {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
			
		} catch (Exception ex) {
			logger.info(transid + ", Create new connection fail ... (" + driver + ", " + user + ", pass = " 
					+ password + ", " + url + "), Exception: " + ex.getMessage());
			throw new SQLException(ex.getMessage());
		}
		return conn;
	}
	
	@Override
	public void releaseAll(Object ... objs) {
		String transid = "inv";
		for (int i = 0; objs != null && i < objs.length; i++) {
			try {
				if (objs[i] == null) {
					continue;
				}
				if (objs[i] instanceof String) {
					transid = (String) objs[i];
				} else if (objs[i] instanceof ResultSet) {
					ResultSet obj = (ResultSet) objs[i];
					obj.close();
				} else if (objs[i] instanceof Statement) {
					Statement obj = (Statement) objs[i];
					obj.close();
				} else if (objs[i] instanceof PreparedStatement) {
					PreparedStatement obj = (PreparedStatement) objs[i];
					obj.close();
				} else if (objs[i] instanceof CallableStatement) {
					CallableStatement obj = (CallableStatement) objs[i];
					obj.close();
				} else if (objs[i] instanceof Connection) {
					Connection obj = (Connection) objs[i];
					putConnection(transid, obj);
				}
			} catch (Exception e) {
				logger.info(transid + ", Exception: " + e.getMessage());
			}
		}
	}
	
	synchronized static DBPoolMnt getInstance(String dbName) {
		return getInstance(dbName, true);
	}

	synchronized static DBPoolMnt getInstance(String poolName, boolean openThread) {
		try {
			if (instanceList.containsKey(poolName)) {
				return (DBPoolMnt) instanceList.get(poolName);
			}
			RandomAccessFile rd = new RandomAccessFile(fileName, "rw");
			String line = null;
			DBPoolMnt dbPool = null;
			while ((line = rd.readLine()) != null) {
				try {
					if (line.trim().startsWith("#") || !line.contains("=")) { 
						continue;
					}
					String key = line.substring(0, line.indexOf("=")).trim();
					String value = line.substring(line.indexOf("=") + 1).trim();
					if ("db_name".equals(key) && value.equals(poolName)) {
						if(dbPool != null){
							return null;
						}
						dbPool = new DBPoolMnt(poolName, openThread);
					} else if (dbPool != null && "driverClassName".equals(key)) {
						dbPool.driver = (value);
					} else if (dbPool != null && "url".equals(key)) {
						dbPool.url = (value);
					} else if (dbPool != null && "user".equals(key)) {
						dbPool.user = (value);
					} else if (dbPool != null && "password".equals(key)) {
						dbPool.password = (value);
					} else if (dbPool != null && "min_connection".equals(key)) {
						dbPool.minConnection = (Integer.parseInt(value));
					} else if (dbPool != null && "max_connection".equals(key)) {
						dbPool.maxConnection = (Integer.parseInt(value));
					} else if (dbPool != null && "sql_keep_connection".equals(key)) {
						dbPool.sqlKeepConnection = (value);
					} else if (dbPool != null && "url_ws_monitor".equals(key)) {
						dbPool.urlWsMonitor = (value);
					}
					if (dbPool != null 
							&& BaseUtils.isNotBlank(dbPool.driver) 
							&& BaseUtils.isNotBlank(dbPool.user) 
							&& BaseUtils.isNotBlank(dbPool.url)
							&& BaseUtils.isNotBlank(dbPool.password) 
							&& BaseUtils.isNotBlank(dbPool.urlWsMonitor)
							&& !dbPool.urlWsMonitor.startsWith("#")
							&& dbPool.minConnection != null 
							&& dbPool.maxConnection != null 
							&& dbPool.sqlKeepConnection != null) {
						
						dbPool.buildPool();
						try {
							rd.close();
						} catch (Exception e) {
							logger.info("Exception: " + e.getMessage());
						}
						instanceList.put(poolName, dbPool);
						return dbPool;
					}
				} catch (Exception e) {
					logger.info("Exception: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		}
		return null;
	}

	static void closeAllConns() {
		try {
			Iterator<String> itr = instanceList.keySet().iterator();
			while (itr.hasNext()) {
				try {
					String key = itr.next();
					DBPoolMnt pool = instanceList.get(key);
					pool.closeConnsInPool();
					
				} catch (Exception e) {
					logger.info("Exception: " + e.getMessage());
				}
			}
			instanceList.clear();
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		}
	}

	public String getInfoByDateStr() {
		return connsManager.getInfoByDateStr();
	}
	
	public String getInfoByHourStr(int hour) {
		return connsManager.getInfoByHourStr(hour);
	}
	
	Integer getMaxConnections() {
		return maxConnection;
	}

	void setMaxConnections(Integer maxConnection) {
		this.maxConnection = maxConnection;
	}

	Integer getMinConnections() {
		return minConnection;
	}

	void setMinConnections(Integer minConnection) {
		this.minConnection = minConnection;
	}

	public String getDriver() {
		return driver;
	}

	void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	void setPassword(String password) {
		this.password = password;
	}

	String getSqlKeepConnection() {
		return sqlKeepConnection;
	}

	void setSqlKeepConnection(String sqlKeepConnection) {
		this.sqlKeepConnection = sqlKeepConnection;
	}

	String getPoolName() {
		return poolName;
	}

	void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	LDConnsManager getConnsManager() {
		return connsManager;
	}

	void setConnsManager(LDConnsManager connsManager) {
		this.connsManager = connsManager;
	}
}
