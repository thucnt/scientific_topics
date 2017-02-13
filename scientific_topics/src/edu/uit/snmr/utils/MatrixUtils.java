/**
 * 
 */
package edu.uit.snmr.utils;

import edu.uit.snmr.exceptions.ErrorCode;
import edu.uit.snmr.exceptions.SnmrMathException;
import edu.uit.snmr.math.DMatrix;
import edu.uit.snmr.math.IMatrix;

/**
 * @author muonnv
 *
 */
public class MatrixUtils {

	public static int[][] add(int[][] a, int[][] b) throws SnmrMathException  {
		if (a.length != b.length || a[0].length != b[0].length) {
			throw new IllegalArgumentException(ErrorCode.getFormatedMessage(ErrorCode.SNMR_MATH_010));
		}
		int sum [][] = new int [a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				sum[i][j] = a[i][j] + b[i][j];
			}
		}
		return sum;
	}

	public static int[][] subtract(int[][] a, int[][] b) throws SnmrMathException  {
		if (a.length != b.length || a[0].length != b[0].length) {
			throw new IllegalArgumentException(ErrorCode.getFormatedMessage(ErrorCode.SNMR_MATH_010));
		}
		int subtraction [][] = new int [a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				subtraction[i][j] = a[i][j] - b[i][j];
			}
		}
		return subtraction;
	}

	public static int[][] multi(int[][] a, int[][] b) throws SnmrMathException  {
		if (a.length != b.length || a[0].length != b[0].length) {
			throw new IllegalArgumentException(ErrorCode.getFormatedMessage(ErrorCode.SNMR_MATH_010));
		}
		int prod [][] = new int [a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				prod[i][j] = 0;
				{
					for(int k = 0; k < a[0].length; k++)
						prod[i][j] = prod[i][j] + a[i][k] * b[k][j];
				}
			}
		}
		return prod;
	}

	public static int[] row(int[][] matrix, int row) {
		return matrix[row];
	}

	public static double[] row(double[][] matrix, int row) {
		return matrix[row];
	}

	public static int[] column(int[][] matrix, int column) {
		int[] data = new int[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			data[i] = matrix[i][column];
		}
		return data;
	}

	public static double[] column(double[][] matrix, int column) {
		double[] data = new double[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			data[i] = matrix[i][column];
		}
		return data;
	}

	public static double[][] divide(int[][] a, int[][] b) {
		double[][] result = new double[a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				result[i][j] = (double)a[i][j] / b[i][j];
			}			
		}
		return result;
	}

	public static int[] sum(int[][] array) {
		int[] result = new int[array[0].length];

		for (int i = 0; i < array.length; i++) {			
			for (int j = 0; j < array[i].length; j++) {
				result[j] = result[j] + array[i][j];
			}
		}
		return result;
	}

	public static int[] sum(int[][] array, int dim) {
		if (dim != 1 && dim != 2) {
			throw new IllegalArgumentException("value of dim must be 1 or 2");
		} else if (dim == 2) {
			int[] result = new int[array[0].length];
			for (int i = 0; i < result.length; i++) {
				int[] acc =column(array, i);
				result[i] = ArrayUtils.sum(acc);				
			}
			return result;
		} else {
			return sum(array);
		}
	}

	public static int[][] initializeMatrix(int[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				array[i][j] = 0;
			}
		}
		return array;
	}

	public static double[][] initializeMatrix(double[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				array[i][j] = 0.0;
			}
		}
		return array;
	}

	public static int[][] repmat(int[][] a, int m, int n) {
		int[][] result = new  int[a.length * m][ a[0].length * n];
		for (int i = 0; i < result.length; i++) {
			result[i] = ArrayUtils.repmat(a[i % a.length], n);			
		}
		return result;
	}

	public static int[][] repmat(int[] a, int m, int n) {
		int[][] result = new  int[m][n];
		for (int i = 0; i < result.length; i++) {
			result[i] = ArrayUtils.repmat(a, n);			
		}
		return result;
	}

	/** Element-by-element right division, C = A./B
	   @param B    another matrix
	   @return     A./B
	 */

	public static double[][] rightDivide (int[][] a, int[][] b) {
		if (a.length != b.length || a[0].length != b[0].length) {
			throw new IllegalArgumentException(ErrorCode.getFormatedMessage(ErrorCode.SNMR_MATH_010));
		}
		double[][] C = new double[a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				C[i][j] = (double)a[i][j] / b[i][j];
			}
		}
		return C;
	}

	/** Element-by-element right division in place, A = A./B
	   @param B    another matrix
	   @return     A./B
	 */
	/*
	   public static int[][] rightDivideEquals (int[][] a, int[][] b) {
		   if (a.length != b.length || a[0].length != b[0].length) {
				throw new IllegalArgumentException(ErrorCode.getFormatedMessage(ErrorCode.SNMR_MATH_010));
			}
	      for (int i = 0; i < a.length; i++) {
	         for (int j = 0; j < a[i].length; j++) {
	            a[i][j] = a[i][j] / b[i][j];
	         }
	      }
	      return a;
	   }
	 */


	public static int[][] getSubMatrix (int[][] matrix, int i0, int i1, int j0, int j1) {
		int[][] X = new int[i1-i0+1][j1-j0+1];
		try {
			for (int i = i0; i <= i1; i++) {
				for (int j = j0; j <= j1; j++) {
					X[i-i0][j-j0] = matrix[i][j];
				}
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
		return X;
	}
	
	public static int[][] getSubMatrix (int[][] matrix, int[] rowIndices, int j0, int j1) {

		int[][] X = new int[rowIndices.length][j1-j0+1];
		try {
			for (int i = 0; i < rowIndices.length; i++) {
				for (int j = j0; j <= j1; j++) {
					X[i][j-j0] = matrix[rowIndices[i]][j];
				}
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
		return X;
	}
	
	public static int[][] getSubMatrix (int[][] matrix, int[] rowIndices, int[]  colIndices) {

		int[][] X = new int[rowIndices.length][colIndices.length];
		try {
			for (int i = 0; i < rowIndices.length; i++) {
				for (int j = 0; j < colIndices.length; j++) {
					X[i][j] = matrix[rowIndices[i]][colIndices[j]];
				}
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
		return X;
	}

	public static void setRow(int[][] bigAT, int i, int[] sum) {
		System.out.println("i " + i);
		System.out.println("sum " + sum.length);
		System.out.println("bigAT " + bigAT[i].length);
		try {
			for (int j = 0; j < bigAT[i].length; j++) {
				bigAT[i][j] = sum[j];
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("Submatrix indices");
		}
	}

	public static int[][] createMatrixWithOnes(int row, int column) {
		int[][] ones = new int[row][column];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				ones[i][j] = 1;
			}
		}
		return ones;
	}

	public static void print(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(" " + matrix[i][j]);
			}
			System.out.println(" ");
		}
	}

}