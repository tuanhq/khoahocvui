/**
 * 
 */
package org.thq.logic;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.thq.api.ResponeRequest;
import org.thq.api.ResponseRequestMps;
import org.thq.business.SubRequestLog;
import org.thq.business.SubRequestLogDAO;
import org.thq.utils.Util;

/**
 * @author tuanhq
 *
 */
public class RequestDispatcher {
	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(RequestDispatcher.class);
	private RegisterService registerSevice;
	private SubRequestLogDAO subRequestLogDao;
	private DestroyService destroyService;
	private ResponeRequest responeRequest;
	private SearchPointService searchPointService;
	private PurchaseService purcharseSevice;
	private DuplicateService duplicateService;
	private AnswerService answerService;
	private ChangeQuestionService changeQuestionService;
	public RequestDispatcher() {
		
		//respone request
		
		responeRequest = new ResponseRequestMps();
		//service dang ki
		registerSevice = new RegisterService();
		subRequestLogDao = new SubRequestLogDAO();
		//service huy
		destroyService = new DestroyService();
		
		//search point service
		
		searchPointService = new SearchPointService();
		
		//purcharse service
		purcharseSevice = new PurchaseService();
		//duplicate 
		duplicateService = new DuplicateService();
		
		// answer sevice
		answerService = new AnswerService();
		//change question 
		
		changeQuestionService = new ChangeQuestionService();
		
	
	}
	
	public void executeMo(String msisdn, String mo){
		
		//insert vao bang sub_request_log
		String mt = "";
		SubRequestLog subRequestLogDto = new SubRequestLog(msisdn, mo, Util.getStringDate());
		try {
			subRequestLogDao.insertSubRequestLog(subRequestLogDto);
		} catch (Exception e) {
			logger.error(Util.stackTraceToString(e));
			e.printStackTrace();
		}
		
		if(mo.equalsIgnoreCase(MoRequest.DK)){
			//xu li dk
			try {
				mt = registerSevice.executeRegister(msisdn);
			} catch (Exception e) {
				logger.error(Util.stackTraceToString(e));
				e.printStackTrace();
			}
			
		}else if(mo.equals(MoRequest.HUY)){
		
			//xu li huy
			try {
				mt = destroyService.destroyService(msisdn);
			} catch (Exception e) {
				// TODO: handle exception
				logger.error(Util.stackTraceToString(e));
			}
		}else if(mo.equals(MoRequest.DIEM)){
		
			//xu li huy
			try {
				mt = searchPointService.executeSearchPointService(msisdn);
			} catch (Exception e) {
				logger.error(Util.stackTraceToString(e));
				e.printStackTrace();
			}
		}else if(mo.equals(MoRequest.MUA)){
		
			//xu li huy
			try {
				mt = purcharseSevice.executePurchase(msisdn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(Util.stackTraceToString(e));
				e.printStackTrace();
			}
		}else if(mo.equals(MoRequest.DUPLICATE)){
		
			try {
				mt = duplicateService.executeDuplicate(msisdn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(Util.stackTraceToString(e));
				e.printStackTrace();
			}
		}else if(mo.equals(MoRequest.ANSWER1) || mo.equalsIgnoreCase(MoRequest.ANSWER2)){
		
			
			try {
				mt = answerService.executeAnswerService(msisdn, mo);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else if(mo.equals(MoRequest.CHANGE)){
		
		
			//xu li huy
			
			try {
				mt = changeQuestionService.executeChangeQuestionService(msisdn);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}else{
	
		}
		if(!"-1".equalsIgnoreCase(mt)){
			responeRequest.responeRequest(msisdn, mt);
		}
		
	}
}
