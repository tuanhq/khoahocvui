package com.ligerdev.appbase.utils.db;

import java.io.Serializable;
import java.util.Date;

import com.ligerdev.appbase.utils.BaseUtils;

public class LDConnInfo implements Serializable, Cloneable, Comparable<LDConnInfo> {
	
	private String transid;
	private long createdTime;
	private long lastPush;
	private long lastPull;
	private boolean isCreatedNew;
	private Throwable throwable;
	private String locationInfo;

	public LDConnInfo() {
	}

	public LDConnInfo(String transid, long createdTime, long lastPush, long lastPull, boolean isCreatedNew) {
		super();
		this.transid = transid;
		this.createdTime = createdTime;
		this.lastPush = lastPush;
		this.lastPull = lastPull;
		this.isCreatedNew = isCreatedNew;
	}

	public String getTransid() {
		return transid;
	}

	public void setTransid(String transid) {
		this.transid = transid;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public long getLastPush() {
		return lastPush;
	}

	public void setLastPush(long lastPush) {
		this.lastPush = lastPush;
	}

	public long getLastPull() {
		return lastPull;
	}

	public void setLastPull(long lastPull) {
		this.lastPull = lastPull;
	}

	public boolean isCreatedNew() {
		return isCreatedNew;
	}

	public void setCreatedNew(boolean isCreatedNew) {
		this.isCreatedNew = isCreatedNew;
	}
	
	public int getProcessTime(){
		return (int) (lastPush - lastPull);
	} 

	// @Override
	public int compareTo(LDConnInfo o) {
		try {
			return getProcessTime() - o.getProcessTime();
		} catch (Exception e) {
			return 0;
		}
	}

	// @Override
	public String toString() {
		return "Trans [" 
				+ "hour=" + new Date(lastPush).getHours()
				+ ", create=" + formatTime_yyyyMMdd_HHmmssSSS(createdTime) 
				+ ", get=" + formatTime_mmssSSS(lastPull) 
				+ ", time=" + getProcessTime() + "ms"
				+ ", caller=" + getLocationInfo()
				+ ", transid=" + transid
				+ "]";
	}
	
	public String toString2() {
		return "Trans ["
				+ "hour=" + new Date(lastPush).getHours()
				+ ", create=" + formatTime_yyyyMMdd_HHmmssSSS(createdTime) 
				+ ", get=" + formatTime_mmssSSS(lastPull) 
				+ ", time=" + (System.currentTimeMillis() - lastPull) + "ms"
				+ ", caller=" + getLocationInfo()
				+ ", transid=" + transid
				+ "]";
	}
	
	private String formatTime_mmssSSS(long time){
		if(time <= 0){
			return "0";
		}
		return BaseUtils.formatTime("mm:ss.SSS", time);
	}
	
	private String formatTime_yyyyMMdd_HHmmssSSS(long time){
		if(time <= 0){
			return "0";
		}
		return BaseUtils.formatTime("yyyy-MM-dd HH:mm:ss.SSS", time);
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
	
	public void createThrowable() {
		this.throwable = new Throwable();
	}
	
	public String getLocationInfo(){
		if(locationInfo != null){
			return locationInfo;
		}
		String str = getLocationInfo0();
		if(str == null){
			return "null";
		}
		locationInfo = str;
		return locationInfo;
	}
	
	private String getLocationInfo0(){
		if(throwable == null){
			return null;
		}
		StackTraceElement elements[] = throwable.getStackTrace();
		if(elements == null || elements.length == 0){
			return null;
		}
		String thisPackage = this.getClass().getName().substring(0, this.getClass().getName().lastIndexOf(".")); 
		String dbPoolPackage = DBPoolMnt.class.getName().substring(0, this.getClass().getName().lastIndexOf(".")); 
		
		for(int i = 0; i < elements.length; i ++){
			StackTraceElement e = elements[i];
			if(!e.getClassName().contains(thisPackage)){  
				StackTraceElement ePrev = elements[i];
				String str = getLocationInfo(ePrev);
				// System.out.println(str);
				return str;
			}
			if(e.getClassName().contains(dbPoolPackage) && e.getMethodName().contains("keepConnections")){
				StackTraceElement ePrev = elements[i];
				String str = getLocationInfo(ePrev);
				// System.out.println(str);
				return str;
			}
		}
		throwable.printStackTrace();
		return null;
	}
	
	private String getLocationInfo(StackTraceElement ePrev){
		// System.out.println(ePrev.getFileName()); 
		String infoCaller = ePrev.getFileName().substring(0, ePrev.getFileName().lastIndexOf("."))  
				+ ":" + ePrev.getMethodName().replaceFirst("<", "(").replaceFirst(">", ")")
				+ "." + ePrev.getLineNumber();
		return infoCaller;
	}
}
