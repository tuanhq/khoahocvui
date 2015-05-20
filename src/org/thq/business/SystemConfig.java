/**
 * 
 */
package org.thq.business;

/**
 * @author tuanhq
 *
 */
public class SystemConfig {
	private String property;
	private String value;
	
	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 
	 */
	
	public SystemConfig() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the status
	 */
	

	public SystemConfig(String  property, String value) {
		this.property = property;
		this.value = value;
	}

}
