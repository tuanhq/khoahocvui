package com.ligerdev.appbase.utils.db.demo;

import java.io.Serializable;
import com.ligerdev.appbase.utils.db.AntTable;
import com.ligerdev.appbase.utils.db.AntColumn;
import java.util.Date;

@AntTable(catalog="sv_smstool", name="stud_account", label="stud_account", key="id")
public class StudAccount implements Serializable {

	private int id;
	private int status;
	private String username;
	private String password;
	private int maxTps;
	private String fullName;
	private Date createdTime;

	public StudAccount(){
	}
	
	public StudAccount(int id, int status, String username, String password, int maxTps, String fullName, Date createdTime){
		this();
		this.id = id;
		this.status = status;
		this.username = username;
		this.password = password;
		this.maxTps = maxTps;
		this.fullName = fullName;
		this.createdTime = createdTime;
	}
	
	@AntColumn(name="id", auto_increment=true, size=11, label="id")
	public void setId(int id){
		this.id = id;
	}
	
	@AntColumn(name="id", auto_increment=true, size=11, label="id")
	public int getId(){
		return this.id;
	}
	
	@AntColumn(name="status", size=2, label="status")
	public void setStatus(int status){
		this.status = status;
	}
	
	@AntColumn(name="status", size=2, label="status")
	public int getStatus(){
		return this.status;
	}
	
	@AntColumn(name="username", size=45, label="username")
	public void setUsername(String username){
		this.username = username;
	}
	
	@AntColumn(name="username", size=45, label="username")
	public String getUsername(){
		return this.username;
	}
	
	@AntColumn(name="password", size=45, label="password")
	public void setPassword(String password){
		this.password = password;
	}
	
	@AntColumn(name="password", size=45, label="password")
	public String getPassword(){
		return this.password;
	}
	
	@AntColumn(name="max_tps", size=4, label="max_tps")
	public void setMaxTps(int maxTps){
		this.maxTps = maxTps;
	}
	
	@AntColumn(name="max_tps", size=4, label="max_tps")
	public int getMaxTps(){
		return this.maxTps;
	}
	
	@AntColumn(name="full_name", size=45, label="full_name")
	public void setFullName(String fullName){
		this.fullName = fullName;
	}
	
	@AntColumn(name="full_name", size=45, label="full_name")
	public String getFullName(){
		return this.fullName;
	}
	
	// @AntColumn(name="created_time", size=19, label="created_time")
	public void setCreatedTime(Date createdTime){
		this.createdTime = createdTime;
	}
	
	 // @AntColumn(name="created_time", size=19, label="created_time")
	public Date getCreatedTime(){
		return this.createdTime;
	}
	
	@Override
	public String toString() {
		return "["
			+ "id=" + id
			+ ", status=" + status
			+ ", username=" + username
			+ ", password=" + password
			+ ", maxTps=" + maxTps
			+ ", fullName=" + fullName
			+ ", createdTime=" + createdTime
			+ "]";
	}
}
