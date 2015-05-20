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
public class QuestionDAO {
	private static final Logger logger = Logger.getLogger(QuestionDAO.class);
	public static void main(String[] args) {
		QuestionDTO dto = new QuestionDTO();
		dto.setQuestion("thanh pho ha noi bao nhieu nam tuoi");
		dto.setAnswer1("1500 nam tuoi");
		dto.setAnswer2("thanh pho ha noi 2000 tuoi");
		dto.setAnswer_true(1);
		dto.setIs_approve(1);
		dto.setIs_delete(0);
		dto.setIs_enable(1);		
		dto.setDate_modified("20150430010101");
		dto.setQuestion_id(2);
		try {
			//new QuestionDAO().insertQuestion(dto);
			//new QuestionDAO().updateQuestion(dto);
			System.out.println("tong so ban ghi cua table question: " + new QuestionDAO().getTotalQuestion());
			List<QuestionDTO> list = new QuestionDAO().getListQuestion();
			for (QuestionDTO questionDTO : list) {
				System.out.println("id " + questionDTO.getQuestion_id() + "- name :" + questionDTO.getQuestion());
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean insertQuestion(QuestionDTO questionDTO) throws ClassNotFoundException, SQLException {

		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		try {
			strSQL = "INSERT INTO question" 
		+ "(question,"
		+ " answer1,"
		+ " answer2,"
		+ " answer_true,"
		+ " is_approve,"
		+ " is_enable,"
		+ " is_delete,"
		+ " date_modified) "
		+ " values(?,?,?,?,?,?,?,?)";


			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, questionDTO.getQuestion());
			stmt.setString(2, questionDTO.getAnswer1());
			stmt.setString(3, questionDTO.getAnswer2());
			stmt.setInt(4, questionDTO.getAnswer_true());
			stmt.setInt(5, questionDTO.getIs_enable());
			stmt.setInt(6, questionDTO.getIs_delete());
			stmt.setInt(7, questionDTO.getIs_approve());
			stmt.setString(8, questionDTO.getDate_modified());
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
	
	public boolean updateQuestion(QuestionDTO questionDTO) throws ClassNotFoundException, SQLException {
		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		try {
			strSQL = "UPDATE question " 
		+ "set question = ?,"
		+ " answer1 = ?,"
		+ " answer2 = ?,"
		+ " answer_true = ?,"
		+ " is_approve = ?,"
		+ " is_enable = ?,"
		+ " is_delete = ?,"
		+ " date_modified = ? where question_id = ?";
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, questionDTO.getQuestion());
			stmt.setString(2, questionDTO.getAnswer1());
			stmt.setString(3, questionDTO.getAnswer2());
			stmt.setInt(4, questionDTO.getAnswer_true());
			stmt.setInt(5, questionDTO.getIs_approve());
			stmt.setInt(6, questionDTO.getIs_enable());
			stmt.setInt(7, questionDTO.getIs_delete());			
			stmt.setString(8, questionDTO.getDate_modified());
			stmt.setInt(9, questionDTO.getQuestion_id());
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
	public List<QuestionDTO> getListQuestion()throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;
		List<QuestionDTO> list = new ArrayList<QuestionDTO>();
		QuestionDTO dto =null;
		try {
			strSQL = "select * from question where 1=1 order by date_modified desc  ";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				dto = new QuestionDTO();				
				dto.setQuestion_id(rs.getInt("question_id"));
				dto.setQuestion(rs.getString("question"));
				dto.setAnswer1(rs.getString("answer1"));
				dto.setAnswer2(rs.getString("answer2"));
				dto.setIs_approve(rs.getInt("is_approve"));
				dto.setAnswer_true(rs.getInt("answer_true"));
				dto.setIs_delete(rs.getInt("is_delete"));
				dto.setIs_enable(rs.getInt("is_enable"));
				dto.setDate_modified(rs.getString("date_modified"));				
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
	
	public int getTotalQuestion()throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;
		int result =0;
		try {
			strSQL = "select count(*) from question where 1=1 ";			
			
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
	
	public QuestionDTO getQuestionActive()throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;		
		QuestionDTO dto =null;
		try {
			strSQL = "select * from question where 1=1 and  is_approve =1 and is_delete =0 and is_enable =1 limit 1";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				dto = new QuestionDTO();				
				dto.setQuestion_id(rs.getInt("question_id"));
				dto.setQuestion(rs.getString("question"));
				dto.setAnswer1(rs.getString("answer1"));
				dto.setAnswer2(rs.getString("answer2"));
				dto.setIs_approve(rs.getInt("is_approve"));
				dto.setAnswer_true(rs.getInt("answer_true"));
				dto.setIs_delete(rs.getInt("is_delete"));
				dto.setIs_enable(rs.getInt("is_enable"));
				dto.setDate_modified(rs.getString("date_modified"));			
				
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
	
	public QuestionDTO getQuestionById(int questionId)throws ClassNotFoundException, SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;		
		QuestionDTO dto =null;
		try {
			strSQL = "select * from question where 1=1 and question_id = ?";			
			
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setInt(1, questionId);
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {
				dto = new QuestionDTO();				
				dto.setQuestion_id(rs.getInt("question_id"));
				dto.setQuestion(rs.getString("question"));
				dto.setAnswer1(rs.getString("answer1"));
				dto.setAnswer2(rs.getString("answer2"));
				dto.setIs_approve(rs.getInt("is_approve"));
				dto.setAnswer_true(rs.getInt("answer_true"));
				dto.setIs_delete(rs.getInt("is_delete"));
				dto.setIs_enable(rs.getInt("is_enable"));
				dto.setDate_modified(rs.getString("date_modified"));			
				
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
	public String getMtFromQuestion(QuestionDTO questionDTO){
		return questionDTO.getQuestion() 
		+ "Cau tra loi 1:" + questionDTO.getAnswer1()
		+ "Cau tra loi 2 :" + questionDTO.getAnswer2();
	}
	
	
}
