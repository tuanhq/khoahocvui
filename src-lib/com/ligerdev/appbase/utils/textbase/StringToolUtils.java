package com.ligerdev.appbase.utils.textbase;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

/**
 * Replace/remove character in a String
 */
public class StringToolUtils {
	
	static final char NINE = (char) 0x39;
	static final char ZERO = (char) 0x30;
	static final char CH_a = (char) 'a';
	static final char CH_z = (char) 'z';
	static final char CH_A = (char) 'A';
	static final char CH_Z = (char) 'Z';

	static final String uniChars = "àáảãạâầấẩẫậăằắẳẵặèéẻẽẹêềếểễệđ" + "îìíỉĩịòóỏõọôồốổỗộơờớởỡợùúủũụưừứửữựỳýỷỹỵÀÁẢÃẠÂẦẤẨẪẬĂẰẮẲ"
			+ "ẴẶÈÉẺẼẸÊỀẾỂỄỆĐÌÍỈĨỊÒÓỎÕỌÔỒỐỔỖỘƠỜỚỞỠỢÙÚỦŨỤƯỪỨỬỮỰỲÝỶỸỴÂĂĐÔƠƯ";
	static final String noneChars = "aaaaaaaaaaaaaaaaaeeeeeeeeeeediiiiiio" + "oooooooooooooooouuuuuuuuuuuyyyyyAAAAAAAAAAAAAA"
			+ "AAAEEEEEEEEEEEDIIIIIOOOOOOOOOOOOOOOOOUUUUUUUUUUUYYYYYAADOOU";

	public static byte[] hexToByte(String hex) {
		int len = hex.length();
		byte[] value = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			value[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
		}
		return value;
	}
	
	public static Collection parseString(String text, String seperator) {
		Vector vResult = new Vector();
		if (text == null || "".equals(text))
			return vResult;
		String tempStr = text.trim();
		String currentLabel = null;
		int index = tempStr.indexOf(seperator);
		while (index != -1) {
			currentLabel = tempStr.substring(0, index).trim();
			if (!"".equals(currentLabel))
				vResult.addElement(currentLabel);
			tempStr = tempStr.substring(index + 1);
			index = tempStr.indexOf(seperator);
		}
		currentLabel = tempStr.trim();
		if (!"".equals(currentLabel))
			vResult.addElement(currentLabel);
		return vResult;
	}

	public static String encodeMD5(String message) {
		try {
			MessageDigest msgDigest = MessageDigest.getInstance("MD5");
			return new BigInteger(1, msgDigest.digest((message).getBytes("UTF-8"))).toString(16);
		} catch (Exception e) {
			return message;
		}
	}

	public static Collection parseStringEx(String text) {
		Vector vResult = new Vector();
		if (text == null || "".equals(text))
			return vResult;
		String tempStr = text.trim();
		String currLabel = "";
		char currChar = 0;
		for (int i = 0; i < tempStr.length(); i++) {
			currChar = tempStr.charAt(i);
			if ((currChar >= ZERO && currChar <= NINE) || (currChar >= CH_a && currChar <= CH_z) || (currChar >= CH_A && currChar <= CH_Z)) {
				currLabel = currLabel + currChar;
			} else if (currLabel.length() > 0) {
				vResult.add(currLabel);
				currLabel = new String("");
			}
		}
		if (currLabel.length() > 0) {
			vResult.add(currLabel);
		}
		return vResult;
	}
	
	public static String removeSundryChars(String s){
		if(s == null){
			return s;
		}
		String uniChars = "àáảãạâầấẩẫậăằắẳẵặèéẻẽẹêềếểễệđ" 
								+ "ìíỉĩịòóỏõọôồốổỗộơờớởỡợùúủũụưừứửữựỳýỷỹỵÀÁẢÃẠÂẦẤẨẪẬĂẰẮẲ"
								+ "ẴẶÈÉẺẼẸÊỀẾỂỄỆĐÌÍỈĨỊÒÓỎÕỌÔỒỐỔỖỘƠỜỚỞỠỢÙÚỦŨỤƯỪỨỬỮỰỲÝỶỸỴÂĂĐÔƠƯ"
								+ " 0123456789().\\,/;:'[]{}!@#$%*qwertyuioplkjhgfdsazxcvbnmQWERTYUIOPLKJHGFDSAZXCVBNM_+=-\n\t<>?";
		char ch[] = s.toCharArray();
		String result = "";
		for(char c : ch){
			if(uniChars.contains(String.valueOf(c))){
				result += c;
			}
		}
		return result;
	}

	public static boolean isNumberic(String sNumber) {
		if (sNumber == null || "".equals(sNumber)) {
			return false;
		}
		char ch_max = (char) 0x39;
		char ch_min = (char) 0x30;

		for (int i = 0; i < sNumber.length(); i++) {
			char ch = sNumber.charAt(i);
			if ((ch < ch_min) || (ch > ch_max)) {
				return false;
			}
		}
		return true;
	}

	public static String generateHexString(long number, int length) {
		String hex = Long.toHexString(number).toUpperCase();
		int hexLength = hex.length();
		if (hexLength > length) {
			hex = hex.substring(hexLength - length);
		} else if (hexLength < length) {
			for (int i = 0; i < (length - hexLength); i++) {
				hex = "0" + hex;
			}
		}
		return hex;
	}

	public static String getPrefix(String s) {
		String result = null;
		String[] tokens = null;
		String seperator = " ";
		tokens = s.split(seperator);
		if (tokens == null) {
			result = s;
		} else {
			result = tokens[0];
		}
		return result;
	}

	public static Timestamp string2Timestamp(String s) throws ParseException {
		Timestamp result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.F");
		Date date = sdf.parse(s);
		result = new Timestamp(date.getTime());
		return result;
	}

	public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		byte[] sha1hash = new byte[40];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		sha1hash = md.digest();
		return "";
	}

	public static String Collection2String(Collection collect, String delimiter) {
		StringBuilder result = new StringBuilder();
		for (Iterator it = collect.iterator(); it.hasNext();) {
			result.append(delimiter);
			result.append((String) it.next());
		}
		return result.toString().substring(delimiter.length());
	}

	public static String Array2String(String[] array, String delimiter) {
		StringBuilder result = new StringBuilder();
		result.append(array[0]);
		for (int i = 1; i < array.length; i++) {
			result.append(delimiter);
			result.append(array[i]);
		}
		return result.toString();
	}

	public static String Array2StrSQL(String[] array, String delimiter) {
		StringBuilder result = new StringBuilder();
		if (array != null && array.length > 0) {
			result.append("'" + array[0] + "'");
			for (int i = 1; i < array.length; i++) {
				result.append(delimiter);
				result.append("'" + array[i] + "'");
			}
		} else {
			result.append("'VMS'");
		}
		return result.toString();
	}

	public static String ShortString(String s, int num) {
		if (s != null && s.length() > num) {
			s = s.substring(0, num / 2) + "..." + s.substring(s.length() - num / 2 - 3, s.length());
		}
		return s;
	}

	public static String getXMLFormatString(String value) {
		while (value.indexOf("  ") >= 0) {
			value = value.replace("  ", " ");
		}
		return value.replace("&", "&amp;").replace(">", "&gt;").replace("<", "&lt;").replace("\"", "&quot;").replace("“", "&quot;").replace("'", "&apos;").replace("\r\n", " \n");
	}

	public static String getStringXMLFormat(String value) {
		return value.replace("&amp;", "&").replace("&gt;", ">").replace("&lt;", "<").replace("&quot;", "\"").replace("&quot;", "“").replace("&apos;", "'").replace(" \n", "\r\n");
	}

	public static String unicode2ASII(String s) {
		String ret = "";
		int pos;
		for (int i = 0; i < s.length(); i++) {
			pos = uniChars.indexOf(s.charAt(i));
			if (pos >= 0) {
				ret += noneChars.charAt(pos);
			} else {
				ret += s.charAt(i);
			}
		}
		return ret;
	}
}