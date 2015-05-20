package com.ligerdev.appbase.utils.emails;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.cache.CacheEH;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class EmailSenderLV2 {

	private static EMailSender sender = EMailSender.getInstance();
	private static CacheEH cache = CacheEH.getInstance("CACHE_BEAN");
	private static Logger logger = Log4jLoader.getLogger();
	private static Object synch = new Object();
	private static ExecutorService executor = Executors.newCachedThreadPool();

	public static boolean ENABLE_EMAIL = false;
	public static String PASSWORD_EMAIL = null;
	public static ArrayList<String> LIST_EMAIL_CLIENT;
	public static String ADDRESS_EMAIL = null;

	public static void sendEMail(final String subject, final String contentTmp) {
		try {
			final String traceLog = "\nSend from: " + Thread.currentThread().getStackTrace()[2].toString() + "\n\nBrgds!\nDucNV";
			executor.execute(new Runnable() {
				// @Override
				public void run() {
					try {
						if (BaseUtils.isBlank(ADDRESS_EMAIL) || BaseUtils.isBlank(PASSWORD_EMAIL) || !ENABLE_EMAIL || LIST_EMAIL_CLIENT == null || LIST_EMAIL_CLIENT.size() == 0) {
							logger.info("Blank accounts or disable email alert => return");
							return;
						}
						final String content = contentTmp + traceLog;
						String key = BaseUtils.getChecksumValue(subject + "-" + content);
						synchronized (synch) {
							Object o = cache.get(key);
							if (o != null) {
								return;
							}
							cache.put(key, true);
						}
						sender.sendMail(ADDRESS_EMAIL, PASSWORD_EMAIL, LIST_EMAIL_CLIENT, subject, content, false);
					} catch (Throwable e) {
						logger.info("Exception: " + e.getMessage());
					}
				}
			});
		} catch (Throwable e) {
			logger.info("Exception: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		cache.put("abc", true);
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
		System.out.println(cache.get("abc"));
	}
}
