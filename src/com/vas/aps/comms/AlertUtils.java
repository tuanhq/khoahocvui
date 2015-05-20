package com.vas.aps.comms;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.cache.CacheSyncFileIM;
import com.ligerdev.appbase.utils.queues.MsgQueueITF;

public class AlertUtils {
	
	private static Logger logger = Logger.getLogger(AlertUtils.class);
	private static CacheSyncFileIM cache = CacheSyncFileIM.getInstance("AlertUtils", 10000);
	private static MsgQueueITF mtQueue = null;
	private static Object synch = new Object();

	public static MsgQueueITF getMtQueue() {
		return mtQueue;
	}

	public static void setMtQueue(MsgQueueITF mtQueue) {
		AlertUtils.mtQueue = mtQueue;
	}

	public static void alert(String moduleName, String msg) {
		try {
			msg = "[" + moduleName.toUpperCase() + " - WARNING]\n" + msg;
			msg = msg + "\nSent from: " + java.lang.Thread.currentThread().getStackTrace()[2].toString();
			String key = BaseUtils.getCRC32Value(msg) + "";
			synchronized (synch) {
				Object o = cache.getObject(key);
				if (o != null) {
					return;
				}
				cache.put(key, Boolean.valueOf(true));
			}
			/*for(String msisdn : XmlConfigs.LIST_ALERT_MSISDN) {
				msisdn = MUtils.formatMsisdn(msisdn, "84");
				MT mt = new MT(msg, msisdn, "WARNING", 0, "inv");
				mt.set_type(MT.TYPE_IGNORE_INSERT);
				mtQueue.addFirst(mt);
			}*/
		} catch (Throwable e) {
			logger.info("Exception: " + e.getMessage() + ", msgAlert = " + msg);
		}
	}
}
 