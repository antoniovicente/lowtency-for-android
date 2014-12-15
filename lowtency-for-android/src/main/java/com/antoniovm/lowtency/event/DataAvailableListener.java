/**
 * 
 */
package com.antoniovm.lowtency.event;

/**
 * @author Antonio Vicente Martin
 *
 */
public interface DataAvailableListener {

	public void onDataAvailableListener(byte[] data, int sampleSize);

}
