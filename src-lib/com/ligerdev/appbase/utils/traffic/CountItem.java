package com.ligerdev.appbase.utils.traffic;
import java.io.Serializable;


public class CountItem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1306400391825408742L;
	private long second;
	private int count = 0;
	private int maxTps = 0;
	
	CountItem() {
		this.second = System.currentTimeMillis()/1000;
	}
	
	int request(){
		long currentSecond = System.currentTimeMillis()/1000;
		if(currentSecond == second){
			count ++;
		} else {
			maxTps = count;
			second = currentSecond;
			count = 1;
		}
		return maxTps;
	}
}
