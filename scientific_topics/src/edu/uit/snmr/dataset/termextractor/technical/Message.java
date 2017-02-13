/**
 * 
 */
package edu.uit.snmr.dataset.termextractor.technical;

import java.util.Date;


import uk.ac.shef.dcs.oak.jate.model.Document;

/**
 * @author muonnv
 *
 */
public interface Message extends Document {
	
	/**
	 * get message id 
	 * @return {@link Integer}
	 */
	public Integer getMid();
	
	
	public Date getSentDate();
	
	/**
	 * @return the subject
	 */
	public String getSubject() ;
	
	/**
	 * return the content of the message
	 */
	public String getContent();

	
	/***
	 * return complete content of the message. It may contains content + subject or only content if subject is null
	 * @return
	 */
	public String getCompleContent();
}
