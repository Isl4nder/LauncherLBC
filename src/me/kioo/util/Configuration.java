package me.kioo.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


/**
 * @author Kevin
 *
 */
public class Configuration{
	
	private Properties properties;
	private File file;

	/**
	 * Loader singleton de Configuration
	 * @return
	 */
	public static Configuration getInstance() {
		return ConfigurationHolder.instance;
	}
	
	/**
	 * Holder
	 */
	private static class ConfigurationHolder {
		private final static Configuration instance = new Configuration();
	}
	
	/**
	 * Constructeur inacessible
	 * @throws IOException 
	 */
	private Configuration() {
		file = Util.getConfigurationFile();
		properties = new Properties();
		properties.clear();
		load();
	}
	
    public void load() {
        if(file == null ) {
            throw new IllegalArgumentException("File is not set");
        }
        try {
        	properties.load(new BufferedInputStream((InputStream) new FileInputStream(file)));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
    public static void store() {
    	Configuration config = Configuration.getInstance();
    	if(config.file == null ) {
            throw new IllegalArgumentException("File is not set");
        }
    	try {
			config.properties.store(new BufferedOutputStream((OutputStream) new FileOutputStream(config.file)), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public static String getProperty(String key) {
		Configuration config = Configuration.getInstance();
		return config.properties.getProperty(key);
	}
	
	public static void setProperty(String key, String value) {
		Configuration config = Configuration.getInstance();
		config.properties.setProperty(key, value);
	}
}