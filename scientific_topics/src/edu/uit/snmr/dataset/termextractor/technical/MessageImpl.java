/**
 * 
 */
package edu.uit.snmr.dataset.termextractor.technical;

import java.net.URL;
import java.util.Date;

/**
 * @author muonnv
 *
 */
public class MessageImpl implements Message {
	private Integer mid;
	
	private Date sentDate;
	
	private String subject;
	
	private String content;
	
	

	/**
	 * @param id
	 * @param content
	 */
	public MessageImpl(Integer mid, String content) {
		this.mid = mid;
		this.content = content;
	}

	
	
	/**
	 * @param id
	 * @param subject
	 * @param content
	 */
	public MessageImpl(Integer mid, String subject, String content) {
		this.mid = mid;
		this.subject = subject;
		this.content = content;
	}
	
	/**
	 * @param id
	 * @param subject
	 * @param content
	 */
	public MessageImpl(Integer mid, Date sentDate, String subject, String content) {
		this.mid = mid;
		this.sentDate = sentDate;
		this.subject = subject;
		this.content = content;
	}


	/**
	 * always return null
	 */
	@Override
	public URL getUrl() {
		return null;
	}

	@Override
	public Date getSentDate() {		
		return this.sentDate;
	}

	/**
	 * @return the mid
	 */
	public Integer getMid() {
		return mid;
	}


	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}


	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}



	@Override
	public String getCompleContent() {
		return this.subject + ". " + this.content;
	}

	
}
