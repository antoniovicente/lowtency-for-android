/**
 * 
 */
package com.antoniovm.lowtency.event;

/**
 * @author Antonio Vicente Martin
 *
 */
public interface ConnectionListener {

	/**
	 * Event launched when the first clients connects
	 */
	public void onFirstClientConnection();

	/**
	 * Event launched when the last clients disconnects
	 */
	public void onLastClientDisconnection();

}
