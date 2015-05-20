package com.ligerdev.appbase.utils.http;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;

import com.ligerdev.appbase.utils.threads.AbsApplication;

@WebService(targetNamespace = "http://javax.jws.server", serviceName = "WSAPIService", portName = "WSAPIPort", name = "WSAPI")
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@HandlerChain(file="ws-config.xml")
public class LDWsTest extends AbsWebservice {

	public LDWsTest() {
		// TODO Auto-generated constructor stub
	}
	
	public String test(int a){
		String soapRequest = getSoapRequest("123321", wsContext);
		logger.info("###### xmlRequest: " + soapRequest);
		return "Hello, mr " + a;
	}
	
	public static void main(String[] args) {
		Endpoint.publish("http://0.0.0.0:8080/test", new LDWsTest());
	}
}
