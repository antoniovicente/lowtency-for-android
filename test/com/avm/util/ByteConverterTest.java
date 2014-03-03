/**
 * 
 */
package com.avm.util;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Antonio Vicente Martin
 *
 */
public class ByteConverterTest {

	@Test
	public void testGetByteAt0() {
		int test = 0x04030201;
		Assert.assertEquals(1, ByteConverter.getByteAt(test, 0));
	}
	
	@Test
	public void testGetByteAt1() {
		int test = 0x04030201;
		Assert.assertEquals(2, ByteConverter.getByteAt(test, 1));
	}
	
	@Test
	public void testGetByteAt2() {
		int test = 0x04030201;
		Assert.assertEquals(3, ByteConverter.getByteAt(test, 2));
	}
	
	@Test
	public void testGetByteAt3() {
		int test = 0x04030201;
		Assert.assertEquals(4, ByteConverter.getByteAt(test, 3));
	}
	
	@Test
	public void testToByteArrayLittleEndian() {
		int test = 0x04030201;
		byte[] expected = { 4, 3, 2, 1 };
		Assert.assertTrue(Arrays.equals(expected, ByteConverter.toBytesArray(test, true)));
	}

	@Test
	public void testToByteArrayBigEndian() {
		int test = 0x04030201;
		byte[] expected = { 1, 2, 3, 4 };
		Assert.assertTrue(Arrays.equals(expected, ByteConverter.toBytesArray(test, false)));
	}

	@Test
	public void testToIntValueLittleEndian() {
		byte[] test = { 4, 3, 2, 1 };
		int expected = 0x04030201;
		Assert.assertEquals(expected, ByteConverter.toIntValue(test, 0, true));
	}

	@Test
	public void testToIntValueBigEndian() {
		int expected = 0x04030201;
		byte[] test = { 1, 2, 3, 4 };
		Assert.assertEquals(expected, ByteConverter.toIntValue(test, 0, false));
	}

}
