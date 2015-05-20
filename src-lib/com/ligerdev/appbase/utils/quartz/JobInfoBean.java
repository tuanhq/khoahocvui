package com.ligerdev.appbase.utils.quartz;

import java.io.Serializable;
import java.util.ArrayList;

public class JobInfoBean implements Serializable {

	private ArrayList<String> listExp;
	private Class clazz;
	private JobValues[] values;

	public JobInfoBean() {
		// TODO Auto-generated constructor stub
	}

	public JobInfoBean(ArrayList<String> listExp, Class clazz, JobValues[] values) {
		super();
		this.listExp = listExp;
		this.clazz = clazz;
		this.values = values;
	}

	public ArrayList<String> getListExp() {
		return listExp;
	}

	public void setListExp(ArrayList<String> listExp) {
		this.listExp = listExp;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public JobValues[] getValues() {
		return values;
	}

	public void setValues(JobValues[] values) {
		this.values = values;
	}
}
