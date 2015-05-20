/**
 * 
 */
package org.thq.business;

/**
 * @author tuanhq
 *
 */
public class SubActiveDuplicateDTO {
	private String subId;
	private int status;
	private String date;
	
	/**
	 * 
	 */
	public SubActiveDuplicateDTO() {
		// TODO Auto-generated constructor stub
	}
	public SubActiveDuplicateDTO(String subId, int status, String date) {
		this.subId = subId;
		this.status = status;
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
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
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
