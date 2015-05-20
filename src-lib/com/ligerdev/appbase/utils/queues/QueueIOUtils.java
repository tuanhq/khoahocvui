package com.ligerdev.appbase.utils.queues;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;

public class QueueIOUtils {

	private static Logger logger = Log4jLoader.getLogger();
	
	public static void waitForQueueEmpty(boolean log, MsgQueueITF itemQueue, int seconds){
		while(itemQueue.size() > 0 || itemQueue.getLastEmptyTime() < seconds){
			try {
				if(log){
					logger.info("Waiting for queue empty, queue size = " + itemQueue.size());
				}
				Thread.sleep(1000);
			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
	}
	
	private static ArrayList<MsgQueueITF> listMntQueues = new ArrayList<MsgQueueITF>();
	private static ArrayList<MsgQueueITF[]> listMntQueueArr = new ArrayList<MsgQueueITF[]>();
	
	public static void saveMmtQueue(){
		try {
			for(int i =0; i < 15; i ++){
				if(QueueIOUtils.queuesIsBlank(listMntQueues) 
						&& QueueIOUtils.queueArrIsBlank(listMntQueueArr)){ 
					break;
				}
				logger.info("Some queues is not empty => wait some seconds to process ...");
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					logger.info("Exception: " + e.getMessage());
				}
			}
			for(MsgQueueITF queue : listMntQueues){
				QueueIOUtils.saveQueue(queue);
			}
			for(MsgQueueITF queues[] : listMntQueueArr){
				if(queues != null && queues.length > 0){
					QueueIOUtils.saveQueue(queues[0].getName(), queues);
				}
			}
		} catch (Exception e2) {
			logger.info("Exception: " + e2.getMessage());
		}
	}
	
	public static boolean queueArrIsBlank(ArrayList<MsgQueueITF[]> listQueues) {
		if(listQueues == null || listQueues.size() ==0){
			return true;
		}
		boolean rs = true;
		for(MsgQueueITF queue[] : listQueues){
			if(queueArrIsBlank(queue) == false){ 
				rs = false;
				break;
			}
		}
		return rs;
	}
	
	public static boolean queueArrIsBlank(MsgQueueITF[] queues) {
		if(queues == null || queues.length ==0){
			return true;
		}
		boolean rs = true;
		for(MsgQueueITF queue : queues){
			if(queue.size() > 0){
				rs = false;
				break;
			}
		}
		return rs;
	}
	
	public static boolean queuesIsBlank(ArrayList<MsgQueueITF> listQueues){
		if(listQueues == null || listQueues.size() ==0){
			return true;
		}
		boolean rs = true;
		for(MsgQueueITF queue : listQueues){
			if(queue.size() > 0){
				rs = false;
				break;
			}
		}
		return rs;
	}

	public static void waitForQueueSmall(MsgQueueITF itemQueue, int size){
		while(itemQueue.size() > size){
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
		DOMConfigurator.configure(new File("./config/", "log4j.xml").getPath());
		MsgQueueITF moQueue = QueueIOUtils.loadQueue(new MsgQueue("moQueue")); 
		System.out.println(moQueue.size());
		QueueIOUtils.saveQueue(moQueue);
		
		MsgQueueITF mo[] = QueueIOUtils.loadQueue("abc", new MsgQueue[1]);
		QueueIOUtils.saveQueue("abc", mo);
		System.out.println(mo.length);
	}

	public static void saveQueue(MsgQueueITF queue) {
		if (queue == null || queue.size() == 0) {
			logger.info("Queue is blank => ignore save");
			return;
		}
		writeObject(BaseUtils.getMyDir() + "./dats/" + queue.getName() + ".dat", queue);
		logger.info("Saved " + queue.getName() + " to file");
	}
	
	public static void saveQueue(String fileName, MsgQueueITF queue[]) {
		if (queue == null || queue.length == 0) {
			logger.info("Queue is blank => ignore save");
			return;
		}
		writeObject(BaseUtils.getMyDir() + "./dats/" + fileName + ".dat", queue);
		logger.info("Saved " + fileName + "[] to file");
	}
	
	public static MsgQueueITF loadQueue(MsgQueueITF queue) {
		return loadQueue(false, false, queue);
	}

	public static MsgQueueITF loadQueue(boolean saveSize, boolean saveOnExit, MsgQueueITF queue) {
		MsgQueueITF q = null;
		try {
			q = (MsgQueueITF) readObject(BaseUtils.getMyDir() + "./dats/" + queue.getName() + ".dat", true);
		} catch (Throwable e) {
		}
		if(q == null || q.size() == 0){
			q = queue;
		}
		logger.info("Load queue " + q.getName() + ", size = " + q.size());
		if(saveOnExit){
			listMntQueues.add(q);
		}
		if(saveSize){
			QueueMonitor.addObject(q);
		}
		return q;
	}
	
	public static MsgQueueITF[] loadQueue(String fileName, MsgQueueITF queue[]) {
		return loadQueue(false, false, fileName, queue);
	}
	
	private static MsgQueueITF[] loadQueue(boolean saveSize, boolean saveOnExit, String fileName, MsgQueueITF queue[]) {
		MsgQueueITF []q = null;
		try {
			q = (MsgQueueITF[]) readObject(BaseUtils.getMyDir() + "./dats/" + fileName + ".dat", true);
		} catch (Throwable e) {
		}
		if(q == null){
			q = queue;
		} else if(q.length != queue.length){
			q = queue;
		}
		logger.info("Load queue " + fileName + "[], length = " + q.length);
		if(saveOnExit){
			listMntQueueArr.add(q);
		}
		if(saveSize){
			for(int i =0 ; queue != null && i < queue.length; i ++){
				QueueMonitor.addObject(queue[i].getName() + "." + i, queue[i]);
			}
		}
		return q;
	}

	// =============================================================
	private static void writeObject(String fileName, Object obj) {
		byte []b = serializeObject(obj);
		if(b != null && b.length > 0){
			writeBytes(b, fileName);
		} else {
			try {
				FileOutputStream fout = new java.io.FileOutputStream(fileName, false); 
				fout.close();
			} catch (Exception e2) {
				logger.info("Exception: " + e2.getMessage());
			}
		}
	}
	
	private static void writeBytes(byte[] b, String fileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			fos.write(b);
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage());
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e2) {
			}
		}
	}
	
	private static Object deserializeObject(byte[] data) {
		ByteArrayInputStream in = null;
		ObjectInputStream is = null;
		try {
			in = new ByteArrayInputStream(data);
			is = new ObjectInputStream(in);
			return is.readObject();
		} catch (Exception e) {
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	private static byte[] serializeObject(Object obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			byte[] yourBytes = bos.toByteArray();
			return yourBytes;
		} catch (Exception e) {
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
			}
			try {
				bos.close();
			} catch (IOException ex) {
			}
		}
		return null;
	}
	
	private static byte[] readBytes(String fileName) {
		File file = new File(fileName);
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[1024];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
			while ((read = ios.read(buffer)) != -1)
				ous.write(buffer, 0, read);
		} catch (IOException e) {
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException e) {
				logger.info("Exception: " + e.getMessage());
			}
			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
				logger.info("Exception: " + e.getMessage());
			}
		}
		return ous.toByteArray();
	}
	
	private static Object readObject(String fileName, boolean clearAfterRead){
		byte[] b = readBytes(fileName);
		Object obj = deserializeObject(b);
		if(obj != null && clearAfterRead){
			try {
				FileOutputStream fout = new java.io.FileOutputStream(fileName, false); 
				fout.close();
			} catch (Exception e2) {
			}
		}
		return obj;
	}
}
