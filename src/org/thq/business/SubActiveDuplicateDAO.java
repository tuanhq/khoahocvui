/**
 * 
 */
package org.thq.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.thq.database.ConnectionPool;
import org.thq.utils.Util;

/**
 * @author tuanhq
 *
 */
public class SubActiveDuplicateDAO {
	private static final Logger logger = Logger.getLogger(SubActiveDuplicateDAO.class);
	
	public boolean insertSubDuplicate(SubActiveDuplicateDTO dto) throws ClassNotFoundException, SQLException {

		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		try {
			strSQL = "INSERT INTO sub_active_duplicate " 
		+ " (sub_id,"
		+ " status,"
		+ " date )"		
		+ " values(?,?,?)";
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, dto.getSubId());
			stmt.setInt(2, dto.getStatus());
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
	
	public boolean updateSubInfo(SubActiveDuplicateDTO dto) throws ClassNotFoundException, SQLException {

		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		try {
			strSQL = "Update sub_active_duplicate " 
		+ " set status = ?,"
		+ " date = ?"		
		+ " where sub_id = ? ";
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);			
			stmt.setInt(1, dto.getStatus());
			stmt.setString(2, dto.getDate());
			stmt.setString(3, dto.getSubId());
			result = stmt.executeUpdate() >= 0;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("insert: Errior executing " + strSQL + " >>> " + e.toString());
			System.out.println(strSQL);
			Util.stackTraceToString(e);

		} finally {
			ConnectionPool.putConnection(conn);
			stmt.close();
		}
		return result;
	}
	
	public SubActiveDuplicateDTO getQuestionActive(String msisdn)throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;		
		SubActiveDuplicateDTO dto =null;
		try {
			strSQL = "select * from sub_active_duplicate where 1=1 and status =1 and sub_id = ? order by date ";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, msisdn);
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				dto = new SubActiveDuplicateDTO();				
				dto.setSubId(rs.getString("sub_id"));
				dto.setStatus(rs.getInt("status"));
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
