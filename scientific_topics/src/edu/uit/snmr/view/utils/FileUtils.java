package edu.uit.snmr.view.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FileUtils {
	public static List<String> loadWordSet(String label) {
		List<String> result = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(
					label)));
			in.readLine();
			in.readLine();
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				StringTokenizer tokens = new StringTokenizer(line, "\t");
				result.add(tokens.nextToken());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}// or add .txt
		return result;
	}

	public static void main(String[] args) {
		System.out.println("11.txt".substring(0, "11.txt".indexOf('.')));
	}
}
