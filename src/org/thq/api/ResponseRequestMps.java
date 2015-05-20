/**
 * 
 */
package org.thq.api;

/**
 * @author tuanhq
 *
 */
public class ResponseRequestMps implements ResponeRequest{

	/* (non-Javadoc)
	 * @see org.thq.api.ResponeRequest#responeRequest(java.lang.String, java.lang.String)
	 */
	@Override
	public void responeRequest(String msisdn, String mt) {
		System.out.println("Da send message: " + mt + " toi thue bao: " + msisdn );
		
	}

	

}
