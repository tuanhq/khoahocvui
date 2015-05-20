/**
 * 
 */
package org.thq.logic;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.thq.business.SubInfoDAO;
import org.thq.business.SubInfoDTO;
import org.thq.business.SubScoreDAO;
import org.thq.business.SubScoreDTO;
import org.thq.utils.Util;

/**
 * @author tuanhq
 *
 */
public class RegisterService {
	private static final Logger logger = Logger.getLogger(RegisterService.class);
	private SubInfoDAO subInfoDao;
	private SubScoreDAO subScoreDao;
	/**
	 * 
	 */
	public RegisterService() {
		subInfoDao = new SubInfoDAO();
		subScoreDao = new SubScoreDAO();
	}
	public String executeRegister(String msisdn) throws ClassNotFoundException, SQLException{
		String mt = "";
		// check xem thue bao la lan dau tien dang ky hay ko  (bang sub_info)
		
		SubInfoDTO subInfoDto = subInfoDao.getSubInfo(msisdn);
		
		//***neu thue bao lan dau thi insert vao bang subscore 500 diem
		//insert vao bang sub_info voi status = active
		if (subInfoDto == null ){
			SubScoreDTO subScoreDto = new SubScoreDTO(msisdn ,
					AnswerScore.FIRST_REGISTER, ScoreType.FIRST_REGISTER, Util.getStringDate());
			subScoreDao.insertSubScore(subScoreDto);
			SubInfoDTO  dto = new SubInfoDTO(msisdn, SubStatus.ACTIVE, Util.getStringDate());
			subInfoDao.insertSubInfo(dto);
			mt = UtilContanst.Register_success_first.replace("XXXX", msisdn);
		}else if(subInfoDto != null && subInfoDto.getStatus() == SubStatus.INACTIVE) {//*** nguoc lai update vao bang sub_info trang thai bang bang =active voi sub_id
			SubInfoDTO  dto = new SubInfoDTO(msisdn, SubStatus.ACTIVE, Util.getStringDate());
			subInfoDao.updateSubInfo(dto);
			mt = UtilContanst.Register_success.replace("XXXX", msisdn);
		}else if(subInfoDto != null && subInfoDto.getStatus() == SubStatus.ACTIVE){
			mt = UtilContanst.Register_duplicate;
					
		}
		return mt;
	
		
		
		
	}
}
