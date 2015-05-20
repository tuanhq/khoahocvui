package com.ligerdev.appbase.utils.queues;

import java.io.Serializable;

public class ItemDelay implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2986212043735342469L;
	private long timeCreated = 0L;
	private Object obj = null;

	public ItemDelay(long timeCreated, Object obj) {
		this.timeCreated = timeCreated;
		this.obj = obj;
	}

	public long getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(long timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.timeCreated = System.currentTimeMillis();
		this.obj = obj;
	}
}
