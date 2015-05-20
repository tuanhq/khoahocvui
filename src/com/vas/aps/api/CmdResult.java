package com.vas.aps.api;

import java.io.Serializable;
import java.util.ArrayList;

import com.vas.aps.db.orm.MtHis;
import com.vas.aps.db.orm.Subscriber;

public class CmdResult implements Serializable {
	
	private String transid;
	private String msisdn;
	private String errorCode;
	private String errorDesc;
	private ArrayList<MtHis> listMT;
	private Subscriber subscriber;

	public CmdResult() {
		this.listMT = new ArrayList<MtHis>();
	}

	public CmdResult(String transid, String msisdn, String errorCode, String errorDesc) {
		this.transid = transid;
		this.msisdn = msisdn;
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}

	public void addMt(MtHis mt) {
		synchronized (this) {
			if (this.listMT == null) {
				this.listMT = new ArrayList<MtHis>();
			}
			MtHis mtNew = new MtHis(mt.getTransId(), mt.getId(), mt.getMsisdn(), mt.getContent(), 
					mt.getMoId(), mt.getCommand(), mt.getCreatedTime(), mt.getSendResult());
			
			if(mtNew.getContent().startsWith("###")){
				mtNew.setContent(mtNew.getContent().replaceFirst("###", "").trim());
			}
			this.listMT.add(mtNew);
		}
	}

	public void parseErr(String err) {
		if (err.contains("|")) {
			String[] tmp = err.split("\\|");
			this.errorCode = tmp[0];
			this.errorDesc = tmp[1];
		} else {
			this.errorCode = err;
		}
	}

	public String getTransid() {
		return this.transid;
	}

	public void setTransid(String transid) {
		this.transid = transid;
	}

	public String getMsisdn() {
		return this.msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return this.errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public ArrayList<MtHis> getListMT() {
		return this.listMT;
	}

	public void setListMT(ArrayList<MtHis> listMT) {
		this.listMT = listMT;
	}

	public String toString() {
		return this.errorCode + "|" + this.errorDesc;
	}

	public Subscriber getSubscriber() {
		return this.subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}
}
