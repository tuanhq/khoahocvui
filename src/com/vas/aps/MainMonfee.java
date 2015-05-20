package com.vas.aps;

import javax.xml.ws.Endpoint;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.ConfigsReader;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.vas.aps.api.mps.WsMonthfee;
import com.vas.aps.comms.XmlConfigs;

public class MainMonfee {

	static {
		Log4jLoader.init();
	}
	protected static Logger logger = Log4jLoader.getLogger();
	
	public static void main(String[] args) {
		try {
			ConfigsReader.init(XmlConfigs.class);
			WsMonthfee wsRenewListener = new WsMonthfee();
			Endpoint.publish(XmlConfigs.MpsListener.MONTHFEE, wsRenewListener);
			
			logger.info("=============== Monfee listener started v1.0 ================"); 
			
		} catch (Exception e) {
			logger.info("Exit! Exception: " + e.getMessage(), e);
			BaseUtils.sleep(1000);
			System.exit(-9);
		}
	}
}
