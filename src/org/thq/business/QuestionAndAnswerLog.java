/**
 * 
 */
package org.thq.business;

/**
 * @author tuanhq
 *
 */
public class QuestionAndAnswerLog {
	
	private String subId;
	private int questionId;
	private String type;
	private int answer;
	private String date;
	
	/**
	 * 
	 */
	public QuestionAndAnswerLog() {
		// TODO Auto-generated constructor stub
	}
	public QuestionAndAnswerLog(String subId, int questionId, String type, int answer, String date) {
		this.subId = subId;
		this.questionId = questionId;
		this.type = type;
		this.answer = answer;
		this.date = date;
	}
	/**
	 * @return the subId
	 */
	public String getSubId() {
		return subId;
	}
	/**
	 * @param subId the subId to set
	 */
	public void setSubId(String subId) {
		this.subId = subId;
	}
	/**
	 * @return the questionId
	 */
	public int getQuestionId() {
		return questionId;
	}
	/**
	 * @param questionId the questionId to set
	 */
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the answer
	 */
	public int getAnswer() {
		return answer;
	}
	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(int answer) {
		this.answer = answer;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	
	

}
