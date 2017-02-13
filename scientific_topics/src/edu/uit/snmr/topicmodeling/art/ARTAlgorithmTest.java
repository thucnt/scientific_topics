package edu.uit.snmr.topicmodeling.art;

import java.io.FileNotFoundException;

import edu.uit.snmr.confs.DatasetPropertiesConfig;


public class ARTAlgorithmTest {

	public static void main(String[] args) {


		DatasetPropertiesConfig conf = DatasetPropertiesConfig.getInstance();
		//PropertiesConf conf = ConfFactory.makeDatasetConfigs("conf/enron.properties");
		ARTAlgorithm algorithm = new ARTAlgorithm(conf);
//		try {
//			//algorithm.loadWords(conf.getMessageTermFrequenlyPath(), conf.getString("WORDFILE_PATTERN"));
//		//	algorithm.loadEmailSenderRecipients(conf.getMessageSenderRecipientsFile(), conf.getString("PAIRFILE_PATTERN"));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//algorithm.run();
		algorithm.analyzeTopPairsWords();
		conf.clear();


		//int[][] pairTopic = new int[1416613329][1];
	}
}
