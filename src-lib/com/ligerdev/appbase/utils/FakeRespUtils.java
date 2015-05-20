package com.ligerdev.appbase.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class FakeRespUtils {

	private static Logger logger = Log4jLoader.getLogger();
	private static String filename = BaseUtils.getMyDir() + "./fakeresponse";
	private static Random random = new Random();
	private static boolean existFile = false;
	
	static {
		check();
		new Thread(){
			public void run() {
				while(true){
					try {
						check();
						Thread.sleep(500);
					} catch (Exception e) {
						logger.info("Exception: " + e.getMessage());
					}
				}
			}
		}.start();
	}
	
	private static void check() {
		if(new File(filename).exists()){
			existFile = true;
		} else {
			existFile = false;
		}
	};
	
	public static String fakeResponse(String transid, String key){
		if(!existFile){
			return null;
		}
		String result = null;
		try {
			Properties p = new Properties();
			FileInputStream is = new FileInputStream(new File(filename));
			p.load(is);
			String temp = p.getProperty(key);
			if(temp != null){
				result =  temp.trim();
			}
			is.close();
		} catch (Exception e) {
		}
		if(result == null){
			return null;
		}
		if(result.contains("@")){
			String tmp[] = result.split("@");
			int r = random.nextInt(tmp.length);
			result = tmp[r];
		}
		result = result.trim();
		logger.info(transid + ", fakeResponse(); key = " + key + "; return = " + result);
		return result;
	}
}
