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
import com.vas.aps.cmd.HelpCmd;
import com.vas.aps.cmd.RegisterCmd;
import com.vas.aps.cmd.UnregisterCmd;
import com.vas.aps.cmd.WrongSyntaxCmd;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.comms.AppUtils;
import com.vas.aps.comms.XmlConfigs;
import com.vas.aps.db.orm.MoHis;

@WebService(targetNamespace = "http://javax.jws.server", serviceName = "WSAPIService", portName = "WSAPIPort", name = "WSAPI")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class WsSubscribe extends AbsWebservice {

	private static ReqCountUtils reqCountUtils = ReqCountUtils.getInstance("SubscribeListener", "S");
	private static BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	
	public String subRequest (
			@WebParam(name = "username") String username, 
			@WebParam(name = "password") String password, 
			@WebParam(name = "serviceid") String serviceid, 
			@WebParam(name = "msisdn") String msisdn, 
			@WebParam(name = "chargetime") String chargetime, 
			@WebParam(name = "params") String params, 
			@WebParam(name = "mode") String mode, 
			@WebParam(name = "amount") String amount, 
			@WebParam(name = "command") String command){
		
		msisdn = BaseUtils.formatMsisdn(msisdn, "84", "84");
		String transid = reqCountUtils.countLongStr() + "@" + BaseUtils.formatMsisdn(msisdn, "84", "");
		if(command == null){
			command = "";
		}
		logger.info(transid + " ######## serviceid = " + serviceid + ", msisdn = " + msisdn + "; chargetime = "
				+ chargetime + "; params = " + params + "; mode = " +  mode + "; amount = "+ amount 
				+ "; command = " + command + "; username = " + username + ", password = " + password);
		
		if(!"REAL".equalsIgnoreCase(mode)){
			logger.info(transid + ", mode is NOT real => return success");
			return "0|success";
		}
		AbstractCmd cmd = null;
		MoHis mo =null;
		
		if("0".equals(params)){
			mo = new MoHis(0, msisdn, command, "register", null, transid.split("@")[0]);
			cmd = new RegisterCmd();
		} 
		else {
			mo = new MoHis(0, msisdn, command, "unregister", null, transid.split("@")[0]);
			cmd = new UnregisterCmd();
			
			if(!command.contains("huy") && !command.contains("yes")){
				cmd.setChannel(AppConstants.CHANNEL_SYS);
			}
		}
		int moId = baseDAO.insertBean(transid, mo);
		mo.setId(moId);
		mo.setTransId(transid);
		cmd.setMo(mo);
		cmd.setAmount(BaseUtils.parseInt(command, 0));
		//cmd.setAmount(BaseUtils.parseInt(amount, 0));
		logger.info(transid + ", command = " + mo.getCommand() + ", moId = " + mo.getId());
		
		CmdResult cmdResult = cmd.execute();
		return cmdResult.getErrorCode() + "|" + cmdResult.getErrorDesc();
	} 
}
