/**
 * 
 */
package org.thq.business;

/**
 * @author tuanhq
 *
 */
public class ChargingInfo {
	String userId;
	String serviceId;
	String commandCode;
	String timeStamp1;
	String timeStamp2;
	String proResult;
	String mts;
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * @return the commandCode
	 */
	public String getCommandCode() {
		return commandCode;
	}
	/**
	 * @param commandCode the commandCode to set
	 */
	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}
	/**
	 * @return the timeStamp1
	 */
	public String getTimeStamp1() {
		return timeStamp1;
	}
	/**
	 * @param timeStamp1 the timeStamp1 to set
	 */
	public void setTimeStamp1(String timeStamp1) {
		this.timeStamp1 = timeStamp1;
	}
	/**
	 * @return the timeStamp2
	 */
	public String getTimeStamp2() {
		return timeStamp2;
	}
	/**
	 * @param timeStamp2 the timeStamp2 to set
	 */
	public void setTimeStamp2(String timeStamp2) {
		this.timeStamp2 = timeStamp2;
	}
	/**
	 * @return the proResult
	 */
	public String getProResult() {
		return proResult;
	}
	/**
	 * @param proResult the proResult to set
	 */
	public void setProResult(String proResult) {
		this.proResult = proResult;
	}
	/**
	 * @return the mts
	 */
	public String getMts() {
		return mts;
	}
	/**
	 * @param mts the mts to set
	 */
	public void setMts(String mts) {
		this.mts = mts;
	}

	public ChargingInfo(String userId, String serviceId, String commandCode, String timeStamp1, String timeStamp2, String proResult, String mts) {
		this.userId = userId;
		this.serviceId = serviceId;
		this.commandCode = commandCode;
		this.timeStamp1 = timeStamp1;
		this.timeStamp2 = timeStamp2;
		this.proResult = proResult;
		this.mts = mts;
	}
}
