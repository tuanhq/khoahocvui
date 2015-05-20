/**
 * 
 */
package org.thq.schedulerTask;

import java.sql.SQLException;
import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.thq.api.ResponeRequest;
import org.thq.api.ResponseRequestMps;
import org.thq.business.QuestionAndAnswerLog;
import org.thq.business.QuestionAndAnswerLogDAO;
import org.thq.business.QuestionDAO;
import org.thq.business.QuestionDTO;
import org.thq.business.SubInfoDAO;
import org.thq.business.SubInfoDTO;
import org.thq.logic.RegisterService;
import org.thq.logic.UtilContanst;
import org.thq.utils.Util;

/**
 * @author tuanhq
 *
 */
public class SendQuestionScheduler extends TimerTask{
	private static final Logger logger = Logger.getLogger(SendQuestionScheduler.class);

	public static void main(String[] args) {
		new SendQuestionScheduler().run();
	}
	
	QuestionDAO questionDao ;
	SubInfoDAO subInfoDao;
	QuestionAndAnswerLogDAO questionAndAnswerDao ;
	/**
	 * 
	 */
	public SendQuestionScheduler() {
		questionAndAnswerDao = new QuestionAndAnswerLogDAO();
		subInfoDao = new SubInfoDAO();
		questionDao = new QuestionDAO();
	}
	@Override
	public void run() {
		System.out.println("Time run tu dong:" + Util.getStringDate());
		try {
		//get question cho thoi diem hien tai
			QuestionDTO questionDTO = questionDao.getQuestionActive();			
			//lay thong tin nhung thue bao dang co trang thai active trong ban subInfo va sen mt
			ResponeRequest responseRequest = new ResponseRequestMps();
			if(questionDTO==null){//neu ko co cau hoi active tren he thong send loi toi admin
				responseRequest.responeRequest("84985633469", "he thong chua co cau hoi active hay check lai he thong ngay");
				return;
			}
			List<SubInfoDTO> listSubInfo =  subInfoDao.getListSubInfoActive();
			for (SubInfoDTO subInfoDTO : listSubInfo) {
				responseRequest.responeRequest(subInfoDTO.getSub_id(), questionDTO.getQuestion() 
						+ "Cau tra loi 1:" + questionDTO.getAnswer1()
						+ "Cau tra loi 2 :" + questionDTO.getAnswer2());
				QuestionAndAnswerLog questionAndAnswerDto = new QuestionAndAnswerLog(subInfoDTO.getSub_id(),
						questionDTO.getQuestion_id(), UtilContanst.QuestionType, -1, Util.getStringDate());
				 questionAndAnswerDao.insertQuestionAndAnswerLog(questionAndAnswerDto);
			}
			questionDTO.setIs_enable(0);
			questionDao.updateQuestion(questionDTO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(Util.stackTraceToString(e));
		}
		
		
		
	}

}
