package com.vas.aps.api;

import com.vas.aps.db.orm.Subscriber;

public class SubsResult {
	
	private int errorCode;
	private Subscriber subscriber;

	public SubsResult() {
	}

	public SubsResult(int errorCode, Subscriber subscriber) {
		this.subscriber = subscriber;
		this.errorCode = errorCode;
	}

	public Subscriber getSubscriber() {
		return this.subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public int getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
