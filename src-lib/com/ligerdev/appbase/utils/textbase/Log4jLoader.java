package com.ligerdev.appbase.utils.textbase;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.encrypt.EncryptManager;
import com.ligerdev.appbase.utils.encrypt.Encrypter;
import com.ligerdev.appbase.utils.threads.AbsApplication;

public class Log4jLoader extends Thread {
	
	private static Thread threadReload = null;
	private static PrintStream printStream = System.err;
	private static boolean enableCheckLsFile = false;
	
	public static void disableLogAndErrOutput(){
		System.setErr(new PrintStream(new OutputStream() {
			public void write(int b) {
			}
		})); 
		disableInitLog = true;
	}
	
	public static void enableLogAndErrOutput(){
		System.setErr(printStream); 
		disableInitLog = false;
	}

	private static boolean disableInitLog = false;
	private static Logger logger = Logger.getLogger("LOG");
	private static ConcurrentHashMap<String, Logger> hashmapLogger = new ConcurrentHashMap<String, Logger>();
	
	private static File file = null;
	private static long lastTimeModified = 0l;
	
	public synchronized static void init() {
		init(BaseUtils.getMyDir() + "./config/log4j.xml"); 
	}
	
	public synchronized static void init(boolean chkls) {
		enableCheckLsFile = chkls;
		init();
	}
	
	public synchronized static void init(String fileName, boolean chkls) {
		enableCheckLsFile = chkls;
		init(fileName);
	}
	
	public synchronized static void init(String fileName) {
		if(threadReload != null){
			return;
		}
		if(disableInitLog == false){
			file = new File(fileName);
			DOMConfigurator.configure(file.getPath());
			lastTimeModified = file.lastModified();
		}
		threadReload = new Thread(){
			public void run() {
				while (AbsApplication.CONTINUES) {
					
					if(Log4jLoader.enableCheckLsFile){
						checkLicenseTime();
					}
					BaseUtils.sleep(2000);
					if(disableInitLog){
						continue;
					}
					try {
						File file = new File("./config/", "log4j.xml");
						if (file.exists() && lastTimeModified != file.lastModified()) {
							lastTimeModified = file.lastModified();
							logger.info("#### reload log4j.xml ####");
							DOMConfigurator.configure(file.getPath());
						}
					} catch (Exception e) {
						logger.info("Exception: " + e.getMessage());
					}
				}
			}
		};
		threadReload.start();
	}
	
	private static void checkLicenseTime() {
		String fileName = BaseUtils.getMyDir() + "./dats/chklct.dat";
		if(!new File(fileName).exists()){
			try {
				new File(fileName).createNewFile();
			} catch (IOException e) {
			}
			System.exit(-9);
			return;
		}
		String data = BaseUtils.readFile(fileName);
		if(BaseUtils.isBlank(data)){
			System.exit(-9);
			return;
		}
		try {
			// 012345678901234567890123456789
			data = Encrypter.decrypt(data);
			long expire = Long.parseLong(data);
			long today = Long.parseLong(BaseUtils.formatTime("yyyyMMdd", System.currentTimeMillis()));
			
			if(today > expire){
				System.exit(-9);
				return;
			}
		} catch (Exception e) {
			System.exit(-9);
			return;
		}
	}

	public static Logger getLogger() {
		// return Logger.getLogger("LOG");
		return logger;
	}
	
	public synchronized static Logger getLogger(String name) {
		Logger logger = hashmapLogger.get(name);
		if(logger == null){
			logger = Logger.getLogger(name);
			hashmapLogger.put(name, logger);
		}
		return logger;
	} 
}
