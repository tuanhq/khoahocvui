package com.ligerdev.appbase.utils.threads;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.http.HttpServerUtils;
import com.ligerdev.appbase.utils.queues.QueueIOUtils;
import com.ligerdev.appbase.utils.textbase.ConfigsReader;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public abstract class AbsApplication extends Thread implements Serializable {

	static {
		Log4jLoader.init();
	}
	
	protected static Logger logger = Log4jLoader.getLogger();
	private static AbsApplication instance = null;
	private static Object locker = new Object();
	
	public static boolean CONTINUES = true;
	public static String VERSION = "1.0";
	
	protected AbsApplication(Class<? extends ConfigsReader> clazzConfig) { 
		this(clazzConfig, null);
	}
	
	protected AbsApplication(Class<? extends ConfigsReader> clazzConfig, String cfgFileName) { 
		synchronized (locker) {
			try {
				if(instance != null){
					throw new Exception("Already init instance ....");
				}
				instance = this;
				
				if(cfgFileName == null){
					ConfigsReader.init(clazzConfig);
				} else {
					ConfigsReader.init(clazzConfig, cfgFileName);
				}
				setName("MainProcess");
				LDInterceptor shuwdown = new LDInterceptor(this);
				Runtime.getRuntime().addShutdownHook(shuwdown);
				setPriority(Thread.MAX_PRIORITY);
				start();
				
			} catch (Exception e) {
				logger.info("Can not init instance, system exited..., "  + e.getMessage(), e);
				BaseUtils.sleep(1000); System.exit(-9);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized static <T> T getInstance(){
		return (T) instance;
	}
	
	protected abstract void initProcess() throws Exception;
	protected abstract void endProcess() throws Exception;
	
	public void run(){
		try {
			initProcess();
			logger.info("============= Application started (v" + VERSION + ")==============");
			
		} catch (Exception e) {
			logger.info("Error when init main class, system exited..., "  + e.getMessage(), e);
			BaseUtils.sleep(1000); System.exit(-9);
		}
	}
	
	void end() throws Exception{
		try {
			CONTINUES = false;
			endProcess();
			
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		} finally {
			try {
				QueueIOUtils.saveMmtQueue();
				HttpServerUtils.shutdownAll();
				BaseDAO.closeAllConns();
				logger.info("============= Exit ==============");
				
			} catch (Exception e2) {
				logger.info("Exception: " + e2.getMessage());
			}
		}
	}
}
