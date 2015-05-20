package com.vas.aps.mpsclient;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.http.HttpClientUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.vas.aps.comms.XmlConfigs;

public class SubsUpdateClient {

	private static Logger logger = Log4jLoader.getLogger();
	private static HttpClientUtils httpClientUtils = HttpClientUtils.getInstance(16000);

	public static String CMD_REGISTER = "REGISTER";
	public static String CMD_CANCEL = "CANCEL";
	public static String CMD_MO = "MO";

	public static UnreqResult requestUnregister(String transid, String msisdn) {
		String queryString = "fee=0&msisdn=" + msisdn 
				+ "&subService=0&command=" + CMD_CANCEL + "&transid=" + transid;
		String url = XmlConfigs.SubsUpdate.URL + "?&" + queryString;
		long l1 = System.currentTimeMillis();
		String resp = httpClientUtils.get(transid, url);
		long l2 = System.currentTimeMillis();
		UnreqResult result = UnreqResult.parse(resp);
		logger.info(transid + ", Request MPS to Unregister, msisdn = " + msisdn + ", respStr = " 
				+ resp + ", result = " + result.isSuccess() + ", timeReq = " + (l2 - l1));
		return result;
	}
}
