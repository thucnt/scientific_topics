/**
 * 
 */
package edu.uit.snmr.topicmodeling.art;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import edu.uit.snmr.confs.DatasetPropertiesConfig;
import edu.uit.snmr.confs.PropertiesConf;
import edu.uit.snmr.utils.RandomGenerator;

/**
 * @author muonnv
 *
 */
public class ARTAlgorithmCore  {
	private static final Logger logger = Logger.getLogger(ARTAlgorithmCore.class);
	public static final int MAXNAMELENGTH = 200;
	public static final int SEED = 1; // Seed for the RNG, change it for different runs to get different samples if necessary. Only use odd positive integers.
	private RandomGenerator randomGenerator = RandomGenerator.getInstance();
	
	private DatasetPropertiesConfig conf;
	private int nTokens;
	private int nTopics;
	private int nWords;
	private int nPairs;
	
	/* A list of global variables that store information used by Gibbs sampling */
	private int[][] pairInfo;		// Each row: doc_id a/r_pair_id
	private int[][] wordInfo;		// Each row: doc_id word_id topic_id a/r_pair_id
	private int[][] docIndex;				// Each row: starting index in pairInfo and # of recipients for each email
	
	private int[][] pairTopic;			// The pairTopic matrix (not sparse)
	private int[][] wordTopic;			// The wordTopic matrix (not sparse)
	private int[] sumWords;				// SUM(wordTopic, 1) along row dim
	private int[] sumTopics;				// SUM(pairTopic, 2) along col dim
	private double[][] probs;		// a 2D prob matrix holder for sampling topics and recipients
	private double alpha;
	private double beta;
	private double vBeta;		// a normalization factor reused frequently in sampling words
	private double tAlpha;		// a normalization factor reused frequently in sampling topics
	
	/**
	 * @param conf
	 * @param nTokens
	 * @param nTopics
	 * @param nWords
	 * @param nPairs
	 * @param pairInfo
	 * @param wordInfo
	 * @param docIndex
	 */
	public ARTAlgorithmCore(DatasetPropertiesConfig conf, int nTokens,
			int nTopics, int nWords, int nPairs, int[][] pairInfo,
			int[][] wordInfo, int[][] docIndex) {
		this.conf = conf;
		this.nTokens = nTokens;
		this.nTopics = nTopics;
		this.nWords = nWords;
		this.nPairs = nPairs;
		this.pairInfo = pairInfo;
		this.wordInfo = wordInfo;
		this.docIndex = docIndex;
		this.pairTopic = new int[nPairs][nTopics];
		this.wordTopic = new int[nWords][nTopics];
		this.sumWords = new int[nTopics];
		this.sumTopics = new int[nPairs];
		this.probs = new double[nTopics][ conf.getRecipientMax()];
		this.alpha = conf.getAlpha();
		this.beta = conf.getAlpha();
		this.vBeta = nWords * beta;
		this.tAlpha = nTopics * alpha;
	}

	
	/**
	 * verified
	 * Assign each word token to a random topic and an recipient 
	 * of the message where the word is located.
	 * Start a chain from the scratch
	 */
	private void startChain() {
		int i, topic, pair, prob;
		double n; 	// random number generated
		randomGenerator.setSeed(SEED); // set seed for random number generator

		logger.info("Starting a chain randomly......");

		for (i = 0; i < nTokens; i++) {
			/*
			 * Assign a topic to this word randomly from a 1D discrete distribution
			 * Each outcome corresponding to a bucket 
			 * with a size of (unnormalized) probability for this outcome.
			 * Here, the (unnormalized) probability for each outcome is 1.
			 */
			
			double randomNr = randomGenerator.randomNumber();
			System.out.println("Start chain: random number (topic) is " + randomNr);
			n = randomNr * nTopics;
			
			topic = 0;		// Index of topics
			prob = 1;		// Cumulative sum of probabilities of topics
			while (n > prob){
				topic++;
				prob++;
			}

			/* Update with selected topic */
			wordInfo[i][2] = topic;				
			// In input files, all indexes start from 1
			wordTopic[wordInfo[i][1] - 1][topic]++; 	
			sumWords[topic]++;

			/* Assign an author/recipient pair randomly */
			double randomNr2 = randomGenerator.randomNumber();
			System.out.println("Start chain: random number (author/recipient) is " + randomNr2);
			n  = randomNr2*docIndex[wordInfo[i][0] - 1][1];
			
			pair = 0; // Index of pairs
			prob = 1; // Cumulative sum of probabilities of pairs
			while (n > prob) {
				pair++;
				prob++;
			}

			/* Update with selected pair */ 
			// wordInfo: topic_id word_id topic_id a/r_pair_id
			wordInfo[i][3] = pairInfo[docIndex[wordInfo[i][0] - 1][0] + pair][1];			
			pairTopic[wordInfo[i][3] - 1][wordInfo[i][2]]++;		
			sumTopics[wordInfo[i][3] - 1]++;
		} 
	}

	//verified
	/* Core: Gibbs sampler (one iteration) */
	private void sampler() {
		int i, j, k, nPairs;	// Used for loop
		// Used for assignment of topic, word and pair
		int currentWord, previousTopic, previousPair, selectedPair;	
		double n, m, totalProb;		// Used for sampling
		double[] topicProb = new double[nTopics];// A topic probability vector

		for (i = 0; i < nTokens; i++){
			/* First exclude the previous selections and update correspondingly */
			currentWord = wordInfo[i][1];
			previousTopic = wordInfo[i][2];
			previousPair = wordInfo[i][3];
			pairTopic[previousPair - 1][previousTopic]--;
			wordTopic[currentWord - 1][previousTopic]--;
			sumWords[previousTopic]--;
			sumTopics[previousPair - 1]--;

			//Cumulative (unnormalized) probabilities of (pair,topic) for this word
			totalProb = 0; 	
			nPairs = docIndex[wordInfo[i][0] - 1][1]; //of pairs of this document

			/* Compute unnormalized probabilities of (pair,topic) */
			for (k = 0; k < nTopics; k++) {		// this is pair independent
				topicProb[k] = ((double)wordTopic[currentWord - 1][k] + beta) 
						/ ((double)sumWords[k] + vBeta);
			}

			for (j = 0; j < nPairs; j++){
				selectedPair = pairInfo[docIndex[wordInfo[i][0] - 1][0] + j][1];
				for (k = 0; k < nTopics; k++){
					probs[k][j] = topicProb[k] * ((double)pairTopic[selectedPair - 1][k] + alpha) 
							/ ((double)sumTopics[selectedPair - 1] + tAlpha);
					totalProb += probs[k][j];
				}
			}

			double randomNr = randomGenerator.randomNumber();
			System.out.println("sampler: random number is " + randomNr);
	
			n = randomNr * totalProb; // Draw a random number
			
			k = 0;	// Index of topics
			j = 0;	// Index of the pairs of this message
			m = probs[0][0];// Cumulative sum of probabilities of (pair,topic)

			/*
			 * Draw a sample from the joint (conditional) distribution of pair and topic 
			 * for this word given what-else 
			 * 2D discrete distribution
			 * topic first, pair second, column by column
			 */ 
			while(n > m){
				k++;
				if (k == nTopics){
					j++;
					k = 0; 
				}
				m += probs[k][j];
			}

			/* Update with selection */
			selectedPair = pairInfo[docIndex[wordInfo[i][0] - 1][0] + j][1];
			wordInfo[i][2] = k;
			wordInfo[i][3] = selectedPair;
			pairTopic[selectedPair - 1][k]++;
			wordTopic[currentWord - 1][k]++;
			sumWords[k]++;
			sumTopics[selectedPair - 1]++;
		} 
	}



	/* Run Gibbs sampling procedure */
	public void runGibbs() {

		int starting, iter, samples, next;

		startChain();			//start a chain randomly

		logger.info("Begin sampling......");

		/* Burn-in period */
		logger.info("Burn-in period......");
		int burin = conf.getBurnin();
		for(iter = 0; iter < burin; iter++)
		{
			logger.info(String.format("Iteration %d......", iter + 1));
			sampler();		// each Gibbs sampling iteration
		} // iter

		starting = iter;

		/* Collect samples every LAG iterations */
		logger.info("Begin collection samples");
		int NSAMPLES = conf.getNSamples(); 
		int LAG = conf.getLag();
		for(samples = 0; samples < NSAMPLES; samples++)
		{
			next = starting + LAG;
			for(iter = starting; iter < next; iter++)  
			{
				logger.info(String.format("Iteration %d......", iter + 1));
				sampler();	// each Gibbs sampling iteration
			}
			starting = iter;
			logger.info(String.format("Write sample %d", samples + 1));
			writeResult(samples + 1);
		}  
	}
	
	/**
	 * Write the pairTopic and wordTopic matrices into disk
	 * Note: sparse representation
	 * @param sampleNr
	 */
	private void writeResult(int sampleNr) {
		String toFolder = conf.getResultFolder();
		try {
			writePairTopics(toFolder, conf.getPtmatrix(), sampleNr);
			writeWordTopics(toFolder, conf.getWtmatrix(), sampleNr);
			writeCurrentChainInWordInfo(toFolder, conf.getChainInfo(), sampleNr);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void writePairTopics(String toFolder, String fileNamePattern, int sampleNr) throws IOException {
		FileWriter fpPT = null;
		try {
			String filePath = toFolder + File.separator + fileNamePattern;
			fpPT = new FileWriter(new File(String.format("%s_%d.txt", filePath, sampleNr))); // Pair/topic is saved into a file
			/* Output the pairTopic matrix (sparse matrix notation to save sapce) */
			for(int i = 0; i < nPairs; i++)
			{
				for(int j = 0; j < nTopics; j++)
				{
					if (pairTopic[i][j] > 0) {	
						fpPT.write(String.format("%d\t%d\t%d\n", i + 1, j + 1, pairTopic[i][j]));// Indexes should start from 1	
					}
				}
			}
		} finally {
			if (fpPT != null) { fpPT.close(); }
		}
	}

	private void writeWordTopics(String toFolder, String fileNamePattern, int sampleNr) throws IOException {
		FileWriter fpWT = null;
		try {
			String filePath = toFolder + File.separator + fileNamePattern;
			fpWT = new FileWriter(new File(String.format("%s_%d.txt", filePath, sampleNr))); // Word/topic is saved into a file
			/* Output the wordTopic matrix (sparse matrix notation to save sapce) */
			for(int i = 0; i < nWords; i++)
			{
				for(int j = 0; j < nTopics; j++)
				{
					if (wordTopic[i][j] > 0) {
						fpWT.write(String.format("%d\t%d\t%d\n", i + 1, j + 1, wordTopic[i][j]));// Indexes should start from 1
					}
				}
			}
		} finally {
			if (fpWT != null) { fpWT.close(); }
		}
	}

	private void writeCurrentChainInWordInfo(String toFolder, String fileNamePattern, int sampleNr) throws IOException {
		FileWriter fpChain = null;
		try {
			String filePath = toFolder + File.separator + fileNamePattern;
			fpChain = new FileWriter(new File(String.format("%s_%d.txt", filePath, sampleNr))); // Current chain in wordInfo is saved into a file
			/* Output the chain information */
			for(int i = 0; i < nTokens; i++)
			{
				fpChain.write(String.format("%d\t%d\n", wordInfo[i][2],wordInfo[i][3]));// Topic_id and a/r_pair_id			  
			}		
		} finally {
			if (fpChain != null) {fpChain.close();}
		}
	}
}
