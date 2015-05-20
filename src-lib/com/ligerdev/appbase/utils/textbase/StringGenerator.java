package com.ligerdev.appbase.utils.textbase;

import java.util.Random;

/**
 * 
 * @author Mr Dung
 */

public class StringGenerator {

	private static final Random random = new Random();
	private static final char[] characters = new char[36];
	private static final char[] digits = new char[10];

	static {
		for (int idx = 0; idx < 10; ++idx) {
			characters[idx] = (char) ('0' + idx);
			digits[idx] = (char) ('0' + idx);
		}
		for (int idx = 10; idx < 36; ++idx) {
			characters[idx] = (char) ('a' + idx - 10);
		}
	}

	public static String randomCharacters(int length) {
		char[] buf = new char[length];
		for (int idx = 0; idx < length; ++idx) {
			buf[idx] = characters[random.nextInt(characters.length)];
		}
		return new String(buf).toUpperCase();
	}

	public static void main(String[] args) {

	}

	public static String randomDigits(int length) {
		if (length <= 0) {
			return "";
		}
		char[] buf = new char[length];
		for (int idx = 0; idx < length; ++idx)
			buf[idx] = digits[random.nextInt(digits.length)];
		return new String(buf);
	}
}
