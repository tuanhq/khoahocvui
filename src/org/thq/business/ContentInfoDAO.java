/**
 * 
 */
package org.thq.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.thq.database.ConnectionPool;

/**
 * @author tuanhq
 *
 */
public class ContentInfoDAO {
	private static final Logger logger = Logger.getLogger(ContentInfoDAO.class);
	public String getMt(String mo) throws ClassNotFoundException, SQLException{

		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		ResultSet rs =null;
		String mt =null;
		
		try {
			strSQL = "select * from content_info where upper(mo)='" + mo + "'"; 
			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			rs = stmt.executeQuery();
			while ((rs != null) && rs.next()) {				
				mt =rs.getString("mt");		
			
			}
		}catch (Exception e) {
			logger.error("insert: Errior executing " + strSQL + " >>> " + e.toString());
			e.printStackTrace();
		}finally {
			ConnectionPool.putConnection(conn);
			stmt.close();
			rs.close();
		}
		return mt;
			
		
	
	}
	public static void main(String[] args)  {
		try {
			String a = new ContentInfoDAO().getMt("KEP");
			System.out.println(a);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

}
