package com.vas.aps.db.orm;

import java.io.Serializable;
import com.ligerdev.appbase.utils.db.AntTable;
import com.ligerdev.appbase.utils.db.AntColumn;
import java.util.Date;

@AntTable(catalog="vt_bnhv_sub", name="WINNER", label="WINNER", key="ID")
public class Winner implements Serializable {

	private int id;
	private String msisdn;
	private int dayScore;
	private int weekScore;
	private int monthScore;
	private int totalScore;
	private String type;
	private String syntheticDate;
	private Date createdTime;

	public Winner(){
	}
	
	public Winner(int id, String msisdn, int dayScore, int weekScore, int monthScore, 
			int totalScore, String type, String syntheticDate, Date createdTime){
		this();
		this.id = id;
		this.msisdn = msisdn;
		this.dayScore = dayScore;
		this.weekScore = weekScore;
		this.monthScore = monthScore;
		this.totalScore = totalScore;
		this.type = type;
		this.syntheticDate = syntheticDate;
		this.createdTime = createdTime;
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
	
	@AntColumn(name="DAY_SCORE", size=6, label="DAY_SCORE")
	public void setDayScore(int dayScore){
		this.dayScore = dayScore;
	}
	
	@AntColumn(name="DAY_SCORE", size=6, label="DAY_SCORE")
	public int getDayScore(){
		return this.dayScore;
	}
	
	@AntColumn(name="WEEK_SCORE", size=6, label="WEEK_SCORE")
	public void setWeekScore(int weekScore){
		this.weekScore = weekScore;
	}
	
	@AntColumn(name="WEEK_SCORE", size=6, label="WEEK_SCORE")
	public int getWeekScore(){
		return this.weekScore;
	}
	
	@AntColumn(name="MONTH_SCORE", size=6, label="MONTH_SCORE")
	public void setMonthScore(int monthScore){
		this.monthScore = monthScore;
	}
	
	@AntColumn(name="MONTH_SCORE", size=6, label="MONTH_SCORE")
	public int getMonthScore(){
		return this.monthScore;
	}
	
	@AntColumn(name="TOTAL_SCORE", size=6, label="TOTAL_SCORE")
	public void setTotalScore(int totalScore){
		this.totalScore = totalScore;
	}
	
	@AntColumn(name="TOTAL_SCORE", size=6, label="TOTAL_SCORE")
	public int getTotalScore(){
		return this.totalScore;
	}
	
	@AntColumn(name="TYPE", size=15, label="TYPE")
	public void setType(String type){
		this.type = type;
	}
	
	@AntColumn(name="TYPE", size=15, label="TYPE")
	public String getType(){
		return this.type;
	}
	
	@AntColumn(name="SYNTHETIC_DATE", size=15, label="SYNTHETIC_DATE")
	public void setSyntheticDate(String syntheticDate){
		this.syntheticDate = syntheticDate;
	}
	
	@AntColumn(name="SYNTHETIC_DATE", size=15, label="SYNTHETIC_DATE")
	public String getSyntheticDate(){
		return this.syntheticDate;
	}
	
	// @AntColumn(name="CREATED_TIME", size=19, label="CREATED_TIME")
	public void setCreatedTime(Date createdTime){
		this.createdTime = createdTime;
	}
	
	// @AntColumn(name="CREATED_TIME", size=19, label="CREATED_TIME")
	public Date getCreatedTime(){
		return this.createdTime;
	}
	
	@Override
	public String toString() {
		return "["
			+ "id=" + id
			+ ", msisdn=" + msisdn
			+ ", dayScore=" + dayScore
			+ ", weekScore=" + weekScore
			+ ", monthScore=" + monthScore
			+ ", totalScore=" + totalScore
			+ ", type=" + type
			+ ", syntheticDate=" + syntheticDate
			+ ", createdTime=" + createdTime
			+ "]";
	}
}
