package com.ligerdev.appbase.utils.textbase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.ligerdev.appbase.utils.BaseUtils;


public class ReqCountUtils {
	
	private class LongWrapper{
		public long counter = 0L;
		public LongWrapper(long counter) {
			this.counter = counter;
		}
	}
	
	private static ConcurrentHashMap<String, ReqCountUtils> listInstance = new ConcurrentHashMap<String, ReqCountUtils>();
	private static Logger logger = Log4jLoader.getLogger();
	private static Properties properties = new Properties();
	private static ConcurrentHashMap<String, LongWrapper> data = new ConcurrentHashMap<String, ReqCountUtils.LongWrapper>();
	private static String fileName = BaseUtils.getMyDir() + "./dats/ReqCount.txt";
	
	private String key = "key";
	private String prefix = "A";
	
	public static void main(String[] args) {
		DOMConfigurator.configure("resource/demo1/config/log4j.xml"); 
	}
	
	static {
		try {
			if(!new File(BaseUtils.getMyDir() + "./dats").exists()){
				new File(BaseUtils.getMyDir() +"./dats").mkdirs();
			}
			File file = new File(fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			FileReader reader = new FileReader(file);
			properties.load(reader);
			reader.close();
			
			new Thread(){
				public void run() {
					while(true){
						try {
							saveAll();
							Thread.sleep(1000);
						} catch (Exception e) {
							logger.info("Exception: " + e.getMessage(), e);
							try {
								Thread.sleep(500);
							} catch (Exception e2) {
								logger.info("Exception: " + e2.getMessage());
							}
						}
					}
				};
			}.start();
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
		}
	}
	
	public synchronized static void saveAll(){
		try {
			Enumeration<String> enums = data.keys();
			while(enums.hasMoreElements()){
				String key = (String) enums.nextElement();
				LongWrapper value = data.get(key);
				properties.put(key, value.counter + "");
			}
			FileOutputStream outStream = new FileOutputStream(fileName);
			properties.save(outStream, "ReqCountManager properties file");
			outStream.close();
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
		}
	}
	
	private ReqCountUtils(String key,  String prefix) {
		try {
			if(key == null){
				key = "null";
			}
			key = key.replace(" ", "_").replace("/", "_").replace("\n", "_").replace("|", "_");
			this.key = key;
			this.prefix = prefix;
			String reqCountStr = (String) properties.get(key);
			if(reqCountStr == null || "".equals(reqCountStr.trim())){
				reqCountStr = "0";
				properties.setProperty(key, reqCountStr);
			}
			long reqCount = parseLong(reqCountStr, 0L);
			LongWrapper longWrapper = new LongWrapper(reqCount);
			data.put(key, longWrapper);
		} catch (Exception e) {
		}
	}
	
	private static Long parseLong(String value, Long defauzt) {
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			return defauzt;
		}
	}
	
	public synchronized static ReqCountUtils getInstance(String key, String prefix){
		if(!listInstance.containsKey(key)){
			ReqCountUtils obj = new ReqCountUtils(key, prefix);
			listInstance.put(key, obj);
			return obj;
		}
		ReqCountUtils tmp = listInstance.get(key);
		return tmp;
	}
	
	public String countLongStr(){
		return prefix + countLong();
	}
	
	public synchronized long countLong(){
		LongWrapper longWrapper = data.get(this.key);
		if(longWrapper.counter >= Long.MAX_VALUE){
			longWrapper.counter = -1;
		}
		longWrapper.counter ++;
		return longWrapper.counter;
	}
	
	public String countIntStr(){
		return prefix + countLong();
	}
	
	public synchronized int countInt(){
		LongWrapper longWrapper = data.get(this.key);
		if(longWrapper.counter >= Integer.MAX_VALUE){
			longWrapper.counter = -1;
		}
		longWrapper.counter ++;
		return (int) longWrapper.counter;
	}
}
