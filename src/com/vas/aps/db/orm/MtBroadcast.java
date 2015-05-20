package com.vas.aps.db.orm;

import java.io.Serializable;
import com.ligerdev.appbase.utils.db.AntTable;
import com.ligerdev.appbase.utils.db.AntColumn;
import java.util.Date;

@AntTable(catalog="vt_bnhv_sub", name="MT_BROADCAST", label="MT_BROADCAST", key="ID")
public class MtBroadcast implements Serializable {

	private int id;
	private String msisdn;
	private String content;
	private Date sendTime;
	private Date createdTime;
	private int status;

	public MtBroadcast(){
	}
	
	public MtBroadcast(int id, String msisdn, String content, Date sendTime, Date createdTime, int status){
		this();
		this.id = id;
		this.msisdn = msisdn;
		this.content = content;
		this.sendTime = sendTime;
		this.createdTime = createdTime;
		this.status = status;
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
	
	@AntColumn(name="CONTENT", size=450, label="CONTENT")
	public void setContent(String content){
		this.content = content;
	}
	
	@AntColumn(name="CONTENT", size=450, label="CONTENT")
	public String getContent(){
		return this.content;
	}
	
	@AntColumn(name="SEND_TIME", size=19, label="SEND_TIME")
	public void setSendTime(Date sendTime){
		this.sendTime = sendTime;
	}
	
	@AntColumn(name="SEND_TIME", size=19, label="SEND_TIME")
	public Date getSendTime(){
		return this.sendTime;
	}
	
	// @AntColumn(name="CREATED_TIME", size=19, label="CREATED_TIME")
	public void setCreatedTime(Date createdTime){
		this.createdTime = createdTime;
	}
	
	// @AntColumn(name="CREATED_TIME", size=19, label="CREATED_TIME")
	public Date getCreatedTime(){
		return this.createdTime;
	}
	
	@AntColumn(name="STATUS", size=2, label="STATUS")
	public void setStatus(int status){
		this.status = status;
	}
	
	@AntColumn(name="STATUS", size=2, label="STATUS")
	public int getStatus(){
		return this.status;
	}
	
	@Override
	public String toString() {
		return "["
			+ "id=" + id
			+ ", msisdn=" + msisdn
			+ ", content=" + content
			+ ", sendTime=" + sendTime
			+ ", createdTime=" + createdTime
			+ ", status=" + status
			+ "]";
	}
}
