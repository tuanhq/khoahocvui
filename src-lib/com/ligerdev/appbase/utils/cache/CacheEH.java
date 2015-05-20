package com.ligerdev.appbase.utils.cache;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class CacheEH {

	private static Logger logger = Log4jLoader.getLogger();
	private static String fileName = BaseUtils.getMyDir() + "./config/ehcache.xml";
	private static ConcurrentHashMap<String, CacheEH> listInstance = new ConcurrentHashMap<String, CacheEH>();
	private static CacheManager ehMgr = new CacheManager(new File(fileName).getAbsolutePath());
	private Cache infoCache = null;

	private CacheEH(String nameSpace) {
		logger.info("File path ehcache config: " + fileName);
		infoCache = ehMgr.getCache(nameSpace);
	}

	public synchronized static CacheEH getInstance(String nameSpace) {
		CacheEH instance = listInstance.get(nameSpace);
		if (instance == null) {
			instance = new CacheEH(nameSpace);
			listInstance.put(nameSpace, instance);
		}
		return instance;
	}

	public static void main(String[] args) {
		CacheEH cache = CacheEH.getInstance("CACHE_BEAN");
		long l1 = System.currentTimeMillis();
		int tmp = 10000;
		for (int i = 0; i < tmp; i++) {
			cache.put("key" + i, "value" + i);
		}
		long l2 = System.currentTimeMillis();
		for (int i = 0; i < tmp; i++) {
			Object obj = cache.get("key" + i);
			if (obj == null) {
				System.out.println("==============");
			}
		}
		long l3 = System.currentTimeMillis();
		System.out.println("Set: " + (l2 - l1));
		System.out.println("Get: " + (l3 - l2));
	}

	public Object get(String key) {
		Element ele = infoCache.get(key);
		if (ele != null) {
			return ele.getValue();
		}
		return null;
	}

	public void put(String key, Object value) {
		Element ele = new Element(key, value);
		infoCache.put(ele);
	}

	public Object remove(String key) {
		Object value = this.get(key);
		infoCache.remove(key);
		return value;
	}

	public void clear() {
		infoCache.removeAll();
	}
}
