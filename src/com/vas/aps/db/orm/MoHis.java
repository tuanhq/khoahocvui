package com.vas.aps.db.orm;

import java.io.Serializable;
import com.ligerdev.appbase.utils.db.AntTable;
import com.ligerdev.appbase.utils.db.AntColumn;
import com.vas.aps.comms.AppConstants;

import java.util.Date;

@AntTable(catalog="vt_bnhv_sub", name="MO_" + AntTable.DATE_PATTERN, label="", key="ID", time_pattern="yyyyMM")
public class MoHis implements Serializable {

	private int id;
	private String msisdn;
	private String content;
	private String command;
	private Date createdTime;
	private String transId;
	
	public MoHis(){
	}
	
	public MoHis(int id, String msisdn, String content, String command, Date createdTime, String transId){
		this();
		this.id = id;
		this.msisdn = msisdn;
		this.content = content;
		this.command = command;
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
	
	@AntColumn(name="CONTENT", size=115, label="CONTENT")
	public void setContent(String content){
		this.content = content;
	}
	
	@AntColumn(name="CONTENT", size=115, label="CONTENT")
	public String getContent(){
		return this.content;
	}
	
	@AntColumn(name="COMMAND", size=25, label="COMMAND")
	public void setCommand(String command){
		this.command = command;
	}
	
	@AntColumn(name="COMMAND", size=25, label="COMMAND")
	public String getCommand(){
		return this.command;
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
			+ ", content=" + content
			+ ", command=" + command
			+ ", createdTime=" + createdTime
			+ ", transId=" + transId
			+ "]";
	}
}
