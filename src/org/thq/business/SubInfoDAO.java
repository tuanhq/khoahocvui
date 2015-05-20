/**
 * 
 */
package org.thq.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.thq.database.ConnectionPool;
import org.thq.utils.Util;

/**
 * @author tuanhq
 *
 */
public class SubInfoDAO {
	private static final Logger logger = Logger.getLogger(SubInfoDAO.class);
	public static void main(String[] args) {
		SubInfoDTO subInfoDTO = new SubInfoDTO();
		subInfoDTO.setSub_id("84985633471");
		subInfoDTO.setStatus(2);
		subInfoDTO.setDate_modified(new SimpleDateFormat("yyyyMMddHHmmSS").format(new Date()));
		try {
			//new SubInfoDAO().insertSubInfo(subInfoDTO);
			//new SubInfoDAO().updateSubInfo(subInfoDTO);
			System.out.println(new SubInfoDAO().checkExist("84985633472"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public boolean insertSubInfo(SubInfoDTO subInfoDTO) throws ClassNotFoundException, SQLException {

		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		try {
			strSQL = "INSERT INTO sub_info" 
		+ " (sub_id,"
		+ " status,"
		+ " date_modified )"		
		+ " values(?,?,?)";
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, subInfoDTO.getSub_id());
			stmt.setInt(2, subInfoDTO.getStatus());
			stmt.setString(3, subInfoDTO.getDate_modified());			
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
	
	public boolean updateSubInfo(SubInfoDTO subInfoDTO) throws ClassNotFoundException, SQLException {

		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		try {
			strSQL = "Update sub_info " 
		+ " set status = ?,"
		+ " date_modified = ?"		
		+ " where sub_id = ? ";
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);			
			stmt.setInt(1, subInfoDTO.getStatus());
			stmt.setString(2, subInfoDTO.getDate_modified());
			stmt.setString(3, subInfoDTO.getSub_id());
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
	
	public boolean checkExist(String msisdn) throws ClassNotFoundException, SQLException {

		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs = null;
		try {
			strSQL = "select * from  sub_info where sub_id = ? ";
		
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);			
			stmt.setString(1, msisdn);			
			
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				result= true;
			}
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
	public SubInfoDTO getSubInfo(String msisdn) throws ClassNotFoundException, SQLException {

		SubInfoDTO result = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs = null;
		try {
			strSQL = "select * from  sub_info where sub_id = ? ";
		
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);			
			stmt.setString(1, msisdn);			
			
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				result = new SubInfoDTO();
				result.setSub_id(rs.getString("sub_id"));
				result.setStatus(rs.getInt("status"));
				result.setDate_modified(rs.getString("date_modified"));
				
			}
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
	
	public List<SubInfoDTO> getListSubInfoActive() throws ClassNotFoundException, SQLException {
		List<SubInfoDTO> list = new ArrayList<SubInfoDTO>();
		SubInfoDTO dto = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs = null;
		try {
			strSQL = "select * from  sub_info where status = 1 ";
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);		
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				dto = new SubInfoDTO();
				dto.setSub_id(rs.getString("sub_id"));
				dto.setStatus(rs.getInt("status"));
				dto.setDate_modified(rs.getString("date_modified"));
				list.add(dto);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("insert: Errior executing " + strSQL + " >>> " + e.toString());
			System.out.println(strSQL);
			Util.stackTraceToString(e);

		} finally {
			ConnectionPool.putConnection(conn);
			stmt.close();
		}
		return list;
	}
}
