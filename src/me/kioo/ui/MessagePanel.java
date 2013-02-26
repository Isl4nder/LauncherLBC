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

import javax.swing.AbstractButton;
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

import me.kioo.GameUpdater;
import me.kioo.util.Configuration;
import me.kioo.util.Util;

public class MessagePanel extends JDialog {
	private static final long serialVersionUID = 1L;
	private JButton btnForceUpdate;
	
	protected JLabel lblMinimumValue = new JLabel("");
	protected JLabel lblMaximumValue = new JLabel("");
	private Configuration config;

	public MessagePanel(Frame parent) {
		super(parent);
		
		config = Configuration.getInstance();
		
		setTitle("Options");
		setModal(true);
		getContentPane().setLayout(new BorderLayout(0, 0));

		// panel général
		JPanel panelOption = new JPanel();
		panelOption.setLayout(new BorderLayout(0, 0));
		panelOption.setBorder(new EmptyBorder(16, 24, 24, 24));
		getContentPane().add(panelOption, BorderLayout.NORTH);
		
		// panel checkbox
		JPanel panelRememberMe = new JPanel();
		panelRememberMe.setBorder(new EmptyBorder(5, 0, 5, 0));
		panelRememberMe.setLayout(new BorderLayout(0, 0));
		JCheckBox chckbxRememberMe = new JCheckBox("Se souvenir de mes identifiants");
		chckbxRememberMe.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
		        boolean selected =  ((AbstractButton)actionEvent.getSource()).getModel().isSelected();
				Configuration.setProperty("rememberMe", selected ? "true" : "false");
			}
		});
		chckbxRememberMe.setHorizontalAlignment(SwingConstants.CENTER);
		chckbxRememberMe.setSelected((config.getProperty("rememberMe") == "true") ? true : false);
		panelRememberMe.add(chckbxRememberMe, BorderLayout.SOUTH);
		panelOption.add(panelRememberMe, BorderLayout.NORTH);
		
		// panel bouton force update
		JPanel panelForceUpdate = new JPanel();
		panelForceUpdate.setBorder(new EmptyBorder(5, 0, 5, 0));
		panelForceUpdate.setLayout(new BorderLayout(0, 0));
		btnForceUpdate = new JButton("Forcer la Mise � jour");
		btnForceUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				GameUpdater.forceUpdate = true;
				btnForceUpdate.setText("Mise � jour en cours");
				btnForceUpdate.setEnabled(false);
			}
		});
		panelForceUpdate.add(btnForceUpdate);
		panelOption.add(panelForceUpdate, BorderLayout.CENTER);
						
		JPanel panelDirLink = new JPanel();
		panelDirLink.setBorder(new EmptyBorder(5, 0, 5, 0));
		panelOption.add(panelDirLink, BorderLayout.SOUTH);
		
		JLabel lblDirLink = new JLabel(Util.getWorkingDirectory().toString());
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
		panelDirLink.setLayout(new BorderLayout(0, 0));
		lblDirLink.setForeground(Color.BLUE);
		lblDirLink.setHorizontalAlignment(SwingConstants.CENTER);
		panelDirLink.add(lblDirLink);
		
		JPanel panelMemory = new JPanel();
		panelMemory.setBorder(new EmptyBorder(16, 24, 24, 24));
		getContentPane().add(panelMemory, BorderLayout.CENTER);
		panelMemory.setLayout(new BorderLayout(0, 0));
		
		JLabel lblGestionMemoire = new JLabel("Gestion de la m\u00E9moire");
		panelMemory.add(lblGestionMemoire, BorderLayout.NORTH);
		lblGestionMemoire.setHorizontalAlignment(SwingConstants.CENTER);
		lblGestionMemoire.setVerticalAlignment(SwingConstants.TOP);
		
		JPanel panelSliderMemory = new JPanel();
		panelMemory.add(panelSliderMemory, BorderLayout.CENTER);
		panelSliderMemory.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblMinimum = new JLabel("Minimum");
		lblMinimum.setHorizontalAlignment(SwingConstants.LEFT);
		
		JSlider sliderMinimum = new JSlider();
		sliderMinimum.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				String strValue = String.valueOf(((JSlider)arg0.getSource()).getValue());
				lblMinimumValue.setText(strValue +" Mo");
				config.setProperty("minimumMemValue", strValue);
			}
		});
		sliderMinimum.setPaintTicks(true);
		sliderMinimum.setMinorTickSpacing(64);
		sliderMinimum.setValue(1024);
		sliderMinimum.setSnapToTicks(true);
		sliderMinimum.setMaximum(1024);
		sliderMinimum.setMinimum(512);
		
		lblMinimumValue.setHorizontalAlignment(SwingConstants.LEFT);
		
		panelSliderMemory.add(lblMinimum);
		panelSliderMemory.add(sliderMinimum);
		panelSliderMemory.add(lblMinimumValue);
		
		JPanel panelLabelMemory2 = new JPanel();
		panelMemory.add(panelLabelMemory2, BorderLayout.SOUTH);
		panelLabelMemory2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblMaximum = new JLabel("Maximum");
		lblMaximum.setHorizontalAlignment(SwingConstants.LEFT);
		
		JSlider sliderMaximum = new JSlider();
		sliderMaximum.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				String strValue = String.valueOf(((JSlider)arg0.getSource()).getValue());
				lblMaximumValue.setText(strValue +" Mo");
				config.setProperty("maximumMemValue", strValue);
			}
		});
		sliderMaximum.setPaintTicks(true);
		sliderMaximum.setMinorTickSpacing(512);
		sliderMaximum.setValue(2048);
		sliderMaximum.setSnapToTicks(true);
		sliderMaximum.setMaximum(8192);
		sliderMaximum.setMinimum(1024);
		
		lblMaximumValue.setHorizontalAlignment(SwingConstants.LEFT);
		
		panelLabelMemory2.add(lblMaximum);
		panelLabelMemory2.add(sliderMaximum);
		panelLabelMemory2.add(lblMaximumValue);
		
		// panel de boutons
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.RIGHT));

		// ajout le panelButton au panel principal
		getContentPane().add(panelButton, BorderLayout.SOUTH);

		// button Cancel
		JButton btnOk = new JButton("Valider");
		btnOk.setActionCommand("OK");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				MessagePanel.this.setVisible(false);
			}
		});
		panelButton.add(btnOk);

		pack();
		setLocationRelativeTo(parent);
	}
}