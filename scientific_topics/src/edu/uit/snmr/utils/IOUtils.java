/**
 * 
 */
package edu.uit.snmr.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.Logger;

/**
 * @author muonnv
 *
 */
public class IOUtils {

	private static Logger logger;

	public static Scanner makeScanner(String filePath) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filePath));
		} catch (FileNotFoundException e) {
			if (logger == null) {
				logger = Logger.getLogger(IOUtils.class);
			}
			logger.error(e.getMessage());
		}
		return scanner;
	}

	public static FileWriter makeFileWriter(String filePath) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(new File(filePath));
		} catch (IOException e) {
			if (logger == null) {
				logger = Logger.getLogger(IOUtils.class);
			}
		}
		return writer;
	}


	public static Logger getLogge(String className) {
		logger = Logger.getLogger(className);
		return logger;
	}

	public static void closeFileWriter(FileWriter writer) {
		try {
			writer.close();
		} catch (IOException e) {
			if (logger == null) {
				logger = Logger.getLogger(IOUtils.class);
			}
		}
	}
}
