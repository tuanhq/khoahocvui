package com.ligerdev.appbase.utils.queues;

import java.io.Serializable;
import java.util.Vector;

public class MyVector implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5273460392182455788L;
	private Vector<Object> v = null;
	private long lastTimeQueueEmpty = System.currentTimeMillis() - 1000 * 60 * 24;

	public MyVector() {
		v = new Vector<Object>();
	}

	public Object remove(int index) {
		synchronized (this) {
			try {
				Object obj = v.remove(index);
				if(v.isEmpty()){
					lastTimeQueueEmpty = System.currentTimeMillis();
				}
				return obj;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public void addLast(Object obj) {
		synchronized (this) {
			try {
				v.add(obj);
			} catch (Exception e) {
			}
		}
	}

	public void addFirst(Object obj) {
		synchronized (this) {
			try {
				v.add(0, obj);
			} catch (Exception e) {
			}
		}
	}

	public int size() {
		synchronized (this) {
			return v.size();
		}
	}

	public boolean isEmpty() {
		synchronized (this) {
			return v.isEmpty();
		}
	}

	public void removeAll() {
		synchronized (this) {
			v.removeAllElements();
			lastTimeQueueEmpty = System.currentTimeMillis();
		}
	}

	public Object get(int index) {
		synchronized (this) {
			try {
				return v.get(index);
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	public long getLastTimeQueueEmpty() {
		return lastTimeQueueEmpty;
	}
}
