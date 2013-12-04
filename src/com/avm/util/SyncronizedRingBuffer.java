package com.avm.util;

import java.util.concurrent.Semaphore;

public class SyncronizedRingBuffer<T> {
	
	public static final int DEFAULT_CAPACITY = 256;

	private Object[] syncronizedBuffer;
	private int head, tail, size;

	private Semaphore itemAvailable, positionAvailable;

	public SyncronizedRingBuffer(int size) {		
		this.syncronizedBuffer = new Object[size];
		this.head = 0;
		this.tail = 0;
		this.size = 0;
		this.itemAvailable = new Semaphore(0);
		this.positionAvailable = new Semaphore(size);
	}

	public SyncronizedRingBuffer() {
		this(DEFAULT_CAPACITY);
	}

	public T pop() {
		try {
			itemAvailable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		T temp = null;

		synchronized (this) {
			temp = getLast();
			upTail();
			size--;
		}

		positionAvailable.release();

		return temp;
	}

	public void put(T item) {
		try {
			positionAvailable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		synchronized (this) {
			setFirst(item);
			upHead();
			size++;
		}

		itemAvailable.release();

	}

	private synchronized void setFirst(T item) {
		syncronizedBuffer[head] = item;
	}

	private synchronized void setLast(T item) {
		syncronizedBuffer[tail] = item;
	}

	private synchronized T getFirst() {
		return (T) syncronizedBuffer[head];
	}

	private synchronized T getLast() {
		return (T) syncronizedBuffer[tail];
	}

	private void upHead() {
		head = updateValue(head, true);
	}


	private void upTail() {
		tail = updateValue(tail, true);
	}

	private int updateValue(int value, boolean up) {
		value = up ? (value + 1) : (value - 1) + syncronizedBuffer.length;
		return value % syncronizedBuffer.length;
	}
	
	public synchronized int size() {
		return size;
	}

}
