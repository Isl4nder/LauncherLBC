package me.kioo;

import java.applet.Applet;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import me.kioo.core.launcher.LauncherApplet;
import me.kioo.core.launcher.Wrapper;
import me.kioo.ui.WindowFrame;
import me.kioo.util.Util;

public class Launcher implements Runnable {
	
	private LauncherApplet applet;
	private URLClassLoader classLoader;
	//private Loader loader;
	private String userName;
	private String sessionId;
	private WindowFrame windowFrame;
	
	//public Map<String, String> customParameters = new HashMap<String, String>();
	
	public Launcher(WindowFrame windowFrame, /*final Loader loader, */ String userName, String sessionId) {
		//this.loader = loader;
		this.userName = userName;
		this.sessionId = sessionId;
		this.windowFrame = windowFrame;
	}
	
	 public void launch() {
			
		// récupération du répertoire binaire
		File dir = Util.getBinDirectory();

		// récupération de la liste des fichiers jar 
		String[] listFileJar = Util.listFileJar;

		// on met ca dans une liste
		List<URL> urls = new ArrayList<URL>();
		for (int i = 0; i < listFileJar.length; i++) {
			try {
				URL url = new File(dir , listFileJar[i]).toURI().toURL();
				System.out.println("Chargement de URL: " + url.toString());
				urls.add(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				System.err.println("MalformedURLException, " + e.toString());
			}
		}
		
		String dirNative = new File(dir, "natives").toString(); 
		System.out.println("Loading natives..." + dirNative);
		System.setProperty("org.lwjgl.librarypath", dirNative);
		System.setProperty("net.java.games.input.librarypath", dirNative);
		
        // Start Minecraft
        this.classLoader = new URLClassLoader(urls.toArray(new URL[0]));
        this.applet = new LauncherApplet(this.userName, this.sessionId);
        
        this.applet.init();

        // mise en forme de la fenetre principale
		this.windowFrame.getContentPane().removeAll();
        this.windowFrame.getContentPane().add(applet, "Center");
        this.windowFrame.setTitle("LBC Techno");
        this.windowFrame.validate();
        
        this.applet.start();
        
        final Thread t = new Thread(this);
        t.start();
    }

	@Override
	public void run() {
		try {
			this.applet.replace(Wrapper.wrap(this));
		} 
		catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the applet
	 */
	public Applet getApplet() {
		return applet;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @return the classLoader
	 */
	public URLClassLoader getClassLoader() {
		return classLoader;
	}
}