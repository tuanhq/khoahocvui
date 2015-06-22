package com.vas.aps.db.orm;

import java.io.Serializable;
import com.ligerdev.appbase.utils.db.AntTable;
import com.ligerdev.appbase.utils.db.AntColumn;
import java.util.Date;

@AntTable(catalog="khoahocvui", name="sub_buy_question", label="sub_buy_question", key="MSISDN")
public class SubBuyQuestion implements Serializable {

	private String msisdn;
	private int numberQuestion;
	private Date dateModified;

	public SubBuyQuestion(){
	}
	
	public SubBuyQuestion(String msisdn, int numberQuestion, Date dateModified){
		this();
		this.msisdn = msisdn;
		this.numberQuestion = numberQuestion;
		this.dateModified = dateModified;
	}
	
	@AntColumn(name="MSISDN", size=15, label="MSISDN")
	public void setMsisdn(String msisdn){
		this.msisdn = msisdn;
	}
	
	@AntColumn(name="MSISDN", size=15, label="MSISDN")
	public String getMsisdn(){
		return this.msisdn;
	}
	
	@AntColumn(name="number_question", size=11, label="number_question")
	public void setNumberQuestion(int numberQuestion){
		this.numberQuestion = numberQuestion;
	}
	
	@AntColumn(name="number_question", size=11, label="number_question")
	public int getNumberQuestion(){
		return this.numberQuestion;
	}
	
	@AntColumn(name="date_modified", size=19, label="date_modified")
	public void setDateModified(Date dateModified){
		this.dateModified = dateModified;
	}
	
	@AntColumn(name="date_modified", size=19, label="date_modified")
	public Date getDateModified(){
		return this.dateModified;
	}
	
	@Override
	public String toString() {
		return "["
			+ "msisdn=" + msisdn
			+ ", numberQuestion=" + numberQuestion
			+ ", dateModified=" + dateModified
			+ "]";
	}
}
