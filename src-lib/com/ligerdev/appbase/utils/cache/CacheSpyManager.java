package com.ligerdev.appbase.utils.cache;

import java.net.InetSocketAddress;
import java.util.Random;

import net.spy.memcached.MemcachedClient;

public class CacheSpyManager {

	private String NAMESPACE = "MSP_";
	private CacheSpyClient[] cacheClient;
	private static CacheSpyManager instance;
	private static String DEFAULT_IP = "192.168.6.199";
	private static int DEFAULT_PORT = 11211;

	public synchronized void init(String ip, int port) {
		if (instance == null) {
			instance = new CacheSpyManager(ip, port);
		}
	}

	private CacheSpyManager(String ip, int port) {
		cacheClient = new CacheSpyClient[1];
		for (int i = 0; i < cacheClient.length; i++) {
			try {
				MemcachedClient client = new MemcachedClient(new InetSocketAddress(ip, port));
				cacheClient[i] = new CacheSpyClient(NAMESPACE, client);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized static CacheSpyManager getInstance() {
		if (instance == null) {
			instance = new CacheSpyManager(DEFAULT_IP, DEFAULT_PORT);
		}
		return instance;
	}

	public void set(String key, int ttl, final Object o) {
		getCache().set(NAMESPACE + key, ttl, o);
	}

	public Object get(String key) {
		Object o = getCache().get(NAMESPACE + key);
		return o;
	}

	public void add(String key, int ttl, final Object o) {
		getCache().add(NAMESPACE + key, ttl, o);
	}

	public void touch(String key, int ttl) {
		getCache().touch(NAMESPACE + key, ttl);
	}

	public Object getAndTouch(String key, int ttl) {
		Object o = getCache().getAndTouch(NAMESPACE + key, ttl);
		return o;
	}

	public Object delete(String key) {
		return getCache().delete(NAMESPACE + key);
	}

	public Object flush() {
		return getCache().flush();
	}

	private Random r = new Random();

	private CacheSpyClient getCache() {
		try {
			int i = r.nextInt(cacheClient.length);
			CacheSpyClient c = cacheClient[i];
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) { }
}
