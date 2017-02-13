/**
 * 
 */
package edu.uit.snmr.exceptions;

/**
 * @author muonnv
 *
 */
public class SnmrMathException extends SnmrException {

	public SnmrMathException(ErrorCode code, String message) {
		super(code, message);		
	}

	public SnmrMathException(String code, String message) {
		super(code, message);			
	}
	
}
