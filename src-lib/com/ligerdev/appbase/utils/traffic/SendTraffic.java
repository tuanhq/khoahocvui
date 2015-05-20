package com.ligerdev.appbase.utils.traffic;

import java.util.concurrent.ConcurrentHashMap;

public class SendTraffic {

	private long lastSecond = 0;
	private int count = 0;

	private static SendTraffic instance = null;
	private static ConcurrentHashMap<String, SendTraffic> hashInstance;

	public SendTraffic() {
	}

	public synchronized static SendTraffic getInstance() {
		if (instance == null) {
			instance = new SendTraffic();
		}
		return instance;
	}
	
	public synchronized static SendTraffic getInstance(String key) {
		if(hashInstance == null){
			hashInstance = new ConcurrentHashMap<String, SendTraffic>();
		}
		SendTraffic obj = hashInstance.get(key);
		if(obj != null){
			return obj;
		}
		SendTraffic instance = new SendTraffic();
		hashInstance.put(key, instance);
		return instance;
	}

	private int getTimeSleep(int limitCount) {
		synchronized (this) {
			if(limitCount <= 333){
				int flag = 1000 / limitCount;
				long currentTimeMillis = System.currentTimeMillis();
				long currentSecond = currentTimeMillis / flag;
				if (currentSecond == lastSecond) {
					count++;
				} else {
					count = 1;
					lastSecond = currentSecond;
				}
				if (count > 1) {
					// compute sleep time to pass this second
					int timeSleep = flag - (int) (currentTimeMillis % flag);
					return timeSleep;
				}
				return 0;
			} else {
				long currentTimeMillis = System.currentTimeMillis();
				long currentSecond = currentTimeMillis / 100;
				if (currentSecond == lastSecond) {
					count++;
				} else {
					count = 1;
					lastSecond = currentSecond;
				}
				if (count > (limitCount / 10)) {
					int timeSleep = 100 - (int) (currentTimeMillis % 100);
					return timeSleep;
				}
				return 0;
			}
		}
	}

	public void limitTraffic(int limitCount) {
		while (true) {
			try {
				int timeSleep = getTimeSleep(limitCount);
				if (timeSleep == 0) {
					break;
				}
				Thread.sleep(timeSleep);
			} catch (Exception e) {
				// e.printStackTrace();
				break;
			}
		}
	}

	private static int testCount = 0;

	public static void main(String[] args) {
		new Thread() {
			public void run() {
				while (true) {
					System.out.println("===");
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
				}
			};
		}.start();
		for (int i = 0; i < 5; i++) {
			new Thread() {
				public void run() {
					while (true) {
						SendTraffic.getInstance("ABC").limitTraffic(10);
						doPermission();
					}
				};
			}.start();
		}
	}
	
	private static Object lockTest = new Object();
	
	private static void doPermission(){
		try {
			Thread.sleep(40);
		} catch (Exception e) {
		}
		synchronized (lockTest) {
			System.out.println(testCount ++); 
		}
	}
}
