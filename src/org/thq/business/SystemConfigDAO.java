/**
 * 
 */
package org.thq.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.thq.database.ConnectionPool;
import org.thq.utils.Util;

/**
 * @author tuanhq
 *
 */
public class SystemConfigDAO {
	private static final Logger logger = Logger.getLogger(SystemConfigDAO.class);
	public boolean updateConfig(SystemConfig dto) throws ClassNotFoundException, SQLException {

		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		try {
			strSQL = "Update systemconfig  " 
		+ " set value = ? where property = ? ";		
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, dto.getValue());
			stmt.setString(2, dto.getProperty());		
			result = stmt.executeUpdate() >= 0;
		} catch (Exception e) {			
			logger.error("insert: Errior executing " + strSQL + " >>> " + e.toString());
			Util.stackTraceToString(e);

		} finally {
			ConnectionPool.putConnection(conn);
			stmt.close();
		}
		return result;
	}
	public SystemConfig getSystemConfi(String  property)throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;		
		SystemConfig dto =null;
		try {
			strSQL = "select * from systemconfig  where 1=1 and property = ?";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, property);
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				dto = new SystemConfig();				
				dto.setProperty(rs.getString("property"));
				dto.setValue(rs.getString("value"));
			}
		}catch (Exception e) {
			logger.error("insert: Errior executing " + strSQL + " >>> " + e.toString());
			e.printStackTrace();
		}finally {
			ConnectionPool.putConnection(conn);
			stmt.close();
			rs.close();
		}
		return dto;
			
	}
	public List<SystemConfig> getListSystemConfi()throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;		
		List<SystemConfig> list = new ArrayList<SystemConfig>();
		SystemConfig dto =null;
		try {
			strSQL = "select * from systemconfig  where 1=1 ";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);		
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {				
				dto = new SystemConfig();				
				dto.setProperty(rs.getString("property"));
				dto.setValue(rs.getString("value"));
				list.add(dto);
			}
		}catch (Exception e) {
			logger.error("insert: Errior executing " + strSQL + " >>> " + e.toString());
			e.printStackTrace();
		}finally {
			ConnectionPool.putConnection(conn);
			stmt.close();
			rs.close();
		}
		return list;
			
	}
}
