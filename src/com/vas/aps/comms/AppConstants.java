package com.vas.aps.comms;

public class AppConstants {
	
	// charge reminder
	public static final int REMINDER_TYPE_RETURN_QUES = 1;
	public static final int REMINDER_TYPE_ADD_SCORE = 2;

	// subs status
	public static final int SUBS_STATUS_ACTIVE = 1;
	public static final int SUBS_STATUS_DEACTIVE = 3;
	
	
	//channel
	public static final String CHANNEL_SMS = "SMS";
	public static final String CHANNEL_WAP = "WAP";
	public static final String CHANNEL_WEB = "WEB";
	public static final String CHANNEL_SYS = "SYS";
	
	//buy question
	
	public static final int  numberQuestionBuy = 5;
	
	
	// score his
	public static final String SCORE_HIS_REASON_CMS = "CMS";
	public static final String SCORE_HIS_REASON_RENEW = "RENEW";
	public static final String SCORE_HIS_REASON_ANSWER = "ANSWER";
	public static final String SCORE_HIS_REASON_REGISTER = "REGISTER";
	public static final String SCORE_HIS_REASON_RE_REGISTER = "RE-REGISTER";
	
	
	// mt
	public static String MT_HELP = "5.2";
	public static String MT_WRONG_SYNTAX = "5.8";
	public static String MT_REQUEST_WHEN_NOT_ACTIVE = "100";
	public static String MT_IGNORE_RECEIVE_QUESTION = "5.4";
	public static String MT_ACCEPT_RECEIVE_QUESTION = "5.5";
	public static String MT_SYSTEM_ERROR = "5.9";
	public static String MT_VIEW_SCORE = "5.6";
	//mt buycontent
	public static String MT_X2_WHEN_ACTIVE_X2 = "5.11";
	public static String MT_X2_WHEN_SUCCESS = "5.12";
	public static String MT_MUA_WHEN_SUCCESS = "5.13";
	
	// mt answer
	public static String MT_ANSWER_WHEN_NOT_RECEIVED_QUESTION = "4.4";
	public static String MT_ANSWER_LAST_QUESTION = "4.3";
	public static String MT_ANSWER_CORRECT = "4.1";
	public static String MT_X2_WHEN_ACTIVE = "4.12";
	public static String MT_ANSWER_CORRECTB2 = "4.13";
	public static String MT_ANSWER_CORRECTB3 = "4.14";
	public static String MT_ANSWER_CORRECTND = "4.15";
	public static String MT_ANSWER_WRONG = "4.2";
	// mt register
	public static String MT_REGISTER_FIRST_MT_1 = "1.1.1";
	public static String MT_REGISTER_FIRST_MT_2 = "1.1.2";
	public static String MT_REGISTER_SECOND_TODAY = "1.2-1.3";
	public static String MT_REGISTER_SECOND_ANOTHER_DAY = "1.4";
	public static String MT_REGISTER_WHEN_ACTIVE = "1.5";
	// mt unregister
	public static String MT_UNREGISTER = "2.1";
	public static String MT_UNREGISTER_WHEN_NOT_ACTIVE = "2.2";
	public static String MT_UNREGISTER_BY_SYSTEM = "5.9";
	
	// reason cdr
	public static String CDR_REASON_UNREGISTER = "UNREGISTER";
	public static String CDR_REASON_REGISTER = "REGISTER";
	public static String CDR_REASON_RE_REGISTER = "RE-REGISTER";
	public static String CDR_REASON_RENEW = "RENEW";
	public static String CDR_REASON_ANSWER = "ANSWER";
	public static String CDR_REASON_BUY_QUESTION_MUA = "MUA";
	public static String CDR_REASON_BUY_QUESTION_DOI = "DOI";
	public static String CDR_REASON_BUY_QUESTION_X2 = "X2";
	
	// parameter to replace
	public static String PARAMS_DAY_SCORE = "[DAY_SCORE]";
	public static String PARAMS_WEEK_SCORE = "[WEEK_SCORE]";
	public static String PARAMS_MONTH_SCORE = "[MONTH_SCORE]";
	public static String PARAMS_TOTAL_SCORE = "[TOTAL_SCORE]";
	public static String PARAMS_CURRENT_MONTH = "[CUR_MONTH]";
	public static String PARAMS_CURRENT_TIME = "{hh:mm:ss}";
	public static String PARAMS_CURRENT_DATE = "{dd/mm/yyy}";
	
}
