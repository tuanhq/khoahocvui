package com.vas.aps.db.orm;

import java.io.Serializable;
import com.ligerdev.appbase.utils.db.AntTable;
import com.ligerdev.appbase.utils.db.AntColumn;
import java.util.Date;

@AntTable(catalog="vt_bnhv_sub", name="CDR_" + AntTable.DATE_PATTERN, label="", key="ID", time_pattern="yyyyMM")
public class CdrHis implements Serializable {

	private int id;
	private String msisdn;
	private int fee;
	private String reason;
	private Date createdTime;
	private String transId;

	public CdrHis(){
	}
	
	public CdrHis(int id, String msisdn, int fee, String reason, Date createdTime, String transId){
		this();
		this.id = id;
		this.msisdn = msisdn;
		this.fee = fee;
		this.reason = reason;
		this.createdTime = createdTime;
		this.transId = transId;
	}
	
	@AntColumn(name="ID", auto_increment=true, size=11, label="ID")
	public void setId(int id){
		this.id = id;
	}
	
	@AntColumn(name="ID", auto_increment=true, size=11, label="ID")
	public int getId(){
		return this.id;
	}
	
	@AntColumn(name="MSISDN", size=15, label="MSISDN")
	public void setMsisdn(String msisdn){
		this.msisdn = msisdn;
	}
	
	@AntColumn(name="MSISDN", size=15, label="MSISDN")
	public String getMsisdn(){
		return this.msisdn;
	}
	
	@AntColumn(name="FEE", size=3, label="FEE")
	public void setFee(int fee){
		this.fee = fee;
	}
	
	@AntColumn(name="FEE", size=3, label="FEE")
	public int getFee(){
		return this.fee;
	}
	
	@AntColumn(name="REASON", size=15, label="REASON")
	public void setReason(String reason){
		this.reason = reason;
	}
	
	@AntColumn(name="REASON", size=15, label="REASON")
	public String getReason(){
		return this.reason;
	}
	
	// @AntColumn(name="CREATED_TIME", size=19, label="CREATED_TIME")
	public void setCreatedTime(Date createdTime){
		this.createdTime = createdTime;
	}
	
	// @AntColumn(name="CREATED_TIME", size=19, label="CREATED_TIME")
	public Date getCreatedTime(){
		return this.createdTime;
	}
	
	@AntColumn(name="TRANS_ID", size=15, label="TRANS_ID")
	public void setTransId(String transId){
		this.transId = transId;
	}
	
	@AntColumn(name="TRANS_ID", size=15, label="TRANS_ID")
	public String getTransId(){
		return this.transId;
	}
	
	@Override
	public String toString() {
		return "["
			+ "id=" + id
			+ ", msisdn=" + msisdn
			+ ", fee=" + fee
			+ ", reason=" + reason
			+ ", createdTime=" + createdTime
			+ ", transId=" + transId
			+ "]";
	}
}
