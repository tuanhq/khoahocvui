/**
 * 
 */
package org.thq.schedulerTask;

import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * @author tuanhq
 *
 */
public class SchedulerManage {
	/**
	 * 
	 */
	public SchedulerManage() {
		// TODO Auto-generated constructor stub
	}
  public  void runScheduler(){
  	Calendar today = Calendar.getInstance();
  	today.set(Calendar.HOUR_OF_DAY, 7);
  	today.set(Calendar.MINUTE, 30);
  	today.set(Calendar.SECOND, 0);

  	// every night at 2am you run your task
  	Timer timer = new Timer();
  	//TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
  	timer.schedule(new SendQuestionScheduler(), today.getTime(),TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS) ); // 60*60*24*100 = 8640000ms
  }
}
