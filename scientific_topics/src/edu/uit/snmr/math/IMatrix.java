/**
 * 
 */
package edu.uit.snmr.math;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author muonnv
 *
 */
public class IMatrix implements Cloneable, java.io.Serializable {
	

	/* ------------------------
	   Class variables
	 * ------------------------ */

	   /**
		 * 
		 */
		private static final long serialVersionUID = -335114814820714407L;

	/** Array for internal storage of elements.
	   @serial internal array storage.
	   */
	   private int[][] A;

	   /** Row and column dimensions.
	   @serial row dimension.
	   @serial column dimension.
	   */
	   private int m, n;

	/* ------------------------
	   Constructors
	 * ------------------------ */

	   /** Construct an m-by-n matrix of zeros. 
	   @param m    Number of rows.
	   @param n    Number of colums.
	   */

	   public IMatrix (int m, int n) {
	      this.m = m;
	      this.n = n;
	      A = new int[m][n];
	   }

	   /** Construct an m-by-n constant matrix.
	   @param m    Number of rows.
	   @param n    Number of colums.
	   @param s    Fill the matrix with this scalar value.
	   */

	   public IMatrix (int m, int n, int s) {
	      this.m = m;
	      this.n = n;
	      A = new int[m][n];
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            A[i][j] = s;
	         }
	      }
	   }

	   /** Construct a matrix from a 2-D array.
	   @param A    Two-dimensional array of integers.
	   @exception  IllegalArgumentException All rows must have the same length
	   @see        #constructWithCopy
	   */

	   public IMatrix (int[][] A) {
	      m = A.length;
	      n = A[0].length;
	      for (int i = 0; i < m; i++) {
	         if (A[i].length != n) {
	            throw new IllegalArgumentException("All rows must have the same length.");
	         }
	      }
	      this.A = A;
	   }

	   /** Construct a matrix quickly without checking arguments.
	   @param A    Two-dimensional array of ints.
	   @param m    Number of rows.
	   @param n    Number of colums.
	   */

	   public IMatrix (int[][] A, int m, int n) {
	      this.A = A;
	      this.m = m;
	      this.n = n;
	   }

	   /** Construct a matrix from a one-dimensional packed array
	   @param vals One-dimensional array of ints, packed by columns (ala Fortran).
	   @param m    Number of rows.
	   @exception  IllegalArgumentException Array length must be a multiple of m.
	   */

	   public IMatrix (int vals[], int m) {
	      this.m = m;
	      n = (m != 0 ? vals.length/m : 0);
	      if (m*n != vals.length) {
	         throw new IllegalArgumentException("Array length must be a multiple of m.");
	      }
	      A = new int[m][n];
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            A[i][j] = vals[i+j*m];
	         }
	      }
	   }

	/* ------------------------
	   Public Methods
	 * ------------------------ */

	   /** Construct a matrix from a copy of a 2-D array.
	   @param A    Two-dimensional array of ints.
	   @exception  IllegalArgumentException All rows must have the same length
	   */

	   public static IMatrix constructWithCopy(int[][] A) {
	      int m = A.length;
	      int n = A[0].length;
	      IMatrix X = new IMatrix(m,n);
	      int[][] C = X.getArray();
	      for (int i = 0; i < m; i++) {
	         if (A[i].length != n) {
	            throw new IllegalArgumentException
	               ("All rows must have the same length.");
	         }
	         for (int j = 0; j < n; j++) {
	            C[i][j] = A[i][j];
	         }
	      }
	      return X;
	   }

	   /** Make a deep copy of a matrix
	   */

	   public IMatrix copy () {
		   IMatrix X = new IMatrix(m,n);
	      int[][] C = X.getArray();
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            C[i][j] = A[i][j];
	         }
	      }
	      return X;
	   }

	   /** Clone the Matrix object.
	   */

	   public Object clone () {
	      return this.copy();
	   }

	   /** Access the internal two-dimensional array.
	   @return     Pointer to the two-dimensional array of matrix elements.
	   */

	   public int[][] getArray () {
	      return A;
	   }

	   /** Copy the internal two-dimensional array.
	   @return     Two-dimensional array copy of matrix elements.
	   */

	   public int[][] getArrayCopy () {
	      int[][] C = new int[m][n];
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            C[i][j] = A[i][j];
	         }
	      }
	      return C;
	   }

	   /** Make a one-dimensional column packed copy of the internal array.
	   @return     Matrix elements packed in a one-dimensional array by columns.
	   */

	   public int[] getColumnPackedCopy () {
	      int[] vals = new int[m*n];
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            vals[i+j*m] = A[i][j];
	         }
	      }
	      return vals;
	   }

	   /** Make a one-dimensional row packed copy of the internal array.
	   @return     Matrix elements packed in a one-dimensional array by rows.
	   */

	   public int[] getRowPackedCopy () {
	      int[] vals = new int[m*n];
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            vals[i*n+j] = A[i][j];
	         }
	      }
	      return vals;
	   }

	   /** Get row dimension.
	   @return     m, the number of rows.
	   */

	   public int getRowDimension () {
	      return m;
	   }

	   /** Get column dimension.
	   @return     n, the number of columns.
	   */

	   public int getColumnDimension () {
	      return n;
	   }

	   /** Get a single element.
	   @param i    Row index.
	   @param j    Column index.
	   @return     A(i,j)
	   @exception  ArrayIndexOutOfBoundsException
	   */

	   public int get (int i, int j) {
	      return A[i][j];
	   }

	   /** Get a submatrix.
	   @param i0   Initial row index
	   @param i1   Final row index
	   @param j0   Initial column index
	   @param j1   Final column index
	   @return     A(i0:i1,j0:j1)
	   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	   */

	   public IMatrix getMatrix (int i0, int i1, int j0, int j1) {
		   IMatrix X = new IMatrix(i1-i0+1,j1-j0+1);
	      int[][] B = X.getArray();
	      try {
	         for (int i = i0; i <= i1; i++) {
	            for (int j = j0; j <= j1; j++) {
	               B[i-i0][j-j0] = A[i][j];
	            }
	         }
	      } catch(ArrayIndexOutOfBoundsException e) {
	         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
	      }
	      return X;
	   }

	   /** Get a submatrix.
	   @param r    Array of row indices.
	   @param c    Array of column indices.
	   @return     A(r(:),c(:))
	   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	   */

	   public IMatrix getMatrix (int[] r, int[] c) {
		   IMatrix X = new IMatrix(r.length,c.length);
	      int[][] B = X.getArray();
	      try {
	         for (int i = 0; i < r.length; i++) {
	            for (int j = 0; j < c.length; j++) {
	               B[i][j] = A[r[i]][c[j]];
	            }
	         }
	      } catch(ArrayIndexOutOfBoundsException e) {
	         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
	      }
	      return X;
	   }

	   /** Get a submatrix.
	   @param i0   Initial row index
	   @param i1   Final row index
	   @param c    Array of column indices.
	   @return     A(i0:i1,c(:))
	   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	   */

	   public IMatrix getMatrix (int i0, int i1, int[] c) {
		   IMatrix X = new IMatrix(i1-i0+1,c.length);
	      int[][] B = X.getArray();
	      try {
	         for (int i = i0; i <= i1; i++) {
	            for (int j = 0; j < c.length; j++) {
	               B[i-i0][j] = A[i][c[j]];
	            }
	         }
	      } catch(ArrayIndexOutOfBoundsException e) {
	         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
	      }
	      return X;
	   }

	   /** Get a submatrix.
	   @param r    Array of row indices.
	   @param i0   Initial column index
	   @param i1   Final column index
	   @return     A(r(:),j0:j1)
	   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	   */

	   public IMatrix getMatrix (int[] r, int j0, int j1) {
		   IMatrix X = new IMatrix(r.length,j1-j0+1);
	      int[][] B = X.getArray();
	      try {
	         for (int i = 0; i < r.length; i++) {
	            for (int j = j0; j <= j1; j++) {
	               B[i][j-j0] = A[r[i]][j];
	            }
	         }
	      } catch(ArrayIndexOutOfBoundsException e) {
	         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
	      }
	      return X;
	   }

	   /** Set a single element.
	   @param i    Row index.
	   @param j    Column index.
	   @param s    A(i,j).
	   @exception  ArrayIndexOutOfBoundsException
	   */

	   public void set (int i, int j, int s) {
	      A[i][j] = s;
	   }

	   /** Set a submatrix.
	   @param i0   Initial row index
	   @param i1   Final row index
	   @param j0   Initial column index
	   @param j1   Final column index
	   @param X    A(i0:i1,j0:j1)
	   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	   */

	   public void setMatrix (int i0, int i1, int j0, int j1, IMatrix X) {
	      try {
	         for (int i = i0; i <= i1; i++) {
	            for (int j = j0; j <= j1; j++) {
	               A[i][j] = X.get(i-i0,j-j0);
	            }
	         }
	      } catch(ArrayIndexOutOfBoundsException e) {
	         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
	      }
	   }

	   /** Set a submatrix.
	   @param r    Array of row indices.
	   @param c    Array of column indices.
	   @param X    A(r(:),c(:))
	   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	   */

	   public void setMatrix (int[] r, int[] c, IMatrix X) {
	      try {
	         for (int i = 0; i < r.length; i++) {
	            for (int j = 0; j < c.length; j++) {
	               A[r[i]][c[j]] = X.get(i,j);
	            }
	         }
	      } catch(ArrayIndexOutOfBoundsException e) {
	         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
	      }
	   }

	   /** Set a submatrix.
	   @param r    Array of row indices.
	   @param j0   Initial column index
	   @param j1   Final column index
	   @param X    A(r(:),j0:j1)
	   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	   */

	   public void setMatrix (int[] r, int j0, int j1, IMatrix X) {
	      try {
	         for (int i = 0; i < r.length; i++) {
	            for (int j = j0; j <= j1; j++) {
	               A[r[i]][j] = X.get(i,j-j0);
	            }
	         }
	      } catch(ArrayIndexOutOfBoundsException e) {
	         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
	      }
	   }

	   /** Set a submatrix.
	   @param i0   Initial row index
	   @param i1   Final row index
	   @param c    Array of column indices.
	   @param X    A(i0:i1,c(:))
	   @exception  ArrayIndexOutOfBoundsException Submatrix indices
	   */

	   public void setMatrix (int i0, int i1, int[] c, IMatrix X) {
	      try {
	         for (int i = i0; i <= i1; i++) {
	            for (int j = 0; j < c.length; j++) {
	               A[i][c[j]] = X.get(i-i0,j);
	            }
	         }
	      } catch(ArrayIndexOutOfBoundsException e) {
	         throw new ArrayIndexOutOfBoundsException("Submatrix indices");
	      }
	   }

	   /** Matrix transpose.
	   @return    A'
	   */

	   public IMatrix transpose () {
		   IMatrix X = new IMatrix(n,m);
	      int[][] C = X.getArray();
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            C[j][i] = A[i][j];
	         }
	      }
	      return X;
	   }



	  

	   /**  Unary minus
	   @return    -A
	   */

	   public IMatrix uminus () {
		   IMatrix X = new IMatrix(m,n);
	      int[][] C = X.getArray();
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            C[i][j] = -A[i][j];
	         }
	      }
	      return X;
	   }

	   /** C = A + B
	   @param B    another matrix
	   @return     A + B
	   */

	   public IMatrix plus (IMatrix B) {
	      checkMatrixDimensions(B);
	      IMatrix X = new IMatrix(m,n);
	      int[][] C = X.getArray();
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            C[i][j] = A[i][j] + B.A[i][j];
	         }
	      }
	      return X;
	   }

	   /** A = A + B
	   @param B    another matrix
	   @return     A + B
	   */

	   public IMatrix plusEquals (IMatrix B) {
	      checkMatrixDimensions(B);
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            A[i][j] = A[i][j] + B.A[i][j];
	         }
	      }
	      return this;
	   }

	   /** C = A - B
	   @param B    another matrix
	   @return     A - B
	   */

	   public IMatrix minus (IMatrix B) {
	      checkMatrixDimensions(B);
	      IMatrix X = new IMatrix(m,n);
	      int[][] C = X.getArray();
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            C[i][j] = A[i][j] - B.A[i][j];
	         }
	      }
	      return X;
	   }

	   /** A = A - B
	   @param B    another matrix
	   @return     A - B
	   */

	   public IMatrix minusEquals (IMatrix B) {
	      checkMatrixDimensions(B);
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            A[i][j] = A[i][j] - B.A[i][j];
	         }
	      }
	      return this;
	   }

	   /** Element-by-element multiplication, C = A.*B
	   @param B    another matrix
	   @return     A.*B
	   */

	   public IMatrix arrayTimes (IMatrix B) {
	      checkMatrixDimensions(B);
	      IMatrix X = new IMatrix(m,n);
	      int[][] C = X.getArray();
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            C[i][j] = A[i][j] * B.A[i][j];
	         }
	      }
	      return X;
	   }

	   /** Element-by-element multiplication in place, A = A.*B
	   @param B    another matrix
	   @return     A.*B
	   */

	   public IMatrix arrayTimesEquals (IMatrix B) {
	      checkMatrixDimensions(B);
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            A[i][j] = A[i][j] * B.A[i][j];
	         }
	      }
	      return this;
	   }

	   /** Element-by-element right division, C = A./B
	   @param B    another matrix
	   @return     A./B
	   */

	   public IMatrix arrayRightDivide (IMatrix B) {
	      checkMatrixDimensions(B);
	      IMatrix X = new IMatrix(m,n);
	      int[][] C = X.getArray();
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            C[i][j] = A[i][j] / B.A[i][j];
	         }
	      }
	      return X;
	   }

	   /** Element-by-element right division in place, A = A./B
	   @param B    another matrix
	   @return     A./B
	   */

	   public IMatrix arrayRightDivideEquals (IMatrix B) {
	      checkMatrixDimensions(B);
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            A[i][j] = A[i][j] / B.A[i][j];
	         }
	      }
	      return this;
	   }

	   /** Element-by-element left division, C = A.\B
	   @param B    another matrix
	   @return     A.\B
	   */

	   public IMatrix arrayLeftDivide (IMatrix B) {
	      checkMatrixDimensions(B);
	      IMatrix X = new IMatrix(m,n);
	      int[][] C = X.getArray();
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            C[i][j] = B.A[i][j] / A[i][j];
	         }
	      }
	      return X;
	   }

	   /** Element-by-element left division in place, A = A.\B
	   @param B    another matrix
	   @return     A.\B
	   */

	   public IMatrix arrayLeftDivideEquals (IMatrix B) {
	      checkMatrixDimensions(B);
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            A[i][j] = B.A[i][j] / A[i][j];
	         }
	      }
	      return this;
	   }

	   /** Multiply a matrix by a scalar, C = s*A
	   @param s    scalar
	   @return     s*A
	   */

	   public IMatrix times (int s) {
		   IMatrix X = new IMatrix(m,n);
	      int[][] C = X.getArray();
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            C[i][j] = s*A[i][j];
	         }
	      }
	      return X;
	   }

	   /** Multiply a matrix by a scalar in place, A = s*A
	   @param s    scalar
	   @return     replace A by s*A
	   */

	   public IMatrix timesEquals (int s) {
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            A[i][j] = s*A[i][j];
	         }
	      }
	      return this;
	   }

	   /** Linear algebraic matrix multiplication, A * B
	   @param B    another matrix
	   @return     Matrix product, A * B
	   @exception  IllegalArgumentException Matrix inner dimensions must agree.
	   */

	   public IMatrix times (IMatrix B) {
	      if (B.m != n) {
	         throw new IllegalArgumentException("Matrix inner dimensions must agree.");
	      }
	      IMatrix X = new IMatrix(m,B.n);
	      int[][] C = X.getArray();
	      int[] Bcolj = new int[n];
	      for (int j = 0; j < B.n; j++) {
	         for (int k = 0; k < n; k++) {
	            Bcolj[k] = B.A[k][j];
	         }
	         for (int i = 0; i < m; i++) {
	            int[] Arowi = A[i];
	            int s = 0;
	            for (int k = 0; k < n; k++) {
	               s += Arowi[k]*Bcolj[k];
	            }
	            C[i][j] = s;
	         }
	      }
	      return X;
	   }


	   /** Matrix trace.
	   @return     sum of the diagonal elements.
	   */

	   public int trace () {
	      int t = 0;
	      for (int i = 0; i < Math.min(m,n); i++) {
	         t += A[i][i];
	      }
	      return t;
	   }



	   /** Generate identity matrix
	   @param m    Number of rows.
	   @param n    Number of colums.
	   @return     An m-by-n matrix with ones on the diagonal and zeros elsewhere.
	   */

	   public static IMatrix identity (int m, int n) {
		   IMatrix A = new IMatrix(m,n);
	      int[][] X = A.getArray();
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            X[i][j] = (i == j ? 1 : 0);
	         }
	      }
	      return A;
	   }


	   /** Print the matrix to stdout.   Line the elements up in columns
	     * with a Fortran-like 'Fw.d' style format.
	   @param w    Column width.
	   @param d    Number of digits after the decimal.
	   */

	   public void print (int w, int d) {
	      print(new PrintWriter(System.out,true),w,d); }

	   /** Print the matrix to the output stream.   Line the elements up in
	     * columns with a Fortran-like 'Fw.d' style format.
	   @param output Output stream.
	   @param w      Column width.
	   @param d      Number of digits after the decimal.
	   */

	   public void print (PrintWriter output, int w, int d) {
	      DecimalFormat format = new DecimalFormat();
	      format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
	      format.setMinimumIntegerDigits(1);
	      format.setMaximumFractionDigits(d);
	      format.setMinimumFractionDigits(d);
	      format.setGroupingUsed(false);
	      print(output,format,w+2);
	   }

	   /** Print the matrix to stdout.  Line the elements up in columns.
	     * Use the format object, and right justify within columns of width
	     * characters.
	     * Note that is the matrix is to be read back in, you probably will want
	     * to use a NumberFormat that is set to US Locale.
	   @param format A  Formatting object for individual elements.
	   @param width     Field width for each column.
	   @see java.text.DecimalFormat#setDecimalFormatSymbols
	   */

	   public void print (NumberFormat format, int width) {
	      print(new PrintWriter(System.out,true),format,width); }

	   // DecimalFormat is a little disappointing coming from Fortran or C's printf.
	   // Since it doesn't pad on the left, the elements will come out different
	   // widths.  Consequently, we'll pass the desired column width in as an
	   // argument and do the extra padding ourselves.

	   /** Print the matrix to the output stream.  Line the elements up in columns.
	     * Use the format object, and right justify within columns of width
	     * characters.
	     * Note that is the matrix is to be read back in, you probably will want
	     * to use a NumberFormat that is set to US Locale.
	   @param output the output stream.
	   @param format A formatting object to format the matrix elements 
	   @param width  Column width.
	   @see java.text.DecimalFormat#setDecimalFormatSymbols
	   */

	   public void print (PrintWriter output, NumberFormat format, int width) {
	      output.println();  // start on new line.
	      for (int i = 0; i < m; i++) {
	         for (int j = 0; j < n; j++) {
	            String s = format.format(A[i][j]); // format the number
	            int padding = Math.max(1,width-s.length()); // At _least_ 1 space
	            for (int k = 0; k < padding; k++)
	               output.print(' ');
	            output.print(s);
	         }
	         output.println();
	      }
	      output.println();   // end with blank line.
	   }

	   /** Read a matrix from a stream.  The format is the same the print method,
	     * so printed matrices can be read back in (provided they were printed using
	     * US Locale).  Elements are separated by
	     * whitespace, all the elements for each row appear on a single line,
	     * the last row is followed by a blank line.
	   @param input the input stream.
	   */

	   public static IMatrix read (BufferedReader input) throws java.io.IOException {
	      StreamTokenizer tokenizer= new StreamTokenizer(input);

	      // Although StreamTokenizer will parse numbers, it doesn't recognize
	      // scientific notation (E or D); however, Integer.valueOf does.
	      // The strategy here is to disable StreamTokenizer's number parsing.
	      // We'll only get whitespace delimited words, EOL's and EOF's.
	      // These words should all be numbers, for Integer.valueOf to parse.

	      tokenizer.resetSyntax();
	      tokenizer.wordChars(0,255);
	      tokenizer.whitespaceChars(0, ' ');
	      tokenizer.eolIsSignificant(true);
	      java.util.Vector v = new java.util.Vector();

	      // Ignore initial empty lines
	      while (tokenizer.nextToken() == StreamTokenizer.TT_EOL);
	      if (tokenizer.ttype == StreamTokenizer.TT_EOF)
		throw new java.io.IOException("Unexpected EOF on matrix read.");
	      do {
	         v.addElement(Integer.valueOf(tokenizer.sval)); // Read & store 1st row.
	      } while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);

	      int n = v.size();  // Now we've got the number of columns!
	      int row[] = new int[n];
	      for (int j=0; j<n; j++)  // extract the elements of the 1st row.
	         row[j]=((Integer)v.elementAt(j)).intValue();
	      v.removeAllElements();
	      v.addElement(row);  // Start storing rows instead of columns.
	      while (tokenizer.nextToken() == StreamTokenizer.TT_WORD) {
	         // While non-empty lines
	         v.addElement(row = new int[n]);
	         int j = 0;
	         do {
	            if (j >= n) throw new java.io.IOException
	               ("Row " + v.size() + " is too long.");
	            row[j++] = Integer.valueOf(tokenizer.sval).intValue();
	         } while (tokenizer.nextToken() == StreamTokenizer.TT_WORD);
	         if (j < n) throw new java.io.IOException
	            ("Row " + v.size() + " is too short.");
	      }
	      int m = v.size();  // Now we've got the number of rows.
	      int[][] A = new int[m][];
	      v.copyInto(A);  // copy the rows out of the vector
	      return new IMatrix(A);
	   }


	/* ------------------------
	   Private Methods
	 * ------------------------ */

	   /** Check if size(A) == size(B) **/

	   private void checkMatrixDimensions (IMatrix B) {
	      if (B.m != m || B.n != n) {
	         throw new IllegalArgumentException("Matrix dimensions must agree.");
	      }
	   }

}
