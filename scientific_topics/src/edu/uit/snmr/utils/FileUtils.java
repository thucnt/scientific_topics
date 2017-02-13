/**
 * 
 */
package edu.uit.snmr.utils;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * @author muonnv
 *
 */
public class FileUtils {
	private static final Logger logger = Logger.getLogger(FileUtils.class);

	public static void cleanupFoldersExcept(String path, String folderPattern) {

		File folder = new File(path);
		if (folder.isDirectory()) {
			File[] subFolders = folder.listFiles();
			for (File eachSubFolder : subFolders) {
				if (eachSubFolder.exists()
						&& eachSubFolder.isDirectory()){
					File[] subSubFolders = eachSubFolder.listFiles();

					for (File eachSubSubFolder : subSubFolders) {
						//System.out.println(eachSubSubFolder.getName().matches(folderPattern));
						if (eachSubSubFolder.exists()
								&& eachSubSubFolder.isDirectory()
								&& !eachSubSubFolder.getName().matches(folderPattern)) {
							deleteFolerRecursively(eachSubSubFolder);
						}
					}
				}
			}
		}
	}

	public static void deleteFolerRecursively(String FolderPath) {
		File folder = new File(FolderPath);
		if (folder.isDirectory()) {
			File[] subFiles = folder.listFiles();			
			for (File eachSubFile : subFiles) {
				if (eachSubFile.isFile()) {
					logger.warn("Can delete file " 
							+ eachSubFile.getName() + ": " + eachSubFile.delete());

				}
				if (eachSubFile.isDirectory()) {
					deleteFolerRecursively(eachSubFile.getAbsolutePath());
				}				
			}
			logger.warn("Can delete foler " 
					+ folder.getName() + ": " + folder.delete());
		}
	}

	public static void deleteFolerRecursively(File folder) {
		if (folder.exists() && folder.isDirectory()) {
			File[] subFiles = folder.listFiles();			
			for (File eachSubFile : subFiles) {
				if (eachSubFile.isFile()) {
					logger.warn("Can delete file " 
							+ eachSubFile.getName() + ": " + eachSubFile.delete());

				}
				if (eachSubFile.isDirectory()) {
					deleteFolerRecursively(eachSubFile.getAbsolutePath());
				}				
			}
			logger.warn("Can delete foler " 
					+ folder.getName() + ": " + folder.delete());
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		cleanupFoldersExcept("C:/data/enron_mail_20110402/maildir", "(.*?)sent(.*?)");

	}

}
