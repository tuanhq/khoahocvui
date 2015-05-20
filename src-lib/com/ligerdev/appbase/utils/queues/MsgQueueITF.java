package com.ligerdev.appbase.utils.queues;

public interface  MsgQueueITF {

	public Object removeFirst();

	public void clear();
	
	public void addLast(Object item);

	public void addFirst(Object item);

	public int size();

	public Object get(int index);

	public Object removeAt(int index);

	public boolean isEmpty();

	public void queueNotify(); 

	public void queueNotifyAll();

	public void queueWait();

	public void callerNotify();

	public void callerNotifyAll();

	public void callerWait();
	
	public String getName();
	
	public void setName(String name);
	
	public void setMaxSize(int size);
	
	public void setDelayTime(int size);
	
	public void setWait2AddWhenFull(boolean value);
	
	public int getLastEmptyTime();
}
