
package com.ligerdev.appbase.utils.encrypt;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.textbase.Log4jLoader;


public class Encrypter {
	
    private static String key ;
    private static EncryptKeySpec encrypter ;
    public static final Logger logger = Log4jLoader.getLogger();
    private static MessageDigest msgDigest = null;
    
    static {
        key = "012345678901234567890123456789";
        try {
            encrypter = new EncryptKeySpec(EncryptKeySpec.DES_ENCRYPTION_SCHEME, key);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (Exception ex) {
		}
    } 
	
    public static void main(String ar[]) throws UnsupportedEncodingException{
    	System.out.println(encodeMD5("", "123456"));
    }
    
	public static String encodeMD5(String nameSpace, String message) throws UnsupportedEncodingException {
		message = nameSpace + message; 
		String	tmp = new BigInteger(1, msgDigest.digest((message).getBytes("UTF-8"))).toString(16);
		return tmp.toLowerCase();
	}
	
    public static String encrypt(String str) throws Exception{
        return encrypter.encrypt(str);
    }
    
    public static String decrypt(String str) throws Exception{
        return encrypter.decrypt(str);
    }
}
