package com.vas.aps;

import javax.xml.ws.Endpoint;

import com.ligerdev.appbase.utils.http.HttpServerUtils;
import com.ligerdev.appbase.utils.quartz.QuartzUtils;
import com.ligerdev.appbase.utils.queues.MsgDelayQueue;
import com.ligerdev.appbase.utils.queues.MsgQueue;
import com.ligerdev.appbase.utils.queues.MsgQueueITF;
import com.ligerdev.appbase.utils.queues.QueueIOUtils;
import com.ligerdev.appbase.utils.textbase.ConfigsReader;
import com.ligerdev.appbase.utils.threads.AbsApplication;
import com.vas.aps.api.WsApi;
import com.vas.aps.api.mps.WsBuyContent;
import com.vas.aps.api.mps.WsMoListener;
import com.vas.aps.api.mps.WsSubscribe;
import com.vas.aps.comms.XmlConfigs;
import com.vas.aps.processor.CreateTblDynamic;
import com.vas.aps.processor.LoadFirstQues;
import com.vas.aps.processor.LoadMtBroadcast;
import com.vas.aps.processor.ProcessCdrMonfee;
import com.vas.aps.processor.ResetDaily;
import com.vas.aps.processor.ReturnFirstQues;
import com.vas.aps.processor.SaveCdrHis;
import com.vas.aps.processor.SaveMtHis;
import com.vas.aps.processor.SaveScoreHis;
import com.vas.aps.processor.ScanMonfeeCdr;
import com.vas.aps.processor.SendSms;
import com.vas.aps.tablecache.MessageFactory;
import com.vas.aps.tablecache.QuestionFactory;

@SuppressWarnings("serial")
public class MainAps extends AbsApplication {
	
	private MsgQueueITF chargeReminderQueue = QueueIOUtils.loadQueue(true, true, new MsgQueue("chargeReminderQueue"));
	private MsgQueueITF firstQuesQueue = QueueIOUtils.loadQueue(true, true, new MsgQueue("firstQuesQueue"));
	private MsgQueueITF mtBroadcastQueue = QueueIOUtils.loadQueue(true, true, new MsgQueue("mtBroadcastQueue"));
	private MsgQueueITF cdrHisQueue = QueueIOUtils.loadQueue(true, true, new MsgQueue("cdrHisQueue"));
	private MsgQueueITF mtHisQueue = QueueIOUtils.loadQueue(true, true, new MsgQueue("mtHisQueue"));
	private MsgQueueITF scoreHisQueue = QueueIOUtils.loadQueue(true, true, new MsgQueue("scoreHisQueue"));
	private MsgQueueITF mtQueue = QueueIOUtils.loadQueue(true, true, new MsgQueue("mtQueue"));
	private MsgQueueITF mtDelay1Queue = QueueIOUtils.loadQueue(true, true, new MsgDelayQueue("mtDelay1Queue", 200000, 8000));
	private MsgQueueITF mtDelay2Queue = QueueIOUtils.loadQueue(true, true, new MsgDelayQueue("mtDelay2Queue", 200000, 16000));
	
	private MainAps(Class<? extends ConfigsReader> clazzConfig) {
		super(clazzConfig);
		AbsApplication.VERSION = "1.0.0";
	}

	@Override
	protected void initProcess() throws Exception {
		
		MessageFactory.initStatic();
		QuestionFactory.initStatic();
		ResetDaily.getInstance().start();
		LoadMtBroadcast.getInstance().start();
		CreateTblDynamic.getInstance().start();
		ScanMonfeeCdr.getInstance().start();
		
		new SaveCdrHis().start();
		new SaveMtHis().start();
		new SaveScoreHis().start();
		
		for(int i = 0; i < 2; i ++){
			new SendSms(mtQueue).start();
			new SendSms(mtDelay1Queue).start();
			new SendSms(mtDelay2Queue).start();
			new SendSms(mtBroadcastQueue).start();
		}
		for(int i = 0; i < 2; i ++){
			new ReturnFirstQues().start(); 
			new ProcessCdrMonfee().start();
		}
		QuartzUtils quartzUtils = new QuartzUtils();
		quartzUtils.put(XmlConfigs.EXP_FIRST_QUESTION, LoadFirstQues.class);
		quartzUtils.start();
		
		WsBuyContent wsBuyContent = new WsBuyContent();
		WsMoListener wsMoListener = new WsMoListener();
		WsSubscribe wsSubscribe = new WsSubscribe();
		
		Endpoint.publish(XmlConfigs.MpsListener.CONTENT, wsBuyContent);
		logger.info("Published WS API: " + XmlConfigs.MpsListener.CONTENT);
		
		Endpoint.publish(XmlConfigs.MpsListener.MO, wsMoListener);
		logger.info("Published WS API: " + XmlConfigs.MpsListener.MO);
		
		Endpoint.publish(XmlConfigs.MpsListener.SUBSCRIBE, wsSubscribe);
		logger.info("Published WS API: " + XmlConfigs.MpsListener.SUBSCRIBE);
		
		Endpoint.publish(XmlConfigs.PUBLISH_WS, new WsApi());
		logger.info("Published WS API: " + XmlConfigs.PUBLISH_WS);
	}

	@Override
	protected void endProcess() throws Exception {
	}

	public static void main(String[] args) {
		new MainAps(XmlConfigs.class);
	}

	public MsgQueueITF getCdrHisQueue() {
		return cdrHisQueue;
	}

	public void setCdrHisQueue(MsgQueueITF cdrHisQueue) {
		this.cdrHisQueue = cdrHisQueue;
	}

	public MsgQueueITF getMtDelay1Queue() {
		return mtDelay1Queue;
	}

	public void setMtDelay1Queue(MsgQueueITF mtDelay1Queue) {
		this.mtDelay1Queue = mtDelay1Queue;
	}

	public MsgQueueITF getMtQueue() {
		return mtQueue;
	}

	public void setMtQueue(MsgQueueITF mtQueue) {
		this.mtQueue = mtQueue;
	}

	public MsgQueueITF getMtDelay2Queue() {
		return mtDelay2Queue;
	}

	public void setMtDelay2Queue(MsgQueueITF mtDelay2Queue) {
		this.mtDelay2Queue = mtDelay2Queue;
	}

	public MsgQueueITF getMtHisQueue() {
		return mtHisQueue;
	}

	public void setMtHisQueue(MsgQueueITF mtHisQueue) {
		this.mtHisQueue = mtHisQueue;
	}

	public MsgQueueITF getScoreHisQueue() {
		return scoreHisQueue;
	}

	public void setScoreHisQueue(MsgQueueITF scoreHisQueue) {
		this.scoreHisQueue = scoreHisQueue;
	}

	public MsgQueueITF getFirstQuesQueue() {
		return firstQuesQueue;
	}

	public void setFirstQuesQueue(MsgQueueITF firstQuesQueue) {
		this.firstQuesQueue = firstQuesQueue;
	}

	public MsgQueueITF getMtBroadcastQueue() {
		return mtBroadcastQueue;
	}

	public void setMtBroadcastQueue(MsgQueueITF mtBroadcastQueue) {
		this.mtBroadcastQueue = mtBroadcastQueue;
	}

	public MsgQueueITF getChargeReminderQueue() {
		return chargeReminderQueue;
	}

	public void setChargeReminderQueue(MsgQueueITF chargeReminderQueue) {
		this.chargeReminderQueue = chargeReminderQueue;
	}
}
