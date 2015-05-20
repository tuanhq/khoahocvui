package com.ligerdev.appbase.utils.emails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class EMailSender {

	private static Logger logger = Log4jLoader.getLogger();
	private static EMailSender instance = null;
	
	private EMailSender() {

	}

	public synchronized static EMailSender getInstance() {
		if (instance == null) {
			instance = new EMailSender();
		}
		return instance;
	}

	public static void main(String[] args) {
		try {
			
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		}
	}

	public void sendMail(final String from, final String pass, List<EMailObj> listObjs) {
		Session session = getSession(from, pass);
		for (int i = 0; listObjs != null && i < listObjs.size(); i++) {
			try {
				EMailObj obj = listObjs.get(i);
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(from));
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(obj.getAddress()));
				msg.setSubject(obj.getSubject());
				if (obj.isHTMLStyle()) {
					msg.setContent(obj.getContent(), "text/html; charset=UTF-8");
				} else {
					msg.setText(obj.getContent());
				}
				msg.setSentDate(new Date());
				Transport.send(msg);
				logger.info("A message sent.");
			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
		logger.info("Done!");
	}

	public void sendMail(String from, String pass, ArrayList <String> recipients, String subject, String content, boolean isHTMLStyle) {
		// -- Create a new message --
		try {
			Message msg = new MimeMessage(getSession(from, pass));
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] addressTo = new InternetAddress[recipients.size()];
			for (int i = 0; i < recipients.size(); i++) {
				String tmp = recipients.get(i);
				addressTo[i] = new javax.mail.internet.InternetAddress(tmp);
			}
			msg.setRecipients(Message.RecipientType.TO, addressTo);
			msg.setSubject(subject);
			if (isHTMLStyle) {
				msg.setContent(content, "text/html; charset=UTF-8");
			} else {
				msg.setText(content);
			}
			msg.setSentDate(new Date());
			// **************** Without Attachments ******************
			Transport.send(msg);
			logger.info("Message sent.");
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		}
	}

	private Session getSession(final String from, final String pass) {
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		// ******************** FOR PROXY ******************
		// props.setProperty("proxySet","true");
		// props.setProperty("socksProxyHost","proxy");
		// props.setProperty("socksProxyPort","8080");
		// props.setProperty("socksProxyVersion","5");
		// ******************** FOR PROXY ******************
		props.put("mail.smtp.user", "username");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "25");
		// props.put("mail.debug", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.EnableSSL.enable", "true");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, pass);
			}
		});
		return session;
	}
}
