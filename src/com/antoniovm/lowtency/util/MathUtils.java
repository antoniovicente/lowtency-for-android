/**
 * 
 */
package com.antoniovm.lowtency.util;

/**
 * @author Antonio Vicente Martin
 *
 */
public class MathUtils {

	public static double max(double[] array, int from, int to) {
		double max = Double.MIN_VALUE;
		for (int i = from; i < to; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}

		return max;
	}

	public static double min(double[] array, int from, int to) {
		double min = Double.MAX_VALUE;
		for (int i = from; i < to; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}

		return min;
	}

}
