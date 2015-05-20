/**
 * 
 */
package org.thq.logic;

import java.sql.SQLException;

import org.thq.business.SubInfoDAO;
import org.thq.business.SubInfoDTO;
import org.thq.business.SubScoreDAO;
import org.thq.business.SubScoreDTO;
import org.thq.utils.Util;

/**
 * @author tuanhq
 *
 */
public class DestroyService {
	/**
	 * 
	 */
	private SubInfoDAO subInfoDao;
	private SubScoreDAO subScoreDao;
	public DestroyService() {
		subInfoDao = new SubInfoDAO();
		subScoreDao = new SubScoreDAO();
	}
	
	public String  destroyService(String msisdn) throws ClassNotFoundException, SQLException{
		String mt = "";
		//check da ton tai trong bang sub_info da ton tai chua
		
		subInfoDao = new SubInfoDAO();
		SubInfoDTO subInfoDto = subInfoDao.getSubInfo(msisdn);
		
		
		//**** neu ton tai roi thi update trong bang sub_info co status =destroy
								//va insert vao sub_score 1 score =-1
		SubInfoDTO dto = new SubInfoDTO(msisdn, SubStatus.INACTIVE, Util.getStringDate());
		if( subInfoDto == null ){
			mt = UtilContanst.Destroy_not_active;
		}else if (subInfoDto != null && subInfoDto.getStatus() == SubStatus.INACTIVE){
			mt = UtilContanst.Destroy_duplicate;					
		}else if(subInfoDto != null && subInfoDto.getStatus() == SubStatus.ACTIVE ){			
			subInfoDao.updateSubInfo(dto);
			SubScoreDTO subScoreDto = new SubScoreDTO(msisdn, AnswerScore.DESTROY_REGISTER, ScoreType.DESTROY, Util.getStringDate());
			subScoreDao.insertSubScore(subScoreDto);
			mt = UtilContanst.Destroy_success;
		}
//		else {//**** neu chua ton tai thi insert vao bang sub_info co status=destroy			
//			subInfoDao.insertSubInfo(dto);
//		}
		
		return mt;
		
	}

}
