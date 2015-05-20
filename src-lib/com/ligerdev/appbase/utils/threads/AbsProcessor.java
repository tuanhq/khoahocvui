package com.ligerdev.appbase.utils.threads;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public abstract class AbsProcessor extends Thread implements Serializable {

	protected static Logger logger = Log4jLoader.getLogger();
	private static AtomicInteger thrIdx = new AtomicInteger(1);
	protected String transid = "inv";
	
	 /*
		private static AbsProcessor instance = null;
		
		private AbsProcessor() {
		}
		
		public synchronized static AbsProcessor getInstance(){
			if(instance == null){
				instance = new Implementor();
			}
			return instance;
		}
	*/
	
	@Override
	public void run() {
		synchronized(thrIdx){
			setName(this.getClass().getName() + "-" + thrIdx.getAndIncrement());
		}
		while(AbsApplication.CONTINUES){
			try {
				execute();
			} catch (Throwable e) {
				logger.error("Exception: " + e.getMessage(),  e);
				exception(e);
			}
			BaseUtils.sleep(sleep());
		}
	}
	
	public abstract void execute();
	public abstract int sleep();
	public abstract void exception(Throwable e);
}
