/**
 * 
 */
package org.thq.logic;

import java.sql.SQLException;
import java.util.List;

import org.thq.api.ResponeRequest;
import org.thq.api.ResponseRequestMps;
import org.thq.business.AddQuestionDAO;
import org.thq.business.QuestionAndAnswerLog;
import org.thq.business.QuestionAndAnswerLogDAO;
import org.thq.business.QuestionDAO;
import org.thq.business.QuestionDTO;
import org.thq.business.SubActiveDuplicateDAO;
import org.thq.business.SubActiveDuplicateDTO;
import org.thq.business.SubInfoDTO;
import org.thq.business.SubScoreDAO;
import org.thq.business.SubScoreDTO;
import org.thq.business.SystemConfig;
import org.thq.business.SystemConfigDAO;
import org.thq.utils.Util;

/**
 * @author tuanhq
 *
 */
public class AnswerService {
	/**
	 * 
	 */

	private static int count=0;
	private SubScoreDAO subScoreDao;
	private SubActiveDuplicateDAO subActiveDuplicateDao;
	private QuestionAndAnswerLogDAO questionAndAnswerDao;
	private QuestionDAO questionDao;
	private AddQuestionDAO addQuestionDao;
	private SystemConfigDAO systemConfigDao;
	public AnswerService() {
		subScoreDao = new SubScoreDAO();
		subActiveDuplicateDao = new SubActiveDuplicateDAO();
		questionAndAnswerDao = new QuestionAndAnswerLogDAO();
		questionDao = new QuestionDAO();
		addQuestionDao = new AddQuestionDAO();
		systemConfigDao = new SystemConfigDAO();
		
	}
	public String executeAnswerService(String msisdn, String mo) throws InterruptedException, ClassNotFoundException, SQLException{
		String mt = null;
	  SystemConfig systemConfi = systemConfigDao.getSystemConfi("receive_answer");
	  if("0".equalsIgnoreCase(systemConfi.getValue())){
	  	mt = UtilContanst.NOT_RECIVE_ANSER;
	  }
		synchronized (msisdn) {
			
			QuestionAndAnswerLog questionLastest = questionAndAnswerDao.getLastestQuestionByMSISDN(msisdn, UtilContanst.QuestionType);
			QuestionDTO questionDto = questionDao.getQuestionById(questionLastest.getQuestionId());
			QuestionAndAnswerLog answerLastest = questionAndAnswerDao.getLastestQuestionByMSISDN(msisdn, UtilContanst.AnswerType);
			int totalQuestionBuy = addQuestionDao.getTotalQuestionAddByMSISDN(msisdn, Util.getStringDateNormal());
			int totalQuestion = totalQuestionBuy + UtilContanst.Question_quota;
					
			int totalAnswer = questionAndAnswerDao.getTotalQuestionOrAnswerByMSISDN(msisdn, UtilContanst.AnswerType);
			//truong hop 1 con quota cau hoi
			if(totalAnswer < totalQuestion){
				//check cau tra loi cua sub_id
					if(Integer.valueOf(mo).intValue() == questionDto.getAnswer_true()) {
						
							//check user da sen nhan doi hay chu 
							SubActiveDuplicateDTO subActiveDto = subActiveDuplicateDao.getQuestionActive(msisdn);
							SubScoreDTO dto = null;
							if(subActiveDto!=null){
								dto = new SubScoreDTO(msisdn, AnswerScore.TRUE_ANSWER_DUP, ScoreType.DUPLICATE_ANSWER, Util.getStringDate());
								mt=UtilContanst.Answer_TrueX2;
								subActiveDto.setStatus(0);
								//inactive ban ghi da nhan doi diem
								subActiveDuplicateDao.updateSubInfo(subActiveDto);
							}else{
							//**** neu cau tra loi dung thi + insert vao bang sub_score = 100 diem. dem den 100 thi gui 1 ma so may man, 1000 thi gui ma so may man ngon nhat
								dto = new SubScoreDTO(msisdn, AnswerScore.TRUE_ANSWER, ScoreType.ANSWER_TRUE, Util.getStringDate());
								mt=UtilContanst.Answer_True;
							}
							subScoreDao.insertSubScore(dto);
							if(count %100 ==99 ){
								dto = new SubScoreDTO(msisdn, AnswerScore.FORTUNATE_ANSWER1, ScoreType.FORTUNATE_ANSWER1, Util.getStringDate());
								mt = ""; 
							}else if(count % 1000 ==999){
							 mt ="";
							 dto = new SubScoreDTO(msisdn, AnswerScore.FORTUNATE_ANSWER4, ScoreType.FORTUNATE_ANSWER4, Util.getStringDate());
							}else if(count % 10000 ==9999){
								mt ="";
							}else{
								dto = null;
							}
							if(dto != null){
								subScoreDao.insertSubScore(dto);
							}			
							count ++;
						
						
					}else{
						mt = UtilContanst.Answer_Fail;
					}
				QuestionAndAnswerLog questionAndAnswerLog = new QuestionAndAnswerLog();
				questionAndAnswerLog.setSubId(msisdn);
				questionAndAnswerLog.setDate(Util.getStringDate());
				questionAndAnswerLog.setQuestionId(questionLastest.getQuestionId());
				questionAndAnswerLog.setType(UtilContanst.AnswerType);
				questionAndAnswerLog.setAnswer(Integer.valueOf(mo).intValue());
				questionAndAnswerDao.insertQuestionAndAnswerLog(questionAndAnswerLog);
				
				//send cau hoi tiep theo neu khach hang con quota cau hoi
				
				if(totalAnswer +1 < totalQuestion ){
					
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
				
				
			}else{
				mt = UtilContanst.Answer_OverQuota;
			}
		
			
		}
		return mt;
	}

}
