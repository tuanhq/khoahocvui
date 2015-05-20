package org.thq.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;

/**
 * @author tuanhq
 *
 */
public class ConnectionPool {
	private static final Logger logger = Logger.getLogger(ConnectionPool.class);
	private static LinkedList<Connection> pool = new LinkedList<Connection>();
	public final static int MAX_CONNECTIONS = 10;
	public final static int INI_CONNECTIONS = 3;
	private static boolean dbAvailable = false;

	public static boolean isDBAvailable() {
		return dbAvailable;
	}

	public static void setDBAvailable(boolean value) {
		dbAvailable = value;
	}

	static {
		try {
			build(INI_CONNECTIONS);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	public static void build(int number) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		for (int i = 0; i < INI_CONNECTIONS; i++) {
			conn = makeDBConnection();
			if (conn != null) {
				pool.addLast(conn);
			}
			if (!isDBAvailable()){
				setDBAvailable(true);
			}
		}
		
	}

	public static Connection getConnection() throws SQLException, InterruptedException, ClassNotFoundException {
		if (!isDBAvailable())
			throw new SQLException("Database is NOT available (may be down).");
		Connection conn = null;
		synchronized (pool) {
			if (pool.size() < 1) {
				
				pool.wait();
				
			}
			conn = (Connection) pool.removeFirst();
		}
		if (conn == null || conn.isClosed()) {
			conn = makeDBConnection();
		}
		
		conn.setAutoCommit(true);
		return conn;
	}

	public static void putConnection(Connection conn) throws ClassNotFoundException, SQLException {
		synchronized (pool) {
			if (pool.size() >= MAX_CONNECTIONS) {
				System.out.println("Close connection because size pool maximum");
				conn.close();
				return;
			}
			if (conn == null || conn.isClosed()) {
				conn = makeDBConnection();
			}

			pool.addLast(conn);
			pool.notify();

		}
	}

	// Remove and close all connections in pool
	public static void release() {
		logger.info("Closing connections in pool...");
		synchronized (pool) {
			for (Iterator it = pool.iterator(); it.hasNext();) {
				Connection conn = (Connection) it.next();
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("release: Cannot close connection! (maybe closed?)");
				}
			}
			pool.clear();
			setDBAvailable(false);
		}
	}

	public static int size() {
		synchronized (pool) {
			return pool.size();
		}
	}

	public static boolean isEmpty() {
		synchronized (pool) {
			return pool.isEmpty();
		}
	}

	public static Connection makeDBConnection() throws SQLException, ClassNotFoundException {
		Connection conn = null;
		DBConnection db = new MySQLConnection();
		conn = db.makeConnection();
		return conn;
	}

	public static void main(String args[]) {
		for(int i =0 ; i <100 ;i++){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Connection conn = ConnectionPool.getConnection();
						Thread.sleep(3000);
						ConnectionPool.putConnection(conn);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}).start();
		}

	}

	public static synchronized void startTransaction(Connection con) {
		try {
			if (con != null && con.getAutoCommit()) {
				con.setAutoCommit(false);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public static synchronized void endTransaction(Connection con) {
		try {
			if (con != null) {
				con.commit();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public static synchronized void rollBackTransaction(Connection con) {
		try {
			if (con != null && !con.getAutoCommit()) {
				con.rollback();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
