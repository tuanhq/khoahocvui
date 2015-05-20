/**
 * 
 */
package org.thq.logic;

import java.sql.SQLException;

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
public class ChangeQuestionService {
	/**
	 * 
	 */
	private QuestionDAO questionDao ;
	private QuestionAndAnswerLogDAO questionAndAnswerDao;
	private AddQuestionDAO addQuestionDao;
	public ChangeQuestionService() {
		questionDao = new QuestionDAO();
		questionAndAnswerDao = new QuestionAndAnswerLogDAO();
		addQuestionDao = new AddQuestionDAO();
	
	}
	public String executeChangeQuestionService(String msisdn) throws ClassNotFoundException, SQLException{
		String mt = "";
		//neu thue bao gui mt send change question thi gui mt question khac cho sub_id
		
		QuestionAndAnswerLog questionLastest = questionAndAnswerDao.getLastestQuestionByMSISDN(msisdn, UtilContanst.QuestionType);		
		QuestionAndAnswerLog answerLastest = questionAndAnswerDao.getLastestQuestionByMSISDN(msisdn, UtilContanst.AnswerType);
		if(questionLastest.getQuestionId()== answerLastest.getQuestionId()){
			mt = UtilContanst.USER_ANSWERED;
		}else if(questionLastest.getQuestionId()!= answerLastest.getQuestionId() &&
			questionLastest.getDate().compareToIgnoreCase(answerLastest.getDate())<0){
			QuestionDTO questionDTO = questionDao.getQuestionActive();
			AddQuestionDTO addQuestionDto = new AddQuestionDTO();
			addQuestionDto.setAddQuestion(UtilContanst.Number_question_doi);
			addQuestionDto.setDate(Util.getStringDate());
			addQuestionDto.setSubId(msisdn);
			addQuestionDto.setType(UtilContanst.DOI);
			addQuestionDao.insertQuestionAndAnswerLog(addQuestionDto);
		
			QuestionAndAnswerLog questionAndAnswerDto = new QuestionAndAnswerLog(msisdn,
					questionDTO.getQuestion_id(), UtilContanst.QuestionType, -1, Util.getStringDate());
			 questionAndAnswerDao.insertQuestionAndAnswerLog(questionAndAnswerDto);
			 mt = questionDao.getMtFromQuestion(questionDTO);
		}else{
			
		}
			
		return mt;
		
		
		//sent mt to sub_id
		
	}

}
