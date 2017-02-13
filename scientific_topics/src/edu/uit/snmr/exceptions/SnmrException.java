/**
 * 
 */
package edu.uit.snmr.exceptions;

/**
 * @author muonnv
 *
 */
public class SnmrException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8592146432789227649L;
	
	private String message;
	private String code;
	private Throwable e;
	
	
	public SnmrException(String code, String message) {
		super(code + ": " + message);
		this.code = code;
		this.message = message;			
	}
	
	public SnmrException(String code, String message, Throwable e) {
		super(code + ": " + message, e);
		this.code = code;
		this.message = message;		
		this.e = e;
	}
	
	public SnmrException(ErrorCode code, String message) {
		super(code.toString() + ": " + message);
		this.code = code.toString();
		this.message = message;			
	}
	
	public SnmrException(ErrorCode code, String message, Throwable e) {
		super(code.toString() + ": " + message, e);
		this.code = code.toString();
		this.message = message;		
		this.e = e;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
