package com.ligerdev.appbase.utils.traffic;
import java.util.concurrent.ConcurrentHashMap;


public class ReceiveTraffic {
	
	private static ConcurrentHashMap<String, CountItem> hashtable = null;
	static {
		hashtable = new ConcurrentHashMap<String, CountItem>();
	}
	
	public static int countRequest(String code){
		try {
			CountItem item = hashtable.get(code);
			if(item == null){
				item = new CountItem();
				hashtable.put(code, item);
			}
			return item.request();
			
		} catch (Exception e) {
			return 1;
		}
	} 
}
