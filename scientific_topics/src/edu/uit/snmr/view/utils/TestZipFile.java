package edu.uit.snmr.view.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class TestZipFile {
	public static MyTreeNode loadTopicsFromDir(String zipFile, MyTreeNode top)
			throws ZipException, IOException {
		ZipFile zf = new ZipFile(new File(zipFile));
		Enumeration<? extends ZipEntry> entries = zf.entries();
		if (entries.hasMoreElements()) {
			entries.nextElement();// omit first entry
		}
		while (entries.hasMoreElements()) {
			ZipEntry e = entries.nextElement();
			if (!e.isDirectory()) {
				MyTreeNode child = new MyTreeNode(fileName(e.getName().substring(0, e.getName().indexOf('.'))));
				top.add(child);
			} else {
				String parentName = e.getName();
				MyTreeNode parent = new MyTreeNode(fileName(parentName));
				while (entries.hasMoreElements()) {
					e = entries.nextElement();
					if (e.getName().indexOf(parentName) != -1) {
						MyTreeNode ch = new MyTreeNode(fileName(e.getName()));
						ch.setWordSet(readContentZipFile(zf, e));
						parent.add(ch);
					} else {
						MyTreeNode ch = new MyTreeNode(fileName(e.getName()));
						top.add(ch);
						break;
					}
				}
				top.add(parent);
			}
		}
		zf.close();
		return top;
	}

	private static String fileName(String fileName) {
		StringTokenizer tokens = new StringTokenizer(fileName, "/");
		int length = tokens.countTokens();
		String result = "";
		for (int i = 0; i < length; i++) {
			result = tokens.nextToken();
		}
		return result;
	}

	public static List<String> readContentZipFile(ZipFile zipFile, ZipEntry e)
			throws IOException {
		List<String> result = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				zipFile.getInputStream(e)));
		in.readLine();// remove 2 first line
		in.readLine();//
		while (true) {
			String line = in.readLine();
			if (line == null)
				break;
			StringTokenizer tokens = new StringTokenizer(line, "\t");
			result.add(tokens.nextToken());
		}
		return result;
	}

	public static void main(String[] args) throws ZipException, IOException {
		// ZipFile zf = new ZipFile(new
		// File("trainedTopics_no_fr_and_reply.zip"));
		// try {
		// for (Enumeration<? extends ZipEntry> e = zf.entries(); e
		// .hasMoreElements();) {
		// ZipEntry ze = e.nextElement();
		// String name = ze.getName();
		// System.out.println(ze.isDirectory());
		// System.out.println(name);
		// }
		// } finally {
		// zf.close();
		// }
		// MyTreeNode top = new MyTreeNode("All");
		// loadTopicsFromDir("trainedTopics_no_fr_and_reply.zip", top);
		//
		// System.out.println(top.getChildCount());

	}
}
