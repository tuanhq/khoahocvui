package com.ligerdev.appbase.utils.db;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.queues.MsgQueueITF;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.ligerdev.appbase.utils.threads.AbsApplication;
import com.ligerdev.appbase.utils.threads.AbsProcessor;

public class LDPoolInfoManager extends AbsProcessor implements Serializable {

	private int today = -1;
	private LDPoolInfo infoByDay = null;
	private Hashtable<Integer, LDPoolInfo> listInfoByHour = null;
	private MsgQueueITF msgQueue = null;
	private String poolName = null;
	private static Logger logger = Log4jLoader.getLogger();
	private String fileNameDay = null;
	private String fileNameHour = null;
	
	@SuppressWarnings("unchecked")
	public LDPoolInfoManager(String poolName, MsgQueueITF msgQueue, ConcurrentHashMap<Connection, LDConnection> listBusy) {
		
		this.poolName = poolName;
		this.msgQueue = msgQueue;
		
		fileNameDay = BaseUtils.getMyDir() + "./dats";
		try {
			if(new File(fileNameDay).exists()){
				new File(fileNameDay).mkdirs();
			}
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
		}
		fileNameDay += "/poolmnt." + this.poolName + ".day.dat";
		fileNameHour = BaseUtils.getMyDir() + "./dats/poolmnt." + this.poolName + ".hour.dat";
		int todayTmp = Integer.parseInt(BaseUtils.formatTime("yyyyMMdd", System.currentTimeMillis()));
		
		infoByDay = (LDPoolInfo) BaseUtils.readObjectFromFile2(fileNameDay, true);
		if(infoByDay == null || infoByDay.getDate() != todayTmp){
			infoByDay = new LDPoolInfo(todayTmp, poolName);
			listInfoByHour = new Hashtable<Integer, LDPoolInfo>();
			
		} else if(listInfoByHour == null){
			listInfoByHour = (Hashtable<Integer, LDPoolInfo>) BaseUtils.readObjectFromFile2(fileNameHour, true);
			if(listInfoByHour == null){
				listInfoByHour = new Hashtable<Integer, LDPoolInfo>();
			}
		}
		infoByDay.setListBusy(listBusy);
		
		new Thread(){
			
			public void run() {
				while(AbsApplication.CONTINUES){
					try {
						scheduleLog();
					} catch (Exception e) {
						logger.info("Exception: " + e.getMessage());
					}
				}
			}
			
			private void scheduleLog() {
				BaseUtils.sleep(1000);
				
				Calendar cal = Calendar.getInstance();
				int now = Integer.parseInt(BaseUtils.formatTime("mmss", cal.getTime())); 
				int curHour = cal.get(Calendar.HOUR_OF_DAY);
				
				if(now >= 5955){
					if(curHour == 23){
						logger.info(getInfoByDateStr());
					} else {
						logger.info(getInfoByHourStr(curHour));
					}
					BaseUtils.sleep(1000 * 6);
				}
				if(now % 10 == 0){
					// logger.info("write pool manager to file");
					BaseUtils.writeObjectToFile2(LDPoolInfoManager.this.fileNameDay, LDPoolInfoManager.this.infoByDay);
					BaseUtils.writeObjectToFile2(LDPoolInfoManager.this.fileNameHour, LDPoolInfoManager.this.listInfoByHour);
				} 
			};
		}.start();
	}
	
	public String getInfoByDateStr() {
		synchronized (this) {
			String str = String.valueOf(infoByDay);
			return str;
		}
	}
	
	public String getInfoByHourStr(int hour) {
		synchronized (this) {
			LDPoolInfo infoByHour = listInfoByHour.get(hour); 
			String str = String.valueOf(infoByHour);
			return str;
		}
	}

	@Override
	public void execute() {
		
		LDConnInfo newInfo = (LDConnInfo) msgQueue.removeFirst();
		Calendar cal = Calendar.getInstance();
		int curDate = Integer.parseInt(BaseUtils.formatTime("yyyyMMdd", cal.getTime())); 
		int curHour = cal.get(Calendar.HOUR_OF_DAY);
		
		synchronized (this) {
			if(today != curDate){
				today = curDate;
				infoByDay = new LDPoolInfo(today, poolName);
				listInfoByHour = new Hashtable<Integer, LDPoolInfo>();
			}
			infoByDay.addInfo(newInfo);
			
			LDPoolInfo infoByHour = listInfoByHour.get(curHour); 
			if(infoByHour == null){
				infoByHour = new LDPoolInfo(today, poolName);
				listInfoByHour.put(curHour, infoByHour);
			}
			infoByHour.addInfo(newInfo);
		}
	}
	
	@Override
	public int sleep() {
		return 0;
	}
	
	@Override
	public void exception(Throwable e) {
	}
 
	public MsgQueueITF getMsgQueue() {
		return msgQueue;
	}

	public void setMsgQueue(MsgQueueITF msgQueue) {
		this.msgQueue = msgQueue;
	}
}
