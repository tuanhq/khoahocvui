package com.ligerdev.appbase.utils.textbase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DocumentUtils {
	
	private static TransformerFactory tf = null;
	private static DocumentBuilderFactory dbf = null;
	private static javax.xml.transform.Transformer trans = null;
	private static Logger logger = Log4jLoader.getLogger();
	
	static {
		try {
			dbf = DocumentBuilderFactory.newInstance();
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		}
		try {
			tf = TransformerFactory.newInstance();
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		}
		try {
			trans = TransformerFactory.newInstance().newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.setParameter("indent-number", 4);
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		}
	}
	

	public static Document parseStringToDoc(String xml){
		StringReader reader = null;
		InputSource is = null;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			reader = new StringReader(xml);
			is = new InputSource(reader);
			Document doc = db.parse(is);
			doc.normalize();
			return doc;
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
		return null;
	}
	
	public static Document parseFileToDoc(File file){
		InputStream is = null;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			is = new FileInputStream(file);
			Document doc = db.parse(is);
			doc.normalize();
			return doc;
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
		return null;
	}
	
	public static Document parseStreamToDoc(InputStream is){
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(is);
			doc.normalize();
			return doc;
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
		return null;
	}
	
	public static String getTextContent(Document doc, int index, String firstChildElementName){
		try {
			NodeList list = doc.getElementsByTagName(firstChildElementName);
			Element e = (Element) list.item(index);
			String str = e.getTextContent();
			return str;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Element getElement(Document doc, int index, String firstChildElementName){
		try {
			NodeList list = doc.getElementsByTagName(firstChildElementName);
			Element e = (Element) list.item(index);
			return e;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getAttribute(Document doc, int index, String firstChildElementName, String attributeName){
		try {
			NodeList list = doc.getElementsByTagName(firstChildElementName);
			Element e = (Element) list.item(index);
			String str = e.getAttribute(attributeName); 
			return str; 
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String convertDocToString(Document doc) {
        StringWriter writer = null;
        try {
            writer = new StringWriter();
            StreamResult rs = new StreamResult(writer);
            tf.newTransformer().transform(new DOMSource(doc), rs);
            String output = writer.getBuffer().toString(); 
            return output;
        } catch (TransformerException e) {
        	logger.info("Exception: " + e.getMessage());
        } finally {
        	try {
				writer.close();
			} catch (IOException e) {
				logger.info("Exception: " + e.getMessage());
			}
        }
        return null;
    }
	
	public static String formatXml(String xml) {
        try {
            Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
            StreamResult res = new StreamResult(new ByteArrayOutputStream());
            serializer.transform(xmlSource, res);
            String s = new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
            res.getOutputStream().close();
            return s;
        } catch (Exception e) {
        	logger.info("Exception: " + e.getMessage());
            return xml;
        }
    }
	
	public static void main(String[] args) {
		System.out.println(formatXml("<a><b><c/><d>text D</d><e value='0'/></b></a>"));
	}
	
	
	public static String getTextContent(Element element, int index, String firstChildElementName){
		try {
			NodeList list = element.getElementsByTagName(firstChildElementName);
			Element e = (Element) list.item(index);
			String str = e.getTextContent();
			return str;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Element getElement(Element element, int index, String firstChildElementName){
		try {
			NodeList list = element.getElementsByTagName(firstChildElementName);
			Element e = (Element) list.item(index);
			return e;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getAttribute(Element element, int index, String firstChildElementName, String attributeName){
		try {
			NodeList list = element.getElementsByTagName(firstChildElementName);
			Element e = (Element) list.item(index);
			String str = e.getAttribute(attributeName); 
			return str; 
		} catch (Exception e) {
			return null;
		}
	}
}
