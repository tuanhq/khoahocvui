package com.vas.aps.processor;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.textbase.ReqCountUtils;
import com.ligerdev.appbase.utils.threads.AbsProcessor;
import com.ligerdev.appbase.utils.traffic.SendTraffic;
import com.vas.aps.MainAps;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.comms.XmlConfigs;
import com.vas.aps.db.orm.MtHis;
import com.vas.aps.db.orm.Question;
import com.vas.aps.db.orm.Subscriber;
import com.vas.aps.tablecache.QuestionFactory;

public class ReturnFirstQues extends AbsProcessor {

	private MainAps mainApp = MainAps.getInstance();
	private BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	private ReqCountUtils reqCountUtils = ReqCountUtils.getInstance("ReturnFirstQues", "F");
	private SendTraffic sendTraffic = SendTraffic.getInstance("ReturnFirstQues");
	
	
	@Override
	public void execute() {
		Subscriber subs = (Subscriber) mainApp.getFirstQuesQueue().removeFirst();
		transid = reqCountUtils.countLongStr() + "@" + BaseUtils.formatMsisdn(subs.getMsisdn(), "84", "");
		sendTraffic.limitTraffic(XmlConfigs.MtSender.MAX_TPS);
		
		Question question = QuestionFactory.getQuestion(transid, subs, AppConstants.CHANNEL_SMS);
		logger.info(transid + ", returning first question (ID = " + question.getId() + ")");
		String content = question.getContent();
		MtHis mt = new MtHis(transid, 0, subs.getMsisdn(), content, 0, "QT-" + question.getId(), null, null);
		mainApp.getMtBroadcastQueue().addLast(mt);
		
		baseDAO.updateBeanFields(transid, subs, true, "LAST_QUES_SMS", "QUES_RECEIVED");
	}

	@Override
	public int sleep() {
		return 0;
	}

	@Override
	public void exception(Throwable e) {
	}
}
