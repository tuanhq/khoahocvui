package com.vas.aps.cmd;

import org.apache.log4j.Logger;

import com.ligerdev.appbase.utils.BaseUtils;
import com.ligerdev.appbase.utils.db.BaseDAO;
import com.ligerdev.appbase.utils.http.HttpClientUtils;
import com.ligerdev.appbase.utils.textbase.Log4jLoader;
import com.ligerdev.appbase.utils.textbase.SyncStrManager;
import com.vas.aps.MainAps;
import com.vas.aps.api.CmdResult;
import com.vas.aps.comms.AlertUtils;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.db.orm.MoHis;

public abstract class AbstractCmd {

	protected static Logger logger = Log4jLoader.getLogger();
	protected static HttpClientUtils httpClientUtils = HttpClientUtils.getInstance(8000);
	protected static MainAps mainApp = MainAps.getInstance();
	protected static SyncStrManager syncStrManager = SyncStrManager.getInstance("AbstractCMD");
	protected static BaseDAO baseDAO = BaseDAO.getInstance(BaseDAO.POOL_NAME_MAIN);
	
	protected boolean mustCallMps = false;
	protected String channel = AppConstants.CHANNEL_SMS;
	protected MoHis mo = null;
	protected CmdResult resultCmd = null;
	protected int amount;

	public AbstractCmd() {
	}

	public CmdResult execute() {
		resultCmd = new CmdResult(mo.getTransId(), mo.getMsisdn(), "0", "success");
		Object objSynch = syncStrManager.createSynchObject(mo.getMsisdn());
		
		synchronized (objSynch) {
			CmdResult rs = null;
			long l1 = System.currentTimeMillis();
			try {
				rs = execute0();
			} catch (Exception e) {
				logger.info(mo.getTransId() + ", Exception: " + e.getMessage(), e);
				AlertUtils.alert("APS", mo.getTransId() + ", Exception, msg = " + e.getMessage());
				
				rs = new CmdResult();
				rs.setErrorCode("404");
				rs.setErrorDesc("Exception: " + e.getMessage());
			}
			long l2 = System.currentTimeMillis();
			logger.info(this.mo.getTransId() + ", execCmd done, resultCode = " 
					+ String.valueOf(rs) + ", time = " + BaseUtils.getDurations(l2, l1));
			return rs;
		}
	}

	protected abstract CmdResult execute0() throws Exception;

	public MoHis getMo() {
		return mo;
	}

	public void setMo(MoHis mo) {
		this.mo = mo;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public boolean isMustCallMps() {
		return mustCallMps;
	}

	public void setMustCallMps(boolean mustCallMps) {
		this.mustCallMps = mustCallMps;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
