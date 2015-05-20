package com.ligerdev.appbase.utils.threads;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.queues.MsgQueueITF;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public abstract class AbsBatchProcessor<T> extends Thread implements Serializable {

	protected static Logger logger = Log4jLoader.getLogger();
	private long lastCallUpdateMethod = System.currentTimeMillis();
	private Object locker = new Object();
	private ArrayList<T> listObj = new ArrayList<T>();
	private static AtomicInteger thrIdx = new AtomicInteger(1);
	protected String transid = "inv";
	
	 /*
		private static AbsBatchProcessor instance = null;
		
		public synchronized static AbsProcessor getInstance(MsgQueueITF msgQueueITF){
			if(instance == null){
				instance = new Implementor(msgQueueITF);
			}
			return instance;
		}
	*/
	
	private MsgQueueITF msgQueueITF = null;
	
	public AbsBatchProcessor(MsgQueueITF msgQueueITF) {
		this.msgQueueITF = msgQueueITF;
	}
	
	private void startThreadTimer() {
		Thread timerExe = new Thread() {
			
			public void run() {
				while (AbsApplication.CONTINUES) {
					BaseUtils.sleep(200);
					
					if (System.currentTimeMillis() - lastCallUpdateMethod >= getTimeToUpdate()) {
						synchronized (locker) {
							AbsBatchProcessor.this.execute("timeFull");
						}
					}
				}
			}
		};
		timerExe.setName("Timer_" + Thread.currentThread().getName());
		timerExe.start();
	}

	@Override
	public void run() {
		synchronized(thrIdx){
			setName(this.getClass().getName() + "-" + thrIdx.getAndIncrement());
		}
		startThreadTimer();
		while (AbsApplication.CONTINUES) {
			try {
				process();
			} catch (Throwable e) {
				logger.info("Exception: " + e.getMessage(), e);
				exception(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void process() {
		T obj = (T) msgQueueITF.removeFirst();
		synchronized (locker) {
			listObj.add(obj);
			if (listObj.size() >= getSizeToUpdate()) {
				execute("sizeFull");
			}
		}
	}

	private void execute(String caller){
		try {
			lastCallUpdateMethod = System.currentTimeMillis();
			if (listObj.size() == 0) {
				return;
			}
			execute(caller, listObj);
			
		} catch (Throwable e) {
			logger.info("Exception: " + e.getMessage(), e);
			exception(e);
		} finally {
			listObj.clear();
		}
	}
	
	public abstract void execute(String caller, ArrayList<T> list);
	public abstract int getTimeToUpdate();
	public abstract int getSizeToUpdate();
	public abstract void exception(Throwable e);
}
