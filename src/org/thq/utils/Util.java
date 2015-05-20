/**
 * 
 */
package org.thq.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tuanhq
 *
 */
public  class Util {
	public static boolean uploadFile(byte[]input, String outputFilePath) throws Exception{
		boolean result = false;	
		File file = null;
		FileOutputStream fos = null;
		try {
			file = new File(outputFilePath);
			fos = new FileOutputStream(file);
			fos.write(input);
			result = true;
		} catch (Exception e) {
				throw e;
		}finally{
			fos.close();
		}		
		return result;
	}
	public static boolean writeStringTofile(String fileName,String content){
		boolean result =false;
		FileOutputStream fop = null;
		File file;	
 
		try {
 
			file = new File(fileName);
			fop = new FileOutputStream(file);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
			result =true;
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return result;
	}
	public static String stackTraceToString(Exception e) {
    StringBuilder sb = new StringBuilder();
    for (StackTraceElement element : e.getStackTrace()) {
        sb.append(element.toString());
        sb.append("\n");
    }
    return sb.toString();
	}
	public static String dateToString(Date date, String format){
		SimpleDateFormat sfd = new SimpleDateFormat(format);
		return sfd.format(date);
	}
	public static String getStringDate(String format){	
		return dateToString(new Date(), format);
	}
	public static String getStringDate(){	
		return dateToString(new Date(), "yyyyMMddHHmmss");
	}
	public static String getStringDateNormal(){	
		return dateToString(new Date(), "yyyyMMdd");
	}
	public static void main(String[] args) {
		Integer a1 = 5;
		System.out.println("start main:" + Integer.toHexString(System.identityHashCode(a1)) );
		System.out.println(a1);
		Util.add(a1);
		System.out.println("sau main:" + Integer.toHexString(System.identityHashCode(a1)) );
		System.out.println(a1);
	}
	public static void add(Integer a1){
		System.out.println("truoc ham :" +Integer.toHexString(System.identityHashCode(a1)));
		System.out.println(a1);
		a1 = a1 +1;
		System.out.println("sau ham :" + Integer.toHexString(System.identityHashCode(a1)));
		System.out.println(a1);
	}
	public static Date stringToDate(String strDate) throws ParseException{
		return stringToDate(strDate, "yyyyMMddHHmmss");
	}
	
	public static Date stringToDate(String strDate, String format) throws ParseException{
		SimpleDateFormat sfd = new SimpleDateFormat(format);
		return sfd.parse(strDate);
		
	}
	public static long subtractionDateToSecond(Date date1, Date date2){
		return  (date1.getTime() - date2.getTime())/1000;
		
	}
	
	
	

}
