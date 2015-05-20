package com.vas.aps.db.orm;

import java.io.Serializable;
import com.ligerdev.appbase.utils.db.AntTable;
import com.ligerdev.appbase.utils.db.AntColumn;
import java.util.Date;

@AntTable(catalog="vt_bnhv_sub", name="MT_" + AntTable.DATE_PATTERN, label="", key="ID", time_pattern="yyyyMM")
public class MtHis implements Serializable {

	private int id;
	private String msisdn;
	private String content;
	private int moId;
	private String command;
	private Date createdTime;
	private String sendResult;
	private String transId;

	public MtHis(){
	}
	
	public MtHis(String transid, int id, String msisdn, String content, int moId, String command, Date createdTime, String sendResult){
		this();
		this.id = id;
		this.msisdn = msisdn;
		this.content = content;
		this.moId = moId;
		this.command = command;
		this.createdTime = createdTime;
		this.transId = transid;
		this.sendResult = sendResult;
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
	
	@AntColumn(name="CONTENT", size=650, label="CONTENT")
	public void setContent(String content){
		this.content = content;
	}
	
	@AntColumn(name="CONTENT", size=650, label="CONTENT")
	public String getContent(){
		return this.content;
	}
	
	@AntColumn(name="MO_ID", size=11, label="MO_ID")
	public void setMoId(int moId){
		this.moId = moId;
	}
	
	@AntColumn(name="MO_ID", size=11, label="MO_ID")
	public int getMoId(){
		return this.moId;
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
	
	@AntColumn(name="SEND_RESULT", size=5, label="SEND_RESULT")
	public String getSendResult() {
		return sendResult;
	}

	@AntColumn(name="SEND_RESULT", size=5, label="SEND_RESULT")
	public void setSendResult(String sendResult) {
		this.sendResult = sendResult;
	}
	
	@Override
	public String toString() {
		return "["
			+ "id=" + id
			+ ", msisdn=" + msisdn
			+ ", content=" + content
			+ ", moId=" + moId
			+ ", command=" + command
			+ ", createdTime=" + createdTime
			+ ", transid=" + transId
			+ ", sendResult=" + sendResult
			+ "]";
	}

	@AntColumn(name="TRANS_ID", size=15, label="TRANS_ID")
	public String getTransId() {
		return transId;
	}

	@AntColumn(name="TRANS_ID", size=15, label="TRANS_ID")
	public void setTransId(String transId) {
		this.transId = transId;
	}
}
