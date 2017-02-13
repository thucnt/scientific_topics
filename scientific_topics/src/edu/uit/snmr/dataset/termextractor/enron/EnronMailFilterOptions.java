/**
 * 
 */
package edu.uit.snmr.dataset.termextractor.enron;

/**
 * @author muonnv
 *
 */
public class EnronMailFilterOptions {

	private boolean removeForwardedContent;
	
	private boolean removeOriginalContent;

	/**
	 * @param removeForwardedContent
	 * @param removeOriginalContent
	 */
	public EnronMailFilterOptions(boolean removeForwardedContent,
			boolean removeOriginalContent) {
		this.removeForwardedContent = removeForwardedContent;
		this.removeOriginalContent = removeOriginalContent;
	}

	/**
	 * @return the removeForwardedContent
	 */
	public boolean isRemoveForwardedContent() {
		return removeForwardedContent;
	}

	/**
	 * @param removeForwardedContent the removeForwardedContent to set
	 */
	public void setRemoveForwardedContent(boolean removeForwardedContent) {
		this.removeForwardedContent = removeForwardedContent;
	}

	/**
	 * @return the removeOriginalContent
	 */
	public boolean isRemoveOriginalContent() {
		return removeOriginalContent;
	}

	/**
	 * @param removeOriginalContent the removeOriginalContent to set
	 */
	public void setRemoveOriginalContent(boolean removeOriginalContent) {
		this.removeOriginalContent = removeOriginalContent;
	}

	
}
