package com.ligerdev.appbase.utils.http;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.sun.net.httpserver.Headers;

public class WSSoapUtils implements SOAPHandler<SOAPMessageContext> {

	private static Logger logger = Log4jLoader.getLogger();

	// @Override
	public boolean handleMessage(SOAPMessageContext context) {
		logSoapXml(context);
		return true;
	}

	// @Override
	public boolean handleFault(SOAPMessageContext context) {
		logSoapXml(context);
		return false;
	}

	// @Override
	public void close(MessageContext context) {
		// logger.info("close");
	}

	// @Override
	public Set<QName> getHeaders() {
		// logger.info("getHeaders");
		return null;
	}

	private void logSoapXml(SOAPMessageContext context) {
		String soapXml = null;
		try {
			soapXml = getXmlMessage(context.getMessage());
			if (soapXml != null) {
				soapXml = soapXml.replace("\n", " ");
			}
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
		}
		if ((Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)) {
			String transid = (String) context.get("transid");
			if (transid == null) {
				// logger.info("SoapResponse: " + soapXml);
			} else {
				logger.info(transid + ", SoapResponse: " + soapXml);
			}
		} else {
			// logger.info("##### SoapRequest: " + soapXml);
			com.sun.net.httpserver.Headers header = (Headers) context.get("com.sun.xml.internal.ws.api.message.packet.outbound.transport.headers");
			header.add("soapxml", soapXml);
			// BaseUtils.printEntrySet(context.entrySet());
		}
	}

	private String getXmlMessage(SOAPMessage message) throws Exception {
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();
			message.writeTo(os);
			final String encoding = (String) message.getProperty(SOAPMessage.CHARACTER_SET_ENCODING);
			if (encoding == null) {
				return new String(os.toByteArray());
			} else {
				return new String(os.toByteArray(), encoding);
			}
		} catch (Exception e) {
		} finally {
			try {
				os.close();
			} catch (Exception e2) {
			}
		}
		return null;
	}
}
