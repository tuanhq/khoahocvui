package com.ligerdev.appbase.utils.encrypt;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptKeySpec {

	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	public static final String DES_ENCRYPTION_SCHEME = "DES";
	public static final String DEFAULT_ENCRYPTION_KEY = "This is a fairly long phrase used to encrypt";
	private KeySpec keySpec;
	private SecretKeyFactory keyFactory;
	private Cipher cipher;
	private static final String UNICODE_FORMAT = "UTF8";
	private static BASE64Encoder encoder = new BASE64Encoder();
	private static BASE64Decoder decoder = new BASE64Decoder();

	protected EncryptKeySpec(String encryptionScheme) throws Exception {
		this(encryptionScheme, DEFAULT_ENCRYPTION_KEY);
	}

	protected EncryptKeySpec(String encryptionScheme, String encryptionKey) throws Exception {
		if (encryptionKey == null)
			throw new IllegalArgumentException("encryption key was null");
		if (encryptionKey.trim().length() < 24)
			throw new IllegalArgumentException("encryption key was less than 24 characters");
		byte[] keyAsBytes = encryptionKey.getBytes(UNICODE_FORMAT);
		if (encryptionScheme.equals(DESEDE_ENCRYPTION_SCHEME)) {
			keySpec = new DESedeKeySpec(keyAsBytes);
		} else if (encryptionScheme.equals(DES_ENCRYPTION_SCHEME)) {
			keySpec = new DESKeySpec(keyAsBytes);
		} else {
			throw new IllegalArgumentException("Encryption scheme not supported: " + encryptionScheme);
		}
		keyFactory = SecretKeyFactory.getInstance(encryptionScheme);
		cipher = Cipher.getInstance(encryptionScheme);
	}

	protected String encrypt(String unencryptedString) throws Exception {
		if (unencryptedString == null || unencryptedString.trim().length() == 0)
			throw new IllegalArgumentException("unencrypted string was null or empty");
		try {
			SecretKey key = keyFactory.generateSecret(keySpec);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] cleartext = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] ciphertext = cipher.doFinal(cleartext);
			return encoder.encode(ciphertext);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	protected String decrypt(String encryptedString) throws Exception {
		if (encryptedString == null || encryptedString.trim().length() <= 0)
			throw new IllegalArgumentException("encrypted string was null or empty");
		try {
			SecretKey key = keyFactory.generateSecret(keySpec);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] cleartext = decoder.decodeBuffer(encryptedString);
			byte[] ciphertext = cipher.doFinal(cleartext);
			return bytes2String(ciphertext);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private static String bytes2String(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			stringBuffer.append((char) bytes[i]);
		}
		return stringBuffer.toString();
	}
	
	public static void main(String[] args) throws Exception {
		String ss = Encrypter.encrypt("and");
		String s = Encrypter.decrypt(ss);
		System.out.println(s); 
	}
}
