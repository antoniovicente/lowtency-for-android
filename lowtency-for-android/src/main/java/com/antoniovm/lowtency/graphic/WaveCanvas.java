/**
 * 
 */
package com.antoniovm.lowtency.graphic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.antoniovm.lowtency.event.DataAvailableListener;
import com.antoniovm.lowtency.util.MathUtils;
import com.antoniovm.util.raw.ByteConverter;

/**
 * @author Antonio Vicente Martin
 *
 */
public class WaveCanvas extends View implements DataAvailableListener {

	private double[] normalizedBuffer;

	private Paint defaultPaint;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 * @param defStyleRes
	 */
	public WaveCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.defaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.defaultPaint.setColor(Color.RED);

		/*int color = Color.RED;
		Drawable background = getBackground();
		if (background instanceof ColorDrawable) {
			color = ((ColorDrawable) background).getColor();
		}*/
	}

	/**
	 * @param context
	 */
	public WaveCanvas(Context context) {
		super(context);

	}

	/**
	 * @param normalizedBuffer
	 *            the normalizedBuffer to set
	 */
	public void setNormalizedBuffer(double[] normalizedBuffer) {
		this.normalizedBuffer = normalizedBuffer;
	}

	/**
	 * @return the normalizedBuffer
	 */
	public double[] getNormalizedBuffer() {
		return normalizedBuffer;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int viewWidth = getWidth();
		int maxAmplitude = getHeight() / 2;
		int centerY = getHeight() / 2;


		for (int i = 0; i < viewWidth - 1; i++) {

			int from = (int) ((i / (double) viewWidth) * normalizedBuffer.length);
			int to = (int) (((i + 1) / (double) viewWidth) * normalizedBuffer.length);

			int maxAmplitudeInRange = (int) (MathUtils.max(normalizedBuffer, from, to) * maxAmplitude);
			int minAmplitudeInRange = (int) (MathUtils.min(normalizedBuffer, from, to) * maxAmplitude);

			canvas.drawLine(i, centerY + maxAmplitudeInRange, i, centerY + minAmplitudeInRange, defaultPaint);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.antoniovm.lowtency.event.DataAvailableListener#onDataAvailableListener
	 * (byte[])
	 */
	@Override
	public void onDataAvailableListener(byte[] data, int sampleSize) {
		ByteConverter.toDoublesArray(data, 0, sampleSize, normalizedBuffer, 0, normalizedBuffer.length, true, false);
		postInvalidate();

	}

}
