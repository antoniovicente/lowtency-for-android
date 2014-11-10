/**
 * 
 */
package com.antoniovm.lowtency.graphic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Antonio Vicente Martin
 *
 */
public class WaveCanvas extends View {

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
	}

	/**
	 * @param context
	 */
	public WaveCanvas(Context context) {
		super(context);

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		for (int i = 0; i < getWidth(); i++) {
			int random = (int) (Math.random() * 150) + 50;

			canvas.drawLine(i, getHeight() / 2 - random, i, getHeight() / 2 + random, defaultPaint);
		}

	}

}
