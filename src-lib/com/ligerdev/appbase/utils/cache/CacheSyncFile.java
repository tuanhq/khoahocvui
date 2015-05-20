package com.ligerdev.appbase.utils.cache;

import java.io.File;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class CacheSyncFile implements Serializable {

	private static String fileName = BaseUtils.getMyDir() + "./dats/liteCacheSeria.dat";
	private static ConcurrentHashMap<String, CacheSyncFile> listInstance = null;
	private static Logger logger = Log4jLoader.getLogger();
	
	private ConcurrentHashMap<String, ObjHolder> data = new ConcurrentHashMap<String, ObjHolder>();
	private int maxSize = 100000;
	private String schema = "DFL";
	
	static {
		if(!new File(BaseUtils.getMyDir() + "./dats").exists()){
			new File(BaseUtils.getMyDir() + "./dats").mkdirs();
		}
		if(new File(fileName).exists()){
			listInstance = (ConcurrentHashMap<String, CacheSyncFile>) BaseUtils.readObjectFromFile2(fileName, true);
		}
		if(listInstance == null){
			listInstance = new ConcurrentHashMap<String, CacheSyncFile>();
		}
		startThreadCleanCache();
	}
	
	private CacheSyncFile(String schema, int maxSize) {
		this.maxSize = maxSize;
		this.schema = schema;
	}

	private static void startThreadCleanCache() {
		Thread thread = new Thread() {
			long  lastWriteFile = System.currentTimeMillis();
			public void run() {
				while (true) {
					try {
						checkExpireObjects();
						if(System.currentTimeMillis() - lastWriteFile > 1000 * 10){
							lastWriteFile = System.currentTimeMillis();
							writeCache2File();
						}
					} catch (Exception e) {
						logger.info("Exception: " + e.getMessage(), e);
					}
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						logger.info("Exception: " + e.getMessage());
					}
				}
			}
		};
		thread.start();
	}

	public static void writeCache2File() {
		synchronized(listInstance){
			BaseUtils.writeObjectToFile2(fileName, listInstance);
		}
	}

	private static void checkExpireObjects() {
		Set<Entry<String, CacheSyncFile>> set = listInstance.entrySet();
		Iterator<Entry<String, CacheSyncFile>> iter = set.iterator();
		while (iter.hasNext()) {
			Entry<String, CacheSyncFile> entry = iter.next();
			CacheSyncFile cache = entry.getValue();
			clearOldObjects(cache);
		}
	}

	private static void clearOldObjects(CacheSyncFile cache) {
		Enumeration<String> enums = cache.data.keys();
		while (enums.hasMoreElements()) {
			String key = enums.nextElement();
			ObjHolder tmp =  cache.data.get(key);
			if(tmp == null){
				continue;
			}
			int secondsOld = (int) ((System.currentTimeMillis() - tmp.getCreatedTime()) / 1000);
			if (secondsOld >= tmp.getTimeToLive()) {
				cache.data.remove(key);
			}
		}
	}
	
	public synchronized static CacheSyncFile getInstance(int maxSize) {
		return getInstance("DFL", maxSize);
	}

	public synchronized static CacheSyncFile getInstance(String schema, int maxSize) { // second
		CacheSyncFile obj = listInstance.get(schema);
		if (obj == null) {
			obj = new CacheSyncFile(schema, maxSize);
			listInstance.put(schema, obj);
		}
		return obj;
	}

	public Serializable getObject(String key) {
		key = schema + "_" + key;
		ObjHolder tmp = data.get(key);
		if (tmp == null) {
			return tmp;
		}
		return tmp.getObj();
	}

	public Serializable remove(String key) {
		key = schema + "_" + key;
		ObjHolder tmp = data.remove(key);
		if (tmp == null) {
			return null;
		}
		return tmp.getObj();
	}

	public ObjHolder put(String key, Serializable obj, int timeToLive) { // second
		key = schema + "_" + key;
		if (data.size() >= maxSize) {
			logger.info("Hashtable size >= maxsize == " + maxSize + " => ignore and reutrn null");
			return null;
		} else {
			ObjHolder holder = new ObjHolder(obj, timeToLive);
			data.put(key, holder);
			return holder;
		}
	}
	
	public static void main(String[] args) {
		CacheSyncFile cacheSeria = CacheSyncFile.getInstance(3);
		cacheSeria.put("A", "String-A", 1);
		cacheSeria.put("B", "String-B", 2);
		cacheSeria.put("C", "String-C", 3);
		cacheSeria.put("D", "String-D", 4);
		
		CacheSyncFile cacheSeria2 = CacheSyncFile.getInstance("CS", 3);
		cacheSeria2.put("A", "String-X", 40);
		writeCache2File();
		
		while(true){ 
			System.out.println(cacheSeria.getObject("A"));  
			System.out.println(cacheSeria.getObject("B"));
			System.out.println(cacheSeria.getObject("C"));
			System.out.println(cacheSeria.getObject("D"));
			System.out.println(cacheSeria2.getObject("A"));
			System.out.println("===============");
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
	}

	private class ObjHolder implements Serializable {

		private Serializable obj;
		private long createdTime = 0l;
		private int timeToLive;

		public ObjHolder(Serializable obj, int timeToLive) {
			this.obj = obj;
			this.createdTime = System.currentTimeMillis();
			this.setTimeToLive(timeToLive);
		}

		public Serializable getObj() {
			return obj;
		}

		public void setObj(Serializable obj) {
			this.obj = obj;
		}

		public long getCreatedTime() {
			return createdTime;
		}

		public void setCreatedTime(long createdTime) {
			this.createdTime = createdTime;
		}

		public int getTimeToLive() {
			return timeToLive;
		}

		public void setTimeToLive(int timeToLive) {
			this.timeToLive = timeToLive;
		}
	}
}
