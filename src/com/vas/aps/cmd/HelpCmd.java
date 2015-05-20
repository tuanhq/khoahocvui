package com.vas.aps.cmd;

import com.vas.aps.api.CmdResult;
import com.vas.aps.comms.AppConstants;
import com.vas.aps.db.orm.MtHis;
import com.vas.aps.tablecache.MessageFactory;

public class HelpCmd extends AbstractCmd {

	@Override
	public CmdResult execute0() throws Exception {
		MtHis mt = MessageFactory.getMessage(mo, AppConstants.MT_HELP, null);
		mainApp.getMtQueue().addLast(mt);
		
		resultCmd.addMt(mt);
		return resultCmd;
	}
}
