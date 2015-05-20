/**
 * 
 */
package org.thq.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.thq.database.ConnectionPool;

/**
 * @author tuanhq
 *
 */
public class ChargingInfoDAO {
	private static final Logger logger = Logger.getLogger(ChargingInfoDAO.class);

	public boolean insertChargingInfor(ChargingInfo chargingInfo) throws ClassNotFoundException, SQLException {

		boolean result = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		String strSQL = null;
		try {
			strSQL = "INSERT INTO charging_info" + "(user_id, service_id, command_code, timestamp1, timestamp2, Proc_result, mts) " + " values(?,?,?,?,?,?,?)";

			conn = ConnectionPool.getConnection();
			stmt = conn.prepareStatement(strSQL);
			stmt.setString(1, chargingInfo.getUserId());
			stmt.setString(2, chargingInfo.getServiceId());
			stmt.setString(3, chargingInfo.getCommandCode());
			stmt.setString(4, chargingInfo.getTimeStamp1());
			stmt.setString(5, chargingInfo.getTimeStamp2());
			stmt.setString(6, chargingInfo.getProResult());
			stmt.setString(7, chargingInfo.getMts());
			result = stmt.executeUpdate() >= 0;
		} catch (Exception e) {
			logger.error("insert: Errior executing " + strSQL + " >>> " + e.toString());
			e.printStackTrace();

		} finally {
			ConnectionPool.putConnection(conn);
			stmt.close();
		}
		return result;

	}

	public static void main(String[] args) {
		ChargingInfo chargingInfo =new ChargingInfo("01245", "6x71", "Kep","123456" , "123234455", "1", "1");
		try {
			new ChargingInfoDAO().insertChargingInfor(chargingInfo);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
