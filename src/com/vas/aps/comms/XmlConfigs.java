package com.vas.aps.comms;

import java.util.ArrayList;

import com.ligerdev.appbase.utils.textbase.ConfigsReader;
import com.vas.aps.entity.MoSyntax;

public class XmlConfigs extends ConfigsReader {
	
	public static int MAX_QUESTION_PER_CHANNEL;
	public static ArrayList<String> EXP_FIRST_QUESTION;
	public static String PUBLISH_WS;
	public static String FOLDER_MONFEE_CDR;
	
	public static class MpsListener {
		public static String MO;
		public static String SUBSCRIBE;
		public static String CONTENT;
		public static String MONTHFEE;
	}
	
	public static class MtSender {
		public static String URL;
		public static String USER;
		public static String PASS;
		public static String SHORTCODE;
		public static String ALIAS;
		public static String PARAMS;
		public static boolean FAKE;
		public static int MAX_TPS;
	}
	
	public static class Syntax {
		public static MoSyntax HELP;
		public static MoSyntax ANSWER;
		public static MoSyntax LAST_QUESTION;
		public static MoSyntax IGNORE_QUESTION;
		public static MoSyntax ACCEPT_QUESTION;
		public static MoSyntax VIEW_SCORE;
	}

	public static class Score {
		public static int REGISTER;
		public static int REREGISTER;
		public static int RENEW;
		public static int ANSWER;
	}
	
	public static class SubsUpdate {
		public static String URL;
	}
	
	@Override
	protected void readPropeties() {
		
		Syntax.HELP = getMoSyntax("help");
		Syntax.ANSWER = getMoSyntax("answer");
		Syntax.LAST_QUESTION = getMoSyntax("last_question");
		Syntax.IGNORE_QUESTION = getMoSyntax("ignore_question");
		Syntax.ACCEPT_QUESTION = getMoSyntax("accept_question");
		Syntax.VIEW_SCORE = getMoSyntax("view_score");
		
		Score.REGISTER = getInt("score|register");
		Score.REREGISTER = getInt("score|reregister");
		Score.RENEW = getInt("score|renew");
		Score.ANSWER = getInt("score|answer");
		
		MpsListener.MO = getString("mps_listener|mo");
		MpsListener.SUBSCRIBE = getString("mps_listener|subscribe");
		MpsListener.CONTENT = getString("mps_listener|content");
		MpsListener.MONTHFEE = getString("mps_listener|monthfee");
		
		PUBLISH_WS = getString("publish_ws");
		MAX_QUESTION_PER_CHANNEL = getInt("max_question");
		SubsUpdate.URL = getString("subs_update|url");
		EXP_FIRST_QUESTION = getList("first_question|expression");
		FOLDER_MONFEE_CDR = getString("folder_monfee_cdr");
		
		MtSender.MAX_TPS = getIntAttr("mt_sender|max_tps");
		MtSender.FAKE = getBooleanAttr("mt_sender|fake");
		MtSender.URL = getString("mt_sender|url");
		MtSender.USER = getString("mt_sender|user");
		MtSender.PASS = getString("mt_sender|pass");
		MtSender.ALIAS = getString("mt_sender|alias");
		MtSender.SHORTCODE = getString("mt_sender|shortcode");
		MtSender.PARAMS = getString("mt_sender|params");
	}
	
	private MoSyntax getMoSyntax(String key){
		String syntaxs[] = getString("mo_syntax|" + key).split(";");
		ArrayList<String> listSyntax = new ArrayList<String>();
		for(String str : syntaxs){
			listSyntax.add(str);
		}
		return new MoSyntax(listSyntax, key); 
	}
}
