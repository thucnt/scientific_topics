package edu.uit.snmr.view.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class FileService {
	public static void saveProperties(String dataset, String users, String resultDir, String topics, String from, String to, String alpha, String beta, String burnIn, String sampling) {
		Properties prop = new Properties();

		try {
			// set the properties value
			prop.setProperty("dataset", dataset);
			prop.setProperty("users", users);
			prop.setProperty("resultDir", resultDir);
			prop.setProperty("topics", topics);
			prop.setProperty("from", from);
			prop.setProperty("to", to);
			prop.setProperty("alpha", alpha);
			prop.setProperty("beta", beta);
			prop.setProperty("burnIn", burnIn);
			prop.setProperty("sampling", sampling);

			// save properties to project root folder
			prop.store(new FileOutputStream("dataset.properties"), null);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		saveProperties("D:\\data", "D:\\users", "D:\\result_dir", "50", "10/10/2010", "20/10/2012", "0.5", "0.01", "50", "1000");
	}
}
