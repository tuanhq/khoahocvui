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
import org.thq.logic.UtilContanst;
import org.thq.utils.Util;

/**
 * @author tuanhq
 *
 */
public class SubScoreDAO {
	private static final Logger logger = Logger.getLogger(SubScoreDAO.class);
	
	public boolean insertSubScore(SubScoreDTO dto)throws ClassNotFoundException, SQLException {

		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		try {
			strSQL = "INSERT INTO sub_score " 
		+ " (sub_id,"
		+ " score,"
		+ " type,"
		+ " date)"		
		+ " values(?,?,?,?)";		
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, dto.getSubId());
			stmt.setInt(2, dto.getScore());
			stmt.setString(3, dto.getType());
			stmt.setString(4, dto.getDate());			
			result = stmt.executeUpdate() >= 0;
		} catch (Exception e) {			
			logger.error("insert: Errior executing " + strSQL + " >>> " + e.toString());
			Util.stackTraceToString(e);
			e.printStackTrace();

		} finally {
			ConnectionPool.putConnection(conn);
			stmt.close();
		}
		return result;
	}
	public List<SubScoreDTO> getListSubScore()throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;
		List<SubScoreDTO> list = new ArrayList<SubScoreDTO>();
		SubScoreDTO dto =null;
		try {
			strSQL = "select * from sub_score where 1=1 order by date desc  ";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				dto = new SubScoreDTO();				
				dto.setSubId(rs.getString("sub_id"));
				dto.setScore(rs.getInt("score"));
				dto.setType(rs.getString("type"));
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
	
	public int getTotalSubScore()throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;
		int result = 0;		
		try {
			strSQL = "select count(*) from sub_score where 1=1";			
			
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
	
	public int getPointByMSISDN(String msisdn)throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;
		int result = 0;		
		try {
			strSQL = "select sum(score) from sub_score where 1=1 and sub_id = ? "
					+ " and date > (select IFNULL(max(date),'"+ UtilContanst.DateDestroy + "') from sub_score where 1=1 and sub_id = ? and score = -1 ) ";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, msisdn);
			stmt.setString(2, msisdn);
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
	public SubScoreDTO getLastestPointByMSISDN(String msisdn)throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;
		SubScoreDTO result = null;		
		try {
			strSQL = "select * from sub_score where 1=1 and sub_id = ? order by date desc LIMIT 1; ";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, msisdn);
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				result = new SubScoreDTO();
				result.setSubId(rs.getString("sub_id"));
				result.setDate(rs.getString("date"));
				result.setScore(rs.getInt("score"));
				result.setType(rs.getString("type"));
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
	public SubScoreDTO getLastestPointByMSISDN(String msisdn,String date)throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;
		SubScoreDTO result = null;		
		try {
			strSQL = "select * from sub_score where 1=1 and sub_id = ? and date = ? order by date desc LIMIT 1; ";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, msisdn);
			stmt.setString(2, date);
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				result = new SubScoreDTO();
				result.setSubId(rs.getString("sub_id"));
				result.setDate(rs.getString("date"));
				result.setScore(rs.getInt("score"));
				result.setType(rs.getString("type"));
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

}
