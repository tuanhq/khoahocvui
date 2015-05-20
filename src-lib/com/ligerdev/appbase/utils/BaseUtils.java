package com.ligerdev.appbase.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.textbase.Log4jLoader;


public class BaseUtils {

	private static Logger logger = Log4jLoader.getLogger();
	
	private BaseUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static <T>void sortCollection(List<T> list, boolean isAsc, String sortBy) {
		if (list == null || list.size() <= 1 || isBlank(sortBy)) {
			return;
		}
		int size = list.size();
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				T objI = list.get(i);
				T objJ = list.get(j);
				if (objI != null && compare(objI, sortBy, objJ, isAsc) > 0) {
					T temp = objI;
					list.set(i, objJ);
					list.set(j, temp);
				}
			}
		}
	}
	
	public static <T>int compare(T o1, String sortBy, T o2, boolean isAsc) {
		try {
			String getter = "get"
					+ String.valueOf(sortBy.charAt(0)).toUpperCase()
					+ sortBy.substring(1);
			Method method = o1.getClass().getMethod(getter);
			Object result1 = method.invoke(o1);
			Object result2 = method.invoke(o2);
			if (result1 == null && result2 == null) {
				return 0;
			}
			Integer finalResult = 0;
			if (result1 == null) {
				finalResult = -1;
			}
			else if (result2 == null) {
				finalResult = 1;
			}
			else if (result1.getClass() != result2.getClass()) {
				throw new Exception();
			}  else {
				if(result1 instanceof java.sql.Date){
					java.sql.Date d1 = (java.sql.Date) result1;
					java.sql.Date d2 = (java.sql.Date) result2;
					finalResult = d1.compareTo(d2);
				} else {
					Method compareToMethod = result1.getClass().getMethod("compareTo", result2.getClass());
					finalResult = (Integer) compareToMethod.invoke(result1, result2);
				}
			}
			if(isAsc){
				return -finalResult;
			}
			return finalResult;
		} catch (Exception e) {
			// logger.info("Exception: " + e.getMessage());
		}
		return 0;
	}
	
	public static void sleep(int time){
		try {
			if(time <= 0){
				return;
			}
			Thread.sleep(time);
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		}
	}
	
	private static String myDir = "";
	
	public static void setMyDir(String initParameter) {
		// System.setProperty("my.dir", initParameter);
		myDir = initParameter;
	}
	
	public static String getMyDir(){
		// String myDir = System.getProperty("my.dir");
		// return myDir == null ? "" : myDir;
		return myDir;
	}
	
	public static ArrayList<LDEntry> getListEntry(Set<Entry<Object, Object>> set, boolean println, boolean log){
		ArrayList<LDEntry> listRs = new ArrayList<LDEntry>();
		Iterator<Entry<Object, Object>> iter = set.iterator();
		while(iter.hasNext()){
			Entry<Object, Object> entry = iter.next();
			LDEntry ldEntry = new LDEntry(entry.getKey(), entry.getValue());
			listRs.add(ldEntry);
			if(println){
				System.out.println(ldEntry.toString());
			}
			if(log){
				logger.info(ldEntry.toString());
			}
		}
		return listRs;
	}
	
	public static <K, V> void retrieveMap(Map<K, V> map, IRetrieveMap<K, V> retrieveMap){
		Set<Entry<K, V>> set = map.entrySet();
		Iterator<Entry<K, V>> iter = set.iterator();
		while(iter.hasNext()){
			Entry<K, V> e = iter.next();
			retrieveMap.found(e.getKey(), e.getValue());
		}
	}
	
	public static <K> void retrieveSet(Set<K> set, IRetrieveMap<Integer, K> retrieveMap){
		Iterator<K> iter = set.iterator();
		int count = 0;
		while(iter.hasNext()){
			K e = iter.next();
			retrieveMap.found(count, e);
			count ++;
		}
	}
	
	public static int getDateDiff(Date d1, Date d2) {
		long l1 = truncDate(d1).getTime();
		long l2 = truncDate(d2).getTime();
		int dayMs = 1000 * 60 * 60 * 24;
		int dateDiff = (int) (Math.abs(l2 - l1) / dayMs);
		return dateDiff;
	}
	
	public static Date truncDate(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		return cal.getTime();
	}

	public static String getObjectInfo(String transid, Object obj) {
		if (obj == null) {
			return null;
		}
		return getObjectInfo(transid, obj, false);
	}
	
	public static boolean deleteDir(File dir) {
	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }
	    return dir.delete();  
	}
	
	public static byte[] combineArray(byte[] one, byte[] two) {
		byte[] combined = new byte[one.length + two.length];
		System.arraycopy(one, 0, combined, 0, one.length);
		System.arraycopy(two, 0, combined, one.length, two.length);
		return combined;
	}

	public static Date parseTime(String pt, String date) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(pt);
			return df.parse(date);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String formatTime(String pt, Object date) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(pt);
			return df.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	private static String getObjectInfo(String transid, Object obj, boolean print) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("\n-------- transid = " + transid + ", class = " + obj.getClass().getName() + " ---------");
			sb.append("\n1: ToString: " + String.valueOf(obj));
			tagObjectInfo0(obj, sb, 0);
			sb.append("\n-------------------------- transid = " + transid + ", end of obj info -------------------------");
			String str = sb.toString();
			if (print) {
				logger.info(str);
			}
			return str;
		} catch (Exception e) {
			return "Can not getInfo Obj: " + obj.getClass().getName();
		}
	}

	public static <T>boolean isNormalType(Class<T> c) {
		if (c.isPrimitive()) {
			return true;
		} else if (c == Byte.class || c == Short.class || c == Integer.class || c == Long.class 
				|| c == Float.class || c == Double.class || c == Boolean.class || c == Character.class 
				|| c == String.class || c == byte.class || c == short.class || c == int.class || c == long.class
				|| c == float.class || c == double.class || c == boolean.class || c == char.class || Date.class == c) {
			return true;
		} else {
			return false;
		}
	}

	private static void tagObjectInfo0(Object obj, StringBuilder sb, int level) {
		String tab = "";
		for (int i = 0; i < level; i++) {
			tab += "\t";
		}
		if (level >= 10) {
			return;
		}
		Method[] methods = obj.getClass().getMethods();
		for (int i = 0; methods != null && i < methods.length; i++) {
			try {
				Method m = methods[i];
				if (!m.getName().startsWith("get")) {
					continue;
				}
				if (m.getParameterTypes() != null && m.getParameterTypes().length > 0) {
					continue;
				}
				Object valueObj = m.invoke(obj);
				if (valueObj == null) {
					sb.append("\n" + (tab) + (level + 1) + ": " + m.getName() + "=" + null);
				} else if (valueObj.getClass().isArray()) {
					String value = ArrayUtils.toString(valueObj);
					sb.append("\n" + (tab) + (level + 1) + ": " + m.getName() + "= (array) " + value);
				} else if (isNormalType(valueObj.getClass())) {
					String value = String.valueOf(valueObj);
					sb.append("\n" + (tab) + (level + 1) + ": " + m.getName() + "=" + value);
				} else if (isCollection(valueObj)) {
					sb.append("\n" + (tab) + (level + 1) + ": " + m.getName() + "=" + valueObj.getClass().getName());
					tagCollectionInfo(valueObj, sb, level + 1);
				} else if (!valueObj.getClass().equals(Class.class)) {
					sb.append("\n" + (tab) + (level + 1) + ": " + m.getName() + "=" + valueObj.getClass().getName());
					tagObjectInfo0(valueObj, sb, level + 1);
				}
			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
	}

	private static void tagCollectionInfo(Object obj, StringBuilder sb, int level) {
		Collection col = (Collection) obj;
		if(col.size() > 0){
			String tab = "";
			for (int i = 0; i < level; i++) {
				tab += "\t";
			}
			for (Object objTmp : col) {
				if(objTmp == null){
					sb.append("\n" + (tab) + ": " + String.valueOf(objTmp));
				} else if(isCollection(objTmp)){
					sb.append("\n" + (tab) + "----- " + objTmp.getClass().getName() + " -----"); 
					tagCollectionInfo(objTmp, sb, level + 1);
				} else if (isNormalType(objTmp.getClass())){
					sb.append("\n" + (tab) + (level + 1) + ": " + String.valueOf(objTmp) + " (" + objTmp.getClass().getName() + ")");  
				} else {
					sb.append("\n" + (tab) + "----- " + objTmp.getClass().getName() + " -----"); 
					tagObjectInfo0(objTmp, sb, level);
				}
			}
		}
	}

	private static boolean isCollection(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass() == List.class) {
			return true;
		}
		try {
			return (obj.getClass().newInstance() instanceof Collection);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static Date addTime(Date date, int type, int value){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(type, value);
		return cal.getTime();
	}
	
	public static String formatMsisdn(String msisdn, String countryCode, String format){
		if(msisdn == null){
			return msisdn;
		}
		msisdn = msisdn.replace(" ", "");
		if(msisdn.startsWith("+" + countryCode)){
			msisdn = msisdn.replaceFirst("+" + countryCode, "");
		} else if(msisdn.startsWith(countryCode)){
			msisdn = msisdn.replaceFirst(countryCode, "");
		} else if(msisdn.startsWith("0")){
			msisdn = msisdn.replaceFirst("0", "");
		}
		msisdn = format + msisdn;
		return msisdn;
	}
	
	public static String parseTime(String format, Object obj){
		try {
			SimpleDateFormat df = new SimpleDateFormat(format);
			return df.format(obj);
			
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage() 
					+ ", format = " + format + ", objValue = " + String.valueOf(obj)); 
		}
		return null;
	}
	
	public static String comvertTime(String format1, String dateStr, String format2){
		try {
			SimpleDateFormat df1 = new SimpleDateFormat(format1);
			SimpleDateFormat df2 = new SimpleDateFormat(format2);
			Date date =  df1.parse(dateStr);
			return df2.format(date);
			
		} catch (ParseException e) {
			logger.info("Exception: " + e.getMessage() 
					+ ", format1 = " + format1 + ", dateStr = " + dateStr + ", format2 = " + format2); 
		}
		return dateStr;
	}
	
	public static String getDurations(long l2, long l1){
		long ms = l2 - l1;
		if(ms < 0){
			ms = -ms;
		}
		long s = ms/1000;
		String temp = ms + "ms/" + s + "s";
		return temp;
	}
	
	public static void main(String[] args) {
		long l1 = System.currentTimeMillis();
		long l2 = l1 + 60001;
		System.out.println(getDurations(l2, l1)); 
	}
	
	public static long getCRC32Value(String xmlContent) {
		return getChecksumValue(new java.util.zip.CRC32(), xmlContent);
	}
	
	private static long getChecksumValue(java.util.zip.Checksum checksum, String contentReceiver) {
		byte[] bytes = contentReceiver.getBytes(Charset.forName("UTF8"));
		int len = bytes.length;
		checksum.update(bytes, 0, len);
		return checksum.getValue();
	}
	
	public static String getOneLineString(String str) {
		if (str == null) {
			return null;
		}
		str = str.replace("|", "::");
		str = str.replace("\r\n", "\\r\\n");
		str = str.replace("\n\r", "\\n\\r");
		str = str.replace("\r", "\\r");
		str = str.replace("\n", "\\n");
		return str;
	}
	
	public static String nomalizeString(String s){
		if(s == null){
			return s;
		}
		s = s.trim();
		s = s.replaceAll("\t", " ");
		s = s.replaceAll(" +", " ");
		return s;
	}
	
	public static String getChecksumValue(String contentReceiver) {
		try {
			byte[] bytes = contentReceiver.getBytes(Charset.forName("UTF8"));
			int len = bytes.length;
			java.util.zip.Checksum checksum = new java.util.zip.CRC32();
			checksum.update(bytes, 0, len);
			return checksum.getValue() + "";
		} catch (Throwable e) {
			return contentReceiver;
		}
	}
	
	public static String trimCp(String str){
		if(str.startsWith(";")){
			str = str.replaceFirst(";", "");
		}
		if(str.endsWith(";")){
			str = str.substring(0, str.length() -1);
		}
		return str;
	}
	
	public static String trimP(String str){
		if(str.startsWith(",")){
			str = str.replaceFirst(",", "");
		}
		if(str.endsWith(",")){
			str = str.substring(0, str.length() -1);
		}
		return str;
	}
	
	public static Timestamp convert2Timestamp(Date date){
		try {
			if(date == null){
				return null;
			}
			return new Timestamp(date.getTime());
		} catch (Exception e) {
			return null;
		}
	}
	
	public static int parseMonthToInt(){
		try {
			SimpleDateFormat dateFormatShort = new SimpleDateFormat("yyyyMM");
			return Integer.parseInt(dateFormatShort.format(System.currentTimeMillis()));
		} catch (Exception e) {
			return 0;
		}
	} 
	
	public static void writeFile(String fileName, String content, boolean append){
		FileOutputStream fout = null;
		try {
			fout = new java.io.FileOutputStream(fileName, append); 
			fout.write(content.getBytes());
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		} finally {
			try {
				fout.close();
			} catch (IOException e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
	}
	
	public static String getStackTraceException(Throwable e) {
		String all = "Stack trace of exception: ";
		try {
			all = e.getMessage() + "\n";
			StackTraceElement stackTrace[] = e.getStackTrace();
			int count = stackTrace.length;
			for (int i = count - 1; i >= 0; i--) {
				all += stackTrace[i].toString() + "\n";
			}
		} catch (Exception e2) {
			all += e2.getMessage();
		}
		return all;
	}
	
	public static Date getLaterDate(Date d1, Date d2){
		if(d1 == null && d2 == null){
			return null;
		}
		if(d1 == null){
			return d2;
		}
		if(d2 == null){
			return d1;
		}
		boolean temp = d1.compareTo(d2) > 0;
		if(temp){
			return d1;
		}
		return d2;
	}
	
	public static int getEarlierTimePosition(Date timeAddSong1, Date timeAddSong2) {
		if(timeAddSong1 == null){
			return 1;
		} else if(timeAddSong2 == null){
			return 2;
		}
		boolean temp = timeAddSong1.compareTo(timeAddSong2) > 0;
		if(temp){
			return 2;
		}
		return 1;
	}
	
	public static Integer parseInt(String value, Integer defauzt) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defauzt;
		}
	}
	
	public static Long parseLong(String value, Long defauzt) {
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			return defauzt;
		}
	}
	
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean isBlank(String str) {
		return str == null || "".equals(str.trim());
	}
	
	public static Object deserializeObject(byte[] data) {
		ByteArrayInputStream in = null;
		ObjectInputStream is = null;
		try {
			in = new ByteArrayInputStream(data);
			is = new ObjectInputStream(in);
			return is.readObject();
		} catch (Exception e) {
			 logger.info("Can not deserializeObject, byteSize = " + (data == null ? null : data.length));
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	public static byte[] serializeObject(Object obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			byte[] yourBytes = bos.toByteArray();
			return yourBytes;
		} catch (Exception e) {
			if(obj == null){
				logger.info("Can not serializeObject: " + e.getMessage());
			} else {
				logger.info("Can not serializeObject: " + e.getMessage() + "; objName = " + obj.getClass().getName());
			}
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
			}
			try {
				bos.close();
			} catch (IOException ex) {
			}
		}
		return null;
	}
	
	public static void writeObjectToFile2(String fileName, Object obj) {
		byte []b = serializeObject(obj);
		if(b != null && b.length > 0){
			writeBytesToFile(b, fileName);
		} else {
			try {
				FileOutputStream fout = new java.io.FileOutputStream(fileName, false); 
				// append nothing to clear
				fout.close();
			} catch (Exception e2) {
				logger.info("Exception: " + e2.getMessage());
			}
		}
	}
	
	public static Hashtable<String, String> parseQueryString(String s){
		if(s == null){
			return null; 
		}
		Hashtable<String, String> list = new Hashtable<String, String>();
		String []tmp = s.split("&");
		for(int i = 0; tmp != null && i < tmp.length; i ++){
			if(!tmp[i].contains("=")){
				list.put(tmp[i], ""); 
			} else {
				String tmp2[] = tmp[i].split("=");
				list.put(tmp2[0], tmp2[1]); 
			}
		}
		return list;
	}
	
	public static Object readObjectFromFile2(String fileName, boolean clearAfterRead){
		byte[] b = readBytesFromFile(new File(fileName));
		Object obj = deserializeObject(b);
		if(obj != null && clearAfterRead){
			try {
				FileOutputStream fout = new java.io.FileOutputStream(fileName, false); // append nothing to clear
				fout.close();
			} catch (Exception e2) {
				logger.info("Exception: " + e2.getMessage());
			}
		}
		return obj;
	}
	
	public static String readInputStream(InputStream is){
		InputStreamReader rd = null;
		BufferedReader in = null;
		try {
			rd = new InputStreamReader(is, "UTF-8");
			in = new BufferedReader(rd);
			String line;
			String all = "";
			while ((line = in.readLine()) != null) {
				all += "\n" + line;
			}
			all = all.replaceFirst("\n", "");
			return all;
		} catch (Exception e) {
		} finally {
			if(in != null){
				try {
					in.close();
				} catch (Exception e2) {
				}
			}
			if(rd != null){
				try {
					rd.close();
				} catch (Exception e2) {
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (Exception e2) {
				}
			}
		}
		return null;
	}
	
	
	public static String readFile(String name) {
		FileInputStream is =  null;
		InputStreamReader rd = null;
		BufferedReader in = null;
		try {
			is = new FileInputStream(name);
			rd = new InputStreamReader(is, "UTF-8");
			in = new BufferedReader(rd);
			
			String line;
			String all = "";
			while ((line = in.readLine()) != null) {
				all += "\n" + line;
			}
			all = all.replaceFirst("\n", "");
			return all;
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
		} finally {
			try {
				in.close();
			} catch (Exception e2) {
			}
			try {
				rd.close();
			} catch (Exception e2) {
			}
			try {
				is.close();
			} catch (Exception e2) {
			}
		}
		return null;
	}

	public void writeFile2(String fileName, String text, boolean append) {
		try {
			FileWriter fw = new FileWriter(fileName, append);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			out.print(text);
			out.close();
			bw.close();
			fw.close();
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		}
	}
	
	public static String cutText(String s, int limit){
		if(s == null){
			return s;
		}
		if(s.length() > limit){
			s = s.substring(0, limit - 3) + "...";
			return s;
		}
		return s;
	}

	public static byte[] getByteArray(InputStream is) {
		ByteArrayOutputStream buffer = null;
		try {
			buffer = new ByteArrayOutputStream();
			int nRead = -1;
			byte[] data = new byte[1024];
			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
			return buffer.toByteArray();
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		} finally {
			try {
				buffer.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				is.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}

	public static Hashtable<String, String> analyzeCdr(String line){
		if(BaseUtils.isBlank(line)){
			return null;
		}
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		try {
			String tmp[] = line.split("\\|");
			for(int i = 0; i < tmp.length; i ++){
				if(!tmp[i].contains("=")){
					hashtable.put("SPC" + i, tmp[i]);
				} else {
					String tmp2[] = tmp[i].split("=");
					hashtable.put(tmp2[0].trim(), tmp2[1].trim());
				}
			}
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
		}
		return hashtable;
	}
	
	public static byte[] getFileBytes(File file) {
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[1024];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
			while ((read = ios.read(buffer)) != -1)
				ous.write(buffer, 0, read);
		} catch (IOException e) {
			logger.info("Exception: " + e.getMessage());
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException e) {
				logger.info("Exception: " + e.getMessage());
			}
			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
		return ous.toByteArray();
	}

	public byte[] getByte(String urlToRead) {
		HttpURLConnection conn = null;
		InputStream is = null;
		ByteArrayOutputStream buffer = null;
		try {
			URL url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			is = conn.getInputStream();
			buffer = new ByteArrayOutputStream();
			int nRead = -1;
			byte[] data = new byte[1024];
			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
			return buffer.toByteArray();
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		} finally {
			try {
				if (buffer != null)
					buffer.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				if (is != null)
					is.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			try {
				if (conn != null)
					conn.disconnect();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}

	public static void writeBytesToFile(byte[] b, String fileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			fos.write(b);
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e2) {
			}
		}
	}

	public static byte[] readBytesFromFile(File file) {
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[1024];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
			while ((read = ios.read(buffer)) != -1)
				ous.write(buffer, 0, read);
		} catch (IOException e) {
			// e.printStackTrace();
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException e) {
				logger.info("Exception: " + e.getMessage());
			}
			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
		return ous.toByteArray();
	}
	
	public static void translateObject(Object src, Object dest){
		Method []methods = src.getClass().getMethods();
		for(int i = 0; methods != null && i < methods.length; i ++){
			try {
				Method getMethodSrc = methods[i];
				if(!getMethodSrc.getName().startsWith("get")){
					continue;
				}
				if(getMethodSrc.getParameterTypes() != null && getMethodSrc.getParameterTypes().length > 0){
					continue;
				}
				if(getMethodSrc.getReturnType() == Class.class){
					continue;
				}
				Object valueGetMethodSrc = getMethodSrc.invoke(src);
				String setMethodName = getMethodSrc.getName().replaceFirst("get", "set");
				try {
					if(valueGetMethodSrc instanceof Collection){
						throw new NoSuchMethodException("");
					}
					Method setMethodDest = dest.getClass().getMethod(setMethodName, valueGetMethodSrc.getClass());
					setMethodDest.invoke(dest, valueGetMethodSrc);
				} catch (NoSuchMethodException e) {
					try {
						Class c = getPrimitiveClass(valueGetMethodSrc.getClass());
						if(c == null){
							throw new NoSuchMethodException("");
						}
						Method setMethod = dest.getClass().getMethod(setMethodName, c); 
						setMethod.invoke(dest, valueGetMethodSrc);
					} catch (NoSuchMethodException e2) {
						try {
							Method methodTmp = dest.getClass().getMethod(getMethodSrc.getName());
							Class c = methodTmp.getReturnType(); 
							if(c == List.class){
								c = ArrayList.class;
							}
							Object tmp = null;
							boolean isCollection = false;
							try {
								if(c.newInstance() instanceof Collection){
									isCollection = true;
								}
							} catch (Exception e3) {
							}
							if(!isCollection){
								tmp = c.newInstance();
								translateObject(valueGetMethodSrc, tmp);
								Method setMethod = dest.getClass().getMethod(setMethodName, c); 
								setMethod.invoke(dest, tmp);
							} else {
								tmp = c.newInstance();
								ParameterizedType pType = (ParameterizedType) dest.getClass().getMethod(getMethodSrc.getName()).getGenericReturnType();
								Class<?> clazz = (Class<?>) pType.getActualTypeArguments()[0];
								translateCollection(valueGetMethodSrc, tmp, clazz); 
								Method setMethod = dest.getClass().getMethod(setMethodName, methodTmp.getReturnType()); 
								setMethod.invoke(dest, tmp);
							}
						} catch (NoSuchMethodException e3) {
							// ignore
						}
					} catch (Exception e3) {
						 throw e3;
					}
				}
			} catch (NullPointerException e) {
				// ignore
			} catch (Exception e) {
				 e.printStackTrace();
			}
		}
	}
	
	public static void translateCollection(Object srcObj, Object destObj, Class destClass){
		Collection src = (Collection) srcObj;
		Collection dest = (Collection) destObj;
		dest.clear();
		for(Object obj : src){
			try {
				Object newObj = destClass.newInstance();
				translateObject(obj, newObj);
				dest.add(newObj);
			}  catch (Exception e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
	}
	
	private static Class getPrimitiveClass(Class c) {
		  if (c == Byte.class){
			  return byte.class;
		  } else if (c == Short.class){
			  return short.class;
		  } else if (c == Integer.class){
			  return int.class;
		  } else if (c == Long.class){
			  return long.class;
		  } else if (c == Float.class){
			  return float.class;
		  } else if (c == Double.class){
			  return double.class;
		  } else if (c == Character.class){
			  return char.class;
		  } else if (c == Short.class){
			  return short.class;
		  } else if (c == Short.class){
			  return short.class;
		  } 
		  return null;
	}
	
	public static boolean isPrimitive(Class c) {
		  if (c.isPrimitive()) {
		    return true;
		  } else if (c == Byte.class
		          || c == Short.class
		          || c == Integer.class
		          || c == Long.class
		          || c == Float.class
		          || c == Double.class
		          || c == Boolean.class
		          || c == Character.class
		          || c == String.class) {
		    return true;
		  } else {
		    return false;
		  }
	}
	
	public static void readInputStream(InputStream is, IReadFile iReadLine){
		InputStreamReader rd = null;
		BufferedReader in = null;
		try {
			rd = new InputStreamReader(is, "UTF-8");
			in = new BufferedReader(rd);

			String line = null;
			while ((line = in.readLine()) != null) {
				iReadLine.readLine(line);
			}
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		} finally {
			if(in != null){
				try {
					in.close();
				} catch (Exception e2) {
				}
			}
			if(rd != null){
				try {
					rd.close();
				} catch (Exception e2) {
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (Exception e2) {
				}
			}
		}
	}
	
	public static void readFile(String fileName, IReadFile iReadFile){
		readFile(new File(fileName), iReadFile);
	}
	
	public static void readFile(File file, IReadFile iReadFile){
		FileInputStream is =  null;
		InputStreamReader rd = null;
		BufferedReader in = null;
		try {
			is = new FileInputStream(file);
			rd = new InputStreamReader(is, "UTF-8");
			in = new BufferedReader(rd);
			
			String line = null;
			while ((line = in.readLine()) != null) {
				boolean continueRead = iReadFile.readLine(line);
				if(continueRead == false){
					break;
				}
			}
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		} finally {
			try {
				in.close();
			} catch (Exception e2) {
			}
			try {
				rd.close();
			} catch (Exception e2) {
			}
			try {
				is.close();
			} catch (Exception e2) {
			}
		}
	}
	
	public static String getGetterName(String fieldName) {
		String index[] = fieldName.split("_");
		String all = "";
		if(!fieldName.toUpperCase().equals(fieldName) && index != null && index.length == 1){
			all = String.valueOf(index[0].charAt(0)).toUpperCase() + index[0].substring(1);
		} else {
			for (int i = 0; index != null && i < index.length; i++) {
				all += String.valueOf(index[i].charAt(0)).toUpperCase() + index[i].substring(1).toLowerCase();
			}
		}
		return "get" + all;
	}
	
	public static String getSetterName(String fieldName) {
		String setter = getGetterName(fieldName).replaceFirst("g", "s");
		return setter;
	}
}
