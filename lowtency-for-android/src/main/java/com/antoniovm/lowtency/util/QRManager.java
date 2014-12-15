/**
 * 
 */
package com.antoniovm.lowtency.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * @author Antonio Vicente Martin
 *
 */
public class QRManager {

	private QRCodeWriter writer;

	/**
	 * 
	 */
	public QRManager() {
		this.writer = new QRCodeWriter();
	}

	/**
	 * 
	 */
	public Bitmap encode(String string, int width, int height) {
		BitMatrix matrix = null;
		try {
			matrix = writer.encode(string, BarcodeFormat.QR_CODE, width, height);
		} catch (WriterException e) {
			e.printStackTrace();
			return null;
		}

		return toBitmap(matrix);
	}

	/**
	 * Writes the given Matrix on a new Bitmap object.
	 * 
	 * @param matrix
	 *            the matrix to write.
	 * @return the new {@link Bitmap}-object.
	 */
	public static Bitmap toBitmap(BitMatrix matrix) {
		int height = matrix.getHeight();
		int width = matrix.getWidth();
		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
			}
		}
		return bmp;
	}

}
