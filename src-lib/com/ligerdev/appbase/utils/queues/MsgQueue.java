package com.ligerdev.appbase.utils.queues;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
import java.io.Serializable;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class MsgQueue  implements MsgQueueITF, Serializable {
	/**
	 * 
	 */
	
	public int timeDelay = 8000;
	public int maxSize = 300000;
	private static final Logger logger = Log4jLoader.getLogger();
	private static final long serialVersionUID = -754400629294636253L;
	protected MyVector queue = null;
	private Integer synchObj = new Integer(1);
	private String name = null;
	private boolean wait2AddWhenQueueFull = false;
	
	
	public MsgQueue(String name) {
		queue = new MyVector();
		this.name = name;
	}
 
	public MsgQueue(String name, int maxSize) {
		this(name);
		this.maxSize = maxSize;
	}
	
	public Object removeFirst() {
		synchronized (queue) {
			while (queue.isEmpty()) {
				try {
					queue.wait();
				} catch (InterruptedException ex) {
					logger.info("Exception: " + ex.getMessage());
				}
			}
			try {
				Object item = queue.remove(0);
				if(wait2AddWhenQueueFull){
					queue.notify();
				}
				return item;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public void addLast(Object item) {
		synchronized (queue) {
			if(wait2AddWhenQueueFull){
				while (queue.size() > maxSize) {
					try {
						queue.wait();
					} catch (Exception e) {
						logger.info("Exception: " + e.getMessage());
					}
				}
			} else {
				if(queue.size() > maxSize){
					logger.info("Queue " + getName() + " is full, ignore to add");
					return;
				}
			}
			queue.addLast(item);
			queue.notify();
		}
	}

	// @Override
	public void addFirst(Object item) {
		synchronized (queue) {
			if(wait2AddWhenQueueFull){
				while (queue.size() > maxSize) {
					try {
						queue.wait();
					} catch (Exception e) {
						logger.info("Exception: " + e.getMessage());
					}
				}
			} else {
				if(queue.size() > maxSize){
					logger.info("Queue " + getName() + " is full, ignore to add");
					return;
				}
			}
			queue.addFirst(item);
			queue.notify();
		}
	}

	public int size() {
		return queue.size();
	}

	// @Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	// @Override
	public Object get(int index) {
		synchronized (queue) {
			while (queue.isEmpty()) {
				try {
					queue.wait();
				} catch (InterruptedException ex) {
					logger.info("Exception: " + ex.getMessage());
				}
			}
			try {
				Object item = queue.get(index);
				return item;
			} catch (Exception e) {
				return null;
			}
		}
	}

	// @Override
	public Object removeAt(int index) {
		synchronized (queue) {
			while (queue.isEmpty()) {
				try {
					queue.wait();
				} catch (InterruptedException ex) {
					logger.info("Exception: " + ex.getMessage());
				}
			}
			try {
				Object item = queue.remove(index);
				if(wait2AddWhenQueueFull){
					queue.notify();
				}
				return item;
			} catch (Exception e) {
				return null;
			}
		}
	}

	// @Override
	public void queueNotify() {
		synchronized (queue) {
			queue.notify();
		}
	}

	// @Override
	public void queueWait() {
		synchronized (queue) {
			try {
				queue.wait();
			} catch (InterruptedException e) {
			}
		}
	}

	// @Override
	public void callerNotify() {
		synchronized (synchObj) {
			synchObj.notify();
		}
	}

	// @Override
	public void callerWait() {
		synchronized (synchObj) {
			try {
				synchObj.wait();
			} catch (InterruptedException e) {
			}
		}
	}

	// @Override
	public void queueNotifyAll() {
		synchronized (queue) {
			queue.notifyAll();
		}
	}

	// @Override
	public void callerNotifyAll() {
		synchronized (synchObj) {
			synchObj.notifyAll();
		}
	}

	// @Override
	public void clear() {
		synchronized (queue) {
			queue.removeAll();
		}
	}

	// @Override
	public String getName() {
		return name;
	}

	// @Override
	public void setName(String name) {
		this.name = name;
	}
	
	// @Override
	public void setMaxSize(int size) {
		maxSize = size;
	}

	// @Override
	public void setDelayTime(int size) {
		timeDelay = size;
	}

	// @Override
	public void setWait2AddWhenFull(boolean value) {
		wait2AddWhenQueueFull = value;
	}

	// @Override
	public int getLastEmptyTime() {
		int oldSeconds = (int) (System.currentTimeMillis() - queue.getLastTimeQueueEmpty()) / 1000;
		return oldSeconds;
	} 
	
	public static void main(String[] args) {
		MsgQueueITF queue = QueueIOUtils.loadQueue(new MsgQueue("abc")); 
		int count = 0;
		while(true){
			try {
				System.out.println(queue.getLastEmptyTime()); 
				Thread.sleep(1000);
				if(count % 10 == 0){
					queue.addLast(new Object());
					queue.removeFirst();
				}
				count ++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
