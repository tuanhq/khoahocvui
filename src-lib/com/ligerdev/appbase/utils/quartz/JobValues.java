package com.ligerdev.appbase.utils.quartz;

import java.io.Serializable;

public class JobValues implements Serializable {

	private String key;
	private Object value;
	
	public JobValues(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
}
