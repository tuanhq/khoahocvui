package com.ligerdev.appbase.utils.cache;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class CacheSyncFileIM implements Serializable {

	private static String fileName = BaseUtils.getMyDir() + "./dats/liteCacheSeriaIM.dat";
	private static ConcurrentHashMap<String, CacheSyncFileIM> listInstance = null;
	private static Logger logger = Log4jLoader.getLogger();
	
	private ConcurrentHashMap<String, ObjHolder> data = new ConcurrentHashMap<String, ObjHolder>();
	private int maxSize = 100000;
	private String schema = "DFL";
	
	static {
		if(!new File(BaseUtils.getMyDir() + "./dats").exists()){
			new File(BaseUtils.getMyDir() + "./dats").mkdirs();
		}
		if(new File(fileName).exists()){
			listInstance = (ConcurrentHashMap<String, CacheSyncFileIM>) BaseUtils.readObjectFromFile2(fileName, true);
		}
		if(listInstance == null){
			listInstance = new ConcurrentHashMap<String, CacheSyncFileIM>();
		}
		startThreadCleanCache();
	}
	
	private CacheSyncFileIM(String schema, int maxSize) {
		this.maxSize = maxSize;
		this.schema = schema;
	}

	private static void startThreadCleanCache() {
		Thread thread = new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000 * 10);
					} catch (Exception e) {
						logger.info("Exception: " + e.getMessage());
					}
					try {
						writeCache2File();
					} catch (Exception e) {
						logger.info("Exception: " + e.getMessage(), e);
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

	public synchronized static CacheSyncFileIM getInstance(int maxSize) {
		return getInstance("DFL", maxSize);
	}

	public synchronized static CacheSyncFileIM getInstance(String schema, int maxSize) { // second
		CacheSyncFileIM obj = listInstance.get(schema);
		if (obj == null) {
			obj = new CacheSyncFileIM(schema, maxSize);
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

	public ObjHolder put(String key, Serializable obj) { // second
		key = schema + "_" + key;
		if (data.size() >= maxSize) {
			clearOldestObj();
		} 
		ObjHolder holder = new ObjHolder(obj);
		data.put(key, holder);
		return holder;
	}
	
	private void clearOldestObj() {
		Set<Entry<String, ObjHolder>> set = data.entrySet();
		Iterator<Entry<String, ObjHolder>> iter = set.iterator();
		
		String oldestObjectKey = null;
		long oldestTime = System.currentTimeMillis();
		while (iter.hasNext()) {
			Entry<String, ObjHolder> entry = iter.next();
			if(oldestTime > entry.getValue().getCreatedTime()){
				oldestTime = entry.getValue().getCreatedTime();
				oldestObjectKey = entry.getKey();
			}
		}
		if(oldestObjectKey != null){
			data.remove(oldestObjectKey);
		}
	}
	
	private void printAll(){
		Set<Entry<String, ObjHolder>> set = data.entrySet();
		Iterator<Entry<String, ObjHolder>> iter = set.iterator();
		
		while (iter.hasNext()) {
			Entry<String, ObjHolder> entry = iter.next();
			System.out.println(entry.getKey() + " = " + entry.getValue().getObj()); 
		}
	}

	public static void main(String[] args) {
		
		CacheSyncFileIM cacheSeria = CacheSyncFileIM.getInstance(2);
		cacheSeria.put("A", "String-A");
		try {
			Thread.sleep(1);
		} catch (Exception e) {
		}
		cacheSeria.put("C", "String-C");
		try {
			Thread.sleep(1);
		} catch (Exception e) {
		}
		cacheSeria.put("D", "String-D");
		cacheSeria.put("X", "String-Z");
		
		cacheSeria.printAll();
	}

	private class ObjHolder implements Serializable {

		private Serializable obj;
		private long createdTime = 0l;

		public ObjHolder(Serializable obj) {
			this.obj = obj;
			this.createdTime = System.currentTimeMillis();
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
	}
}
