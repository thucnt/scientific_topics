/**
 * 
 */
package edu.uit.snmr.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uit.snmr.exceptions.ErrorCode;

/**
 * @author muonnv
 *
 */
public class ArrayUtils {

	

	public static double[] devide(int[] a, int[] b) {
		double[] result = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			result[i] = (double)a[i] / b[i];
		}
		return result;
	}

	public static double[] devide(int[] a, int b) {
		double[] result = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			result[i] = (double)a[i] / b;
		}
		return result;
	}

	public static int sum(int[] a) {
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum;
	}

	
	/**
	 * Compute reverse 
	 */
	public static int[] reverse(int[] p) {
		int[] pi = new int[p.length];
		for (int i = 0; i < pi.length; i++)
			pi[i] = p[pi.length - i - 1];
		return pi;
	}

	

	public static int[] repmat(int[] a, int m) {
		int[] result = new int[a.length * m];
		for (int i = 0; i < a.length * m; i++) {
			result[i] = a[i % (a.length)];
		}
		return result;
	}

	public static int[] plus(int[] array, int num) {
		int[] result = new int[array.length];
		for (int j = 0; j < array.length; j++) {
			result[j] = array[j] + num;
		}
		return result;
	}
	
	public static int[] findIndicesOfNonzeroElements(int[] array) {
		int[] acc = new int[array.length];
		int size = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] != 0) {
				acc[i] = i + 1;
				size++;
			}
		}
		return Arrays.copyOf(acc, size);
	} 
	
	public static int[] findIndicesOfZeroElements(int[] array) {
		int[] acc = new int[array.length];
		int size = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == 0) {
				acc[i] = i + 1;
				size++;
			}
		}
		return Arrays.copyOf(acc, size);
	} 
	
	public static int[] createArrayWithOnes(int row) {
		   int[] ones = new int[row];
		   for (int i = 0; i < row; i++) {
			   ones[i] = 1;
		   }
		   return ones;
	   }

	public static double[] multi(double[] p1, double num) {
		double[] result = new double[p1.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = p1[i] * num;
		}
		return result;
	}

	public static double[] plus(double[] a, double[] b) {
		if (a.length != b.length) {
			throw new IllegalArgumentException(ErrorCode.getFormatedMessage(ErrorCode.SNMR_MATH_010));
		}
		double[] result = new double[a.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = a[i] + b[i];
		}
		return result;
	}


	public static Integer[] reverse(Integer[] p) {
		Integer[] pi = new Integer[p.length];
		for (Integer i = 0; i < pi.length; i++)
			pi[i] = p[pi.length - i - 1];
		return pi;		
	}

	public static double[] reverse(double[] p) {
		double[] pi = new double[p.length];
		for (Integer i = 0; i < pi.length; i++)
			pi[i] = p[pi.length - i - 1];
		return pi;	
	}
}
