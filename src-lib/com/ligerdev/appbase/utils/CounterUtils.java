package com.ligerdev.appbase.utils;

import java.util.concurrent.ConcurrentHashMap;

public class CounterUtils<T> {

	private ConcurrentHashMap<T, Integer> hashmap = null;
	
	public CounterUtils() {
		hashmap = new ConcurrentHashMap<T, Integer>();
	}
	
	public void put(T key, int add){
		Integer value = hashmap.get(key);
		if(value == null){
			value = 0;
		}
		hashmap.put(key, value + add);
	}
	
	public ConcurrentHashMap<T, Integer> get(){
		return hashmap;
	}
}
