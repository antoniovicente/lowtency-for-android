/**
 * 
 */
package com.avm.util;

/**
 * @author Antonio Vicente Martin
 * 
 *         This class is a tool to convert or split primitives types into bytes
 *         arrays
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
	public static byte[] toBytesArray(int value) {
		return toBytesArray(value, 4);
	}
	
	/**
	 * Returns a new array of bytes with the splitted value
	 * @param value The vale to convert
	 * @param numBytes The number of bytes
	 * @return bytes The array of bytes
	 */
	public static byte[] toBytesArray(long value, int numBytes) {
		byte[] bytes = new byte[numBytes];

		return toBytesArray(value, bytes);
	}
	
	/**
	 * Returns a value splitted into a specified byte array
	 * @param value The long value to split
	 * @param bytes The array of bytes to write in
	 * @return bytes The value's bytes
	 */
	public static byte[] toBytesArray(long value, byte [] bytes) {
		return toBytesArray(value, bytes, 0, bytes.length);
	}
	
	/**
	 * Returns a value splitted into a specified byte array
	 * @param value The long value to split
	 * @param bytes The array of bytes to write in
	 * @param start The start iteration index
	 * @param end The end iterarion index
	 * @return bytes The value's bytes
	 */
	public static byte[] toBytesArray(long value, byte [] bytes, int start, int end) {
		for (int i = start; i < end; i++) {
			bytes[i] = getByteAt(value, i);
		}

		return bytes;
	}
	
	/**
	 * Return an integer built from an array of bytes
	 * @param bytes The raw bytes
	 * @param index The initial position
	 * @param littleEndian The byte order
	 * @return The int value
	 */
	public static int toIntValue(byte [] bytes, int index, boolean littleEndian) {
		int value = 0;
		
		int i = index;
		int sum = 1;
		
		if (littleEndian) {
			// Reverse iteration
			i = Math.min(bytes.length ,4 + index);
			sum = -1;
		}
		
		for (; i < 4 && i < bytes.length && i >= index; i+=sum) {
			value += bytes[i] << 8*i;
		}
		
		return value;

	}

}
