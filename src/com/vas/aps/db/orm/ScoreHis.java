package com.vas.aps.db.orm;

import java.io.Serializable;
import com.ligerdev.appbase.utils.db.AntTable;
import com.ligerdev.appbase.utils.db.AntColumn;
import java.util.Date;

@AntTable(catalog="vt_bnhv_sub", name="SCORE_HIS", label="SCORE_HIS", key="ID")
public class ScoreHis implements Serializable {

	private int id;
	private String msisdn;
	private int addedScore;
	private int dayScore;
	private int weekScore;
	private int monthScore;
	private int totalScore;
	private String reason;
	private Date createdTime;
	private String transId;

	public ScoreHis(){
	}
	
	public ScoreHis(int id, String msisdn, int addedScore, int dayScore, int weekScore,
			int monthScore, int totalScore, String reason, Date createdTime, String transId){
		this();
		this.id = id;
		this.msisdn = msisdn;
		this.addedScore = addedScore;
		this.dayScore = dayScore;
		this.weekScore = weekScore;
		this.monthScore = monthScore;
		this.totalScore = totalScore;
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
	
	@AntColumn(name="ADDED_SCORE", size=6, label="ADDED_SCORE")
	public void setAddedScore(int addedScore){
		this.addedScore = addedScore;
	}
	
	@AntColumn(name="ADDED_SCORE", size=6, label="ADDED_SCORE")
	public int getAddedScore(){
		return this.addedScore;
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
	
	@AntColumn(name="REASON", size=45, label="REASON")
	public void setReason(String reason){
		this.reason = reason;
	}
	
	@AntColumn(name="REASON", size=45, label="REASON")
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
			+ ", addedScore=" + addedScore
			+ ", dayScore=" + dayScore
			+ ", weekScore=" + weekScore
			+ ", monthScore=" + monthScore
			+ ", totalScore=" + totalScore
			+ ", reason=" + reason
			+ ", createdTime=" + createdTime
			+ ", transId=" + transId
			+ "]";
	}
}
