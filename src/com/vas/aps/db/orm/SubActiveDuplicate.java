package com.vas.aps.db.orm;

import java.io.Serializable;
import com.ligerdev.appbase.utils.db.AntTable;
import com.ligerdev.appbase.utils.db.AntColumn;
import java.util.Date;

@AntTable(catalog="khoahocvui", name="sub_active_duplicate", label="sub_active_duplicate", key="msisdn")
public class SubActiveDuplicate implements Serializable {

	private String msisdn;
	private int isEnable;
	private Date dateModified;

	public SubActiveDuplicate(){
	}
	
	public SubActiveDuplicate(String msisdn, int isEnable, Date dateModified){
		this();
		this.msisdn = msisdn;
		this.isEnable = isEnable;
		this.dateModified = dateModified;
	}
	
	@AntColumn(name="MSISDN", size=15, label="MSISDN")
	public void setMsisdn(String msisdn){
		this.msisdn = msisdn;
	}
	
	@AntColumn(name="MSISDN", size=15, label="MSISDN")
	public String getMsisdn(){
		return this.msisdn;
	}
	
	@AntColumn(name="is_enable", size=11, label="is_enable")
	public void setIsEnable(int isEnable){
		this.isEnable = isEnable;
	}
	
	@AntColumn(name="is_enable", size=11, label="is_enable")
	public int getIsEnable(){
		return this.isEnable;
	}
	
	@AntColumn(name="date_modified", size=19, label="date_modified")
	public void setDateModified(Date dateModified){
		this.dateModified = dateModified;
	}
	
	@AntColumn(name="date_modified", size=19, label="date_modified")
	public Date getDateModified(){
		return this.dateModified;
	}
	
	@Override
	public String toString() {
		return "["
			+ "msisdn=" + msisdn
			+ ", isEnable=" + isEnable
			+ ", dateModified=" + dateModified
			+ "]";
	}
}
