/**
 * 
 */
package org.thq.business;

/**
 * @author tuanhq
 *
 */
public class SubRequestLog {
	private String subId;
	private String mo;
	private String date;
	/**
	 * 
	 */
	public SubRequestLog() {
		// TODO Auto-generated constructor stub
	}
	public SubRequestLog(String subId, String mo, String date) {
		this.subId = subId;
		this.mo = mo;
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
	 * @return the mo
	 */
	public String getMo() {
		return mo;
	}
	/**
	 * @param mo the mo to set
	 */
	public void setMo(String mo) {
		this.mo = mo;
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
