/**
 * 
 */
package com.antoniovm.lowtency.core;

import com.antoniovm.util.raw.ByteConverter;


/**
 * @author Antonio Vicente Martin
 *
 */
public class StreamHeader {

	private int bufferSize;

    public static final int RAW_LENGTH = 4;

    /**
     * Creates a new StreamHeader with the specified arguments
     * @param bufferSize The message size in bytes
     */
	public StreamHeader(int bufferSize) {
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
		byte[] serializedHeader = new byte[RAW_LENGTH];

        ByteConverter.toBytesArray(bufferSize, serializedHeader,0,4, true);

		return serializedHeader;
	}

    /**
     * Builds a new StreamHeader from a serialized array
     * @param serializedHeader The raw serialized fields
     * @return A new StreamHeader
     */
    public static StreamHeader buildFromSerialized(byte[] serializedHeader) {
        if (serializedHeader.length < RAW_LENGTH){
            throw new IllegalArgumentException("The minimum length must be " + RAW_LENGTH);
        }

		int bufferSize = ByteConverter.toIntValue(serializedHeader, 0, true);

		return new StreamHeader(bufferSize);
	}

}
