package com.vas.aps.api.mps;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.http.AbsWebservice;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.ligerdev.appbase.utils.textbase.ReqCountUtils;
import com.vas.aps.comms.XmlConfigs;

@WebService(targetNamespace = "http://javax.jws.server", serviceName = "WSAPIService", portName = "WSAPIPort", name = "WSAPI")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class WsMonthfee extends AbsWebservice {

	private static ReqCountUtils reqCountUtils = ReqCountUtils.getInstance("WsRenewListener", "R");
	protected static Logger loggerCDR = Log4jLoader.getLogger("MONTHFEE");
	
	public String resultRequest (
			@WebParam(name = "username") String username, 
			@WebParam(name = "password") String password, 
			@WebParam(name = "serviceid") String serviceid, 
			@WebParam(name = "msisdn") String msisdn, 
			@WebParam(name = "chargetime") String chargetime, 
			@WebParam(name = "params") String params, 
			@WebParam(name = "mode") String mode, 
			@WebParam(name = "amount") String amount){
		
		msisdn = BaseUtils.formatMsisdn(msisdn, "84", "84");
		String transid = reqCountUtils.countLongStr() + "@" + BaseUtils.formatMsisdn(msisdn, "84", "");

		logger.info(transid + " ######## serviceid = " + serviceid + "; msisdn = " + msisdn + "; chargetime = "
				+ chargetime + "; params = " + params + "; mode = " +  mode + "; amount = "
				+ amount + "; username = " + username + ", password = " + password);
		//thuc hien insert du lieu vao databse o day
		loggerCDR.info(BaseUtils.formatTime("yyyy/MM/dd HH:mm:ss", System.currentTimeMillis()) 
				+ "|serviceid=" + serviceid + "|msisdn=" + msisdn + "|chargetime=" + chargetime + "|params=" + params + "|mode=" 
				+ mode + "|amount=" + amount + "|resp=0|transid=" + transid); 
		
		return "0";
	} 
	
	public static void main(String[] args) {
		Log4jLoader.init();
		Endpoint.publish(args[0], new WsMonthfee());
		logger.info("Published WS URL: " + args[0]); 
	}
}
