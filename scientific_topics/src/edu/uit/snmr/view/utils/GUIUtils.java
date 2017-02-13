package edu.uit.snmr.view.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.tree.MutableTreeNode;

import model.ATopic;
import model.ListATopic;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.uit.snmr.confs.DatasetPropertiesConfig;

public class GUIUtils {

        public static String getProperty(String key) {
		return get(key);
	}
    
        public static String get(String key) {
		Locale locale = Locale.getDefault();
		ResourceBundle rb = ResourceBundle.getBundle("i18n", locale);
		return rb.getString(key);
	}


	public static List<ATopic> loadTopicsFromDir(String dirName) {
		List<ATopic> topics = new ArrayList<ATopic>();
		File file = new File(dirName);
		if (file.exists()) {
			File[] files = file.listFiles();
			for (File fi : files) {
				if (fi.isFile()) {
					ATopic t = new ATopic(null, fi.getName());
					topics.add(t);
				} else {
					ATopic parent = new ATopic(null, fi.getName());
					topics.add(parent);
					List<ATopic> sub = loadTopicsFromDir(fi.getPath(), parent);
					topics.addAll(sub);
				}
			}
		}
		return topics;
	}

	private static List<ATopic> loadTopicsFromDir(String dirName, ATopic parent) {
		List<ATopic> result = new ArrayList<ATopic>();
		File file = new File(dirName);
		File[] files = file.listFiles();
		System.out.println("called.");
		for (File fi : files) {
			if (fi.isFile()) {
				ATopic t = new ATopic(parent, fi.getName());
				result.add(t);
			} else {
				ATopic tmp = new ATopic(parent, fi.getName());
				List<ATopic> sub = loadTopicsFromDir(fi.getPath(), tmp);
				result.addAll(sub);
			}
		}
		return result;
	}

	public static MyTreeNode loadTopicsFromDir(String dirName, MyTreeNode top) {
		if (top == null)
			top = new MyTreeNode("All");
		File file = new File(dirName);
		if (file.exists()) {
			File[] files = file.listFiles();
			for (File fi : files) {
				if (fi.isFile()) {
					MyTreeNode child = new MyTreeNode(fi.getName().substring(0, fi.getName().indexOf('.')));
					child.setWordSet(readContentFromFile(fi));
					top.add(child);
				} else {
					MyTreeNode child = new MyTreeNode(fi.getName());
					loadTopicsFromDir(fi.getPath(), child);
					top.add(child);
				}
			}
		}
		return top;
	}

	public static List<ATopic> nodeToList(MyTreeNode top) {
		List<ATopic> result = new ArrayList<>();
		for (Enumeration<MyTreeNode> children = top.children(); children
				.hasMoreElements();) {
			MyTreeNode node = children.nextElement();

			if (node.getChildCount() == 0) {
				ATopic t = new ATopic(null, node.getName());
				t.setWordSet(node.getWordSet());
				result.add(t);
			} else {
				ATopic pa = new ATopic(null, node.getName());
				pa.setWordSet(node.getWordSet());
				result.add(pa);
				List<ATopic> re = nodeToList(node, pa);
				result.addAll(re);
			}
		}

		return result;
	}

	public static List<ATopic> nodeToList(MyTreeNode top, ATopic pa) {
		List<ATopic> result = new ArrayList<>();
		for (Enumeration<MyTreeNode> children = top.children(); children
				.hasMoreElements();) {
			MyTreeNode node = children.nextElement();

			if (node.getChildCount() == 0) {
				ATopic t = new ATopic(pa, node.getName());
				t.setWordSet(node.getWordSet());
				result.add(t);
			} else {
				ATopic p = new ATopic(pa, node.getName());
				p.setWordSet(node.getWordSet());
				List<ATopic> re = nodeToList(node, p);
				result.add(p);
			}
		}
		return result;
	}

	public static List<String> readContentFromFile(File file) {
		BufferedReader in = null;
		List<String> result = new ArrayList<String>();
		try {
			in = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			in.readLine();// remove 2 first line
			in.readLine();//
			while (true) {
				String line = in.readLine();
				if (line == null)
					break;
				StringTokenizer tokens = new StringTokenizer(line, "\t");
				result.add(tokens.nextToken());
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public static MyTreeNode removeUncheckedRow(MyTreeNode top) {
		MyTreeNode tmp = top;
		for (Enumeration<MyTreeNode> children = tmp.children(); children
				.hasMoreElements();) {
			MyTreeNode node = children.nextElement();
			if (!node.getData1() && node.isLeaf()) {
				tmp.remove(node);
			}
		}
		return tmp;
	}

	// This export method is not used.
	public static boolean exportXML(MyTreeNode top) {

		try {
			XStream xStream = new XStream(new DomDriver());
			xStream.alias("Topic", MyTreeNode.class);
			xStream.omitField(javax.swing.tree.DefaultMutableTreeNode.class,
					"allowsChildren");
			xStream.omitField(javax.swing.tree.DefaultMutableTreeNode.class,
					"parent");
			xStream.toXML(top, new FileOutputStream("tree.xml"));
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}

	public static void toStringListTopics(List<ATopic> topics) {
		int i = 1;
		for (ATopic t : topics)
			System.out.println(i++ + " " + t.getLabel());
	}

	public static boolean exportTopicsToXML(MyTreeNode top) {
		try {
			ListATopic topics = new ListATopic();

			topics.addAll(nodeToList(top));
			XStream xstream = new XStream(new DomDriver());
			xstream.alias("topic", ATopic.class);
			xstream.alias("topics", ListATopic.class);
			xstream.addImplicitCollection(ListATopic.class, "topics");
			xstream.setMode(XStream.ID_REFERENCES);
			xstream.toXML(topics, new FileOutputStream("tree.xml"));
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}
	
	public static List<ATopic> loadTopicFromXml(String xmlFilePath) {
		File file = new File (xmlFilePath);
		XStream xstream = new XStream(new DomDriver());	
		//xstream.alias("Topic", ATopic.class);
		xstream.alias("Topic", MyTreeNode.class);
		xstream.omitField(javax.swing.tree.DefaultMutableTreeNode.class,
				"allowsChildren");
		xstream.omitField(javax.swing.tree.DefaultMutableTreeNode.class,
				"parent");
		MyTreeNode obj = (MyTreeNode)xstream.fromXML(file);
		return nodeToList(obj);
	}

	

	public static int countTopics(List<ATopic> topics) {
		int result = 0;
		for (ATopic t : topics) {
			if (t.getParent() == null)
				result++;
			else {
				while (t.getParent() != null) {
					result++;
					t = t.getParent();
				}
			}
		}
		return result;
	}

	// ///////////////////// ok

	public static MyTreeNode loadTopicsFromZip(String zipFile, MyTreeNode top)
			throws ZipException, IOException {
		ZipFile zf = new ZipFile(new File(zipFile));
		Enumeration<? extends ZipEntry> entries = zf.entries();
		if (entries.hasMoreElements()) {
			entries.nextElement();// omit first entry
		}
		while (entries.hasMoreElements()) {
			ZipEntry e = entries.nextElement();
			if (!e.isDirectory()) {
				MyTreeNode child = new MyTreeNode(fileName(e.getName()));
				child.setWordSet(readContentZipFile(zf, e));
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
						ch.setWordSet(readContentZipFile(zf, e));
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
	//	from MyTreeNode to MutableTreeNode
	public static MutableTreeNode convertToTreeNode(MyTreeNode top) {
		MyMutableTreeNode root = new MyMutableTreeNode(top.getName()); 
		root.setWordset(top.getWordSet());
		for (Enumeration<MyTreeNode> children = top.children(); children
				.hasMoreElements();) {
			MyTreeNode node = children.nextElement();

			if (node.getChildCount() == 0) {
				MyMutableTreeNode tmp = new MyMutableTreeNode(node.getName());
				tmp.setWordset(node.getWordSet());
				root.add(tmp);
			} else {
				root.add(convertToTreeNode(node));
				//				System.out.println(node.getName());
			}
		}
		return root;
	}

	public static void main(String[] args) throws IOException {
//		MyTreeNode top = new MyTreeNode("All");
//		loadTopicsFromDir("trainedTopics_no_fr_and_reply", top);
//		//		loadTopicsFromZip("trainedTopics_no_fr_and_reply.zip", top);
//		//
//		List<ATopic> list = nodeToList(top);
//		System.out.println(list.size());
//		System.out.println(top.getChildCount());
//
//		exportTopicsToXML(top);
		loadTopicFromXml("tree.xml");
	}
}
