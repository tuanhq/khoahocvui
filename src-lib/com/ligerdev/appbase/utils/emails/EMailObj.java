package com.ligerdev.appbase.utils.emails;

import java.io.Serializable;

public class EMailObj implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 310599798032234157L;
	
	private String address;
	private String subject;
	private String content;
	private boolean isHTMLStyle;
	
	public EMailObj() {
		// TODO Auto-generated constructor stub
	}

	public EMailObj(String address, String subject, String content, boolean isHTMLStyle) {
		super();
		this.address = address;
		this.subject = subject;
		this.content = content;
		this.isHTMLStyle = isHTMLStyle;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isHTMLStyle() {
		return isHTMLStyle;
	}

	public void setHTMLStyle(boolean isHTMLStyle) {
		this.isHTMLStyle = isHTMLStyle;
	}
	
}
