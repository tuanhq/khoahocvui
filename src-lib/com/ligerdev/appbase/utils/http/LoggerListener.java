package com.ligerdev.appbase.utils.http;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.ligerdev.appbase.utils.textbase.ReqCountUtils;

public class LoggerListener extends HttpServlet {

	private static ReqCountUtils reqCount = ReqCountUtils.getInstance("LoggerListener", "DL");
	private static Logger logger = Log4jLoader.getLogger();
	private static final long serialVersionUID = 1L;
	
	public LoggerListener() {
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/xml");
		String transid = reqCount.countLongStr();
		try {
			String addr = req.getRemoteAddr() + ":" + req.getRemotePort();
			logger.info(transid + " #### LOG_ALL_REQ: Addr = "  + addr + ", servlet = " + req.getServletPath());
			logger.info(transid + ", getQueryStr = " + req.getQueryString());
			HttpServerUtils.getParameters(transid, req, true);
			HttpServerUtils.getHeaders(transid, req, true);
			String xml = HttpServerUtils.getXmlPost(req);
			logger.info(transid + ", bodyStr = " + xml);
			
			resp.getWriter().println(transid);
			resp.getWriter().close();
			
		} catch (Exception e) {
			logger.info(transid + ", Exception: " + e.getMessage(), e);
		}
	}
	
	public String getParams(String transid, HttpServletRequest req) { 
		ArrayList<NameValuePair> list = HttpServerUtils.getParameters(transid, req, false);
		String res = "";
		for(int i = 0;  list != null && i < list.size(); i ++){
			NameValuePair bean = list.get(i);
			res += "&" + bean.getName() + "=" + bean.getValue();
		}
		res = res.replaceFirst("&", "");
		return res;
	}
	
	public static void main(String[] args) throws Exception { 
		Log4jLoader.init();
		HttpServerUtils server = new HttpServerUtils();
		server.addServlet("http://0.0.0.0:8080/logger", new LoggerListener());
		System.out.println("http://0.0.0.0:8080/logger"); 
		server.startServer();
	}
}
