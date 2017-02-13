/**
 * 
 */
package edu.uit.snmr.topicmodeling.art;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.uit.snmr.confs.DatasetPropertiesConfig;
import edu.uit.snmr.utils.ArrayUtils;
import edu.uit.snmr.utils.DoubleArrayIndexComparator;
import edu.uit.snmr.utils.IOUtils;
import edu.uit.snmr.utils.IntegerArrayIndexComparator;
import edu.uit.snmr.utils.MatrixUtils;


/**
 * @author muonnv
 *
 */
public class ARTAlgorithm {
	private DatasetPropertiesConfig conf;
	private ARTAlgorithmCore algorithmCore;

	private int nTokens;
	private int nAuthors;
	private int nPairs;
	private int nTopics;
	private int nWords;
	private int nPairAppearances;
	private int recipientsMax;

	/* A list of global variables that store information used by Gibbs sampling */
	private int[][] pairInfo;	// Each row: doc_id a/r_pair_id
	private Map<Integer, Set<Integer>> senderDocInfo; 
	//private int[][] senderDocInfo; // Each row: senderId / doc_id(s)
	private Map<Integer, Set<Integer>> recipientDocInfo;
	//private int[][] recipientDocInfo; // Each row: recipientId / doc_id(s)
	//private int[][] wordDocInfo; // Each row: wordId / doc_id(s)
	private Map<Integer, Set<Integer>> wordDocInfo;
	private int[][] wordInfo;	// Each row: doc_id word_id topic_id a/r_pair_id
	private int[][] docIndex;	// Each row: starting index in pairInfo and # of recipients for each email

	private static Logger logger = Logger.getLogger(ARTAlgorithm.class);

	/**
	 * constructor for creating a new ARTAlgorithm for given dataset configuration
	 * @param conf refer to {@link PropertiesConfiguration}. It contains dataset configuration
	 */
	public ARTAlgorithm(DatasetPropertiesConfig conf) {
		this.conf = conf;
		this.nTokens = conf.getNTokens();
		this.nAuthors = conf.getNAuthors();
		this.nPairs = nAuthors * nAuthors;
		this.nTopics = conf.getNTopics();
		this.nWords = conf.getNTerms();
		this.nPairAppearances = conf.getNPairAppearances();
		this.recipientsMax = conf.getRecipientMax();
		this.pairInfo = new int[nPairAppearances][2];
		this.senderDocInfo = new HashMap<Integer, Set<Integer>>();
		this.recipientDocInfo = new HashMap<Integer, Set<Integer>>();
		this.wordDocInfo = new HashMap<Integer, Set<Integer>>();
		this.wordInfo = new  int[nTokens][4];
		this.docIndex = new int[conf.getNMessages()][2];
		this.algorithmCore = 
				new ARTAlgorithmCore(conf, nTokens, nTopics, nWords, nPairs, pairInfo, wordInfo, docIndex);
	}


	/** 
	 * Fetch data from disk (two files containing author/recipient information and word information, respectively)
	 * For formats, see readme.txt for details
	 */
	private void loadData() {
		try {
			this.loadEmailSenderRecipients(conf.getMessageSenderRecipientsFile(), conf.getString("PAIRFILE_PATTERN"));
			this.loadWords(conf.getMessageTermFrequenlyPath(), conf.getString("WORDFILE_PATTERN"));
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * load pair-topic matrix. example: '../enron_tiny_result/enron_tiny_pt_10.txt'
	 * @param pairTopicTxtFilePath path to the pair-topic file
	 * @return
	 */
	public int[][] loadPairTopicMatrix(String pairTopicTxtFilePath) {
		Scanner scanner = IOUtils.makeScanner(pairTopicTxtFilePath);
		int[][] pt = new  int[nPairs][nTopics];
		pt = MatrixUtils.initializeMatrix(pt);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();				
			if (line != null && line.trim().length() > 0 
					&& line.matches("^\\d+\\s\\d+\\s\\d+$")) {					
				String[] subStrs = line.split("\\s");
				pt[Integer.parseInt(subStrs[0]) - 1][Integer.parseInt(subStrs[1]) - 1] 
						= Integer.parseInt(subStrs[2]);
			}
		}
		scanner.close();
		MatrixUtils.print(pt);
		return pt;
	}

	//'../enron_tiny_result/enron_tiny_wt_10.txt'
	private  int[][] loadWordTopicMatrix(String wordTopicTxtFilePath) {
		Scanner scanner = IOUtils.makeScanner(wordTopicTxtFilePath);
		int[][] wt = new int[nWords][nTopics];
		wt = MatrixUtils.initializeMatrix(wt);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();				
			if (line != null && line.trim().length() > 0 
					&& line.matches("^\\d+\\s\\d+\\s\\d+$")) {					
				String[] subStrs = line.split("\\s");
				wt[Integer.parseInt(subStrs[0]) - 1][Integer.parseInt(subStrs[1]) - 1] 
						= Integer.parseInt(subStrs[2]);
			}
		}
		scanner.close();
		return wt;
	}

	public void loadEmailSenderRecipients(String pairFilePath, String rowPattern) throws FileNotFoundException {
		int counter = 1;		// Deal with first line outside of the while loop below
		int maxRecipients = 0;
		int recipient = 0;
		int docId = 1;			// Deal with first doc separately below
		Scanner fpPair = null;
		try {
			logger.info("Loading author/recipient information......");

			fpPair = new Scanner(new File(pairFilePath)); //email_author_recipient.txt

			docIndex[0][0] = 0;		// Set the starting index of the first document in pairInfo to 0
			
			
			if (fpPair.hasNextLine()) {
				String line = fpPair.nextLine();				
				if (line != null && line.trim().length() > 0 && line.matches(rowPattern)) {					
					String[] subStrs = line.split("\\s");
					
					pairInfo[0][0] = new Integer(subStrs[0]); //email_id
					pairInfo[0][1] =  new Integer(subStrs[1]); //sender_id
					recipient = new Integer(subStrs[2]); //rpt_i
					
					// moi them
					Set<Integer> valuesOfSender = new HashSet<Integer>();
					valuesOfSender.add(new Integer(subStrs[0]));
					this.senderDocInfo.put(new Integer(subStrs[1]), valuesOfSender);
					
					Set<Integer> valuesOfRecipient = new HashSet<Integer>();
					valuesOfRecipient.add(new Integer(subStrs[0]));
					this.recipientDocInfo.put(recipient, valuesOfRecipient);
				} else {
					logger.error("(137)Error occurred while loading " + pairFilePath);
					return;
				}
			}



			/*
			 * Convert (sender-recipient) pair into a single number index
			 * For consistency, still ranges from 1 to NAUTHORS * NAUTHORS
			 */
			pairInfo[0][1] = (pairInfo[0][1] - 1) * nAuthors + recipient; 			

			/* Collecting the starting index in pairInfo and the number of recipients of this email */
			while (counter < nPairAppearances)
			{
				String line = fpPair.nextLine();				
				if (line != null && line.trim().length() > 0 && line.matches(rowPattern)) {
					
					String[] subStrs = line.split("\\s");	
					int mailId = new Integer(subStrs[0]);
					int senderId =  new Integer(subStrs[1]);
					pairInfo[counter][0] = mailId;
					pairInfo[counter][1] = senderId;
					recipient = new Integer(subStrs[2]);
					
					// them vo
					if (this.senderDocInfo.containsKey(senderId)) {
						Set<Integer> values = this.senderDocInfo.get(senderId);
						values.add(mailId);
						this.senderDocInfo.put(senderId, values);
					} else {
						Set<Integer> values = new HashSet<Integer>();
						values.add(mailId);
						this.senderDocInfo.put(senderId, values);
					}
					
					if (this.recipientDocInfo.containsKey(recipient)) {
						Set<Integer> values = this.recipientDocInfo.get(recipient);
						values.add(mailId);
						this.recipientDocInfo.put(recipient, values);
					} else {
						Set<Integer> values = new HashSet<Integer>();
						values.add(mailId);
						this.recipientDocInfo.put(recipient, values);
					}
				} else {
					logger.error("(159)Error occurred while loading" + pairFilePath);
					return;
				}

				/* Convert (sender-recipient) pair into a single number index */
				pairInfo[counter][1] = (pairInfo[counter][1] - 1) * nAuthors + recipient; 
				
				if( pairInfo[counter][0] > pairInfo[counter - 1][0]) 			// A new email
				{
					docIndex[docId][0] = counter;					// Set starting index for current docId
					docIndex[docId - 1][1] = counter - docIndex[docId - 1][0]; 	// Count the number of recipients for previous email
					if(docIndex[docId - 1][1] > maxRecipients)			// Update maxRecipents if necessary
						maxRecipients = docIndex[docId - 1][1];
					docId++;
				}
				counter++; 
			}

			docIndex[docId - 1][1] = counter - docIndex[docId - 1][0]; 			//count the number of recipients for last email
			if(docIndex[docId - 1][1] > maxRecipients)
				maxRecipients = docIndex[docId - 1][1];

			if(maxRecipients > recipientsMax)
			{
				
				logger.error("RECIPIENTSMAX is too small, please adjust it to at least "+ maxRecipients);
				return;
			}
		} finally {
			if (fpPair != null) { fpPair.close(); }
		}
		MatrixUtils.print(docIndex);
	}


	public void loadWords(String wordFilePath, String rowPattern) throws FileNotFoundException {
		logger.info("Loading word information......");
		Scanner fpWord = null;
		int counter = 0;		// Deal with first line outside of the while loop below
		int wordFreq = 0;
		try {
			fpWord = new Scanner(new File(wordFilePath));//enron_tiny_words.txt

			//read message-word information
			while (counter < nTokens) {
				String line = fpWord.nextLine();
				if (line != null && line.trim().length() > 0 && line.matches(rowPattern)) {					
					String[] subStrs = line.split("\\s");					
					wordInfo[counter][0] = new Integer(subStrs[0]);
					wordInfo[counter][1] = new Integer(subStrs[1]);
					wordFreq = new Integer(subStrs[2]);
					
					if (this.wordDocInfo.containsKey(wordInfo[counter][1])) {
						Set<Integer> values = this.wordDocInfo.get(wordInfo[counter][1]);
						values.add(wordInfo[counter][0]);
						this.wordDocInfo.put(wordInfo[counter][1], values);
					} else {
						Set<Integer> values = new HashSet<Integer>();
						values.add(wordInfo[counter][0]);
						this.wordDocInfo.put(wordInfo[counter][1], values);
					}
				} else {
					logger.error("(201)Error occurred while loading " + wordFilePath);
					return;
				}

				while(wordFreq > 1 )	// If this word appears more than once in this document, repeat them in wordInfo. 
					// Need to draw a topic for each token
				{
					counter++;
					wordFreq--;
					wordInfo[counter][0] = wordInfo[counter - 1][0];
					wordInfo[counter][1] = wordInfo[counter - 1][1];
				}
				counter++;
			}
			//MatrixUtils.print(wordInfo);
			logger.info("Loading finished.");
		} finally {

			if (fpWord != null) { fpWord.close(); }
		}
	}


	public void run() {
		loadData();
		long startTime = System.currentTimeMillis();
		this.algorithmCore.runGibbs();
		long endTime = System.currentTimeMillis();
		long totalSeconds = endTime - startTime;

		/* Output the elapsed time */
		long hours = (totalSeconds / 3600);
		long minutes = ((totalSeconds - hours * 3600) / 60);
		long seconds = (totalSeconds - hours * 3600 - minutes * 60);
		logger.info(String.format("Time for this run: %d hour(s) %d minute(s) %d second(s)\n", hours, minutes, seconds));
	}

	//-------------------------------------------------------

	// Get the top N words and authors for each topic
	// Note: Currently works for enron email dataset.
	public void analyzeTopPairsWords() {
		String srcFolder = conf.getResultFolder();
		int maxSampleNr = conf.getNSamples();
		String ptFilePath = srcFolder + File.separator + conf.getPtmatrix();
		int[][] pt = loadPairTopicMatrix(String.format("%s_%d.txt", ptFilePath, maxSampleNr));

		String wtfilePath = srcFolder + File.separator + conf.getWtmatrix();
		int[][] wt = loadWordTopicMatrix(String.format("%s_%d.txt", wtfilePath, maxSampleNr));
		String[] vocabularySet = loadVocabularySet(conf.getTermsPath());
		String[] authorSet = loadAuthorSet(conf.getEmployeeListFile());

		//Decompose pair topic to author-topic and recipient-topic
		int[][] bigAT = new int[nAuthors][nTopics]; // author-topic or sender
		bigAT = MatrixUtils.initializeMatrix(bigAT);
		int[][] bigAT2 = new int[nAuthors][nTopics];// recipient-topic
		bigAT2 = MatrixUtils.initializeMatrix(bigAT2);		


		//marginalize pair
		int[] idx = new int[nAuthors];
		for (int i = 0; i < idx.length; i++) {
			idx[i] = i * nAuthors;
		}

		for (int i = 1; i <= nAuthors; i++) {
			int[][] subMatrix = 
					MatrixUtils.getSubMatrix(pt, ((i - 1) * nAuthors), 
							(i * nAuthors - 1), 0, pt[i].length - 1);

			MatrixUtils.setRow(bigAT, i - 1, MatrixUtils.sum(subMatrix));


			int[] iplusidx = ArrayUtils.plus(idx, i -1);			
			int[][] subMatrix2 = 
					MatrixUtils.getSubMatrix(pt, iplusidx, 0, pt[i].length -1);

			MatrixUtils.setRow(bigAT2, i - 1, MatrixUtils.sum(subMatrix2));			

		}
		//check whether some author never send email or receive email
		idx = ArrayUtils.findIndicesOfZeroElements(MatrixUtils.sum(bigAT, 2));

		for (int i = 0; i < idx.length; i++) {
			MatrixUtils.setRow(bigAT, idx[i], ArrayUtils.createArrayWithOnes(nAuthors));
		}

		idx = ArrayUtils.findIndicesOfZeroElements(MatrixUtils.sum(bigAT2, 2));
		for (int i = 0; i < idx.length; i++) {
			MatrixUtils.setRow(bigAT2, idx[i], ArrayUtils.createArrayWithOnes(nAuthors));
		}

		//-------------------------------------------
		
		FileWriter writer = IOUtils.makeFileWriter(conf.getResultFolder() + File.separator + conf.getString("TOPNRESULTS"));	
		//list the top 10 topics of all authors
		listToptenTopicsOfAuthors(authorSet, bigAT, writer);

		//list the top 10 topics of all recipients
		listToptenTopicsOfRecipients(authorSet, bigAT2, writer);

		//writeTopPairsWords(writer, pt, wt, authorSet, vocabularySet, topN);		
		try {
			writer.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		String extractedTopicsFolderName = conf.getExtractedTopicsFolder();
		File extractedTopicsFolder = new File(extractedTopicsFolderName);
		if (!extractedTopicsFolder.exists()) {
			extractedTopicsFolder.mkdirs();
		}
		writeTopPairsWords(extractedTopicsFolderName, pt, wt, authorSet, vocabularySet);
	}

	private void writeTopPairsWords(FileWriter writer, int[][] pt, int[][] wt, String[] authorSet, String[] wordSet, int topN) {
		try {
			for (int i = 0; i < nTopics; i++)  {
				int[] topPairs = MatrixUtils.column(pt, i);
				double[] ptopPairs = ArrayUtils.devide(topPairs, ArrayUtils.sum(topPairs)); // normalized probabilities
				//x: probability

				DoubleArrayIndexComparator comparator = new DoubleArrayIndexComparator(ptopPairs);
				Integer[] ttopPairs = comparator.createIndexArray();
				Arrays.sort(ttopPairs, comparator);
				Arrays.sort(ptopPairs);
				ttopPairs = ArrayUtils.reverse(ttopPairs);
				ptopPairs = ArrayUtils.reverse(ptopPairs);

				//compute the most probable words on each topic 
				int[] topWords = MatrixUtils.column(wt, i);

				double[] ptopWords = ArrayUtils.devide(topWords, ArrayUtils.sum(topWords)); //normalized probabilities

				DoubleArrayIndexComparator comparator2 = new DoubleArrayIndexComparator(ptopWords);
				Integer[] ttopWords = comparator2.createIndexArray();
				Arrays.sort(ttopWords, comparator2);
				Arrays.sort(ptopWords);
				ttopWords = ArrayUtils.reverse(ttopWords);
				ptopWords = ArrayUtils.reverse(ptopWords);				

				writer.write(String.format("\nTopic %d\n", i));
				writer.flush();
				for (int j = 0; j < topN; j++) {
					writer.write(String.format("%s\t%.4f\n", wordSet[ttopWords[j]], ptopWords[j]));
					writer.flush();
				}

				for (int k = 0; k < topN; k++) {
					int sender = Math.round((ttopPairs[k] - 1) / nAuthors) + 1;
					int recipient = ttopPairs[k] - (sender - 1)* nAuthors;
					writer.write(String.format("%s\t%.4f\n", authorSet[sender - 1], ptopPairs[k])); 
					writer.flush();					
					//						writer.write(String.format("%s\t\n", authorSet[recipient-1]));
					//						writer.flush();
				}
			}} catch (IOException e) {
				logger.error(e.getMessage());
			}
	}
	
	private void writeTopPairsWords(String topicsFolder, int[][] pt, int[][] wt, String[] authorSet, String[] wordSet) {
		try {
			for (int i = 0; i < nTopics; i++)  {
				int[] topPairs = MatrixUtils.column(pt, i);
				double[] ptopPairs = ArrayUtils.devide(topPairs, ArrayUtils.sum(topPairs)); // normalized probabilities
				//x: probability

				DoubleArrayIndexComparator comparator = new DoubleArrayIndexComparator(ptopPairs);
				Integer[] ttopPairs = comparator.createIndexArray();
				Arrays.sort(ttopPairs, comparator);
				Arrays.sort(ptopPairs);
				ttopPairs = ArrayUtils.reverse(ttopPairs);
				ptopPairs = ArrayUtils.reverse(ptopPairs);

				//compute the most probable words on each topic 
				int[] topWords = MatrixUtils.column(wt, i);

				double[] ptopWords = ArrayUtils.devide(topWords, ArrayUtils.sum(topWords)); //normalized probabilities

				DoubleArrayIndexComparator comparator2 = new DoubleArrayIndexComparator(ptopWords);
				Integer[] ttopWords = comparator2.createIndexArray();
				Arrays.sort(ttopWords, comparator2);
				Arrays.sort(ptopWords);
				ttopWords = ArrayUtils.reverse(ttopWords);
				ptopWords = ArrayUtils.reverse(ptopWords);				

				FileWriter writer = IOUtils.makeFileWriter(topicsFolder + File.separator + i + ".txt");
				writer.write("#terms#\n");
				writer.flush();
				for (int j = 0; j < conf.getNTopTerm(); j++) {
					writer.write(String.format("%s\t%.4f\n", wordSet[ttopWords[j]], ptopWords[j]));
					writer.flush();
				}

				writer.write("#senders#\n");
				writer.flush();
				for (int k = 0; k < conf.getNTopAuthor(); k++) {
					int sender = Math.round((ttopPairs[k] - 1) / nAuthors) + 1;
					//int recipient = ttopPairs[k] - (sender - 1)* nAuthors;
					writer.write(String.format("%s\t%.4f\n", authorSet[sender - 1], ptopPairs[k])); 
					writer.flush();					
//					writer.write(String.format("%s\t\n", authorSet[recipient-1]));
//					writer.flush();
				}
				
				writer.write("#recipients#\n");
				writer.flush();
				for (int k = 0; k < conf.getNTopRecipient(); k++) {
					int sender = Math.round((ttopPairs[k] - 1) / nAuthors) + 1;
					int recipient = ttopPairs[k] - (sender - 1)* nAuthors;								
					writer.write(String.format("%s\t\n", authorSet[recipient-1]));
					writer.flush();
				}
				writer.close();
			}
			
		} catch (IOException e) {
				logger.error(e.getMessage());
			}
	}

	//list the top 10 topics of all authors
	private void listToptenTopicsOfAuthors(String[] authorSet, int[][] bigAT, FileWriter writer) {	
			
		try {
			writer.write("list the top 10 topics of all authors(senders)\n");
			writer.flush();
			for (int i = 0; i < nAuthors; i++) {
				int[] row = MatrixUtils.row(bigAT, i);
				IntegerArrayIndexComparator comparator = new IntegerArrayIndexComparator(row);
				Integer[] indices = comparator.createIndexArray();
				Arrays.sort(indices, comparator);				
				Arrays.sort(row);			
				
								
				writer.write(authorSet[i].toString() + ": ");
				writer.flush();

				StringBuilder builder = new StringBuilder("Topic: ");
				for (int j = indices.length - 1; j > indices.length - 10 - 1; j--) {
					builder.append(indices[j] + " ");
				}
				writer.write(builder.toString() + "\n");
				writer.flush();
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	//list the top 10 topics of all recipients
	private void listToptenTopicsOfRecipients(String[] authorSet, int[][] bigAT2, FileWriter writer) {

		try {
			writer.write("list the top 10 topics of all recipients\n");
			writer.flush();
			for (int i = 0; i < nAuthors; i++) {
				int[] row = bigAT2[i];
				IntegerArrayIndexComparator comparator = new IntegerArrayIndexComparator(row);
				Integer[] indices = comparator.createIndexArray();
				Arrays.sort(indices, comparator);				
				Arrays.sort(row);
				//Arrays.sort(row);

				writer.write(authorSet[i].toString() + ": ");
				writer.flush();

				StringBuilder builder = new StringBuilder("Topic: ");
//				System.out.println(Arrays.toString(indices));
				for (int j = indices.length - 1; j > indices.length - 10 - 1; j--) {
					builder.append(indices[j] + " ");
				}
				writer.write(builder.toString() + "\n");
				writer.flush();
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		} 
	}


	private String[] loadVocabularySet(String wordsTxtFilePath) {
		Scanner scanner = IOUtils.makeScanner(wordsTxtFilePath);
		String[] vocabularySet = new String[nWords];
		int i = 0;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line != null && line.trim().length() > 0) {
				vocabularySet[i] = line.trim();
				i++;
			}
		}
		scanner.close();
		return vocabularySet;
	}

	private String[] loadAuthorSet(String authorTxtFilePath) {
		String[] authorSet = new String[nAuthors];
		Scanner scanner = IOUtils.makeScanner(authorTxtFilePath);
		int i = 0;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] array = line.trim().split("\t");
			authorSet[i] = array[0].trim();
			i++;
		}
		scanner.close();
		return authorSet;
	}

	public void comTopicDist(String pairTopicTxtFilePath, String wordTopicTxtFilePath, String similarTopicsFilePath) {
		int[][] pt = loadPairTopicMatrix(pairTopicTxtFilePath);
		int[][] wt = loadWordTopicMatrix(wordTopicTxtFilePath);

		double[][] distributionOfWT = new double[nTopics][nTopics];
		distributionOfWT = MatrixUtils.initializeMatrix(distributionOfWT);

		double[][] distributionOfPT = new double[nTopics][nTopics];
		distributionOfPT = MatrixUtils.initializeMatrix(distributionOfPT);

		double[][] pWT = MatrixUtils.rightDivide(wt, 
				MatrixUtils.repmat(MatrixUtils.sum(wt), nWords, 1));
		double[][] pPT = MatrixUtils.rightDivide(pt, 
				MatrixUtils.repmat(MatrixUtils.sum(pt), nWords, 1));

		//Compute JSD between any two topics
		for (int i = 0; i < nTopics; i++) {
			for (int j = 0; j < nTopics; j++) {
				//for j = (i+1):nTopics % compute half
				distributionOfWT[i][j] = 
						DivergenceUtils.jsd(MatrixUtils.column(pWT, i), MatrixUtils.column(pWT, j), 0.5, 0.5); //no knowledge which topic is more important

				distributionOfPT[i][j] = 
						DivergenceUtils.jsd(MatrixUtils.column(pPT, i), MatrixUtils.column(pPT, j), 0.5, 0.5);
			}
		}

		writeToFile(distributionOfWT, distributionOfPT, similarTopicsFilePath);
	}

	private void writeToFile(double[][] distributionOfWT, double[][] distributionOfPT, String similarTopicsFilePath) {
		try {
			FileWriter writer = IOUtils.makeFileWriter(similarTopicsFilePath);
			int topN = 5;
			writer.write("According to word:\n");
			writer.flush();

			for (int i = 0; i < nTopics; i++) {
				double[] row = MatrixUtils.row(distributionOfWT, i);
				DoubleArrayIndexComparator comparator = new DoubleArrayIndexComparator(row);
				Integer[] indexes = comparator.createIndexArray();
				Arrays.sort(indexes, comparator);				
				Arrays.sort(row);			
				writer.write(String.format("Topic %d:", i));
				writer.flush();

				StringBuilder builder = new StringBuilder();
				for (int k = 1; k < topN + 1; k++) {
					builder.append(String.format("\t%d(%.4f)", indexes[k], row[k])); // chua xong
				}
				builder.append("\n");
				writer.write(builder.toString());
				writer.flush();
			}

			writer.write("\nAccording to author/recipient pair:\n");
			writer.flush();
			for (int i = 0; i < nTopics; i++) {
				double[] row = MatrixUtils.row(distributionOfPT, i);
				DoubleArrayIndexComparator comparator = new DoubleArrayIndexComparator(row);
				Integer[] indexes = comparator.createIndexArray();
				Arrays.sort(indexes, comparator);
				Arrays.sort(row);
				writer.write(String.format("Topic %d:", i));
				writer.flush();

				StringBuilder builder = new StringBuilder();
				for (int k = 1; k < topN + 1; k++) {
					builder.append(String.format("\t%d(%.4f)", indexes[k], row[k])); 
				}
				builder.append("\n");
				writer.write(builder.toString());
				writer.flush();
			}
			writer.close();
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
	}
}
