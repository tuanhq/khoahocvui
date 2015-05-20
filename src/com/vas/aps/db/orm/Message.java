package com.vas.aps.db.orm;

import java.io.Serializable;
import com.ligerdev.appbase.utils.db.AntTable;
import com.ligerdev.appbase.utils.db.AntColumn;

@AntTable(catalog="vt_bnhv_sub", name="MESSAGE", label="MESSAGE", key="ID")
public class Message implements Serializable {

	private String code;
	private int status;
	private String desc;
	private String content;
	private String comment;

	public Message(){
	}
	
	public Message(String code, int status, String desc, String content, String comment){
		this();
		this.code = code;
		this.status = status;
		this.desc = desc;
		this.content = content;
		this.comment = comment;
	}
	
	@AntColumn(name="CODE", size=15, label="CODE")
	public void setCode(String code){
		this.code = code;
	}
	
	@AntColumn(name="CODE", size=15, label="CODE")
	public String getCode(){
		return this.code;
	}
	
	@AntColumn(name="STATUS", size=3, label="STATUS")
	public void setStatus(int status){
		this.status = status;
	}
	
	@AntColumn(name="STATUS", size=3, label="STATUS")
	public int getStatus(){
		return this.status;
	}
	
	@AntColumn(name="DESC", size=145, label="DESC")
	public void setDesc(String desc){
		this.desc = desc;
	}
	
	@AntColumn(name="DESC", size=145, label="DESC")
	public String getDesc(){
		return this.desc;
	}
	
	@AntColumn(name="CONTENT", size=450, label="CONTENT")
	public void setContent(String content){
		this.content = content;
	}
	
	@AntColumn(name="CONTENT", size=450, label="CONTENT")
	public String getContent(){
		return this.content;
	}
	
	@AntColumn(name="COMMENT", size=100, label="COMMENT")
	public void setComment(String comment){
		this.comment = comment;
	}
	
	@AntColumn(name="COMMENT", size=100, label="COMMENT")
	public String getComment(){
		return this.comment;
	}
	
	@Override
	public String toString() {
		return "["
			+ "code=" + code
			+ ", status=" + status
			+ ", desc=" + desc
			+ ", content=" + content
			+ ", comment=" + comment
			+ "]";
	}
}
