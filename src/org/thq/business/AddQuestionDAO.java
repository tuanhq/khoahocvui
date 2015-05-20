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
public class AddQuestionDAO {
	private static final Logger logger = Logger.getLogger(AddQuestionDAO.class);
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		AddQuestionDTO dto = new AddQuestionDTO("84985633469", 5, Util.getStringDate(), "X2");
		new AddQuestionDAO().insertQuestionAndAnswerLog(dto);
		int point = new AddQuestionDAO().getTotalQuestionAddByMSISDN("84985633470", "20150503");
		System.out.println("POINT LA:" + point);
	}
	public boolean insertQuestionAndAnswerLog(AddQuestionDTO dto) throws ClassNotFoundException, SQLException {

		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		try {
			strSQL = "INSERT INTO sub_add_question " 
		+ " (sub_id,"
		+ " add_question,"		
		+ "  date,"
		+ "	 type )"		
		+ " values(?,?,?,?)";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, dto.getSubId());
			stmt.setInt(2, dto.getAddQuestion());
			stmt.setString(3, dto.getDate());			
			stmt.setString(4, dto.getType());			
			result = stmt.executeUpdate() >= 0;
		} catch (Exception e) {			
			logger.error("insert: Errior executing " + strSQL + " >>> " + e.toString());
			e.printStackTrace();
			logger.error(Util.stackTraceToString(e));

		} finally {
			ConnectionPool.putConnection(conn);
			stmt.close();
		}
		return result;
	}
	public int getTotalQuestionAddByMSISDN(String msisdn, String date)throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;		
		int result =0;
		try {
			strSQL = "select sum(add_question) from sub_add_question  where 1=1 and  sub_id = ? and date like '" + date + "%'";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, msisdn);		
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

}
