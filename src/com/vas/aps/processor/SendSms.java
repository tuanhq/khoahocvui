package com.vas.aps.processor;

import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.http.HttpClientUtils;
import com.ligerdev.appbase.utils.queues.MsgQueueITF;
import com.ligerdev.appbase.utils.textbase.StringToolUtils;
import com.ligerdev.appbase.utils.threads.AbsProcessor;
import com.ligerdev.appbase.utils.traffic.SendTraffic;
import com.vas.aps.MainAps;
import com.vas.aps.comms.XmlConfigs;
import com.vas.aps.comms.XmlConfigs.MtSender;
import com.vas.aps.db.orm.MtHis;
import com.vas.aps.mpsclient.SmsClient;

public class SendSms extends AbsProcessor {

	private MainAps mainApp = MainAps.getInstance();
	private BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	private SendTraffic sendTraffic = SendTraffic.getInstance("SendSms");
	private MsgQueueITF queue = null;
	
	public SendSms(MsgQueueITF queue) {
		this.queue = queue;
	}
	
	@Override
	public void execute() {
		MtHis mt = (MtHis) queue.removeFirst();
		sendTraffic.limitTraffic(XmlConfigs.MtSender.MAX_TPS);
		
		String content = StringToolUtils.unicode2ASII(mt.getContent()); 
		String sendResult = SmsClient.sendSms(mt.getTransId(), mt.getMsisdn(), content, mt.getCommand());
		mt.setSendResult(sendResult);
		mainApp.getMtHisQueue().addLast(mt);
	}

	@Override
	public int sleep() {
		return 0;
	}

	@Override
	public void exception(Throwable e) {
	}
}
