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
public class SubRequestLogDAO {
	private static final Logger logger = Logger.getLogger(SubRequestLogDAO.class);
	public boolean insertSubRequestLog(SubRequestLog dto)throws ClassNotFoundException, SQLException {

		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		try {
			strSQL = "INSERT INTO sub_request_log" 
		+ " (sub_id,"
		+ " MO,"
		+ " date )"		
		+ " values(?,?,?)";
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, dto.getSubId());
			stmt.setString(2, dto.getMo());
			stmt.setString(3, dto.getDate());			
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
	public List<SubRequestLog> getListRequestLog()throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;
		List<SubRequestLog> list = new ArrayList<SubRequestLog>();
		SubRequestLog dto =null;
		try {
			strSQL = "select * from sub_request_log where 1=1 order by date desc  ";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				dto = new SubRequestLog();				
				dto.setSubId(rs.getString("sub_id"));
				dto.setMo(rs.getString("mo"));
				dto.setDate(rs.getString("date"));								
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
	
	public int getTotalRequestLog()throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;
		int result = 0;		
		try {
			strSQL = "select count(*) from sub_request_log where 1=1";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				result = rs.getInt(1);
			}
		}catch (Exception e) {
			logger.error("insert: Errior executing " + strSQL + " >>> " + e.toString());
			e.printStackTrace();
		}finally {
			ConnectionPool.putConnection(conn);
			stmt.close();
			rs.close();
		}
		return result;
			
	}
	
	public SubRequestLog getLastestRequestLog(String msisdn)throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;		
		SubRequestLog dto =null;
		try {
			strSQL = "select * from sub_request_log where 1=1 order by date desc LIMIT 1; ";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				dto = new SubRequestLog();				
				dto.setSubId(rs.getString("sub_id"));
				dto.setMo(rs.getString("mo"));
				dto.setDate(rs.getString("date"));							
				
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
}
