package com.ligerdev.appbase.utils.textbase;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class SyncStrManager {
	
	private static ConcurrentHashMap<String, SyncStrManager> listInstance = new ConcurrentHashMap<String, SyncStrManager>();
	private static Logger logger = Log4jLoader.getLogger();
	private Hashtable<String, ObjBean> hashMap = null;
	
	public static class ObjBean {
		private long createdTime = System.currentTimeMillis();
	}
	
	private SyncStrManager (){
		hashMap = new Hashtable<String, ObjBean>();
		new Thread(){
			public void run() {
				while(true){
					try {
						clear();
					} catch (Exception e) {
						logger.info("Exception: " + e.getMessage());
					}
					try {
						Thread.sleep(10000);
					} catch (Exception e) {
						logger.info("Exception: " + e.getMessage());
					}
				}
			};
		}.start();
	}
	
	public synchronized static SyncStrManager getInstance(String namespace){
		if(!listInstance.containsKey(namespace)){
			SyncStrManager obj = new SyncStrManager();
			listInstance.put(namespace, obj);
			return obj;
		}
		SyncStrManager tmp = listInstance.get(namespace);
		return tmp;
	}

	public Object createSynchObject(String key) {
		synchronized (hashMap) {
			key = key + "";
			ObjBean obj = hashMap.get(key);
			if (obj == null) {
				obj = new ObjBean();
				hashMap.put(key, obj);
			}
			return obj;
		}
	}
	
	private void clear(){
		Enumeration<String> enums = hashMap.keys();
		while(enums.hasMoreElements()){
			String key = enums.nextElement();
			ObjBean obj = hashMap.get(key);
			if(obj != null && System.currentTimeMillis() - obj.createdTime > 1000 * 60 * 10 ){
				hashMap.remove(key);
			}
		}
	}
	
	public ObjBean removeSynchObject(String key) {
		synchronized (hashMap) {
			if (key == null) {
				return null;
			}
			ObjBean obj = hashMap.remove(key);
			return obj;
		}
	}
}
