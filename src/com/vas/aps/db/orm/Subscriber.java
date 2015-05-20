package com.vas.aps.db.orm;

import java.io.Serializable;
import com.ligerdev.appbase.utils.db.AntTable;
import com.ligerdev.appbase.utils.db.AntColumn;
import java.util.Date;

@AntTable(catalog="vuadaubep", name="SUBSCRIBER", label="SUBSCRIBER", key="MSISDN")
public class Subscriber implements Serializable {

	private String msisdn;
	private Date activeTime;
	private Date deactiveTime;
	private String activeChannel;
	private String deactiveChannel;
	private int dayScore;
	private int weekScore;
	private int monthScore;
	private int totalScore;
	private Date lastModified;
	private int status;
	private String quesReceived;
	private int lastQuesSms;
	private Date expireTime;
	private int answeredSms;
	private int ignoreQuestion;
	private int answeredWap;
	private int lastQuesWap;
	private int answeredWeb;
	private int lastQuesWeb;

	public Subscriber(){
	}
	
	public Subscriber(String msisdn, Date activeTime, Date deactiveTime, String activeChannel, String deactiveChannel, int dayScore, int weekScore, int monthScore, int totalScore, Date lastModified, int status, String quesReceived, int lastQuesSms, Date expireTime, int answeredSms, int ignoreQuestion, int answeredWap, int lastQuesWap, int answeredWeb, int lastQuesWeb){
		this();
		this.msisdn = msisdn;
		this.activeTime = activeTime;
		this.deactiveTime = deactiveTime;
		this.activeChannel = activeChannel;
		this.deactiveChannel = deactiveChannel;
		this.dayScore = dayScore;
		this.weekScore = weekScore;
		this.monthScore = monthScore;
		this.totalScore = totalScore;
		this.lastModified = lastModified;
		this.status = status;
		this.quesReceived = quesReceived;
		this.lastQuesSms = lastQuesSms;
		this.expireTime = expireTime;
		this.answeredSms = answeredSms;
		this.ignoreQuestion = ignoreQuestion;
		this.answeredWap = answeredWap;
		this.lastQuesWap = lastQuesWap;
		this.answeredWeb = answeredWeb;
		this.lastQuesWeb = lastQuesWeb;
	}
	
	@AntColumn(name="MSISDN", size=15, label="MSISDN")
	public void setMsisdn(String msisdn){
		this.msisdn = msisdn;
	}
	
	@AntColumn(name="MSISDN", size=15, label="MSISDN")
	public String getMsisdn(){
		return this.msisdn;
	}
	
	@AntColumn(name="ACTIVE_TIME", size=19, label="ACTIVE_TIME")
	public void setActiveTime(Date activeTime){
		this.activeTime = activeTime;
	}
	
	@AntColumn(name="ACTIVE_TIME", size=19, label="ACTIVE_TIME")
	public Date getActiveTime(){
		return this.activeTime;
	}
	
	@AntColumn(name="DEACTIVE_TIME", size=19, label="DEACTIVE_TIME")
	public void setDeactiveTime(Date deactiveTime){
		this.deactiveTime = deactiveTime;
	}
	
	@AntColumn(name="DEACTIVE_TIME", size=19, label="DEACTIVE_TIME")
	public Date getDeactiveTime(){
		return this.deactiveTime;
	}
	
	@AntColumn(name="ACTIVE_CHANNEL", size=15, label="ACTIVE_CHANNEL")
	public void setActiveChannel(String activeChannel){
		this.activeChannel = activeChannel;
	}
	
	@AntColumn(name="ACTIVE_CHANNEL", size=15, label="ACTIVE_CHANNEL")
	public String getActiveChannel(){
		return this.activeChannel;
	}
	
	@AntColumn(name="DEACTIVE_CHANNEL", size=15, label="DEACTIVE_CHANNEL")
	public void setDeactiveChannel(String deactiveChannel){
		this.deactiveChannel = deactiveChannel;
	}
	
	@AntColumn(name="DEACTIVE_CHANNEL", size=15, label="DEACTIVE_CHANNEL")
	public String getDeactiveChannel(){
		return this.deactiveChannel;
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
	
	@AntColumn(name="LAST_MODIFIED", size=19, label="LAST_MODIFIED")
	public void setLastModified(Date lastModified){
		this.lastModified = lastModified;
	}
	
	@AntColumn(name="LAST_MODIFIED", size=19, label="LAST_MODIFIED")
	public Date getLastModified(){
		return this.lastModified;
	}
	
	@AntColumn(name="STATUS", size=2, label="STATUS")
	public void setStatus(int status){
		this.status = status;
	}
	
	@AntColumn(name="STATUS", size=2, label="STATUS")
	public int getStatus(){
		return this.status;
	}
	
	@AntColumn(name="QUES_RECEIVED", size=5000, label="QUES_RECEIVED")
	public void setQuesReceived(String quesReceived){
		this.quesReceived = quesReceived;
	}
	
	@AntColumn(name="QUES_RECEIVED", size=5000, label="QUES_RECEIVED")
	public String getQuesReceived(){
		return this.quesReceived;
	}
	
	@AntColumn(name="LAST_QUES_SMS", size=8, label="LAST_QUES_SMS")
	public void setLastQuesSms(int lastQuesSms){
		this.lastQuesSms = lastQuesSms;
	}
	
	@AntColumn(name="LAST_QUES_SMS", size=8, label="LAST_QUES_SMS")
	public int getLastQuesSms(){
		return this.lastQuesSms;
	}
	
	@AntColumn(name="EXPIRE_TIME", size=19, label="EXPIRE_TIME")
	public void setExpireTime(Date expireTime){
		this.expireTime = expireTime;
	}
	
	@AntColumn(name="EXPIRE_TIME", size=19, label="EXPIRE_TIME")
	public Date getExpireTime(){
		return this.expireTime;
	}
	
	@AntColumn(name="ANSWERED_SMS", size=3, label="ANSWERED_SMS")
	public void setAnsweredSms(int answeredSms){
		this.answeredSms = answeredSms;
	}
	
	@AntColumn(name="ANSWERED_SMS", size=3, label="ANSWERED_SMS")
	public int getAnsweredSms(){
		return this.answeredSms;
	}
	
	@AntColumn(name="IGNORE_QUESTION", size=1, label="IGNORE_QUESTION")
	public void setIgnoreQuestion(int ignoreQuestion){
		this.ignoreQuestion = ignoreQuestion;
	}
	
	@AntColumn(name="IGNORE_QUESTION", size=1, label="IGNORE_QUESTION")
	public int getIgnoreQuestion(){
		return this.ignoreQuestion;
	}
	
	@AntColumn(name="ANSWERED_WAP", size=3, label="ANSWERED_WAP")
	public void setAnsweredWap(int answeredWap){
		this.answeredWap = answeredWap;
	}
	
	@AntColumn(name="ANSWERED_WAP", size=3, label="ANSWERED_WAP")
	public int getAnsweredWap(){
		return this.answeredWap;
	}
	
	@AntColumn(name="LAST_QUES_WAP", size=8, label="LAST_QUES_WAP")
	public void setLastQuesWap(int lastQuesWap){
		this.lastQuesWap = lastQuesWap;
	}
	
	@AntColumn(name="LAST_QUES_WAP", size=8, label="LAST_QUES_WAP")
	public int getLastQuesWap(){
		return this.lastQuesWap;
	}
	
	@AntColumn(name="ANSWERED_WEB", size=3, label="ANSWERED_WEB")
	public void setAnsweredWeb(int answeredWeb){
		this.answeredWeb = answeredWeb;
	}
	
	@AntColumn(name="ANSWERED_WEB", size=3, label="ANSWERED_WEB")
	public int getAnsweredWeb(){
		return this.answeredWeb;
	}
	
	@AntColumn(name="LAST_QUES_WEB", size=8, label="LAST_QUES_WEB")
	public void setLastQuesWeb(int lastQuesWeb){
		this.lastQuesWeb = lastQuesWeb;
	}
	
	@AntColumn(name="LAST_QUES_WEB", size=8, label="LAST_QUES_WEB")
	public int getLastQuesWeb(){
		return this.lastQuesWeb;
	}
	
	@Override
	public String toString() {
		return "["
			+ "msisdn=" + msisdn
			+ ", activeTime=" + activeTime
			+ ", deactiveTime=" + deactiveTime
			+ ", activeChannel=" + activeChannel
			+ ", deactiveChannel=" + deactiveChannel
			+ ", dayScore=" + dayScore
			+ ", weekScore=" + weekScore
			+ ", monthScore=" + monthScore
			+ ", totalScore=" + totalScore
			+ ", lastModified=" + lastModified
			+ ", status=" + status
			+ ", quesReceived=" + quesReceived
			+ ", lastQuesSms=" + lastQuesSms
			+ ", expireTime=" + expireTime
			+ ", answeredSms=" + answeredSms
			+ ", ignoreQuestion=" + ignoreQuestion
			+ ", answeredWap=" + answeredWap
			+ ", lastQuesWap=" + lastQuesWap
			+ ", answeredWeb=" + answeredWeb
			+ ", lastQuesWeb=" + lastQuesWeb
			+ "]";
	}
}
