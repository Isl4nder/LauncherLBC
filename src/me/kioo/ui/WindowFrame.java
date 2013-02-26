package me.kioo.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URLEncoder;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;

import me.kioo.Launcher;
import me.kioo.core.updater.Updater;
import me.kioo.core.launcher.LauncherApplet;
import me.kioo.util.Configuration;
import me.kioo.util.Util;

public class WindowFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int VERSION = 13;

	private Launcher launcher;
	private MainPanel mainPanel;

	/**
	 * Constructor
	 */
	public WindowFrame() {
		
        System.setProperty("minecraft.applet.WrapperClass", LauncherApplet.class.getCanonicalName());
        
        // configuration de la fenetre
        this.setTitle("LBC - Launcher");
		this.setBackground(Color.BLACK);
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(1280, 900));
		try {
			this.setIconImage(ImageIO.read(Launcher.class.getResource("/me/kioo/res/favicon.png")));						// chargement de l'icon
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("IOException, " + e.toString());
		}
		
		// ajout du main panel
		this.mainPanel = new MainPanel(this, new BorderLayout());
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		// et affichage
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);

		// action listener de la fermeture de la fenetre
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
		        new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(30000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("Forcing exit!");
						System.exit(0);
					}
				}).start();
				/*if (WindowFrame.this.launcher.getApplet() != null) {
					WindowFrame.this.launcher.getApplet().stop();
					WindowFrame.this.launcher.getApplet().destroy();
				}*/
		        Configuration.store();
				System.exit(0);
			}
		});
	}
	
	/**
	 * Login, official update, unofficial update, puis launcher
	 * @param userName
	 * @param password
	 */
	public void login(String userName, String password) {
		this.setMessage("Connexion en cours");
		try {
			// test de connexion
			String parameters = "user=" + URLEncoder.encode(userName, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + VERSION;
			String result = Util.excutePost("https://login.minecraft.net/", parameters);
			if (result == null) {
				setMessage("Impossible de se connecter au serveur d'identification");
				return;
			}
			if (!result.contains(":")) {
				if ((result.trim().equals("Bad request"))
				|| (result.trim().equals("Bad login"))
				|| (result.trim().equals("User not premium"))) {
					setMessage("Echec de connexion");
				} else {
					setMessage(result);
				}
				return;
			}
			String[] values = result.split(":");
			
			if ((Configuration.getProperty("launchAuto") != null) && (Configuration.getProperty("rememberMe").equals("true"))) {
				Util.writeUsername(this.mainPanel.loginPanel);
			}

			//launcher.customParameters.putAll(this.customParameters);
			/*Configuration config = Configuration.getInstance();
			config.set("latestVersion", values[0].trim());
			config.set("downloadTicket", values[1].trim());
			config.set("userName", values[2].trim());
			config.set("sessionId", values[3].trim());*/
			/*String latestVersion = values[0].trim();
			String downloadTicket = values[1].trim();*/
			String realUserName = values[2].trim();
			String sessionId = values[3].trim();
			
			//Loader loader = new Loader();
			// test de l'update official
			// test de l'update unofficial

			// lancement du launcher
			//this.launcher = new Launcher(this, loader, realUserName, sessionId);
			
			//NewsWebView.resetInstance();
			
			this.launcher = new Launcher(this, realUserName, sessionId);
			this.launcher.launch();

			this.mainPanel = null;

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Exception, " + e.toString());
			//setMessage(e.toString());
		}
	}

	/**
	 * Affiche l'erreur 
	 * @param error
	 */
	public void setMessage(String error) {
		//this.mainPanel.setMessage(error);
		//validate();
	}
	
   public static void main(String[] args) throws IOException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Exception, " + e.toString());
		}
		
		Updater updater = new  Updater();
		updater.update();
		updater.removeOldFile();
		WindowFrame windowFrame = new WindowFrame();
		
		/*if (args.length >= 3) {
			String ip = args[2];
			String port = "25565";
			if (ip.contains(":")) {
				String[] parts = ip.split(":");
				ip = parts[0];
				port = parts[1];
			}

			
		}
*/
		/*
			Configuration.setProperty("server", ip);
			Configuration.setProperty("port", port);


			Configuration.setProperty("stand-alone", "true");
			
		 * if (args.length >= 1) {
			windowFrame.mainPanel.loginPanel.txtfieldLogin.setText(args[0]);
			if (args.length >= 2) {
				windowFrame.mainPanel.loginPanel.txtfieldPassword.setText(args[1]);
				windowFrame.login(args[0], args[1]);
			}
		}*/
	}
}