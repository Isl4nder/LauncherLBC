package me.kioo.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Random;
import java.util.jar.Pack200;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.net.ssl.HttpsURLConnection;

import me.kioo.ui.LoginPanel;

public class Util {
	private static String workName = "lbcTechnoDev";
	private static final String STR_CONFIGURATION = "config.properties";
	private static final String STR_CONFIG_INSIDE = "/me/kioo/res/option.properties";
	private static File workingDirectory;
	private static File binDirectory;
	private static File configurationFile;
	private static File configInsideFile;
	private static Boolean lzmaSupported;
	private static Boolean pack200Supported;
	
	public static String[] listFileJar = {"lwjgl.jar", "jinput.jar", "lwjgl_util.jar", "minecraft.jar"};
	
	public static enum OS {
		linux, solaris, win, mac, unknown;
	}

		
	public static Boolean getLzmaSupported() {
		if (Util.lzmaSupported != null) {
			return Util.lzmaSupported;
		}
		try {
			Class.forName("LZMA.LzmaInputStream");
			Util.lzmaSupported = true;
			return Util.lzmaSupported;
		} catch (Throwable localThrowable) {
		}
		Util.lzmaSupported = false;
		return Util.lzmaSupported;
	}
	
	public static Boolean getPack200Supported() {
		if (Util.pack200Supported != null) {
			return Util.pack200Supported;
		}
		try {
			Pack200.class.getSimpleName();
			Util.pack200Supported = true;
			return Util.pack200Supported;
		} catch (Throwable localThrowable1) {
		}
		Util.pack200Supported = false;
		return Util.pack200Supported;
	}

	public static String trimExtensionByCapabilities(String strFile) {
		if (Util.getPack200Supported()) {
			strFile= strFile.replaceAll(".pack", "");
		}
		if (Util.getLzmaSupported()) {
			strFile = strFile.replaceAll(".lzma", "");
		}
		return strFile;
	}
	
	/**
	 * Retourne le système d'exploitation sous forme de chaine de caractères
	 * @return OS
	 */
	public static OS getPlatform() {
		String osName = System.getProperty("os.name").toLowerCase();
	     if (osName.contains("win"))			return OS.win;
	     if (osName.contains("mac"))			return OS.mac;
	     if (osName.contains("solaris"))		return OS.solaris;
	     if (osName.contains("sunos"))		return OS.solaris;
	     if (osName.contains("linux"))		return OS.linux;
	     if (osName.contains("unix"))			return OS.linux;
	     return OS.unknown;
	}
	
	public static File getConfigurationFile() {
		if (Util.configurationFile != null) {
			return Util.configurationFile;
		}
		File configurationFile = new File(Util.getWorkingDirectory(), Util.STR_CONFIGURATION);
		if(!configurationFile.exists()) {
			try {
				configurationFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		Util.configurationFile = configurationFile;
		return configurationFile;
	}
	
	public static File getConfigInsideFile() throws URISyntaxException {
		if (Util.configInsideFile != null) {
			return Util.configInsideFile;
		}
		URL url = Util.class.getResource(Util.STR_CONFIG_INSIDE);
		File configInsideFile = new File(url.toURI());
		if(!configInsideFile.exists()) {
			throw new RuntimeException("Le répertoire binaire n'a pas pu être créé: " + binDirectory);
		}
		Util.configurationFile = configInsideFile;
		return configInsideFile;
	}
	
	/**
	 * Retourne le répertoire binaire
	 * @return File
	 */
	public static File getBinDirectory() {
		if (Util.binDirectory != null) {
			return Util.binDirectory;
		}
		
		File binDirectory = new File(Util.getWorkingDirectory() + File.separator + "bin" + File.separator);
		if ((!binDirectory.exists())
		&& (!binDirectory.mkdirs())) {
			throw new RuntimeException("Le répertoire binaire n'a pas pu être créé: " + binDirectory);
		}
		
		Util.binDirectory= binDirectory;
		return binDirectory;
	}
	
	/**
	 * Retourne le répertoire de travail
	 * @return File
	 */
	public static File getWorkingDirectory() {
		if (Util.workingDirectory != null) {
			return Util.workingDirectory;
		}
		
		String userHome = System.getProperty("user.home", ".");			// retourne le chemin utilisateur ou . si le chemin utilisateur n'existe pas
		File workingDirectory;
		OS os = Util.getPlatform();
		if (os == OS.solaris || os == OS.linux) {
			workingDirectory = new File(userHome, '.'+Util.workName+File.separator);
		} else if (os == OS.win) {
			String applicationData = System.getenv("APPDATA");
			if (applicationData != null)
				workingDirectory = new File(applicationData, "."+Util.workName+File.separator);
			else
				workingDirectory = new File(userHome, '.'+Util.workName+File.separator);
		} else if (os == OS.mac) {
			workingDirectory = new File(userHome, "Library"+File.separator+"Application Support"+File.separator+Util.workName+File.separator);
		} else {
			workingDirectory = new File(userHome, Util.workName+File.separator);
		}

		// création du répertoire si besoin
		if ((!workingDirectory.exists())
		&& (!workingDirectory.mkdirs())) {
			throw new RuntimeException("Le répertoire de travail n'a pas pu être créé: " + workingDirectory);
		}

		Util.workingDirectory = workingDirectory;
		return workingDirectory;
	}
	
	/**
	 * Lire un fichier
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String readVersionFile(File file) throws Exception {
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		String str = dis.readUTF();
		dis.close();
		return str;
	}
	
	
	/**
	 * Ecrire un fichier
	 * @param file
	 * @param version
	 * @throws Exception
	 */
	public static void writeVersionFile(File file, String str) throws Exception {
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
		dos.writeUTF(str);
		dos.close();
	}
	
	
	/**
	 * Fonction d'encryptage
	 * @param mode
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static Cipher getCipher(int mode, String password) throws Exception {
		Random random = new Random(43287234L);
		byte[] salt = new byte[8];
		random.nextBytes(salt);
		PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 5);
		SecretKey pbeKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(password.toCharArray()));
		Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
		cipher.init(mode, pbeKey, pbeParamSpec);
		return cipher;
	}

	public static String excutePost(String targetURL, String urlParameters) {
		HttpsURLConnection connection = null;
		try {
			URL url = new URL(targetURL);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			connection.connect();
			Certificate[] certs = connection.getServerCertificates();

			byte[] bytes = new byte[294];
			DataInputStream dis = new DataInputStream(Util.class.getResourceAsStream("/me/kioo/res/minecraft.key"));
			dis.readFully(bytes);
			dis.close();

			Certificate c = certs[0];
			PublicKey pk = c.getPublicKey();
			byte[] data = pk.getEncoded();

			for (int i = 0; i < data.length; i++) {
				if (data[i] == bytes[i])
					continue;
				throw new RuntimeException("Les clées publics sont différentes");
			}

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			StringBuffer response = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();

			String str = response.toString();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	public static boolean isEmpty(String str) {
		return (str == null) || (str.length() == 0);
	}

	/**
	 * Ouvre un lien
	 * @param uri
	 */
	public static void openLink(URI uri) {
		try {
			Object o = Class.forName("java.awt.Desktop").getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
			o.getClass().getMethod("browse", new Class[] {
				URI.class
			}).invoke(o, new Object[] {
				uri
			});
		} catch (Throwable e) {
			System.out.println("Erreur à l'ouverture du lien " + uri.toString());
		}
	}
	
	/**
	 * Lit les login et mot de passe du fichier lastlogin
	 */
	public static void readUserName(LoginPanel loginPanel) {
		try {
			File lastLogin = new File(Util.getWorkingDirectory(), "lastlogin");
			if (!lastLogin.exists()) {
				return;
			}
			Cipher cipher = Util.getCipher(2, "passwordfile");
			DataInputStream dis;
			if (cipher != null)
				dis = new DataInputStream(new CipherInputStream(new FileInputStream(lastLogin), cipher));
			else {
				dis = new DataInputStream(new FileInputStream(lastLogin));
			}
			loginPanel.txtfieldLogin.setText(dis.readUTF());
			loginPanel.txtfieldPassword.setText(dis.readUTF());
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ecrit les login et mot de passe dans le fichier lastlogin
	 * @param password2 
	 * @param userName 
	 */
	public static void writeUsername(LoginPanel loginPanel) {
		try {
			File lastLogin = new File(Util.getWorkingDirectory(), "lastlogin");

			Cipher cipher = Util.getCipher(1, "passwordfile");
			DataOutputStream dos;
			if (cipher != null)
				dos = new DataOutputStream(new CipherOutputStream(new FileOutputStream(lastLogin), cipher));
			else {
				dos = new DataOutputStream(new FileOutputStream(lastLogin));
			}
			dos.writeUTF(loginPanel.txtfieldLogin.getText());
			dos.writeUTF(new String(loginPanel.txtfieldPassword.getPassword()));
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}