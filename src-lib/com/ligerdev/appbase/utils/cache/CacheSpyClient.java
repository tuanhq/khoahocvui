package com.ligerdev.appbase.utils.cache;

import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

public class CacheSpyClient {

	private String NAMESPACE;
	private MemcachedClient client;
	
	public CacheSpyClient(String nameSpace, MemcachedClient client) {
		this.client = client;
		this.NAMESPACE = nameSpace;
	}
	
	public void set(String key, int ttl, Object o) {
		synchronized (this) {
			client.set(NAMESPACE + key, ttl, o);
		}
	}
	
	public void add(String key, int ttl, Object o) {
		synchronized (this) {
			client.add(NAMESPACE + key, ttl, o);
		}
	}
	
	public Object get(String key) {
		synchronized (this) {
			Object o = client.get(NAMESPACE + key);
			return o;
		}
	}
	
	public Object getAndTouch(String key, int ttl) {
		synchronized (this) {
			CASValue o = client.getAndTouch(NAMESPACE + key, ttl);
			if(o != null){
				Object obj = o.getValue();
				return obj;
			}
			return null;
		}
	}
	
	public Object touch(String key, int ttl) {
		synchronized (this) {
			Object o = client.touch(NAMESPACE + key, ttl);
			return o;
		}
	}

	public Object delete(String key) {
		synchronized (this) {
			return client.delete(NAMESPACE + key);
		}
	}
	
	public Object flush() {
		synchronized (this) {
			return client.flush();
		}
	}
}
