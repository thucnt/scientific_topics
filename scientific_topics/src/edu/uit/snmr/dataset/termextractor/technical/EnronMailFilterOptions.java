/**
 * 
 */
package edu.uit.snmr.dataset.termextractor.technical;

/**
 * @author muonnv
 *
 */
public class EnronMailFilterOptions {
	private boolean includeCc;
	private boolean includeDcc;
	private boolean includeSentDate;
	private boolean includeForwardText;
	private boolean includeOriginalText;
	private boolean incluseGreetings;
	private boolean includeSignature;
	private boolean skipInvalidMail;
	
	public EnronMailFilterOptions() {
		
	}

	/**
	 * @param includeCc
	 * @param includeDcc
	 * @param includeSentDate
	 * @param includeForwardText
	 * @param includeOriginalText
	 * @param incluseGreetings
	 * @param includeSignature
	 */
	public EnronMailFilterOptions(boolean includeCc, boolean includeDcc,
			boolean includeSentDate, boolean includeForwardText,
			boolean includeOriginalText, boolean incluseGreetings,
			boolean includeSignature) {
		this.includeCc = includeCc;
		this.includeDcc = includeDcc;
		this.includeSentDate = includeSentDate;
		this.includeForwardText = includeForwardText;
		this.includeOriginalText = includeOriginalText;
		this.incluseGreetings = incluseGreetings;
		this.includeSignature = includeSignature;
	}

	/**
	 * @return the includeCc
	 */
	public boolean isIncludeCc() {
		return includeCc;
	}

	/**
	 * @param includeCc the includeCc to set
	 */
	public void setIncludeCc(boolean includeCc) {
		this.includeCc = includeCc;
	}

	/**
	 * @return the includeDcc
	 */
	public boolean isIncludeDcc() {
		return includeDcc;
	}

	/**
	 * @param includeDcc the includeDcc to set
	 */
	public void setIncludeDcc(boolean includeDcc) {
		this.includeDcc = includeDcc;
	}

	/**
	 * @return the includeSentDate
	 */
	public boolean isIncludeSentDate() {
		return includeSentDate;
	}

	/**
	 * @param includeSentDate the includeSentDate to set
	 */
	public void setIncludeSentDate(boolean includeSentDate) {
		this.includeSentDate = includeSentDate;
	}

	/**
	 * @return the includeForwardText
	 */
	public boolean isIncludeForwardText() {
		return includeForwardText;
	}

	/**
	 * @param includeForwardText the includeForwardText to set
	 */
	public void setIncludeForwardText(boolean includeForwardText) {
		this.includeForwardText = includeForwardText;
	}

	/**
	 * @return the includeOriginalText
	 */
	public boolean isIncludeOriginalText() {
		return includeOriginalText;
	}

	/**
	 * @param includeOriginalText the includeOriginalText to set
	 */
	public void setIncludeOriginalText(boolean includeOriginalText) {
		this.includeOriginalText = includeOriginalText;
	}

	/**
	 * @return the incluseGreetings
	 */
	public boolean isIncluseGreetings() {
		return incluseGreetings;
	}

	/**
	 * @param incluseGreetings the incluseGreetings to set
	 */
	public void setIncluseGreetings(boolean incluseGreetings) {
		this.incluseGreetings = incluseGreetings;
	}

	/**
	 * @return the includeSignature
	 */
	public boolean isIncludeSignature() {
		return includeSignature;
	}

	/**
	 * @param includeSignature the includeSignature to set
	 */
	public void setIncludeSignature(boolean includeSignature) {
		this.includeSignature = includeSignature;
	}

	/**
	 * @return the skipInvalidMail
	 */
	public boolean isSkipInvalidMail() {
		return skipInvalidMail;
	}

	/**
	 * @param skipInvalidMail the skipInvalidMail to set
	 */
	public void setSkipInvalidMail(boolean skipInvalidMail) {
		this.skipInvalidMail = skipInvalidMail;
	}
	
	
}
