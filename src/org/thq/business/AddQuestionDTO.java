/**
 * 
 */
package org.thq.business;

/**
 * @author tuanhq
 *
 */
public class AddQuestionDTO {
	private String subId;
	private int addQuestion;
	private String date;
	private String type;
	/**
	 * 
	 */
	public AddQuestionDTO() {
		// TODO Auto-generated constructor stub
	}
	public AddQuestionDTO(String subId, int addQuestion, String date, String type) {
		this.subId = subId;
		this.addQuestion = addQuestion;
		this.date = date;
		this.setType(type);
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
	 * @return the addQuestion
	 */
	public int getAddQuestion() {
		return addQuestion;
	}
	/**
	 * @param addQuestion the addQuestion to set
	 */
	public void setAddQuestion(int addQuestion) {
		this.addQuestion = addQuestion;
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
	

}
