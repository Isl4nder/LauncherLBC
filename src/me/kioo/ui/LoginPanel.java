package me.kioo.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import me.kioo.ui.component.LogoPanel;
//import me.kioo.ui.component.NewsWebView;
import me.kioo.ui.component.TexturedPanel;
import me.kioo.ui.component.TransparentButton;
import me.kioo.ui.component.TransparentLabel;
import me.kioo.ui.component.TransparentPanel;


public class LoginPanel extends TexturedPanel {
	private static final long serialVersionUID = 1L;
	private static final int PANEL_SIZE = 120;
	
	//private WindowFrame windowFrame;
	private TransparentLabel lblLogin = new TransparentLabel("Login:");
	private TransparentLabel lblPassword = new TransparentLabel("Mot de passe:");
	private JButton btnOption = new TransparentButton("Options");
	private JButton btnCredit = new TransparentButton("Crédits");
	
	public JButton btnExecute = new TransparentButton("Lancer");
	public JTextField txtfieldLogin = new JTextField(20);															// en public pour avoir acces dans les autres classes sans trop se faire chier
	public JPasswordField txtfieldPassword = new JPasswordField(20);									// en public pour avoir acces dans les autres classes sans trop se faire chier
	private WindowFrame windowFrame;
	
	public LoginPanel(final WindowFrame windowFrame) {
		super();
		this.windowFrame = windowFrame;
		
		// quelques propriétés
		this.setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
		this.setBorder(new EmptyBorder(0, 10, 0, 10));
		this.setLayout(new BorderLayout());
		
		// ajout du logo
		this.add(new LogoPanel(), BorderLayout.EAST);
		
		// ajout du login
		TransparentPanel tp = new TransparentPanel(new GridBagLayout());
		JPanel formLogin = buildFormLoginPanel();															
		GridBagConstraints gbc = new GridBagConstraints();
		tp.add(formLogin, gbc);
		add(tp, BorderLayout.WEST);
		
		// action listener pour les fields login et password ainsi que le button launchs (pour listen les touches entrée et le click)
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (WindowFrame.READY_TO_LAUNCH) {
					new Thread(new Runnable() {
						public void run() {
							LoginPanel.this.windowFrame.login(txtfieldLogin.getText(), new String(txtfieldPassword.getPassword()));
						}
					}).start();
				}
			}
		};
		this.txtfieldLogin.addActionListener(al);
		this.txtfieldPassword.addActionListener(al);
		this.btnExecute.addActionListener(al);
		// action listener pour le bouton option
		this.btnOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				new OptionPanel(windowFrame).setVisible(true);
			}
		});
		this.btnCredit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CreditPanel(windowFrame).setVisible(true);
			}
		});
		
		
	}
	
	/**
	 * @return JPanel
	 */
	private JPanel buildFormLoginPanel() {
		
		TransparentPanel panelForm = new TransparentPanel(new BorderLayout());
		
		TransparentPanel panelLogin = new TransparentPanel(new BorderLayout(5, 0));
		this.lblLogin.setText("Login");
		this.lblLogin.setVerticalAlignment(SwingConstants.BOTTOM);
		this.lblLogin.setHorizontalAlignment(SwingConstants.LEFT);
		panelLogin.add(this.lblLogin, BorderLayout.NORTH);
		panelLogin.add(this.txtfieldLogin, BorderLayout.SOUTH);
		panelForm.add(panelLogin, BorderLayout.WEST);

		TransparentPanel panelPassword = new TransparentPanel(new BorderLayout(5, 0));
		this.lblPassword.setText("Mot de passe");
		this.lblPassword.setVerticalAlignment(SwingConstants.BOTTOM);
		this.lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		panelPassword.add(this.lblPassword, BorderLayout.NORTH);
		panelPassword.add(this.txtfieldPassword, BorderLayout.SOUTH);
		panelForm.add(panelPassword, BorderLayout.CENTER);

		TransparentPanel panelButton = new TransparentPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelButton.add(this.btnExecute);
		panelButton.add(this.btnOption);
		panelButton.add(this.btnCredit);
		panelForm.add(panelButton, BorderLayout.EAST);
		
		return panelForm;
	}
	
	public void setMessage(String message) {
		//lblMessage.setText(message);
	}

}