package com.ligerdev.appbase.utils.http;

import java.net.InetSocketAddress;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.sun.net.httpserver.HttpExchange;
import com.sun.xml.internal.ws.developer.JAXWSProperties;


/*
@WebService(targetNamespace = "http://javax.jws.server", serviceName = "WSAPIService", portName = "WSAPIPort", name = "WSAPI")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@HandlerChain(file="ws-config.xml")
*/
public abstract class AbsWebservice {

	protected static Logger logger = Log4jLoader.getLogger();

	/*
		private static AbsWebservice instance = null;
		
		private AbsWebservice() {
		}
		
		public synchronized static AbsWebservice getInstance(){
			if(instance == null){
				instance = new Implementor();
			}
			return instance;
		}
	 */
	
	@Resource
	protected WebServiceContext wsContext;
	
	public AbsWebservice() {
	}
	
	/*
		public String helloworld(
				@WebParam(name = "name") String name
		){
			return "Hi " + name.toUpperCase() + ", your ws client connect to me successfully!";
		}
	*/
	
	protected static String getClientInfo(WebServiceContext wsContext) {
		MessageContext mc = wsContext.getMessageContext();
		HttpExchange exchange = (HttpExchange) mc.get(JAXWSProperties.HTTP_EXCHANGE);
		InetSocketAddress remoteAddress = exchange.getRemoteAddress();
		String remoteHost = remoteAddress.getHostName();
		int port = remoteAddress.getPort();
		return remoteHost + ":" + port;
	}
	
	protected static HttpExchange getHttpExchange(WebServiceContext wsContext){
		MessageContext mc = wsContext.getMessageContext();
		HttpExchange exchange = (HttpExchange) mc.get(JAXWSProperties.HTTP_EXCHANGE);
		return exchange;
	}

	protected static HttpServletRequest getHttpRequest(WebServiceContext wsContext) {
		MessageContext mc = wsContext.getMessageContext();
		HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
		return req;
	}

	protected static String getSoapRequest(String transid, WebServiceContext wsContext) {
		String soapRequest = null;
		try {
			com.sun.net.httpserver.Headers header = (com.sun.net.httpserver.Headers) wsContext.getMessageContext()
					.get("com.sun.xml.internal.ws.api.message.packet.outbound.transport.headers");
			soapRequest = (String) header.remove("soapxml").get(0);
			wsContext.getMessageContext().put("transid", transid);
			
		} catch (Exception e) {
			logger.info(transid + "; Exception: " + e.getMessage());
		}
		return soapRequest;
	}
}
