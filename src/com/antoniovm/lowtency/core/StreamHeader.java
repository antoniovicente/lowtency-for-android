/**
 * 
 */
package com.antoniovm.lowtency.core;

import com.antoniovm.lowtency.util.ByteConverter;

/**
 * @author Antonio Vicente Martin
 *
 */
public class StreamHeader {

	private int bufferSize;

	/**
	 * 
	 */
	public StreamHeader(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * 
	 */
	public StreamHeader() {

	}

	/**
	 * @param bufferSize
	 *            the bufferSize to set
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * @return the bufferSize
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	/**
	 * Returs a byte array built from the properties of this class
	 * 
	 * @return
	 */
	public byte[] getSerialized() {
		byte[] serializedHeader = new byte[4];

		ByteConverter.toBytesArray(bufferSize, serializedHeader, true);

		return serializedHeader;
	}

	public static StreamHeader buildFromSerialized(byte[] serializedHeader, StreamHeader streamHeader) {

		int bufferSize = ByteConverter.toIntValue(serializedHeader, 0, true);
		streamHeader.setBufferSize(bufferSize);

		return streamHeader;
	}

}
