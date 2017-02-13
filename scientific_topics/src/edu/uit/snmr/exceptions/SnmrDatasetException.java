/**
 * 
 */
package edu.uit.snmr.exceptions;

/**
 * @author muonnv
 *
 */
public class SnmrDatasetException extends SnmrException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3006603061647976480L;

	
	public SnmrDatasetException(ErrorCode code, String message) {
		super(code, message);
	}
	
	public SnmrDatasetException(String code, String message) {
		super(code, message);
	}

	public SnmrDatasetException(ErrorCode code, String message, Throwable e) {
		super(code, message, e);
	}
	
	public SnmrDatasetException(String code, String message, Throwable e) {
		super(code, message, e);
	}
}
