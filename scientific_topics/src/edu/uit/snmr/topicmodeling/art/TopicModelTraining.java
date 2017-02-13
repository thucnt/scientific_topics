package edu.uit.snmr.topicmodeling.art;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.IDSorter;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelSequence;

public class TopicModelTraining {

	public void trainTopics(File trainingTopicFolder, int numTopics, 
			double alpha, double beta, int minimumWeight, String outputFolderName) throws IOException {

		// Begin by importing documents from text to feature sequences 
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		// Pipes: lowercase, tokenize, remove stopwords, map to features
		pipeList.add( new CharSequenceLowercase() );
		pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
		pipeList.add( new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false) );
		pipeList.add( new TokenSequence2FeatureSequence() );

		InstanceList instances = new InstanceList (new SerialPipes(pipeList));

		//File corpus = new File("C:/data/allinone2");
		for (File file : trainingTopicFolder.listFiles()) {
			Reader fileReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
			instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("(.*)$"), 1, 1, 1)); // data, label, name fields
		}


		// Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
		//  Note that the first parameter is passed as the sum over topics, while
		//  the second is the parameter for a single dimension of the Dirichlet prior.        
		ParallelTopicModel model = new ParallelTopicModel(numTopics, alpha, beta);

		model.addInstances(instances);

		// Use two parallel samplers, which each look at one half the corpus and combine
		//  statistics after every iteration.
		model.setNumThreads(2);

		// Run the model for 50 iterations and stop (this is for testing only, 
		//  for real applications, use 1000 to 2000 iterations)
		model.setNumIterations(2000);
		model.estimate();

		// Show the words and topics in the first instance

		// The data alphabet maps word IDs to strings
		Alphabet dataAlphabet = instances.getDataAlphabet();

		FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
		Object obj = model.getData().get(0).instance.getName();

		LabelSequence topics = model.getData().get(0).topicSequence;
		Formatter out = new Formatter(new StringBuilder(), Locale.US);
		for (int position = 0; position < tokens.getLength(); position++) {
			out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
		}
		System.out.println(out);

		// Estimate the topic distribution of the first instance, 
		//  given the current Gibbs state.
		double[] topicDistribution = model.getTopicProbabilities(0);

		// Get an array of sorted sets of word ID/count pairs
		ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

		File outputFolder = new File(outputFolderName);
		if (!outputFolder.exists()) {
			outputFolder.mkdir();
		}

		// Show top 5 words in topics with proportions for the first document
		for (int topic = 0; topic < numTopics; topic++) {
			Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
			File topicFile = new File (outputFolder, (topic + 1) + ".txt");
			Formatter formatter = new Formatter(topicFile);
			formatter.format("%d\t%.3f\n", topic + 1, topicDistribution[topic]);
			formatter.format("#%s#\n", "vocabulary");
			formatter.flush();
			boolean isBreak = false;
			while (iterator.hasNext() && !isBreak) {				
				IDSorter idCountPair = iterator.next();
				if (idCountPair.getWeight() < minimumWeight) {
					isBreak = true;
				} else {
					formatter.format("%s\t%.0f\n", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
					formatter.flush();
				}
			}
			formatter.close();
		}		
	}
	
	/** This class illustrates how to build a simple file filter */
	private class TxtFilter implements FileFilter {

		/** Test whether the string representation of the file 
		 *   ends with the correct extension. Note that {@ref FileIterator}
		 *   will only call this filter if the file is not a directory,
		 *   so we do not need to test that it is a file.
		 */
		public boolean accept(File file) {
			return file.isFile() && file.getName().endsWith(".txt");

		}
	}
	
	public static void main(String[] args) {
		TopicModelTraining topicModelTraining = new TopicModelTraining();
		File trainingTopicFolder = new File("C:/data/test/aa");
		int numTopics = 50;
		double alpha = 1.0;
		double beta = 0.01;
		String outputFolderName = "C:/data/test/trainTopics";
		try {
			topicModelTraining.trainTopics(trainingTopicFolder, numTopics, alpha, beta, 5, outputFolderName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

