package com.vas.aps.mpsclient;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.http.HttpClientUtils;
import com.ligerdev.appbase.utils.http.HttpServerUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.vas.aps.api.WsApi;
import com.vas.aps.comms.XmlConfigs;
import com.vas.aps.comms.XmlConfigs.MtSender;

public class SmsClient {

	private static Logger logger = Log4jLoader.getLogger();
	private static HttpClientUtils httpClientUtils = HttpClientUtils.getInstance(6000);
	
	public static String sendSms(String transid, String msisdn, String content, String code){
		
		long l1 = System.currentTimeMillis(); 
		String xmlRequest =
				"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " 
						+ "xmlns:xsd=\"http://smsws/xsd\"><soapenv:Header/>" +
					"<soapenv:Body>" +
						"<xsd:smsRequest>" +
							   "<xsd:username>" + XmlConfigs.MtSender.USER + "</xsd:username>"
							+ "<xsd:password>" + XmlConfigs.MtSender.PASS + "</xsd:password>" 
							+ "<xsd:msisdn>" + msisdn + "</xsd:msisdn>" 
							+ "<xsd:content>" + content.replace("\\n", "\n") + "</xsd:content>"  
							+ "<xsd:shortcode>" + XmlConfigs.MtSender.SHORTCODE + "</xsd:shortcode>" 
							+ "<xsd:alias>" + XmlConfigs.MtSender.ALIAS + "</xsd:alias>" 
							+ "<xsd:params>" + XmlConfigs.MtSender.PARAMS + "</xsd:params>" 
						+ "</xsd:smsRequest>" 
					+ "</soapenv:Body>" 
			+ "</soapenv:Envelope>";
		
		// logger.info(transid + ", mtCode = " + code + ", xmlRequest = " + xmlRequest);
		logger.info(transid + ", send mt, code = " + code + ", content = " + content);
		String xmlResponse = null;
		
		if (content.startsWith("###")) {
			logger.info(transid + ", ignore this msg due to it'll be sent by mps");
			xmlResponse = "<xml><return>0</return></xml>";
			
		} else if (transid.contains("Z")) {
			logger.info(transid + ", ignore this msg due to it'll be created by web/wap channel");
			xmlResponse = "<xml><return>0</return></xml>";
			
		} else if(MtSender.FAKE){
			xmlResponse = "<xml><return>0</return></xml>";
			BaseUtils.sleep(50);
		} 
		else {
			xmlResponse = httpClientUtils.post(xmlRequest, XmlConfigs.MtSender.URL, xmlRequest);
		}
		String sendResult = HttpServerUtils.parseData(xmlResponse, "return");
		long l2 = System.currentTimeMillis();
		
		// logger.info(transid + ", mtCode = " + code + ", xmlResponse = " + xmlResponse);
		logger.info(transid + ", time = " + (l2 - l1) + ", sendResult = " + sendResult);
		System.err.println(transid + ", time = " + (l2 - l1) + ", sendResult = " + sendResult + "-content:" + content);
		
		return sendResult;
	}
}
