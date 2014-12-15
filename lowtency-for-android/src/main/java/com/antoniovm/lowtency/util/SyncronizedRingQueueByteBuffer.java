package com.antoniovm.lowtency.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Handles a ByteBuffers concurrent safe ring queue to store byte data.
 * 
 * @author Antonio Vicente Martin
 * 
 */
public class SyncronizedRingQueueByteBuffer extends SyncronizedRingQueue<ByteBuffer> {

	private int byteBufferCapazity;
	private ByteBuffer filling;

	public SyncronizedRingQueueByteBuffer(int byteBufferCapazity) {
		this.byteBufferCapazity = byteBufferCapazity;
		prepareBuffers();
		this.filling = ByteBuffer.allocate(byteBufferCapazity);
		filling.order(ByteOrder.LITTLE_ENDIAN);
	}

	/**
	 * Inits buffer array with ByteBuffers
	 */
	private void prepareBuffers() {
		for (int i = 0; i < syncronizedBuffer.length; i++) {
			syncronizedBuffer[i] = ByteBuffer.allocate(byteBufferCapazity);
			((ByteBuffer)syncronizedBuffer[i]).order(ByteOrder.LITTLE_ENDIAN);
		}
	}

	/**
	 * Dumps raw byte data into buffer
	 * 
	 * @param array
	 */
	public void dumpData(byte[] array) {
		int endPos = Math.min(array.length,filling.remaining());
		int startPos = array.length - endPos;
		
		while (startPos < array.length) {
			filling.put(array,startPos,endPos);

			if (!filling.hasRemaining()) {
				insertSwapping();
			}
			
			endPos = Math.min(array.length,filling.remaining());
			startPos += endPos;
		}
		
		
	}

	private ByteBuffer getNextFreeBuffer() {
		return (ByteBuffer) syncronizedBuffer[head];
	}

	/**
	 * Swaps local filling buffer to a new free buffer, and inserts filling data
	 * into buffers array
	 */
	private void insertSwapping() {
		ByteBuffer newFilling = getNextFreeBuffer();
		put(filling);
		(this.filling = newFilling).clear();

	}

}
