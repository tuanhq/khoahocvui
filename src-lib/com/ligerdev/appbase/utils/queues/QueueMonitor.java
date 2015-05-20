package com.ligerdev.appbase.utils.queues;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.ligerdev.appbase.utils.threads.AbsApplication;

public class QueueMonitor {
	
	private static Logger logger = Log4jLoader.getLogger();
	private static ConcurrentHashMap<String, Object> listData = new ConcurrentHashMap<String, Object>();
	private static String lastStringWrote = null;
	private static Thread threadWriteFile = null;
	
	static {
		if(!new File(BaseUtils.getMyDir() +"./mnt").exists()){
			new File(BaseUtils.getMyDir() + "./mnt").mkdirs();
		}
	}
	
	public static void addObject(MsgQueueITF q){
		listData.put(q.getName(), q);
		startMonitor();
	}
	
	public static void addObject(String name, Object obj){
		listData.put(name, obj);
		startMonitor();
	}
	
	public synchronized static void startMonitor(){
		if(threadWriteFile == null || !threadWriteFile.isAlive()){
			threadWriteFile = new Thread(){
				public void run() {
					String threadName = Thread.currentThread().getName();
					logger.info(threadName + " is started ...");
					while (AbsApplication.CONTINUES) {
						try {
							process();
							Thread.sleep(1000);
						} catch (Throwable e) {
							logger.info("Exception: " + e.getMessage());
							try {
								Thread.sleep(200);
							} catch (Exception e2) {
							}
						}
					}
				}
			};
			threadWriteFile.start();
		}
	}
	
	private static int count = 0;
	private static String all = "";
	
	private static void process() {
		if(count >= 100){
			count = -1;
			all = "";
		}
		count ++;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		all += "\n\n====== " + df.format(new Date()) + " ======\n";
		try {
			Enumeration<String> nums = listData.keys();
			while(nums.hasMoreElements()){
				try {
					String key = nums.nextElement();
					Object obj = listData.get(key);
					String value = null;
					if(obj == null){
						continue;
					}
					if(obj instanceof MsgQueueITF){
						MsgQueueITF queue =  (MsgQueueITF) obj;
						value = queue.size() + "";
					} else if(obj instanceof String){
						value = (String) obj;
					} else if(obj instanceof Integer){
						value = ((Integer) obj) + "";
					} else if(obj instanceof Collection){
						value = ((Collection) obj).size() + "";
					} else if(obj instanceof Map){
						value = ((Map) obj).size() + "";
					} else {
						value = String.valueOf(obj); 
					}
					all += key + " = " + value + "\n";
				} catch (Exception e) {
					logger.info("Exception: " + e.getMessage());
				}
			}
			if(all.equals(lastStringWrote)){ 
				return;
			}
			BaseUtils.writeFile(BaseUtils.getMyDir() + "./mnt/queues.mnt", all, false);
			lastStringWrote = all;
			
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		}
	} 
}
