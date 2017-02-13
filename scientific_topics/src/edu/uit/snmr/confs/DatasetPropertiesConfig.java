/**
 * 
 */
package edu.uit.snmr.confs;

import java.io.File;


/**
 * @author muonnv
 *
 */
public class DatasetPropertiesConfig extends PropertiesConf {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7159557519408618394L;

	private static DatasetPropertiesConfig instnace;	

	private DatasetPropertiesConfig()  {
		super("conf/dataset.properties");		
	}

	public static DatasetPropertiesConfig getInstance() {
		if (instnace == null) {
			instnace = new DatasetPropertiesConfig();
		}
		return instnace;
	}


	public String getTermIdsPath() {
		return getResultFolder() + File.separator + getProperty("TERM_ID_FILE");
	}

	public String getTermsPath() {
		return getResultFolder() + File.separator + getProperty("TERM_FILE");
	}

	public int getViewState() {
		return getInt("VIEW_STATE");
	}

	public void setViewState(int value) {
		setProperty("VIEW_STATE", value + "");
	}


	public String getCorpusPath() {
		return getProperty("CORPUS_PATH");
	}

	public void setCorpusPath(String value) {
		setProperty("CORPUS_PATH", value);
	}


	public String getEmployeeListFile() {
		return getProperty("EMPLOYEE_FILE");
	}

	public void setEmployeeListFile(String value) {
		setProperty("EMPLOYEE_FILE", value);
	}

	public int getNTokens() {
		return getInt("NTOKENS");
	}

	public void setNTokens(int nTokens) {
		setProperty("NTOKENS", nTokens +"");
	}


	/**
	 * @return the emailSenderRecipientFile
	 */
	public String getMessageSenderRecipientsFile() {
		return getResultFolder() + File.separator + getProperty("MESSAGE_AUTHOR_RECIPIENTS");
	}

	/**
	 * @param emailSenderRecipientFile the emailSenderRecipientFile to set
	 */
	public void setEmailSenderRecipientFile(String value) {
		setProperty("MESSAGE_AUTHOR_RECIPIENTS", value);
	}

	/**
	 * @return the docTermFreFile
	 */
	public String getMessageTermFrequenlyPath() {
		return getResultFolder() + File.separator + getProperty("MESSAGE_TERM_FRE");
	}

	/**
	 * @param docTermFreFile the docTermFreFile to set
	 */
	public void setMessageTermFrequenlyPath(String value) {
		setProperty("MESSAGE_TERM_FRE", value);
	}



	/**
	 * @return the resultFolder
	 */
	public String getResultFolder() {
		return getString("RESULT_FOLDER");
	}

	/**
	 * @param resultFolder the resultFolder to set
	 */
	public void setResultFolder(String value) {
		setProperty("RESULT_FOLDER", value);
	}



	/**
	 * @return the ptmatrix
	 */
	public String getPtmatrix() {
		return getProperty("PTMATRIX");
	}

	/**
	 * @param ptmatrix the ptmatrix to set
	 */
	public void setPtmatrix(String value) {
		setProperty("PTMATRIX", value);
	}

	/**
	 * @return the wtmatrix
	 */
	public String getWtmatrix() {
		return getProperty("WTMATRIX");
	}

	/**
	 * @param wtmatrix the wtmatrix to set
	 */
	public void setWtmatrix(String value) {
		setProperty("WTMATRIX", value);
	}

	/**
	 * @return the chainInfo
	 */
	public String getChainInfo() {
		return getProperty("CHAININFO");
	}

	/**
	 * @param chainInfo the chainInfo to set
	 */
	public void setChainInfo(String value) {
		setProperty("CHAININFO", value);
	}

	/**
	 * @return the alpha
	 */
	public double getAlpha() {
		return getDouble("ALPHA");
	}

	/**
	 * @param alpha the alpha to set
	 */
	public void setAlpha(double value) {
		setProperty("ALPHA", value + "");
	}

	/**
	 * @return the beta
	 */
	public double getBeta() {
		return getDouble("BETA");
	}

	/**
	 * @param beta the beta to set
	 */
	public void setBeta(double value) {
		setProperty("BETA", value + "");
	}

	/**
	 * @return the recipientMax
	 */
	public int getRecipientMax() {
		return getInt("RECIPIENTSMAX");
	}

	/**
	 * @param recipientMax the recipientMax to set
	 */
	public void setRecipientMax(int value) {
		setProperty("RECIPIENTSMAX", value + "");
	}

	/**
	 * @return the burnin
	 */
	public int getBurnin() {
		return getInt("BURNIN");
	}

	/**
	 * @param burnin the burnin to set
	 */
	public void setBurnin(int value) {
		setProperty("BURNIN", value + "");
	}

	/**
	 * @return the lag
	 */
	public int getLag() {
		return getInt("LAG");
	}

	/**
	 * @param lag the lag to set
	 */
	public void setLag(int value) {
		setProperty("LAG", value + "");
	}

	/**
	 * @return the samples
	 */
	public int getNSamples() {
		return getInt("NSAMPLES");
	}

	/**
	 * @param samples the samples to set
	 */
	public void setNSamples(int value) {
		setProperty("NSAMPLES", value + "");
	}

	public String getMessageIdFilePath() {
		return getResultFolder() + File.separator + getString("MESSAGE_ID_FILE");
	}

	public String getFilteredMessageFolder() {
		return getResultFolder() + File.separator + getString("FILTERED_MESSAGE_FOLDER");
	}

	public int getNMessages() {
		return getInt("NMESSAGES");
	}

	public void setNMessages(int value) {
		setProperty("NMESSAGES", value + "");

	}	

	public int getNTerms() {
		return getInt("NTERMS");
	}

	public void setNTerms(int value) {
		setProperty("NTERMS", value + "");
	}

	public String getValidFrom() {
		return getProperty("VALID_FROM");
	}

	public void setValidFrom(String value) {
		setProperty("VALID_FROM", value);
	}




	public String getValidTo() {
		return getProperty("VALID_TO");
	}

	public void setValidTo(String value) {
		setProperty("VALID_TO", value);
	}

	public int getNTopics() {		
		return getInt("NTOPICS");
	}

	public void setNTopics(int value) {
		setProperty("NTOPICS", value + "");
	}

	public int getNAuthors() {
		return getInt("NAUTHORS");
	}

	public int getNPairAppearances() {
		return getInt("NPAIRAPPEARANCES");
	}

	public String getExtractedTopicsFolder() {
		return getResultFolder() + File.separator + getString("EXTRACTED_TOPIC_FOLDER");
	}

	public int getNTopTerm() {

		return getInt("NTOP_TERM");
	}

	public int getNTopAuthor() {
		return getInt("NTOP_AUTHOR");
	}

	public int getNTopRecipient() {
		return getInt("NTOP_RECIPIENT");
	}

	public boolean isTrainningTopicImported() {
		return getInt("TRAINNING_TOPIC_IMPORTED") == 1;
	}

	public void setTrainningTopicImported(boolean value) {
		setProperty("TRAINNING_TOPIC_IMPORTED", value? "1" : "0");
	}
}
