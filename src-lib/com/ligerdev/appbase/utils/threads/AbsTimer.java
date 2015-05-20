package com.ligerdev.appbase.utils.threads;

import java.util.concurrent.atomic.AtomicInteger;

import com.ligerdev.appbase.utils.BaseUtils;

public abstract class AbsTimer {
	
	private static AtomicInteger thrIdx = new AtomicInteger(1);
	private int timeSleep;
	private long counter = 1;
	
	// public static void initStatic(){};
	
	public AbsTimer(int timeSleep) {
		execute(counter);
		this.timeSleep = timeSleep;
		
		new Thread(){
			public void run() {
				synchronized(thrIdx){
					setName("Timer-" + this.getClass().getName() + "-" + thrIdx.getAndIncrement());
				}
				while(AbsApplication.CONTINUES){
					if(counter >= Long.MAX_VALUE){
						counter = 0;
					}
					counter ++;
					BaseUtils.sleep(AbsTimer.this.timeSleep);
					execute(counter);
				}
			};
		}.start();
	}
	
	public abstract void execute(long counter);
}
