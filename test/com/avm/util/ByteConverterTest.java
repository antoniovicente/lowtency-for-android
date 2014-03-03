/**
 * 
 */
package com.avm.util;

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
	
	/*
	 * @Test public void testToByteArrayLittleEndian() { int test = 0x04030201;
	 * byte [] out = {4, 3, 2, 1}; Assert.assertTrue(Arrays.equals(out,
	 * ByteConverter.toBytesArray(test,true))); }
	 * 
	 * @Test public void testToByteArrayBigEndian() { int test = 0x04030201;
	 * byte [] out = {1, 2, 3, 4}; Assert.assertTrue(Arrays.equals(out,
	 * ByteConverter.toBytesArray(test,false))); }
	 */

}
