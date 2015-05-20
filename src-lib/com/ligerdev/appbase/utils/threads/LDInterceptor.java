package com.ligerdev.appbase.utils.threads;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class LDInterceptor extends Thread {
	
	private static Logger logger = Log4jLoader.getLogger();
	private AbsApplication app = null;

	public LDInterceptor(AbsApplication app) {
		this.app = app;
	}

	@Override
	public void run() {
		try {
			logger.info("Call the shutdown routine");
			app.end();
			
		} catch (Exception ex) {
			logger.info("Error at thread shutdown interceptor: " + ex.getMessage());
		}
	}
}
