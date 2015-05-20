package com.vas.aps.tablecache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.ligerdev.appbase.utils.threads.AbsTimer;
import com.vas.aps.MainAps;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.db.orm.Message;
import com.vas.aps.db.orm.MoHis;
import com.vas.aps.db.orm.MtHis;
import com.vas.aps.db.orm.Subscriber;

public class MessageFactory {
	
	protected static Logger logger = Log4jLoader.getLogger();
	private static ConcurrentHashMap<String, ArrayList<Message>> listData = null;
	private static MainAps mainApp = MainAps.getInstance();
	private static BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	private static Random random = new Random();
	public static void initStatic(){};
	
	static {
		new AbsTimer(1000 * 60 * 2) {
			@Override
			public void execute(long counter) {
				MessageFactory.load();
			}
		};
	}
	
	private MessageFactory() {
	}

	private static void load() {
		String sql = "select * from MESSAGE where STATUS = 1";
		ArrayList<Message> listMsg = baseDAO.getListBySql("ReloadMsg", Message.class, sql, null	, null);
		ConcurrentHashMap<String, ArrayList<Message>> listDataTemp = new ConcurrentHashMap<String, ArrayList<Message>>();
		
		for(Message msg : listMsg){
			ArrayList<Message> listChild = listDataTemp.get(msg.getCode());
			if(listChild == null){
				listChild = new ArrayList<Message>();
				listDataTemp.put(msg.getCode(), listChild);
			}
			listChild.add(msg);
		}
		listData = listDataTemp;
	}

	public static MtHis getMessage(MoHis mo, String code, Subscriber subs){
		return getMessage(mo.getTransId(), mo.getMsisdn(), mo.getId(), code, subs);
	}
	
	public static MtHis getMessage(String transid, String msisdn, int moId, String code, Subscriber subs){
		try {
			ArrayList<Message> listMsg = listData.get(code);
			Message msg = listMsg.get(random.nextInt(listMsg.size()));
			String content = msg.getContent();
			
			if(subs != null){
				content = content.replace(AppConstants.PARAMS_DAY_SCORE, String.valueOf(subs.getDayScore()));
				content = content.replace(AppConstants.PARAMS_WEEK_SCORE, String.valueOf(subs.getWeekScore()));
				content = content.replace(AppConstants.PARAMS_MONTH_SCORE, String.valueOf(subs.getMonthScore()));
				content = content.replace(AppConstants.PARAMS_TOTAL_SCORE, String.valueOf(subs.getTotalScore()));
				msg.setContent(content);
			}
			if(content.contains(AppConstants.PARAMS_CURRENT_MONTH)){
				 int curMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
				 content = content.replace(AppConstants.PARAMS_CURRENT_MONTH, String.valueOf(curMonth));
				 msg.setContent(content);
			}
			return new MtHis(transid, 0, msisdn, msg.getContent(), moId, code, null, null);
			
		} catch (Exception e) {
			logger.info("Not found message with code = " + code);
		}
		return null;
	}
}
