package com.vas.aps.db.orm;

import java.io.Serializable;
import com.ligerdev.appbase.utils.db.AntTable;
import com.ligerdev.appbase.utils.db.AntColumn;
import java.util.Date;

@AntTable(catalog="vt_bnhv_sub", name="CHARGE_REMINDER", label="CHARGE_REMINDER", key="ID")
public class ChargeReminder implements Serializable {

	private int id;
	private String msisdn;
	private int addScore;
	private int type;
	private Date createdTime;
	private String transId;

	public ChargeReminder(){
	}
	
	public ChargeReminder(int id, String msisdn, int addScore, int type, Date createdTime, String transId){
		this();
		this.id = id;
		this.msisdn = msisdn;
		this.addScore = addScore;
		this.type = type;
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
	
	@AntColumn(name="ADD_SCORE", size=4, label="ADD_SCORE")
	public void setAddScore(int addScore){
		this.addScore = addScore;
	}
	
	@AntColumn(name="ADD_SCORE", size=4, label="ADD_SCORE")
	public int getAddScore(){
		return this.addScore;
	}
	
	@AntColumn(name="TYPE", size=4, label="TYPE")
	public void setType(int type){
		this.type = type;
	}
	
	@AntColumn(name="TYPE", size=4, label="TYPE")
	public int getType(){
		return this.type;
	}
	
	@AntColumn(name="CREATED_TIME", size=19, label="CREATED_TIME")
	public void setCreatedTime(Date createdTime){
		this.createdTime = createdTime;
	}
	
	@AntColumn(name="CREATED_TIME", size=19, label="CREATED_TIME")
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
			+ ", addScore=" + addScore
			+ ", type=" + type
			+ ", createdTime=" + createdTime
			+ ", transId=" + transId
			+ "]";
	}
}
