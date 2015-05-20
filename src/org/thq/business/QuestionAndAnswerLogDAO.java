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
public class QuestionAndAnswerLogDAO {
	public static void main(String[] args) {
		QuestionAndAnswerLogDAO dao = new QuestionAndAnswerLogDAO();
		QuestionAndAnswerLog dto = new QuestionAndAnswerLog("84985633469",1,"answer",2,Util.getStringDate());
		try {
			dao.insertQuestionAndAnswerLog(dto);
			QuestionAndAnswerLog dto2 = dao.getLastestQuestionByMSISDN("84985633469", "answer");
			System.out.println(dto2.getDate() +" :" + dto2.getSubId() + " :" + dto2.getType() );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
	}
	private static final Logger logger = Logger.getLogger(QuestionAndAnswerLogDAO.class);
	public boolean insertQuestionAndAnswerLog(QuestionAndAnswerLog dto) throws ClassNotFoundException, SQLException {

		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		try {
			strSQL = "INSERT INTO question_answer_log " 
		+ " (sub_id,"
		+ " question_id,"
		+ " type,"
		+ " answer,"
		+ "  date )"		
		+ " values(?,?,?,?,?)";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, dto.getSubId());
			stmt.setInt(2, dto.getQuestionId());
			stmt.setString(3, dto.getType());
			stmt.setInt(4, dto.getAnswer());
			stmt.setString(5, dto.getDate());			
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
	public QuestionAndAnswerLog getLastestQuestionByMSISDN(String msisdn, String type)throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;		
		QuestionAndAnswerLog dto =null;
		try {
			strSQL = "select * from question_answer_log  where 1=1 and  sub_id = ? and type = ? order by date desc limit 1";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, msisdn);
			stmt.setString(2,type);
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				dto = new QuestionAndAnswerLog();
				dto.setSubId(rs.getString("sub_id"));
				dto.setQuestionId(rs.getInt("question_id"));
				dto.setType(rs.getString("type"));
				dto.setAnswer(rs.getInt("answer"));
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
	
	public int getTotalQuestionOrAnswerByMSISDN(String msisdn, String type)throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;		
		int result =0;
		try {
			strSQL = "select count(*) from question_answer_log  where 1=1 and  sub_id = ? and type = ? ";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, msisdn);
			stmt.setString(2,type);
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
