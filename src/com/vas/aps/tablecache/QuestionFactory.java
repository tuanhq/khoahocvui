package com.vas.aps.tablecache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.ligerdev.appbase.utils.textbase.StringToolUtils;
import com.ligerdev.appbase.utils.threads.AbsTimer;
import com.vas.aps.comms.AlertUtils;
import com.vas.aps.comms.AppUtils;
import com.vas.aps.db.orm.Question;
import com.vas.aps.db.orm.Subscriber;

public class QuestionFactory {

	private static Logger logger = Log4jLoader.getLogger();
	private static BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	private static Random random = new Random();
	private static Hashtable<String, Question> hashQuestions = null;
	private static String ids = "";
	public static void initStatic(){};
	
	static {
		new AbsTimer(1000 * 60 * 3) {
			@Override
			public void execute(long counter) {
				QuestionFactory.load(counter == 1);
			}
		};
	}

	private QuestionFactory() {
	}

	private static void load(boolean isFirstLoad) {
		String transid = "ReloadQuestion";
		try {
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			String idsTemp = "";
			Hashtable<String, Question> hashTemp = new Hashtable<String, Question>();
			try {
				String sql = "SELECT * FROM QUESTION WHERE STATUS = 1 ORDER BY CREATED_TIME DESC";
				conn = baseDAO.getConnection();
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				
				while (rs.next()) {
					try {
						Question obj = new Question();
						obj.setId(rs.getInt("ID"));
						String content = rs.getString("CONTENT");
						// content = StringToolUtils.unicode2ASII(content);
						content = content.replace("\n", "\\n");
						
						content = StringToolUtils.removeSundryChars(content);
						obj.setContent(content);
						obj.setResult(rs.getString("RESULT"));
						obj.setCreatedTime(rs.getTimestamp("CREATED_TIME"));
						obj.setStatus(rs.getInt("STATUS"));
						obj.setConfirmCorrectMt(rs.getString("CONFIRM_CORRECT_MT"));
						obj.setConfirmWrongMt(rs.getString("CONFIRM_WRONG_MT"));
						String validAnswer = rs.getString("VALID_ANSWER");
						validAnswer = ";" + validAnswer.replace(" ", "") + ";";
						obj.setValidAnswer(validAnswer);
						hashTemp.put(obj.getId() + "", obj);
						idsTemp += ";" + obj.getId();
						
					} catch (Exception e) {
						logger.info(transid + ", Exception" + e.getMessage(), e);
						AlertUtils.alert("transid = " + transid + ", APS", "Exception, msg = " + e.getMessage());
					}
				}
			} catch (Exception e) {
				logger.info(transid + ", Exception" + e.getMessage(), e);
				AlertUtils.alert("APS", "transid = " + transid + ", Exception, msg = " + e.getMessage());
			} finally {
				baseDAO.releaseAll(rs, stmt, conn);
			}
			if (hashTemp == null || hashTemp.size() == 0) {
				throw new Exception();
			}
			idsTemp += ";";
			QuestionFactory.hashQuestions = hashTemp;
			QuestionFactory.ids = idsTemp;
			
			if(isFirstLoad){
				logger.info("Found " + hashTemp.size() + " questions. ids = " + idsTemp);
			}
		} catch (Exception e) {
			logger.info("Can not load questions, " + e.getMessage(), e);
			AlertUtils.alert("APS", "Can not load questions, ex = " + e.getMessage());
		}
	}

	public static Question getQuestion(String transid, Subscriber subs, String channel) {
		Question q = getQuestion(subs.getQuesReceived());
		if (BaseUtils.isBlank(subs.getQuesReceived())) {
			subs.setQuesReceived(";");

		} else if (subs.getQuesReceived().length() >= 4500) {
			logger.info(transid + ", Question received is too long => reset it now, msisdn = " 
					+ subs.getMsisdn() + ", questionReceiveds = " + subs.getQuesReceived());
			subs.setQuesReceived(";");
		}
		if (!subs.getQuesReceived().contains(";" + q.getId() + ";")) {
			subs.setQuesReceived(subs.getQuesReceived() + q.getId() + ";");
		}
		AppUtils.setLastQuesId(subs, channel, q.getId());
		logger.info(transid + ", find question for msisdn = " + subs.getMsisdn() + ", return quesId = " + q.getId());
		return q;
	}

	private static Question getQuestion(String readQuestionIds) {
		if (readQuestionIds == null) {
			readQuestionIds = "";
		}
		String idsUnread = ids;
		String[] readIds = readQuestionIds.split(";");
		for (int i = 0; i < readIds.length; i++) {
			if (BaseUtils.isNotBlank(readIds[i])) {
				idsUnread = idsUnread.replace(";" + readIds[i] + ";", ";");
			}
		}
		if (";".equals(idsUnread) || BaseUtils.isBlank(idsUnread)) {
			idsUnread = ids;
		}
		idsUnread = idsUnread.replaceFirst(";", "");
		idsUnread = idsUnread.substring(0, idsUnread.length() - 1);

		String[] unreadIdsArray = idsUnread.split(";");
		int temp = unreadIdsArray.length;
		if (temp > 10) {
			temp = 10;
		}
		int rd = random.nextInt(temp);
		String keyID = unreadIdsArray[rd];
		return getQuestionById(keyID);
	}

	private static Question getQuestionById(String id) {
		Question question = (Question) hashQuestions.get(id);
		return question;
	}

	public static Question getQuestionById(int id) {
		Question question = (Question) hashQuestions.get(id + "");
		return question;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			Question q = getQuestion("2;3");
			System.out.println(q.getId());
		}
	} 
}
