/**
 * 
 */
package edu.uit.snmr.exceptions;

import java.util.ResourceBundle;

/**
 * @author muonnv
 *
 */
public enum ErrorCode {
	SNMR_COMM_010,
	SNMR_MATH_010,
	SNMR_DTS_201, // sql error
	SNMR_IOE_001;
	
	public static String getMessage(ErrorCode code) {
		ResourceBundle bundle = ResourceBundle.getBundle("edu.uit.snmr.i18n.messages");
		
		return bundle.getString(code.toString());
	}
	
	public static String getFormatedMessage(ErrorCode code) {
		String msg = getMessage(code);
		return code.toString() + ": " + msg;
	}
}
