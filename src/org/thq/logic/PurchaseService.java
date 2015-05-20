/**
 * 
 */
package org.thq.logic;

import java.sql.SQLException;

import org.thq.api.ResponeRequest;
import org.thq.api.ResponseRequestMps;
import org.thq.business.AddQuestionDAO;
import org.thq.business.AddQuestionDTO;
import org.thq.business.QuestionAndAnswerLog;
import org.thq.business.QuestionAndAnswerLogDAO;
import org.thq.business.QuestionDAO;
import org.thq.business.QuestionDTO;
import org.thq.utils.Util;

/**
 * @author tuanhq
 *
 */
public class PurchaseService {
	private AddQuestionDAO addQuestionDao;
	/**
	 * 
	 */
	private QuestionAndAnswerLogDAO questionAndAnswerDao ;
	private QuestionDAO questionDao ;
	public PurchaseService() {
		
		addQuestionDao = new AddQuestionDAO();
		questionAndAnswerDao = new QuestionAndAnswerLogDAO();
		questionDao = new QuestionDAO(); 
	}
	public String executePurchase(String msisdn) throws ClassNotFoundException, SQLException, InterruptedException{
		String mt="";
		//check xem con cau hoi hay ko ?
		int totalQuestion =0, totalAnswer=0 , totalQuestionBuy =0;
		totalQuestionBuy = addQuestionDao.getTotalQuestionAddByMSISDN(msisdn, Util.getStringDateNormal());
		
		totalQuestion = totalQuestionBuy + UtilContanst.Question_quota;				
		totalAnswer = questionAndAnswerDao.getTotalQuestionOrAnswerByMSISDN(msisdn, UtilContanst.AnswerType);
		if(totalAnswer < totalQuestion){
			mt = UtilContanst.Answer_NOT_OverQuota;
		}else{
		AddQuestionDTO addQuestionDto = new AddQuestionDTO();
		addQuestionDto.setSubId(msisdn);
		addQuestionDto.setType(UtilContanst.MUA);
		addQuestionDto.setAddQuestion(UtilContanst.Number_question_buy);
		addQuestionDto.setDate(Util.getStringDate());
		addQuestionDao.insertQuestionAndAnswerLog(addQuestionDto);
		mt = UtilContanst.MUA_MT;
		
		//get question cho thoi diem hien tai
			QuestionDTO questionDTO = questionDao.getQuestionActive();			
			//lay thong tin nhung thue bao dang co trang thai active trong ban subInfo va sen mt
			ResponeRequest responseRequest = new ResponseRequestMps();
			responseRequest.responeRequest(msisdn,mt);
			Thread.sleep(1000);
			mt ="-1";
			if(questionDTO==null){//neu ko co cau hoi active tren he thong send loi toi admin
				responseRequest.responeRequest("84985633469", "he thong chua co cau hoi active hay check lai he thong ngay");
				
			}else{
				responseRequest.responeRequest(msisdn, questionDTO.getQuestion() 
						+ "Cau tra loi 1:" + questionDTO.getAnswer1()
						+ "Cau tra loi 2 :" + questionDTO.getAnswer2());
				QuestionAndAnswerLog questionAndAnswerDto = new QuestionAndAnswerLog(msisdn,
						questionDTO.getQuestion_id(), UtilContanst.QuestionType, -1, Util.getStringDate());
				 questionAndAnswerDao.insertQuestionAndAnswerLog(questionAndAnswerDto);
					questionDTO.setIs_enable(0);
					questionDao.updateQuestion(questionDTO);
			}
		
	
		
		
		
		}
		return mt;
		
	}

}
