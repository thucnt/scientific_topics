package edu.uit.snmr.view.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class TextFileFilter extends FileFilter {
	private final String[] okFileExtensions = new String[] { "txt"};

	public boolean accept(File file) {
		for (String extension : okFileExtensions) {
			if (file.getName().toLowerCase().endsWith(extension) || file.isDirectory()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		return ".txt";
	}
}