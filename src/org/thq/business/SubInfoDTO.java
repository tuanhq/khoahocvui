/**
 * 
 */
package org.thq.business;

/**
 * @author tuanhq
 *
 */
public class SubInfoDTO {	
	private String sub_id ;
	private int status;
	private String date_modified;
	/**
	 * 
	 */
	public SubInfoDTO() {
		// TODO Auto-generated constructor stub
	}
	public SubInfoDTO(String sub_id, int status, String date) {
		this.sub_id = sub_id;
		this.status = status;
		this.date_modified = date;
	}
	/**
	 * @return the sub_id
	 */
	public String getSub_id() {
		return sub_id;
	}
	/**
	 * @param sub_id the sub_id to set
	 */
	public void setSub_id(String sub_id) {
		this.sub_id = sub_id;
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
	 * @return the date_modified
	 */
	public String getDate_modified() {
		return date_modified;
	}
	/**
	 * @param date_modified the date_modified to set
	 */
	public void setDate_modified(String date_modified) {
		this.date_modified = date_modified;
	}
	

}
