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
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.ligerdev.appbase.utils.threads.AbsApplication;

class DBPoolNormal  implements IDBPool { 
	
	private static Logger logger = Log4jLoader.getLogger();
	private static String FILE_NAME = BaseUtils.getMyDir() + "./config/database.cfg";
	private static HashMap<String, DBPoolNormal> hashMapPool = new HashMap<String, DBPoolNormal>();
	
	private String poolName = null;
	private LinkedList<Connection> listConnections = new LinkedList<Connection>();
	private Integer maxConnection = null;
	private Integer minConnection = null;
	private String driver = null;
	private String url = null;
	private String user = null;
	private String password = null;
	private String sqlKeepConnection = null;
	private boolean enableKeepConnection;

	static class SchemaName {
		static String MAIN = "main";
	}

	private DBPoolNormal(String poolName, boolean enableKeepConnection) {
		this.poolName = poolName;
		this.enableKeepConnection = enableKeepConnection;
	}

	private void keepConnections() {
		if (minConnection == null || BaseUtils.isBlank(sqlKeepConnection)) {
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
		synchronized (listConnections) {
			while (listConnections.size() < minConnection) {
				try {
					Connection conn = createNewConnection();
					if (conn != null) {
						listConnections.addLast(conn);
					}
				} catch (Exception ex) {
					BaseUtils.sleep(5000);
				}
			}
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

	public Connection getConnection(String transid, boolean autoCommit, Integer timeout) {
		Connection conn = null;
		while (conn == null) {
			try {
				synchronized (listConnections) {
					if (listConnections.size() > 0) {
						conn = (Connection) listConnections.removeFirst();
						if (conn != null && !conn.isClosed()) {
							conn.setAutoCommit(autoCommit);
							break;
						}  
					}
				}
				conn = createNewConnection();
				if (conn != null) {
					conn.setAutoCommit(autoCommit);
					break;
				}
			} catch (Exception ex) {
				logger.info("Exception: " + ex.getMessage());
				BaseUtils.sleep(1000);
			}
		}
		return conn;
	}

	void putConnection(Connection conn) {
		synchronized (listConnections) {
			try {
				if (conn == null || conn.isClosed()) {
					return;
				}
				if (listConnections.size() >= minConnection) {
					conn.close();
					return;
				}
			} catch (Exception e) {
			}
			listConnections.addLast(conn);
			listConnections.notify();
		}
	}

	void closeConnsInPool() {
		synchronized (listConnections) {
			for (Iterator<Connection> it = listConnections.iterator(); it.hasNext();) {
				Connection conn = (Connection) it.next();
				try {
					if (conn != null && !conn.isClosed()) {
						conn.close();
					}
				} catch (Exception e) {
					logger.info("release: Cannot close connection! (maybe closed?)");
				}
			}
			if(listConnections.size() > 0){
				listConnections.clear();
				logger.info("Close connections in pool '" + poolName + "' successfully");
			}
		}
	}

	private Connection createNewConnection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
			
		} catch (Exception ex) {
			logger.info("Create new connection fail ... (" + driver + ", " + user + ", " + password + ", " + url + "), Exception: " + ex.getMessage());
			throw new SQLException(ex.getMessage());
		}
		return conn;
	}

	@Override
	public void releaseAll(Object ... objs) {
		for (int i = 0; objs != null && i < objs.length; i++) {
			try {
				if (objs[i] == null) {
					continue;
				}
				if (objs[i] instanceof ResultSet) {
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
					putConnection(obj);
				}
			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
	}
	
	synchronized static DBPoolNormal getInstance(String dbName) {
		return getInstance(dbName, true);
	}

	synchronized static DBPoolNormal getInstance(String poolName, boolean openThread) {
		try {
			if (hashMapPool.containsKey(poolName)) {
				return (DBPoolNormal) hashMapPool.get(poolName);
			}
			RandomAccessFile rd = new RandomAccessFile(FILE_NAME, "rw");
			String line = null;
			DBPoolNormal dbPool = null;
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
						dbPool = new DBPoolNormal(poolName, openThread);
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
					}
					if (dbPool != null 
							&& BaseUtils.isNotBlank(dbPool.driver) 
							&& BaseUtils.isNotBlank(dbPool.user) 
							&& BaseUtils.isNotBlank(dbPool.url)
							&& BaseUtils.isNotBlank(dbPool.password) 
							&& dbPool.minConnection != null 
							&& dbPool.maxConnection != null 
							&& dbPool.sqlKeepConnection != null) {
						
						dbPool.buildPool();
						try {
							rd.close();
						} catch (Exception e) {
							logger.info("Exception: " + e.getMessage());
						}
						hashMapPool.put(poolName, dbPool);
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
			Iterator<String> itr = hashMapPool.keySet().iterator();
			while (itr.hasNext()) {
				try {
					String key = itr.next();
					DBPoolNormal pool = hashMapPool.get(key);
					pool.closeConnsInPool();
				} catch (Exception e) {
					logger.info("Exception: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		}
	}

	String getPoolName() {
		return poolName;
	}

	void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	Integer getMaxConnection() {
		return maxConnection;
	}

	void setMaxConnection(Integer maxConnection) {
		this.maxConnection = maxConnection;
	}

	Integer getMinConnection() {
		return minConnection;
	}

	void setMinConnection(Integer minConnection) {
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

	@Override
	public String getInfoByDateStr() {
		return "inv";
	}

	@Override
	public String getInfoByHourStr(int hour) {
		return "inv";
	}
}
