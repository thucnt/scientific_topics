/**
 * 
 */
package edu.uit.snmr.topicmodeling.art;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.ATopic;

import edu.uit.snmr.view.utils.GUIUtils;

/**
 * @author muonnv
 *
 */
public class TopicTokenizer {


	public ATopic loadTrainedTopic(File topicPath) throws FileNotFoundException {
		List<String> wordSet = new ArrayList<String>();
		Scanner scanner = new Scanner(topicPath);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line != null && !line.trim().isEmpty()
					&& line.matches("^\\w+\\t\\d+$")) {					
				String[] subStrs = line.split("\\t");
				if (subStrs.length > 0) {
					wordSet.add(subStrs[0]);
				}
			}
		}
		scanner.close();
		return new ATopic(topicPath.getName().substring(0, topicPath.getName().lastIndexOf('.')), wordSet);
	}

	public ATopic loadExtractedTopic(File topicPath) throws FileNotFoundException {
		List<String> wordSet = new ArrayList<String>();
		Scanner scanner = new Scanner(topicPath);
		if (scanner.hasNextLine()) {
			String firtLine = scanner.nextLine();
			if (firtLine == null || !firtLine.trim().endsWith("#terms#") ) {
				scanner.close();
				return null;
			}
		}
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line != null && !line.trim().isEmpty()
					&& line.matches("^\\w+\\s\\d+[.]?\\d*$")) {					
				String[] subStrs = line.split("\\s");
				if (subStrs.length > 0) {
					wordSet.add(subStrs[0]);
				}
			} else if (line != null && line.trim().equalsIgnoreCase("#senders#")) {
				break;
				
			}
		}
		scanner.close();
		return new ATopic(topicPath.getName().substring(0, topicPath.getName().lastIndexOf('.')), wordSet);
	}
	
	public List<ATopic> loadTrainedTopics(File trainTopicsFolder) throws FileNotFoundException {
		List<ATopic>  result = new ArrayList<ATopic>();
		if (trainTopicsFolder == null || !trainTopicsFolder.isDirectory()) {
			return null;
		}
		File[] files = trainTopicsFolder.listFiles(new TxtFileFilter());
		for (File file : files) {
			result.add(loadTrainedTopic(file));
		}
		return result;
	}
	
	public List<ATopic> loadTrainedTopicsFromXml(String xmlFilePath) throws FileNotFoundException {
		
		return GUIUtils.loadTopicFromXml(xmlFilePath);
	}
	
	
	public List<ATopic> loadExtractedTopics(File extractedTopicsFolder) throws FileNotFoundException {
		List<ATopic>  result = new ArrayList<ATopic>();
		if (extractedTopicsFolder == null || !extractedTopicsFolder.isDirectory()) {
			return null;
		}
		File[] files = extractedTopicsFolder.listFiles(new TxtFileFilter());
		for (File file : files) {
			result.add(loadExtractedTopic(file));
		}
		return result;
	}

	private	class TxtFileFilter implements java.io.FileFilter {


		@Override
		public boolean accept(File f) {
			int i = f.getName().lastIndexOf('.');
			if (i > 0) {
				String extension = f.getName().substring(i+1);
				return extension.equalsIgnoreCase("txt");
			}
			return false;
		}
	}	
}


