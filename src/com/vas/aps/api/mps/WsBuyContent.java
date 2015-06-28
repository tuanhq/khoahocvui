package com.vas.aps.api.mps;

import java.text.SimpleDateFormat;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.http.AbsWebservice;
import com.ligerdev.appbase.utils.textbase.ReqCountUtils;
import com.vas.aps.MainAps;
import com.vas.aps.api.CmdResult;
import com.vas.aps.cmd.AbstractCmd;
import com.vas.aps.cmd.ActiveDuplicateCmd;
import com.vas.aps.cmd.BuyQuestionCmd;
import com.vas.aps.cmd.ChangeQuestionCmd;
import com.vas.aps.cmd.RegisterCmd;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.comms.AppUtils;
import com.vas.aps.comms.XmlConfigs;
import com.vas.aps.db.orm.MoHis;
import com.vas.aps.db.orm.MtHis;
import com.vas.aps.db.orm.Question;
import com.vas.aps.db.orm.Subscriber;
import com.vas.aps.tablecache.MessageFactory;
import com.vas.aps.tablecache.QuestionFactory;

@WebService(targetNamespace = "http://javax.jws.server", serviceName = "WSAPIService", portName = "WSAPIPort", name = "WSAPI")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class WsBuyContent extends AbsWebservice {

	private static ReqCountUtils reqCountUtils = ReqCountUtils.getInstance("BuyContentListener", "C");
	private static BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	private MainAps mainApp = MainAps.getInstance();
	
	public String contentRequest (
			@WebParam(name = "username") String username, 
			@WebParam(name = "password") String password, 
			@WebParam(name = "serviceid") String serviceid, 
			@WebParam(name = "msisdn") String msisdn, 
			@WebParam(name = "params") String params,  
			@WebParam(name = "mode") String mode, 
			@WebParam(name = "amount") String amount){
		
		msisdn = BaseUtils.formatMsisdn(msisdn, "84", "84");
		String transid = reqCountUtils.countLongStr() + "@" + BaseUtils.formatMsisdn(msisdn, "84", "");
		
		logger.info(transid + " ######## serviceid = " + serviceid + "; msisdn = " + msisdn 
				+ "; params = " + params + "; mode = " +  mode + "; amount = " + amount 
				+ "; username = " + username + ", password = " + password);
		
		
		
		Subscriber subs = baseDAO.getBeanByKey(transid, Subscriber.class, msisdn);
		
		if (subs == null || subs.getStatus() == AppConstants.SUBS_STATUS_DEACTIVE) {
			logger.info( transid + ", subs is not activing");
			
			MtHis mt = MessageFactory.getMessage(transid, msisdn, 0, AppConstants.MT_REQUEST_WHEN_NOT_ACTIVE, subs);
			mainApp.getMtQueue().addLast(mt);
			return "1|Failed";
		}
		
		
		if("CHECK".equalsIgnoreCase(mode) && ("DOI".equalsIgnoreCase(params)||"X2".equalsIgnoreCase(params))){
			//logger.info(transid + ", mode is NOT real => return success");
			//check user have quota answer
			
			int answerTh = AppUtils.getAnsweredCount(subs, "SMS"); 
			if (answerTh >= XmlConfigs.MAX_QUESTION_PER_CHANNEL ) {
				logger.info(transid + ", over limit answer for a channel");
				
				MtHis mt = MessageFactory.getMessage(transid, msisdn, 0, AppConstants.MT_ANSWER_LAST_QUESTION, subs);
				mainApp.getMtQueue().addLast(mt);
				
				return "1|Failed";
			}
			
			return "0|success";
		}
		
		
		if(!"REAL".equalsIgnoreCase(mode)){
			logger.info(transid + ", mode is NOT real => return success");
			return "0|success";
		}
		
		AbstractCmd cmd = null;
		MoHis mo =null;
		
		if("X2".equalsIgnoreCase(params)){
			//insert thue bao vao bang duplicate point ansewer
			mo = new MoHis(0, msisdn, params, "X2", null, transid.split("@")[0]);
			cmd = new ActiveDuplicateCmd();
			
		}
		if("MUA".equalsIgnoreCase(params)){
			//insert to bang mua question
			mo = new MoHis(0, msisdn, params, "MUA", null, transid.split("@")[0]);
			cmd = new BuyQuestionCmd();
			
			
		}if("DOI".equalsIgnoreCase(params)){
			//tra ve cau hoi moi cho thue bao
			mo = new MoHis(0, msisdn, params, "DOI", null, transid.split("@")[0]);
			cmd = new ChangeQuestionCmd();
			
		}
		
		int moId = baseDAO.insertBean(transid, mo);
		mo.setId(moId);
		mo.setTransId(transid);
		cmd.setMo(mo);
		logger.info(transid + ", command = " + mo.getCommand() + ", moId = " + mo.getId());
		
		CmdResult cmdResult = cmd.execute();
		return cmdResult.getErrorCode() + "|" + cmdResult.getErrorDesc();
		
	} 
}
