package me.kioo.ui;

import java.awt.BorderLayout;

import me.kioo.ui.component.TransparentPanel;
import me.kioo.util.Configuration;
import me.kioo.util.Util;

public class MainPanel extends TransparentPanel {

	private static final long serialVersionUID = 1L;

	private WindowFrame windowFrame;
	public LoginPanel loginPanel;
	//private JFXPanel newsPanel;

	public MainPanel(final WindowFrame windowFrame, BorderLayout borderLayout) {
		super(borderLayout);
		this.windowFrame = windowFrame;

		buildMainPanel();

	}
	
	private void buildMainPanel() {
		/*SwingUtilities.invokeLater(new Runnable() {		// ajout d'un composant javafx dans un container swing
			@Override																// doit être threadé différement
            public void run() {
            	newsPanel = NewsWebView.getInstance();
            	add(newsPanel, BorderLayout.CENTER);		// ajout les news
            }
        });*/
		this.loginPanel = new LoginPanel(this.windowFrame);		// le panel texturé avec le formulaire de login et le logo
		add(this.loginPanel, BorderLayout.SOUTH);

		// les login et mot de passe pour les afficher dans les txtField
		if ((Configuration.getProperty("rememberMe") != null)
		&& (Configuration.getProperty("rememberMe").equals("true"))) {
			Util.readUserName(this.loginPanel);
		}
	}
	
	/**
	 * Affiche le terme Connexion 
	 */
	public void setLoggingIn() {
		setMessage("Connexion à LbcTechno");
	}

	/**
	 * Affiche une erreur au dessus du formulaire de login, supprime pour tout réafficher
	 * @param errorMessage
	 */
	public void setMessage(String message) {
		removeAll();
		buildMainPanel();
		loginPanel.setMessage(message);
		validate();
	}
}