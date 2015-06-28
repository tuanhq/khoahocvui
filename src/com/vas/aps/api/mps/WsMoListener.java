package com.vas.aps.api.mps;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.http.AbsWebservice;
import com.ligerdev.appbase.utils.textbase.ReqCountUtils;
import com.vas.aps.api.CmdResult;
import com.vas.aps.cmd.AbstractCmd;
import com.vas.aps.cmd.AcceptQuestionCmd;
import com.vas.aps.cmd.AnswerCmd;
import com.vas.aps.cmd.HelpCmd;
import com.vas.aps.cmd.IgnoreQuestionCmd;
import com.vas.aps.cmd.LastQuestionCmd;
import com.vas.aps.cmd.TopScoreCmd;
import com.vas.aps.cmd.ViewScoreCmd;
import com.vas.aps.cmd.WrongSyntaxCmd;
import com.vas.aps.comms.AppUtils;
import com.vas.aps.comms.XmlConfigs;
import com.vas.aps.db.orm.MoHis;

@WebService(targetNamespace = "http://javax.jws.server", serviceName = "WSAPIService", portName = "WSAPIPort", name = "WSAPI")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class WsMoListener extends AbsWebservice {

	private static ReqCountUtils reqCountUtils = ReqCountUtils.getInstance("MoListener", "M");
	private static BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	
	public String moRequest(
			@WebParam(name = "username") String username, 
			@WebParam(name = "password") String password, 
			@WebParam(name = "source") String msisdn,  
			@WebParam(name = "dest") String dest,  
			@WebParam(name = "content") String content) {
		
		msisdn = BaseUtils.formatMsisdn(msisdn, "84", "84");
		String transid = reqCountUtils.countLongStr() + "@" + BaseUtils.formatMsisdn(msisdn, "84", "");
		
		logger.info(transid + " ######## source = " + msisdn + "; dest = " + dest 
				+ "; content = " + content+ "; user = " + username + ", pass = " + password);
		content = BaseUtils.cutText(content, 50);
		
		StringBuilder cmdValue = new StringBuilder();
		AbstractCmd cmd = null;
		MoHis mo =null;
		
		if(AppUtils.isMatch(XmlConfigs.Syntax.HELP, content, cmdValue)){
			mo = new MoHis(0, msisdn, content, cmdValue.toString(), null, transid.split("@")[0]);
			cmd = new HelpCmd();
			
		} else if(AppUtils.isMatch(XmlConfigs.Syntax.ANSWER, content, cmdValue)){
			mo = new MoHis(0, msisdn, content, cmdValue.toString(), null, transid.split("@")[0]);
			cmd = new AnswerCmd();
			
		} else if(AppUtils.isMatch(XmlConfigs.Syntax.LAST_QUESTION, content, cmdValue)){
			mo = new MoHis(0, msisdn, content, cmdValue.toString(), null, transid.split("@")[0]);
			cmd = new LastQuestionCmd();
			
		} else if(AppUtils.isMatch(XmlConfigs.Syntax.IGNORE_QUESTION, content, cmdValue)){
			mo = new MoHis(0, msisdn, content, cmdValue.toString(), null, transid.split("@")[0]);
			cmd = new IgnoreQuestionCmd();
			
		} else if(AppUtils.isMatch(XmlConfigs.Syntax.ACCEPT_QUESTION, content, cmdValue)){
			mo = new MoHis(0, msisdn, content, cmdValue.toString(), null, transid.split("@")[0]);
			cmd = new AcceptQuestionCmd();
			
		} else if(AppUtils.isMatch(XmlConfigs.Syntax.VIEW_SCORE, content, cmdValue)){
			mo = new MoHis(0, msisdn, content, cmdValue.toString(), null, transid.split("@")[0]);
			cmd = new ViewScoreCmd();
		}else if(AppUtils.isMatch(XmlConfigs.Syntax.TOP_SCORE, content, cmdValue)){
			mo = new MoHis(0, msisdn, content, cmdValue.toString(), null, transid.split("@")[0]);
			cmd = new TopScoreCmd();
		}
		
		else {
			mo = new MoHis(0, msisdn, content, "wrong_syntax", null, transid.split("@")[0]);
			cmd = new WrongSyntaxCmd();
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
