package com.vas.aps.api.mps;

import java.text.SimpleDateFormat;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.http.AbsWebservice;
import com.ligerdev.appbase.utils.textbase.ReqCountUtils;
import com.vas.aps.comms.XmlConfigs;

@WebService(targetNamespace = "http://javax.jws.server", serviceName = "WSAPIService", portName = "WSAPIPort", name = "WSAPI")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class WsBuyContent extends AbsWebservice {

	private static ReqCountUtils reqCountUtils = ReqCountUtils.getInstance("BuyContentListener", "C");
	
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
		
		// dv này k sử dụng hàm này
		return "0";
	} 
}
