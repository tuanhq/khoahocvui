package com.ligerdev.appbase.utils;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class LDEntry implements Serializable {
	
	private static Logger logger = Log4jLoader.getLogger();

	private Object key;
	private Object value;

	public LDEntry() {
		// TODO Auto-generated constructor stub
	}

	public LDEntry(Object key, Object value) {
		super();
		this.key = key;
		this.value = value;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public void println(){
		System.out.println(toString()); 
	}
	
	public void log(){
		logger.info(toString()); 
	}

	@Override
	public String toString() {
		String keyClass  = null;
		String valueClass = null;
		if(key != null){
			keyClass = key.getClass().getName();
		}
		if(value != null){
			valueClass = value.getClass().getName();
		}
		return "LDEntry [key=" + key + ", keyClass=" + keyClass 
					+ ", value=" + value + ", valueClass=" + valueClass + "]";
	}
}
