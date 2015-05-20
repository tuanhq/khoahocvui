package com.vas.aps.processor;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.IReadFile;
import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.textbase.ReqCountUtils;
import com.ligerdev.appbase.utils.threads.AbsProcessor;
import com.vas.aps.MainAps;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.comms.XmlConfigs;
import com.vas.aps.db.orm.CdrHis;
import com.vas.aps.db.orm.ChargeReminder;

public class ScanMonfeeCdr extends AbsProcessor {

	private MainAps mainApp = MainAps.getInstance();
	private BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	private static ReqCountUtils reqCountUtils = ReqCountUtils.getInstance("ScanMonfeeCdr", "MF");
	private static ScanMonfeeCdr instance = null;
	
	private ScanMonfeeCdr() {
	}
	
	public synchronized static ScanMonfeeCdr getInstance(){
		if(instance == null){
			instance = new ScanMonfeeCdr();
		}
		return instance;
	}
	
	@Override
	public void execute() {
		File folder = new File(XmlConfigs.FOLDER_MONFEE_CDR);
		if(folder.exists() == false){
			return;
		}
		File files[] = folder.listFiles();
		if(files == null || files.length == 0){
			return;
		}
		for(int i = 0; i < files.length; i ++){
			try {
				if(files[i].isFile() == false){
					continue;
				}
				if(files[i].getName().endsWith(".txt") == false){
					continue;
				}
				readFileCdr(files[i]);
				
				String date = BaseUtils.formatTime("yyyyMMdd", new Date());
				File folderBackup = new File(XmlConfigs.FOLDER_MONFEE_CDR + "/backup/" + date + "/");
				if(!folderBackup.exists()){
					folderBackup.mkdirs();
				}
				boolean move = files[i].renameTo(new File(XmlConfigs.FOLDER_MONFEE_CDR + "/backup/"  + date + "/" + files[i].getName()));
				logger.info("Move file " + files[i].getName() + " to backup folder, result = " + move);
				
			} catch (Exception e) {
				logger.info("Exception: " + e.getMessage(), e);
			}
		}
	}

	private void readFileCdr(File file) {
		BaseUtils.readFile(file, new IReadFile() {
			@Override
			public boolean readLine(String line) {
				if(BaseUtils.isBlank(line)){
					return true;
				}
				Hashtable<String, String> hashtable = analyzeCdr(line);
				if(hashtable == null || hashtable.size() == 0){
					return true;
				} 
				if(!"0".equals(hashtable.get("params"))){
					return true;
				}
				String msisdn = BaseUtils.formatMsisdn(hashtable.get("msisdn"), "84", "84");
				String transid = reqCountUtils.countLongStr() + "@" + BaseUtils.formatMsisdn(msisdn, "84", "");
				int amount = BaseUtils.parseInt(hashtable.get("amount"), 0);
				
				
				ChargeReminder obj = new ChargeReminder();
				obj.setMsisdn(msisdn);  
				obj.setAddScore(XmlConfigs.Score.RENEW);
				obj.setType(AppConstants.REMINDER_TYPE_RETURN_QUES);
				obj.setTransId(transid);
				mainApp.getChargeReminderQueue().addLast(obj);
				
				CdrHis cdrHis = new CdrHis(0, obj.getMsisdn(), amount, 
						AppConstants.CDR_REASON_RENEW, null, transid);
				mainApp.getCdrHisQueue().addLast(cdrHis);
				
				return true;
			}
		});
	}
	
	private Hashtable<String, String> analyzeCdr(String line){
		if(BaseUtils.isBlank(line)){
			return null;
		}
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		try {
			String tmp[] = line.split("\\|");
			for(int i = 0; i < tmp.length; i ++){
				if(!tmp[i].contains("=")){
					hashtable.put("SPC" + i, tmp[i]);
				} else {
					String tmp2[] = tmp[i].split("=");
					hashtable.put(tmp2[0].trim(), tmp2[1].trim());
				}
			}
		} catch (Exception e) {
			logger.info("Exception: " + e.getMessage(), e);
		}
		return hashtable;
	}

	@Override
	public int sleep() {
		return 1000;
	}

	@Override
	public void exception(Throwable e) {
	}
}
