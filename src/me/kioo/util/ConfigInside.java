package me.kioo.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;


/**
 * @author Kevin
 *
 */
public class ConfigInside{
	
	private Properties properties;
	private File file;

	/**
	 * Loader singleton de Configuration
	 * @return
	 */
	public static ConfigInside getInstance() {
		return ConfigInsideHolder.instance;
	}
	
	/**
	 * Holder
	 */
	private static class ConfigInsideHolder {
		private final static ConfigInside instance = new ConfigInside();
	}
	
	/**
	 * Constructeur inacessible
	 * @throws IOException 
	 */
	private ConfigInside() {
		try {
			file = Util.getConfigInsideFile();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
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
	
    /**
     * Retourne une propriété
     * @param key
     * @return
     */
	public static String getProperty(String key) {
		ConfigInside config = ConfigInside.getInstance();
		return config.properties.getProperty(key);
	}
	
	/**
	 * Positionne une propriété
	 * @param key
	 * @param value
	 */
	public static void setProperty(String key, String value) {
		ConfigInside config = ConfigInside.getInstance();
		config.properties.setProperty(key, value);
	}
}