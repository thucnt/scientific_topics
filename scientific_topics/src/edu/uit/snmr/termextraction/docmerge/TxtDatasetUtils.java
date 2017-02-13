/**
 * 
 */
package edu.uit.snmr.termextraction.docmerge;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author muonnv
 *
 */
public class TxtDatasetUtils {
	

	public static void mergeTxtDocumentsByTopic(String txtDatasetURI, String toFolderPath) throws IOException {
		File toFolder = new File(toFolderPath);
		if(!toFolder.exists()) {
			toFolder.mkdir();
		}
		File datasetFolder = new File(txtDatasetURI);
		if (datasetFolder.exists() && datasetFolder.isDirectory()) {
			File[] topicFolders = datasetFolder.listFiles(new FolderFilter());
			for (File eachTopicFolder : topicFolders) {				
				mergeDocumentIntoATopic(eachTopicFolder, 
						toFolderPath + File.separator + eachTopicFolder.getName() + ".txt");
			}
		}
	}
	
	public static void mergeTxtDocumentsToOneFile(String txtDatasetURI, String toFilePath) 
			throws IOException {
		FileWriter writer = null;
		try {
			File datasetFolder = new File(txtDatasetURI);
			writer = new FileWriter(new File(toFilePath), true);
			if (datasetFolder.exists()  && datasetFolder.isDirectory()) {
				File[] topicFolders = datasetFolder.listFiles(new FolderFilter());
				for (File eachTopicFolder : topicFolders) {				
					mergeTxtDocumentsIntoATopic(eachTopicFolder, writer);
				}
			}
		} finally {
			if (writer != null) { writer.close();}
		}
	}
	
	/**
	 * merge txt documents for each topic. the input is a dataset for all topics.
	 *  Therefore, the 
	 * @param topicDatasetURI
	 * @param destinationTxt
	 * @throws IOException 
	 */
	private static void mergeDocumentIntoATopic(File topicFolder, String outputTopicPath) 
			throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File(outputTopicPath), true);
			File[] sourceTxtFiles = getTxtFiles(topicFolder);
			String fileName = topicFolder.getName();
			String documentId = (fileName.length() >=3)? fileName.substring(0, 3) : fileName;

			if (sourceTxtFiles != null) {
				for (int i = 0; i < sourceTxtFiles.length; i++) {
					mergeTxtDocument(sourceTxtFiles[i], writer, documentId.toUpperCase() + (i + 1));
				}
			}	
		} finally {
			if (writer != null) { writer.close();}
		}
	}

	/**
	 * merge txt documents for each topic. the input is a dataset for all topics.
	 *  Therefore, the 
	 * @param topicDatasetURI
	 * @param destinationTxt
	 * @throws IOException 
	 */
	public static void mergeTxtDocumentsIntoATopic(File topicFolder, FileWriter outputWriter)
			throws IOException {		
		File[] sourceTxtFiles = getTxtFiles(topicFolder);
		String fileName = topicFolder.getName();
		String documentId = (fileName.length() >=3)? fileName.substring(0, 3) : fileName;
		if (sourceTxtFiles != null) {
			for (int i = 0; i < sourceTxtFiles.length; i++) {
				mergeTxtDocument(sourceTxtFiles[i], outputWriter, documentId.toUpperCase() + (i+1));
			}

		}

	}
		

	private static void mergeTxtDocument(File sourceTxtFile, FileWriter writer, String topic) throws IOException {
		StringBuilder builder = readTxtDocument(sourceTxtFile);		
		writer.append(topic + "\t" + builder.toString() + System.getProperty("line.separator"));
		writer.flush();
	}
	
	private static StringBuilder readTxtDocument(File sourceTxtFile) throws FileNotFoundException {
		Scanner scanner = 
				new Scanner(new BufferedInputStream(new FileInputStream(sourceTxtFile)));		
		StringBuilder builder = new StringBuilder();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line != null && !line.trim().equals("")) {
				builder.append(line + " ");
			}
		}
		scanner.close();
		return builder;
	}

	private static File[] getTxtFiles(File file) {
		if (file.isDirectory()) {
			return file.listFiles(new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					int dot = pathname.getName().lastIndexOf(".");
					String extension = pathname.getName().substring(dot + 1);
					return extension.equalsIgnoreCase("txt");
				}
			});		
		} else {
			return null;
		}
		
	}
	
	
}

class FolderFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {			
		return pathname.isDirectory();
	}
	
}
