/**
 * 
 */
package com.avm.util;

/**
 * @author Antonio Vicente Martin
 * 
 *         This class is a tool to convert or split primitives types into bytes
 *         values
 */
public class ByteConverter {

	/**
	 * Returns the corresponding byte inside the value
	 * 
	 * @param value
	 * @param position
	 * @return
	 */
	public static byte getByteAt(long value, int position) {
		byte byteSelected = (byte) (value >> 8 * position);
		return byteSelected;
	}

	/**
	 * Returns an int value splitted into a byte array
	 * @param value The int value
	 * @param bytes 
	 * @return bytes The value's bytes
	 */
	public static byte[] toBytesArray(int value, boolean littleEndian) {
		return toBytesArray(value, 4, littleEndian);
	}
	
	/**
	 * Returns a value splitted into a byte array
	 * @param value
	 * @param numBytes
	 * @return bytes The value's bytes
	 */
	private static byte[] toBytesArray(long value, int numBytes,  boolean littleEndian) {
		byte[] bytes = new byte[numBytes];
		
		int i = 0;
		int sum = 1;
		
		if (littleEndian) {
			// Reverse endian iteration
			i = numBytes - 1;
			sum = -1;
		}
		
		for (int j = 0; i < bytes.length && i >= 0; i += sum) {
			bytes[j++] = getByteAt(value, i);
		}

		return bytes;
	}

}
