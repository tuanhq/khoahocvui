/**
 * 
 */
package org.thq.business;

/**
 * @author tuanhq
 *
 */
public class SubScoreDTO {	
	
	private String subId;
	private int score;
	private String type;
	private String date;
	/**
	 * 
	 */
	public SubScoreDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public SubScoreDTO(String subId, int score, String type, String date) {
		this.subId = subId;
		this.score = score;
		this.type = type;
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
	 * @return the score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
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
