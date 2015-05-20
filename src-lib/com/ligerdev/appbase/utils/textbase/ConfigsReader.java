package com.ligerdev.appbase.utils.textbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.ligerdev.appbase.utils.BaseUtils;

public abstract class ConfigsReader {

	protected static Logger logger = Log4jLoader.getLogger();

	private boolean autoReload = true;
	private boolean firstLoad = true;
	private String fileName = null;
	private File file = null;
	private long lastModified = 0;
	private Document doc = null;
	private static ArrayList<Class<? extends ConfigsReader>> listCheck = new ArrayList<Class<? extends ConfigsReader>>();

	protected abstract void readPropeties();
	
	public ConfigsReader() {
		// TODO Auto-generated constructor stub
	}
	
	private void loadFile() {
		try {
			logger.info("================ load config ================");
			readPropeties();
			logger.info("Load config successfully, file = " + file.getAbsolutePath() + "\n\n");
			firstLoad = false;
			
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
			if (firstLoad) {
				BaseUtils.sleep(1000); System.exit(-9);
			}
		}
	}
	
	public static void init(Class<? extends ConfigsReader> clazz){
		ConfigsReader.init(clazz, BaseUtils.getMyDir() + "./config/config.xml");
	}
	
	public static void init(Class<? extends ConfigsReader> clazz, String filename){
		try {
			if(clazz == null){
				return;
			}
			synchronized (listCheck) {
				if(listCheck.contains(clazz)){
					logger.info(clazz.getName() + " already init before => ignore.");
					return;
				}
				listCheck.add(clazz);
				
				ConfigsReader configsReader = (ConfigsReader) clazz.newInstance();
				configsReader.fileName = filename;
				configsReader.initConfig();
			}
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		}
	}

	private void initConfig() {
		
		logger.info("Check Xmlconfig level 1, fileName = " + fileName);
		file = new File(fileName);
		
		if (!file.exists()) {
			logger.info("File not found, filePath = " + file.getAbsolutePath());
			
			String fullPath = ConfigsReader.class.getClassLoader().getResource("").getPath();
			fullPath = fullPath.substring(0, fullPath.indexOf("/WEB-INF/")) + File.separator + fileName;
			logger.info("Check Xmlconfig level 2 (getClassLoader.getResource), fullPath = " + fullPath);
			file = new File(fullPath);

			if (!file.exists()) {
				logger.info("Check Xmlconfig level 3 (new FileInputStream)...");
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(fileName);
					if (fis != null) {
						load2Doc(fis);
						return;
					}
				} catch (Exception e) {
					// logger.info("Exception: " + e.getMessage());
				}
				logger.info("Check Xmlconfig level 4 (getClassLoader.getResourceAsStream)...");
				InputStream is = ConfigsReader.class.getClassLoader().getResourceAsStream(fileName);
				load2Doc(is);
				return;
			}
		}
		if (!file.exists()) {
			logger.info("Configuration file not found, file path to load = " + file.getAbsolutePath());
			if (firstLoad) {
				BaseUtils.sleep(1000); System.exit(-9);
			}
		} else if (file.lastModified() <= 0) {
			logger.info("Can not get last time modified of configuration file");
			if (firstLoad) {
				BaseUtils.sleep(1000); System.exit(-9);
			}
		} else {
			load2Doc();
			if (autoReload) {
				startThreadReloadConfig();
			}
		}
	}

	private void startThreadReloadConfig() {
		new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						logger.info("Exception: " + e.getMessage());
					}
					try {
						if (file.lastModified() != lastModified) {
							load2Doc();
						}
					} catch (Exception e) {
						logger.info("Exception: " + e.getMessage());
					}
				}
			};
		}.start();
	}
	
	private void load2Doc() {
		doc = DocumentUtils.parseFileToDoc(file);
		if (doc != null) {
			loadFile();
		}
		lastModified = file.lastModified();
	}

	private void load2Doc(InputStream is) {
		doc = DocumentUtils.parseStreamToDoc(is);
		if (doc != null) {
			loadFile();
		}
		lastModified = file.lastModified();
	}

	// ===================================================================== read xml

	protected boolean getBoolean(String key) {
		return getBoolean(doc.getDocumentElement(), key);
	}

	protected boolean getBoolean(Element element, String key) {
		String value = getString(element, key);
		return "on".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value);
	}

	protected boolean getBooleanAttr(String key) {
		return getBooleanAttr(doc.getDocumentElement(), key);
	}

	protected boolean getBooleanAttr(Element element, String key) {
		String value = getStringAttr(element, key);
		return "on".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value);
	}

	protected String getString(String key) {
		return getString(doc.getDocumentElement(), key);
	}

	protected String getString(Element element, String key) {
		String result = null;
		try {
			String tags[] = key.replace("|", ":").split(":");
			for (int i = 0; tags != null && i < tags.length; i++) {
				element = (Element) element.getElementsByTagName(tags[i]).item(0);
			}
			result = element.getTextContent();
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage() + ", can not load property: " + key);
			if (firstLoad) {
				BaseUtils.sleep(1000); System.exit(-9);
			}
		}
		String log = "property: " + key + " = " + result;
		logger.info(log);
		return result;
	}

	protected Integer getInt(String key) {
		return getInt(doc.getDocumentElement(), key);
	}

	protected Integer getInt(Element element, String key) {
		int result = -1;
		try {
			String stringValue = getString(element, key).trim();
			result = Integer.parseInt(stringValue);
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage() + ", can not load property: " + key);
			if (firstLoad) {
				BaseUtils.sleep(1000); System.exit(-9);
			}
		}
		return result;
	}

	protected ArrayList<String> getList(String key) {
		return getList(doc.getDocumentElement(), key);
	}

	protected ArrayList<String> getList(Element element, String key) {
		try {
			ArrayList<String> list = new ArrayList<String>();
			String tags[] = key.replace("|", ":").split(":");
			for (int i = 0; tags != null && i < tags.length; i++) {
				if (i != tags.length - 1) { // not be last index
					element = (Element) element.getElementsByTagName(tags[i]).item(0);
				} else {
					NodeList nodeList = element.getElementsByTagName(tags[i]);
					for (int k = 0; nodeList != null && k < nodeList.getLength(); k++) {
						element = (Element) nodeList.item(k);
						list.add(element.getTextContent());
					}
				}
			}
			String log = "property: " + key + " = list has size = " + list.size();
			logger.info(log);
			return list;
			
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage() + ", can not load property: " + key);
			if (firstLoad) {
				BaseUtils.sleep(1000); System.exit(-9);
			}
		}
		return null;
	}

	protected ArrayList<Element> getListElement(String key) {
		return getListElement(doc.getDocumentElement(), key);
	}

	protected ArrayList<Element> getListElement(Element element, String key) {
		try {
			ArrayList<Element> list = new ArrayList<Element>();
			String tags[] = key.replace("|", ":").split(":");
			for (int i = 0; tags != null && i < tags.length; i++) {
				if (i != tags.length - 1) { // not be last index
					element = (Element) element.getElementsByTagName(tags[i]).item(0);
				} else {
					NodeList nodeList = element.getElementsByTagName(tags[i]);
					for (int k = 0; nodeList != null && k < nodeList.getLength(); k++) {
						element = (Element) nodeList.item(k);
						list.add(element);
					}
				}
			}
			String log = "property: " + key + " = list elements size = " + list.size();
			logger.info(log);
			return list;
			
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage() + ", can not load property: " + key);
			if (firstLoad) {
				BaseUtils.sleep(1000); System.exit(-9);
			}
		}
		return null;
	}

	protected String getStringAttr(String key) {
		return getStringAttr(doc.getDocumentElement(), key);
	}

	protected String getStringAttr(Element element, String key) {
		String result = null;
		try {
			String tags[] = key.replace("|", ":").split(":");
			for (int i = 0; tags != null && i < tags.length - 1; i++) {
				element = (Element) element.getElementsByTagName(tags[i]).item(0);
			}
			result = element.getAttribute(tags[tags.length - 1]);
			if (result == null) {
				throw new Exception("Do not have attribute " + key);
			}
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage() + ", can not load property: " + key);
			if (firstLoad) {
				BaseUtils.sleep(1000); System.exit(-9);
			}
		}
		String log = "property(attr): " + key + " = " + result;
		logger.info(log);
		return result;
	}

	protected Integer getIntAttr(String key) {
		return getIntAttr(doc.getDocumentElement(), key);
	}

	protected Integer getIntAttr(Element element, String key) {
		int result = -1;
		try {
			String stringValue = getStringAttr(element, key).trim();
			result = Integer.parseInt(stringValue);
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage() + ", can not load property: " + key);
			if (firstLoad) {
				BaseUtils.sleep(1000); System.exit(-9);
			}
		}
		return result;
	}

	protected ArrayList<String> getListUpperCase(String key) {
		return getListUpperCase(doc.getDocumentElement(), key);
	}

	protected ArrayList<String> getListUpperCase(Element element, String key) {
		try {
			ArrayList<String> list = new ArrayList<String>();
			String tags[] = key.replace("|", ":").split(":");
			for (int i = 0; tags != null && i < tags.length; i++) {
				if (i != tags.length - 1) { // not be last index
					element = (Element) element.getElementsByTagName(tags[i]).item(0);
				} else {
					NodeList nodeList = element.getElementsByTagName(tags[i]);
					for (int k = 0; nodeList != null && k < nodeList.getLength(); k++) {
						element = (Element) nodeList.item(k);
						list.add(element.getTextContent().toUpperCase());
					}
				}
			}
			String log = "property: " + key + " = list has size = " + list.size();
			logger.info(log);
			return list;
			
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage() + ", can not load property: " + key);
			if (firstLoad) {
				BaseUtils.sleep(1000); System.exit(-9);
			}
		}
		return null;
	}

	protected ArrayList<String> getListLowerCase(String key) {
		return getListLowerCase(doc.getDocumentElement(), key);
	}

	protected ArrayList<String> getListLowerCase(Element element, String key) {
		try {
			ArrayList<String> list = new ArrayList<String>();
			String tags[] = key.replace("|", ":").split(":");
			for (int i = 0; tags != null && i < tags.length; i++) {
				if (i != tags.length - 1) { // not be last index
					element = (Element) element.getElementsByTagName(tags[i]).item(0);
				} else {
					NodeList nodeList = element.getElementsByTagName(tags[i]);
					for (int k = 0; nodeList != null && k < nodeList.getLength(); k++) {
						element = (Element) nodeList.item(k);
						list.add(element.getTextContent().toLowerCase());
					}
				}
			}
			String log = "property: " + key + " = list has size = " + list.size();
			logger.info(log);
			return list;
			
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage() + ", can not load property: " + key);
			if (firstLoad) {
				BaseUtils.sleep(1000); System.exit(-9);
			}
		}
		return null;
	}

}
