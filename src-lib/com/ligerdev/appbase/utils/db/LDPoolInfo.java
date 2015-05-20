package com.ligerdev.appbase.utils.db;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class LDPoolInfo implements Serializable {

	private int date;
	private String poolName;
	private int totalUse;
	private int totalCreateNew;
	private int totalProcessTime;
	private ArrayList<LDConnInfo> topProcessTimeConns = new ArrayList<LDConnInfo>();
	private transient ConcurrentHashMap<Connection, LDConnection> listBusy = null;
	private static Logger logger = Log4jLoader.getLogger();

	public LDPoolInfo(int date, String poolName) {
		this.date = date;
		this.setPoolName(poolName);
	}

	public void addInfo(LDConnInfo info){
		if(info == null){
			return;
		}
		totalUse ++;
		totalProcessTime += Math.abs(info.getProcessTime()); 
		if(info.isCreatedNew()){
			totalCreateNew ++;
		}
		int processTimeNew = info.getProcessTime();
		
		if(topProcessTimeConns.size() < 10){
			
			if(topProcessTimeConns.size() == 0){
				topProcessTimeConns.add(info);
				return;
			}
			int processTimeOld = -1;
			int indexTemp = -1;
			
			for(int i = 0; i < topProcessTimeConns.size(); i ++){
				LDConnInfo infoTmp = topProcessTimeConns.get(i);
				
				if(info.getLocationInfo().equals(infoTmp.getLocationInfo())){ 
					processTimeOld = infoTmp.getProcessTime();
					indexTemp = i;
					break;
				}
			}
			if(indexTemp < 0){ // not exist
				topProcessTimeConns.add(info);
			} else {
				if(processTimeNew >= processTimeOld){
					topProcessTimeConns.set(indexTemp, info);
				}
			}
		} else {
			int processTimeOld = topProcessTimeConns.get(0).getProcessTime();
			int indexTemp = 0;
			
			for(int i = 1; i < topProcessTimeConns.size(); i ++){
				LDConnInfo infoTmp = topProcessTimeConns.get(i);
				int processTimeTmp = infoTmp.getProcessTime();
				
				if(info.getLocationInfo().equals(infoTmp.getLocationInfo())){ 
					if(processTimeNew >= processTimeTmp){
						topProcessTimeConns.set(i, info);
					}
					return;
				}
				if(processTimeOld > processTimeTmp){
					processTimeOld = processTimeTmp;
					indexTemp = i;
				}
			}
			if(processTimeNew >= processTimeOld){
				topProcessTimeConns.set(indexTemp, info);
			}
		}
	}
	
	public int getTotalUse() {
		return totalUse;
	}

	public void setTotalUse(int totalUse) {
		this.totalUse = totalUse;
	}

	public int getTotalCreateNew() {
		return totalCreateNew;
	}

	public void setTotalCreateNew(int totalCreateNew) {
		this.totalCreateNew = totalCreateNew;
	}

	public int getTotalProcessTime() {
		return totalProcessTime;
	}

	public void setTotalProcessTime(int totalProcessTime) {
		this.totalProcessTime = totalProcessTime;
	}

	public ArrayList<LDConnInfo> getTopProcessTimeConns() {
		return topProcessTimeConns;
	}

	public void setTopProcessTimeConns(ArrayList<LDConnInfo> topProcessTimeConns) {
		this.topProcessTimeConns = topProcessTimeConns;
	}

	@Override
	public String toString() {
		String str = 
				"\n================================="
				+ "==================================\n"
				+ "# ------------ pool's base info ------------\n" 
				+ "PoolInfo: [" 
				+ " name=" + poolName 
				+ ", date=" + date 
				+ ", totalUse=" + totalUse 
				+ ", totalCreateNew=" + totalCreateNew 
				+ ", totalTime=" + (String.format("%.1f", totalProcessTime/1000.0)) + "s"
				+ ", avrTime=" + (totalUse  == 0 ? 0 : (String.format("%.1f", (float)totalProcessTime / totalUse))) + "ms"
				+ "]";
		
		str += "\n\n# ------------ top slow transactions  ------------" ;
				
		Collections.sort(topProcessTimeConns);
		for(int i = 0; i < topProcessTimeConns.size(); i ++){
			LDConnInfo info = topProcessTimeConns.get(i);
			str += "\n" + String.format("%02d", (i + 1)) + "-"  + String.valueOf(info);
		}
		if(listBusy != null){
			Set<Entry<Connection, LDConnection>> set =  listBusy.entrySet();
			Iterator<Entry<Connection, LDConnection>> iter = set.iterator();
			
			if(iter.hasNext()){
				str += "\n\n# ------------ list using transactions  ------------" ;
			}
			int i = 0;
			while(iter.hasNext()){
				try {
					Entry<Connection, LDConnection> entry = iter.next();
					LDConnection ldConn = entry.getValue();
					LDConnInfo connInfo = (LDConnInfo) ldConn.getConnInfo().clone();
					str += "\n" + String.format("%02d", (i + 1)) + "-"  + (connInfo == null ? null : connInfo.toString2());
					i ++;
				} catch (Exception e) {
					logger.info("Exception: " + e.getMessage());
				}
			}
		}
		
		str +=
				"\n=================================" +
				"==================================";
		return str;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public ConcurrentHashMap<Connection, LDConnection> getListBusy() {
		return listBusy;
	}

	public void setListBusy(ConcurrentHashMap<Connection, LDConnection> listBusy) {
		this.listBusy = listBusy;
	}
}
