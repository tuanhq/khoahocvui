package com.ligerdev.appbase.utils.db;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.http.AbsWebservice;

@WebService(targetNamespace = "http://javax.jws.server", serviceName = "WSAPIService", portName = "WSAPIPort", name = "WSAPI")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class WSPoolMonitor extends AbsWebservice {
	
	private DBPoolMnt dbPoolUtils = null;
	
	public WSPoolMonitor(DBPoolMnt dbPoolUtils) {
		this.dbPoolUtils = dbPoolUtils;
	}

	public String logPoolByDate(){
		
		String log = dbPoolUtils.getInfoByDateStr() ;
		if(BaseUtils.isBlank(log) || "null".equalsIgnoreCase(log)){
			log = "Not found info for today"; 
			logger.info(log);
			return log;
		}
		logger.info(log);
		return "." + log + "\n.";
	}
	
	public String logPoolByHour(
			@WebParam(name = "hour") int hour){
		
		String log = dbPoolUtils.getInfoByHourStr(hour);
		if(BaseUtils.isBlank(log) || "null".equalsIgnoreCase(log)){
			log = "Not found info for hour = " + hour; 
			logger.info(log);
			return log;
		}
		logger.info(log);
		return "." + log + "\n.";
	}
}
