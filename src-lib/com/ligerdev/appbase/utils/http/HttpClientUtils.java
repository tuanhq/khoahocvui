package com.ligerdev.appbase.utils.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sun.misc.BASE64Encoder;

import com.ligerdev.appbase.utils.textbase.DocumentUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class HttpClientUtils {

	private HttpClient httpClient = null;
	private static Logger logger = Log4jLoader.getLogger();
	private static ConcurrentHashMap<String, HttpClientUtils> listInstance = new ConcurrentHashMap<String, HttpClientUtils>();

	private HttpClientUtils(int timeout){
		try {
			MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
			HttpConnectionManagerParams params = new HttpConnectionManagerParams();
			params.setDefaultMaxConnectionsPerHost(800);
			params.setMaxTotalConnections(1500);
			params.setParameter(HttpConnectionManagerParams.SO_TIMEOUT, timeout);
			params.setParameter(HttpConnectionManagerParams.CONNECTION_TIMEOUT, timeout);
			connectionManager.setParams(params);
			httpClient = new HttpClient(connectionManager);
		} catch (Throwable e) {
			logger.info("Exception: " + e.getMessage());
		}
	}
	
	public synchronized static HttpClientUtils getInstance(int timeout){
		HttpClientUtils tmp = listInstance.get(timeout + "");
		if(tmp == null){
			tmp = new HttpClientUtils(timeout);
			listInstance.put(timeout + "", tmp);
		}
		return tmp;
	}
	
	public String post(String transid, String url, String txt) {
		String response = "";
		PostMethod postMethod = new PostMethod(url);
		try {
			ByteArrayRequestEntity entity = new ByteArrayRequestEntity(txt.getBytes("UTF-8"));
			postMethod.setRequestHeader("Content-Type", "text/xml; charset=utf-8");
			postMethod.setRequestEntity(entity);
			int result = httpClient.executeMethod(postMethod);
			if (result == HttpStatus.SC_OK) {
				response =  readText(postMethod.getResponseBodyAsStream());
			} else {
				logger.info(transid + ", Request(POST) fail, url = " + url 
						+ "; request = " + txt + "; respCode = " + result);
			}
		} catch (Throwable e) {
			logger.info(transid + ", url = " + url + ", Exception: " + e.getMessage(), e);
		} finally {
			postMethod.releaseConnection();
		}
		return response;
	}
	
	public String get(String transid, String url) {
		GetMethod method = new GetMethod(url);
		method.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");
		method.setRequestHeader("User-Agent", "Mozilla/5.0 " + "(Windows NT 6.1; rv:10.0.2) " + "Gecko/20100101 Firefox/10.0.2");
		try {
			httpClient.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				String rs = readText(method.getResponseBodyAsStream());
				// logger.debug(transid + ", get() method return value '" + rs + "'");
				return rs;
			} else {
				logger.info(transid + ", Request(GET) fail, url = " + url  + "; respCode = " + method.getStatusCode());
			}
		} catch (Exception e) {
			logger.info(transid + ", url = " + url + ", Exception: " + e.getMessage(), e);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null;
	}
	
	public String get(String transid, String url, String user, String pass) {
		GetMethod method = new GetMethod(url);
		method.setRequestHeader("Content-Type", "text/xml; charset=UTF-8");
		method.setRequestHeader("User-Agent", "Mozilla/5.0 " + "(Windows NT 6.1; rv:10.0.2) " + "Gecko/20100101 Firefox/10.0.2");
		try {
			BASE64Encoder encoder = new BASE64Encoder();
			String encoding = encoder.encode((user + ":" + pass).getBytes());
			method.setRequestHeader("Authorization", "Basic " + encoding);
			
			httpClient.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				String rs = readText(method.getResponseBodyAsStream());
				// logger.debug(transid +", get() method return value '" + rs + "'");
				return rs;
			} else {
				logger.info(transid + ", Request(GET) fail, url = " + url  + "; respCode = " + method.getStatusCode());
			}
		} catch (Exception e) {
			logger.info(transid + ", url = " + url + ", Exception: " + e.getMessage(), e);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null;
	}
	
	public String post(String transid, String url, String txt, 
			ArrayList<NameValuePair> params, ArrayList<NameValuePair> headers) {
		
		String response = "";
		PostMethod postMethod = new PostMethod();
		try {
			url = url.trim();
			if(!url.contains("?")){
				url += "?";
			}
			ByteArrayRequestEntity entity = new ByteArrayRequestEntity(txt.getBytes("UTF-8"));
			for(int i = 0; params != null && i < params.size(); i ++){
				NameValuePair nvp = params.get(i);
				// postMethod.addParameter(nvp);
				url += "&" + nvp.getName()
						+ "=" + URLEncoder.encode(nvp.getValue(), "UTF-8");
			}
			for(int i = 0; headers != null && i < headers.size(); i ++){
				NameValuePair nvp = headers.get(i);
				postMethod.addRequestHeader(nvp.getName(), nvp.getValue());
			}
			postMethod.setURI(new URI(url)); 
			postMethod.setRequestEntity(entity);
			int result = httpClient.executeMethod(postMethod);
			if (result == HttpStatus.SC_OK) {
				response =  readText(postMethod.getResponseBodyAsStream());
			} else {
				logger.info(transid + ", Request(POST) fail, url = " + url + "; request = " + txt + "; respCode = " + result);
			}
		} catch (Throwable e) {
			logger.info(transid + ", Exception: " + e.getMessage(), e);
		} finally {
			postMethod.releaseConnection();
		}
		return response;
	}
	
	public String post2(String transid, String url, String txt, 
			ArrayList<NameValuePair> params, ArrayList<NameValuePair> headers) {
		
		PostMethod method = new PostMethod();
		try {
			ByteArrayRequestEntity entity = new ByteArrayRequestEntity(txt.getBytes("UTF-8"));
			NameValuePair paramsArr[] = null;
			if(params != null ){
				paramsArr = new NameValuePair[params.size()];
				params.toArray(paramsArr);
			}
			for(int i = 0; headers != null && i < headers.size(); i ++){
				NameValuePair nvp = headers.get(i);
				method.addRequestHeader(nvp.getName(), nvp.getValue());
			}
			// logger.info(transid + ", PostURL: " + url);
			method.setURI(new URI(url)); 
			
			method.setRequestEntity(entity);
			if(paramsArr != null) {
				method.setRequestBody(paramsArr);
			}
			int result = httpClient.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				String rs =  readText(method.getResponseBodyAsStream());
				return rs;
			} else {
				logger.info(transid + ", Request(POST-2) fail, url = " + url + "; request = " + txt + "; respCode = " + result);
			}
		} catch (Throwable e) {
			logger.info(transid + ", URL = " +url + "; Exception: " + e.getMessage(), e);
		} finally {
			method.releaseConnection();
		}
		return null;
	}
	
	public String get(String transid, String url, ArrayList<NameValuePair> params, ArrayList<NameValuePair> headers) {
		GetMethod method = new GetMethod();
		try {
			url = url.trim();
			if(!url.contains("?")){
				url += "?";
			}
			for(int i = 0; params != null && i < params.size(); i ++){
				NameValuePair nvp = params.get(i);
				url += "&" + URLEncoder.encode(nvp.getName(), "UTF-8") 
						+ "=" + URLEncoder.encode(nvp.getValue(), "UTF-8");
			}
			for(int i = 0; headers != null && i < headers.size(); i ++){
				NameValuePair nvp = headers.get(i);
				method.addRequestHeader(nvp.getName(), nvp.getValue());
			}
			method.setURI(new URI(url));
			httpClient.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				String rs = readText(method.getResponseBodyAsStream());
				return rs;
			} else {
				logger.info(transid + ", Request(GET) fail, url = " + url  + "; respCode = " + method.getStatusCode());
			}
		} catch (Exception e) {
			logger.info(transid + ", url = " + url +  ", Exception: " + e.getMessage(), e);
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null;
	}
	
	public String readText(InputStream is){
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
	
	public static String parseData(String xml, String xmlTag) {
		try {
			String startTag = "<" + xmlTag + ">";
			String endTag = "</" + xmlTag + ">";
			int start = xml.indexOf(startTag);
			int end = xml.indexOf(endTag);
			if ((start < 0) || (end < 0)) {
				logger.info("===> Parse document, tag = " + xmlTag);
				Document doc = DocumentUtils.parseStringToDoc(xml);
				Element e = (Element) doc.getDocumentElement().getElementsByTagName(xmlTag).item(0);
				if(e == null){
					logger.info("\n Can not parseData\n xml:\n" + xml + "\n\nxmlTag\n" + xmlTag);
				}
				String rs = e.getTextContent();
				return rs;
			}
			start = start + startTag.length();
			String result = xml.substring(start, end);
			return result;
		} catch (Exception e) {
			return "ex";
		}
	}
	
	public static String parseListParams2Str(String transid, ArrayList<NameValuePair> list) { 
		String res = "";
		for(int i = 0;  list != null && i < list.size(); i ++){
			NameValuePair bean = list.get(i);
			res += "&" + bean.getName() + "=" + bean.getValue();
		}
		res = res.replaceFirst("&", "");
		return res;
	}
}
