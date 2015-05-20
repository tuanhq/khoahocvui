/**
 * 
 */
package org.thq.api;

import org.thq.logic.RequestDispatcher;

/**
 * @author tuanhq
 *
 */
public class RequestReceiveMps implements RequestReceive {

	/* (non-Javadoc)
	 * @see org.thq.api.RequestReceive#request(java.lang.String, java.lang.String)
	 */
	/**
	 * 
	 */
	private RequestDispatcher request;
	public RequestReceiveMps() {
		request = new RequestDispatcher();
	}
	
	@Override
	public void request(String msisdn, String mo) {
	
		request.executeMo(msisdn, mo);
	}

}
