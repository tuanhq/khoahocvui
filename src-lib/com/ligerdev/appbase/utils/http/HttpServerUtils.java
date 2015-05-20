package com.ligerdev.appbase.utils.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class HttpServerUtils {

	private static Logger logger = Log4jLoader.getLogger();
	private ConcurrentHashMap<String, Server> listServer = new ConcurrentHashMap<String, Server>();
	private ConcurrentHashMap<String, ServletContextHandler> listContext = new ConcurrentHashMap<String, ServletContextHandler>();
	private static ArrayList<HttpServerUtils> listInstance = new ArrayList<HttpServerUtils>();
	
	public HttpServerUtils() {
		listInstance.add(this);
	}
	
	public void addServlet(String url, HttpServlet servlet){
		String[] tmp = url.replaceFirst("http://", "").split(":");
		String portStr = tmp[1].substring(0, tmp[1].indexOf("/"));
		String path = tmp[1].replaceFirst(portStr, "");
		if(path.contains("?")){
			path = path.substring(0, path.indexOf("?"));
		}
		addServlet(url, portStr, path, servlet);
	}
	
	public void addServlet(String port, String path, HttpServlet servlet){
		logger.info("addServlet: port = " + port + "; path = " + path + "; servlet = " + servlet.getClass().getName()); 
		Server server = listServer.get(port);
		if(server == null){
			server = new Server(Integer.parseInt(port)); 
			listServer.put(port, server);
		}
		ServletContextHandler context = listContext.get(port);
		if(context == null){
			context = new ServletContextHandler(0);
			context.setContextPath("/");
			listContext.put(port, context);
		}
		ServletHolder holder = new ServletHolder(servlet);
		context.addServlet(holder, path);
		server.setHandler(context);
	}
	
	public void addServlet(String orgUrl, String port, String path, HttpServlet servlet){
		logger.info("addServlet: port = " + port + "; path = " + path + "; servlet = " + servlet.getClass().getName()); 
		Server server = listServer.get(port);
		if(server == null){
			server = new Server(Integer.parseInt(port)); 
			listServer.put(port, server);
		}
		String attName = ("A_" + port + "_" + path).replace("/", "_").replace(" ", "_");
		server.setAttribute(attName, orgUrl);
		ServletContextHandler context = listContext.get(port);
		if(context == null){
			context = new ServletContextHandler(ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			listContext.put(port, context);
		}
		ServletHolder holder = new ServletHolder(servlet);
		context.addServlet(holder, path);
		server.setHandler(context);
	}
	
	public void startServer() throws Exception{
		Enumeration<String> enums = listServer.keys();
		while(enums.hasMoreElements()){
			String key = enums.nextElement();
			Server server = listServer.get(key);
			try {
				server.start();
				@SuppressWarnings("unchecked")
				Enumeration<String> atts = server.getAttributeNames();
				boolean hasElement = false;
				while(atts.hasMoreElements()){
					String attName = atts.nextElement();
					String orgUrl = (String) server.getAttribute(attName);
					logger.info("Published HTTP URL: " + orgUrl);
					hasElement = true;
				}
				if(!hasElement){
					logger.info("Start server http port " + key + " successfully...");
				}
			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage() + "; port = " + key, e);
				throw e;
			}
		}
	}
	
	public void shutdown() {
		Enumeration<String> enums = this.listServer.keys();
		while (enums.hasMoreElements()) {
			String key = (String) enums.nextElement();
			Server server = (Server) this.listServer.get(key);
			try {
				server.stop();
				logger.info("Stop server http port " + key + " successfully...");
			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage() + "; port = " + key, e);
			}
		}
	}
	
	public static void shutdownAll() {
		for(HttpServerUtils server : listInstance){
			server.shutdown();
		}
	}
	
	public static String getXmlPost(HttpServletRequest request) {
		String xml = "";
		InputStream is = null;
		InputStreamReader rd = null;
		BufferedReader in = null;
		try {
			is = request.getInputStream();
			rd = new InputStreamReader(is, "UTF-8");
			in = new BufferedReader(rd);
			String line;
			while ((line = in.readLine()) != null) {
				xml = xml + line + "\n";
			}
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e2) {
					logger.info("Exception: " + e2.getMessage());
				}
			}
			if (rd != null) {
				try {
					rd.close();
				} catch (Exception e2) {
					logger.info("Exception: " + e2.getMessage());
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (Exception e2) {
					logger.info("Exception: " + e2.getMessage());
				}
			}
		}
		return xml;
	}
	
	public static String parseData(String xml, String xmlTag) {
		try {
			String startTag = "<" + xmlTag + ">";
			String endTag = "</" + xmlTag + ">";
			int start = xml.indexOf(startTag);
			int end = xml.indexOf(endTag);
			
			if (start < 0 || end < 0) {
				return null;
			}
			start = start + startTag.length();
			String result = xml.substring(start, end);
			return result.trim();
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public static ArrayList<NameValuePair> getHeaders(String transid, HttpServletRequest req, boolean log){
		ArrayList<NameValuePair> headers = new ArrayList<NameValuePair>();
		Enumeration<String> enums =  req.getHeaderNames();
		while(enums.hasMoreElements()){
			String key = enums.nextElement();
			Enumeration<String> valuesEnums = req.getHeaders(key);
			while(valuesEnums.hasMoreElements()){
				String value = valuesEnums.nextElement();
				if(log) {
					logger.info(transid + ", Header: key = " + key + ", value = " + value); 
				}
				headers.add(new NameValuePair(key, value)); 
			}
		}
		return headers;
	}
	
	public static ArrayList<NameValuePair> getParameters(String transid, HttpServletRequest req, boolean log){
		Map<String, String[]> map = req.getParameterMap();
		Set<Entry<String, String[]>> set = map.entrySet();
		Iterator<Entry<String, String[]>> iter = set.iterator();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		while(iter.hasNext()){
			Entry<String, String[]> e = (Entry<String, String[]>) iter.next();
			String key = e.getKey();
			String value[] = e.getValue();
			for(int i = 0 ; value != null && i < value.length; i ++){
				if(log) {
					logger.info(transid + ", Parameter: key = " + key + ", value = " + value[i]);  
				}
				params.add(new NameValuePair(key, value[i])); 
			}
		}
		return params;
	}
	
	public static Hashtable<String, String> parseQueryString(String s){
		if(s == null){
			return null; 
		}
		Hashtable<String, String> list = new Hashtable<String, String>();
		String []tmp = s.split("&");
		for(int i = 0; tmp != null && i < tmp.length; i ++){
			if(BaseUtils.isBlank(tmp[i])){
				continue;
			}
			if(!tmp[i].contains("=")){
				list.put(tmp[i], ""); 
				// logger.info("Put params: " + tmp[i] + "=");
			} else {
				String tmp2[] = tmp[i].split("=");
				if(tmp2.length == 1){
					list.put(tmp2[0], "");  
				} else {
					String value = URLDecoder.decode(tmp2[1]);
					list.put(tmp2[0], value);  
				}
				// logger.info("Put params: " + tmp[i] + "=" + value);
			}
		}
		return list;
	}
}
