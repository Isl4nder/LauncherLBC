package me.kioo;

import java.util.ArrayList;

import me.kioo.ui.OptionPanel;
import me.kioo.ui.WindowFrame;
import me.kioo.util.Configuration;

public class Starter {

	public static void main(String[] args) throws Exception {
		try {
			String pathToJar = Starter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			
			String strMaxMem = (Configuration.getProperty("maximumMemValue") != null) ? String.valueOf(Configuration.getProperty("maximumMemValue")) : String.valueOf(OptionPanel.MAXIMUM_MEM_VALUE_DEFAULT);
			String strMinMem = (Configuration.getProperty("minimumMemValue") != null) ? String.valueOf(Configuration.getProperty("minimumMemValue")) : String.valueOf(OptionPanel.MINIMUM_MEM_VALUE_DEFAULT);
			
			ArrayList<String> params = new ArrayList<String>();
			params.add("java");
			params.add("-Xms"+strMinMem+"m");
			params.add("-Xmx"+strMaxMem+"m");
			params.add("-classpath");
			params.add(pathToJar);
			params.add("me.kioo.ui.WindowFrame");
			ProcessBuilder pb = new ProcessBuilder(params);
			Process process = pb.start();
			if (process == null) {
				throw new Exception("!");
			}
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			WindowFrame.main(args);
		}
	}
}