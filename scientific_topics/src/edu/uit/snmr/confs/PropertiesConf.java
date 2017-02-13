/**
 * 
 */
package edu.uit.snmr.confs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Properties;

/**
 * @author muonnv
 *
 */
public abstract class PropertiesConf extends Properties {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5754145281535910357L;

	private String propName;

	protected PropertiesConf(String propName)  {
		this.propName = propName;
		FileInputStream in = null;
		try {
			in = new FileInputStream(new File(propName));
			this.load(in);
		} catch (IOException ex) {
			System.out.println("ERROR: " + ex.getMessage());
		} finally {
			try {
				if (in != null) { in.close(); }
			} catch (IOException e) {
				System.out.println("ERROR: " + e.getMessage());
			}
		}
	}

	public int getInt(String key) {
		return Integer.parseInt(getProperty(key));
	}

	public double getDouble(String key) {
		return Double.parseDouble(getProperty(key));
	}

	public String getString(String key) {
		return getProperty(key);
	}	
	
	
	public void store() throws IOException {
		String comments = "Updated the configuration at " + new Date();
		Writer writer = new FileWriter(propName);
		super.store(writer, comments);
		
	}
}
