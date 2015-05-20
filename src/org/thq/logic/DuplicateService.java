/**
 * 
 */
package org.thq.logic;

import java.sql.SQLException;

import org.thq.business.SubActiveDuplicateDAO;
import org.thq.business.SubActiveDuplicateDTO;
import org.thq.utils.Util;

/**
 * @author tuanhq
 *
 */
public class DuplicateService {
	/**
	 * 
	 */
	private SubActiveDuplicateDAO subActiveDao;
	public DuplicateService() {
		subActiveDao = new SubActiveDuplicateDAO();
	}
	public String executeDuplicate(String msisdn) throws ClassNotFoundException, SQLException{
		String mt ="";
		//neu sub_id request duplicate thi se active trong bang X2_score de sau khi thue bao tra loi se tham 
		//chieu vao do de nhan doi so diem 
		SubActiveDuplicateDTO dto = new SubActiveDuplicateDTO(msisdn, SubStatus.ACTIVE, Util.getStringDate());
		subActiveDao.insertSubDuplicate(dto);
		mt = UtilContanst.X2_MT;
		return mt;
	}

}
