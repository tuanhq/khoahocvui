package com.vas.aps.db.orm;

import java.io.Serializable;
import com.ligerdev.appbase.utils.db.AntTable;
import com.ligerdev.appbase.utils.db.AntColumn;
import java.util.Date;

@AntTable(catalog="vt_bnhv_sub", name="QUESTION", label="QUESTION", key="ID")
public class Question implements Serializable {

	private int id;
	private int status;
	private String content;
	private String confirmCorrectMt;
	private String confirmWrongMt;
	private String result;
	private Date createdTime;
	private String validAnswer;
	private Date returnDate;
	private int questionOrder;

	public Question(){
	}
	
	public Question(int id, int status, String content, String confirmCorrectMt, String confirmWrongMt, String result, Date createdTime, String validAnswer, Date returnDate, int questionOrder){
		this();
		this.id = id;
		this.status = status;
		this.content = content;
		this.confirmCorrectMt = confirmCorrectMt;
		this.confirmWrongMt = confirmWrongMt;
		this.result = result;
		this.createdTime = createdTime;
		this.validAnswer = validAnswer;
		this.returnDate = returnDate;
		this.questionOrder = questionOrder;
	}
	
	@AntColumn(name="ID", auto_increment=true, size=11, label="ID")
	public void setId(int id){
		this.id = id;
	}
	
	@AntColumn(name="ID", auto_increment=true, size=11, label="ID")
	public int getId(){
		return this.id;
	}
	
	@AntColumn(name="STATUS", size=3, label="STATUS")
	public void setStatus(int status){
		this.status = status;
	}
	
	@AntColumn(name="STATUS", size=3, label="STATUS")
	public int getStatus(){
		return this.status;
	}
	
	@AntColumn(name="CONTENT", size=450, label="CONTENT")
	public void setContent(String content){
		this.content = content;
	}
	
	@AntColumn(name="CONTENT", size=450, label="CONTENT")
	public String getContent(){
		return this.content;
	}
	
	@AntColumn(name="CONFIRM_CORRECT_MT", size=145, label="CONFIRM_CORRECT_MT")
	public void setConfirmCorrectMt(String confirmCorrectMt){
		this.confirmCorrectMt = confirmCorrectMt;
	}
	
	@AntColumn(name="CONFIRM_CORRECT_MT", size=145, label="CONFIRM_CORRECT_MT")
	public String getConfirmCorrectMt(){
		return this.confirmCorrectMt;
	}
	
	@AntColumn(name="CONFIRM_WRONG_MT", size=145, label="CONFIRM_WRONG_MT")
	public void setConfirmWrongMt(String confirmWrongMt){
		this.confirmWrongMt = confirmWrongMt;
	}
	
	@AntColumn(name="CONFIRM_WRONG_MT", size=145, label="CONFIRM_WRONG_MT")
	public String getConfirmWrongMt(){
		return this.confirmWrongMt;
	}
	
	@AntColumn(name="RESULT", size=2, label="RESULT")
	public void setResult(String result){
		this.result = result;
	}
	
	@AntColumn(name="RESULT", size=2, label="RESULT")
	public String getResult(){
		return this.result;
	}
	
	@AntColumn(name="CREATED_TIME", size=19, label="CREATED_TIME")
	public void setCreatedTime(Date createdTime){
		this.createdTime = createdTime;
	}
	
	@AntColumn(name="CREATED_TIME", size=19, label="CREATED_TIME")
	public Date getCreatedTime(){
		return this.createdTime;
	}
	
	@AntColumn(name="VALID_ANSWER", size=45, label="VALID_ANSWER")
	public void setValidAnswer(String validAnswer){
		this.validAnswer = validAnswer;
	}
	
	@AntColumn(name="VALID_ANSWER", size=45, label="VALID_ANSWER")
	public String getValidAnswer(){
		return this.validAnswer;
	}
	
	@AntColumn(name="RETURN_DATE", size=19, label="RETURN_DATE")
	public void setReturnDate(Date returnDate){
		this.returnDate = returnDate;
	}
	
	@AntColumn(name="RETURN_DATE", size=19, label="RETURN_DATE")
	public Date getReturnDate(){
		return this.returnDate;
	}
	
	@AntColumn(name="QUESTION_ORDER", size=2, label="QUESTION_ORDER")
	public void setQuestionOrder(int questionOrder){
		this.questionOrder = questionOrder;
	}
	
	@AntColumn(name="QUESTION_ORDER", size=2, label="QUESTION_ORDER")
	public int getQuestionOrder(){
		return this.questionOrder;
	}
	
	@Override
	public String toString() {
		return "["
			+ "id=" + id
			+ ", status=" + status
			+ ", content=" + content
			+ ", confirmCorrectMt=" + confirmCorrectMt
			+ ", confirmWrongMt=" + confirmWrongMt
			+ ", result=" + result
			+ ", createdTime=" + createdTime
			+ ", validAnswer=" + validAnswer
			+ ", returnDate=" + returnDate
			+ ", questionOrder=" + questionOrder
			+ "]";
	}
}
