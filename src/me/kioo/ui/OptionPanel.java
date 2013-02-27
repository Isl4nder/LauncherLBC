package me.kioo.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import me.kioo.util.ConfigInside;
import me.kioo.util.Configuration;
import me.kioo.util.Util;

public class OptionPanel extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public static final int MINIMUM_MEM_VALUE_DEFAULT = 512;
	public static final int MAXIMUM_MEM_VALUE_DEFAULT = 1024;
	public static final int MAXIMUM_MEM_MAX = ((System.getProperty("sun.arch.data.model").equals("32")) ? Integer.parseInt(ConfigInside.getProperty("max_mem_32")) : Integer.parseInt(ConfigInside.getProperty("max_mem_64")));
			
	private JCheckBox chckbxRememberMe;
	private JCheckBox checkbxLaunchAuto;
	private JSlider sliderMinimum;
	private JLabel lblMinimumValue = new JLabel("");
	private JSlider sliderMaximum;
	private JLabel lblMaximumValue = new JLabel("");
	private JButton btnForceUpdate;

	public OptionPanel(Frame parent) {
		super(parent);
		
		this.setTitle("Options");
		this.setModal(true);
		this.getContentPane().setLayout(new BorderLayout());

		////////// panel général ////////////
		JPanel panelOption = new JPanel();
		this.getContentPane().add(panelOption, BorderLayout.NORTH);
		
		panelOption.setLayout(new BorderLayout(0, 0));
		panelOption.setBorder(new EmptyBorder(16, 24, 24, 24));
		
		////////// panel checkbox ////////////
		JPanel panelCheckbox = new JPanel();
		panelOption.add(panelCheckbox, BorderLayout.NORTH);
		
		panelCheckbox.setBorder(new EmptyBorder(5, 0, 5, 0));
		panelCheckbox.setLayout(new BorderLayout(0, 0));
		
		checkbxLaunchAuto = new JCheckBox("Lancer automatiquement le serveur");
		panelCheckbox.add(checkbxLaunchAuto, BorderLayout.NORTH);
		checkbxLaunchAuto.setHorizontalAlignment(SwingConstants.CENTER);
		checkbxLaunchAuto.setSelected((Configuration.getProperty("launchAuto") != null) && (Configuration.getProperty("launchAuto").equals("true")));
		
		chckbxRememberMe = new JCheckBox("Se souvenir de mes identifiants");
		panelCheckbox.add(chckbxRememberMe, BorderLayout.SOUTH);
		chckbxRememberMe.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxRememberMe.setSelected((Configuration.getProperty("launchAuto") != null) && (Configuration.getProperty("rememberMe").equals("true")));
		
		////////// panel force update ////////////
		JPanel panelForceUpdate = new JPanel();
		panelOption.add(panelForceUpdate, BorderLayout.CENTER);
		
		panelForceUpdate.setBorder(new EmptyBorder(5, 0, 5, 0));
		panelForceUpdate.setLayout(new BorderLayout(0, 0));
		
		this.btnForceUpdate = new JButton("Forcer la Mise à jour");
		panelForceUpdate.add(this.btnForceUpdate);
		
		this.btnForceUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			/*public void actionPerformed(ActionEvent ae) {
				GameUpdater.forceUpdate = true;
				OptionPanel.this.btnForceUpdate.setText("Mise à jour en cours");
				OptionPanel.this.btnForceUpdate.setEnabled(false);
			}*/
		});
		
		////////// panel lien ////////////
		JPanel panelDirLink = new JPanel();
		panelOption.add(panelDirLink, BorderLayout.SOUTH);
		
		panelDirLink.setBorder(new EmptyBorder(5, 0, 5, 0));
		panelDirLink.setLayout(new BorderLayout(0, 0));
		
		JLabel lblDirLink = new JLabel(Util.getWorkingDirectory().toString());
		panelDirLink.add(lblDirLink);
		
		lblDirLink.setHorizontalAlignment(SwingConstants.CENTER);
		lblDirLink.setForeground(Color.BLUE);
		lblDirLink.setCursor(Cursor.getPredefinedCursor(12));
		lblDirLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				try {
					Util.openLink(new URL("file://"+ Util.getWorkingDirectory().getAbsolutePath()).toURI());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		////////// panel memory ////////////
		JPanel panelMemory = new JPanel();
		this.getContentPane().add(panelMemory, BorderLayout.CENTER);
		
		panelMemory.setBorder(new EmptyBorder(16, 24, 24, 24));
		panelMemory.setLayout(new BorderLayout(0, 0));
		
		JLabel lblGestionMemoire = new JLabel("Gestion de la m\u00E9moire (redémarrage requis)");
		panelMemory.add(lblGestionMemoire, BorderLayout.NORTH);
		lblGestionMemoire.setHorizontalAlignment(SwingConstants.CENTER);
		lblGestionMemoire.setVerticalAlignment(SwingConstants.TOP);
		
		JPanel panelSliderMemory = new JPanel();
		panelMemory.add(panelSliderMemory, BorderLayout.CENTER);
		panelSliderMemory.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblMinimum = new JLabel("Minimum");
		lblMinimum.setHorizontalAlignment(SwingConstants.LEFT);
		
		sliderMinimum = new JSlider();
		sliderMinimum.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				String str = String.valueOf(((JSlider)arg0.getSource()).getValue());
				OptionPanel.this.lblMinimumValue.setText(str +" Mo");
			}
		});
		sliderMinimum.setPaintTicks(true);
		sliderMinimum.setMinorTickSpacing(64);
		sliderMinimum.setSnapToTicks(true);
		sliderMinimum.setMaximum(1024);
		sliderMinimum.setMinimum(512);
		sliderMinimum.setValue((Configuration.getProperty("minimumMemValue") != null) ? Integer.parseInt(Configuration.getProperty("minimumMemValue")) : OptionPanel.MINIMUM_MEM_VALUE_DEFAULT );
		
		this.lblMinimumValue.setHorizontalAlignment(SwingConstants.LEFT);
		
		panelSliderMemory.add(lblMinimum);
		panelSliderMemory.add(sliderMinimum);
		panelSliderMemory.add(this.lblMinimumValue);
		
		JPanel panelLabelMemory2 = new JPanel();
		panelMemory.add(panelLabelMemory2, BorderLayout.SOUTH);
		panelLabelMemory2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblMaximum = new JLabel("Maximum");
		lblMaximum.setHorizontalAlignment(SwingConstants.LEFT);
		
		sliderMaximum = new JSlider();
		sliderMaximum.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				String str = String.valueOf(((JSlider)arg0.getSource()).getValue());
				OptionPanel.this.lblMaximumValue.setText(str +" Mo");
			}
		});
		sliderMaximum.setPaintTicks(true);
		sliderMaximum.setMinorTickSpacing(512);
		sliderMaximum.setSnapToTicks(true);
		sliderMaximum.setMaximum(OptionPanel.MAXIMUM_MEM_MAX);
		sliderMaximum.setMinimum(1024);
		sliderMaximum.setValue((Configuration.getProperty("maximumMemValue") != null) ? Integer.parseInt(Configuration.getProperty("maximumMemValue")) : OptionPanel.MAXIMUM_MEM_VALUE_DEFAULT );
		
		this.lblMaximumValue.setHorizontalAlignment(SwingConstants.LEFT);
		
		panelLabelMemory2.add(lblMaximum);
		panelLabelMemory2.add(sliderMaximum);
		panelLabelMemory2.add(this.lblMaximumValue);
		
		// panel de boutons
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JButton btnOk = new JButton("Valider");
		btnOk.setActionCommand("OK");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				OptionPanel.this.setVisible(false);
				boolean selectedRememberMe =  OptionPanel.this.chckbxRememberMe.getModel().isSelected();
				Configuration.setProperty("rememberMe", selectedRememberMe ? "true" : "false");
				boolean selectedLaunchAuto =  OptionPanel.this.checkbxLaunchAuto.getModel().isSelected();
				Configuration.setProperty("launchAuto", selectedLaunchAuto ? "true" : "false");
				Configuration.setProperty("maximumMemValue", String.valueOf(OptionPanel.this.sliderMaximum.getValue()));
				Configuration.setProperty("minimumMemValue", String.valueOf(OptionPanel.this.sliderMinimum.getValue()));
				Configuration.store();
			}
		});
		panelButton.add(btnOk);
		// ajout le panelButton au panel principal
		this.getContentPane().add(panelButton, BorderLayout.SOUTH);

		this.pack();
		this.setLocationRelativeTo(parent);
	}
}