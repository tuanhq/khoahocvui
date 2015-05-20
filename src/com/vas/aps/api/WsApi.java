package com.vas.aps.api;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.http.AbsWebservice;
import com.ligerdev.appbase.utils.textbase.ReqCountUtils;
import com.vas.aps.MainAps;
import com.vas.aps.cmd.AnswerCmd;
import com.vas.aps.cmd.LastQuestionCmd;
import com.vas.aps.cmd.ViewScoreCmd;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.comms.XmlConfigs;
import com.vas.aps.db.orm.MoHis;
import com.vas.aps.db.orm.Subscriber;

@WebService(targetNamespace = "http://javax.jws.server", serviceName = "WSAPIService", portName = "WSAPIPort", name = "WSAPI")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class WsApi extends AbsWebservice {
	
	private MainAps mainApp = MainAps.getInstance();
	private BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	private static ReqCountUtils countManager = ReqCountUtils.getInstance("WsApi", "");
	
	public CmdResult getQuestionCmd(
			@WebParam(name = "msisdn") String msisdn,
			@WebParam(name = "channel") String channel,
			@WebParam(name = "sendSms") boolean sendSms) {
		
		long l1 = System.currentTimeMillis();
		msisdn = BaseUtils.formatMsisdn(msisdn, "84", "84");
		String transid = getTransid(sendSms, msisdn, channel);
		
		logger.info(transid + " ########, sendSms = " + sendSms + ", channel = " + channel);
		MoHis mo = new MoHis(0, msisdn, "#" + channel.toLowerCase(), 
				XmlConfigs.Syntax.LAST_QUESTION.getCommand(), null, transid.split("@")[0]);
		
		int moId = baseDAO.insertBean(transid, mo);
		mo.setId(moId);
		mo.setTransId(transid);

		LastQuestionCmd cmd = new LastQuestionCmd();
		cmd.setChannel(channel);
		cmd.setMo(mo);
		
		CmdResult result = cmd.execute();
		if (result != null && result.getSubscriber() != null) {
			result.getSubscriber().setQuesReceived(null);
		}
		logger.info(transid + ", resp errCode = " + String.valueOf(result) + ", time = " + (System.currentTimeMillis() - l1));
		return result;
	}

	public CmdResult answerCmd(
			@WebParam(name = "msisdn") String msisdn, 
			@WebParam(name = "answer") String answer, 
			@WebParam(name = "channel") String channel,
			@WebParam(name = "sendSms") boolean sendSms) {
		
		long l1 = System.currentTimeMillis();
		msisdn = BaseUtils.formatMsisdn(msisdn, "84", "84");
		String transid = getTransid(sendSms, msisdn, channel);
		
		logger.info(transid + " ######## sendSms = " + sendSms + ", answer = " + answer + ", channel = " + channel);
		MoHis mo = new MoHis(0, msisdn,  answer, XmlConfigs.Syntax.ANSWER.getCommand(), null, transid.split("@")[0]);

		int moId = baseDAO.insertBean(transid, mo);
		mo.setId(moId);
		mo.setTransId(transid);

		AnswerCmd cmd = new AnswerCmd();
		cmd.setChannel(channel);
		cmd.setMo(mo);
		
		CmdResult result = cmd.execute();
		if (result != null && result.getSubscriber() != null) {
			result.getSubscriber().setQuesReceived(null);
		}
		logger.info(transid + ", resp errCode = " + String.valueOf(result) + ", time = " + (System.currentTimeMillis() - l1));
		return result;
	}

	public CmdResult viewScoreCmd(
			@WebParam(name = "msisdn") String msisdn, 
			@WebParam(name = "channel") String channel,
			@WebParam(name = "sendSms") boolean sendSms) {
		
		long l1 = System.currentTimeMillis();
		msisdn = BaseUtils.formatMsisdn(msisdn, "84", "84");
		String transid = getTransid(sendSms, msisdn, channel);
		
		logger.info(transid + " ########, sendSms = " + sendSms + ", channel = " + channel);
		MoHis mo = new MoHis(0, msisdn,  "#" + channel.toLowerCase(), 
				XmlConfigs.Syntax.VIEW_SCORE.getCommand(), null, transid.split("@")[0]);

		int moId = baseDAO.insertBean(transid, mo);
		mo.setId(moId);
		mo.setTransId(transid);

		ViewScoreCmd cmd = new ViewScoreCmd();
		cmd.setChannel(channel);
		cmd.setMo(mo);
		
		CmdResult result = cmd.execute();
		if (result != null && result.getSubscriber() != null) {
			result.getSubscriber().setQuesReceived(null);
		}
		logger.info(transid + ", resp errCode = " + String.valueOf(result) + ", time = " + (System.currentTimeMillis() - l1));
		return result;
	}

	public SubsResult getSubscriber(
			@WebParam(name = "msisdn") String msisdn,
			@WebParam(name = "channel") String channel) {
		
		long l1 = System.currentTimeMillis();
		msisdn = BaseUtils.formatMsisdn(msisdn, "84", "84");
		String transid = getTransid(true, msisdn, channel);
		
		logger.info(transid + " ######## get subscriber info, channel = " + channel);
		Subscriber subs = baseDAO.getBeanByKey(transid, Subscriber.class, msisdn);
		if (subs == null) {
			SubsResult result = new SubsResult(404, null);
			logger.info(transid + ", resp errCode = " + result.getErrorCode() + ", time = " + (System.currentTimeMillis() - l1));
			return result;
		}
		SubsResult result = new SubsResult(0, subs);
		logger.info(transid + ", resp errCode = " + result.getErrorCode() + ", time = " + (System.currentTimeMillis() - l1));
		return result;
	}
	
	private String getTransid(boolean sendSms, String msisdn, String channel){
		String prefix = null;

		if (AppConstants.CHANNEL_WAP.equalsIgnoreCase(channel)) {
			prefix = "WA";
			
		} else if (AppConstants.CHANNEL_WEB.equalsIgnoreCase(channel)) {
			prefix = "WE";  
			
		} else {
			prefix = "WU";
		}
		String smsCode = "";
		if(sendSms == false){
			smsCode = "Z";
		}
		String code = countManager.countLongStr() + "@" + BaseUtils.formatMsisdn(msisdn, "84", "");
		String transid = prefix + smsCode + code;
		return transid;
	}
}
