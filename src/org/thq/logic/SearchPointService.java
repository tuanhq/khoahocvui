/**
 * 
 */
package org.thq.logic;

import java.sql.SQLException;

import org.thq.business.SubScoreDAO;

/**
 * @author tuanhq
 *
 */
public class SearchPointService {
	/**
	 * 
	 */
	private SubScoreDAO subScoreDao;
	public SearchPointService() {
		subScoreDao = new SubScoreDAO();
	}
	public String executeSearchPointService(String msisdn) throws ClassNotFoundException, SQLException{
		//search trong bang sub_score va goi ham send mt toi thue bao diem cua thue bao hien tai
		String mt ="";
		int point = subScoreDao.getPointByMSISDN(msisdn);
		mt = UtilContanst.Search_point.replace("YYYY",String.valueOf(point));
		
		return mt;
	}

}
