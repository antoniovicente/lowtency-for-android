/**
 * 
 */
package com.avm.util;

/**
 * @author Antonio Vicente Martin
 *
 * This class is a tool to convert or split primitives types into bytes values
 */
public class ByteConverter { 
	
	/**
	 * Returns the corresponding byte inside the value
	 * @param value
	 * @param position
	 * @return
	 */
	public static byte getByteAt(int value, int position) {
		byte byteSelected = (byte)(value >> 8*position); 
		return byteSelected;
	}
	
}
