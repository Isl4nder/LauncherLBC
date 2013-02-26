package me.kioo.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class CreditPanel extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public CreditPanel(Frame parent) {
		super(parent);
		
		//Configuration config = Configuration.getInstance();
		this.setTitle("Crédits");
		this.setModal(true);
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JEditorPane creditEditorPane = new JEditorPane();
		creditEditorPane.setBackground(SystemColor.control);
		creditEditorPane.setEditable(false);
	    try {
	    	URL fileURL = new File("C:"+File.separator+"Users"+File.separator+"Kevin"+File.separator+"Desktop"+File.separator+"text2.html").toURI().toURL(); // Transform path into URL
	    	creditEditorPane.setPage(fileURL); // Load the file to the editor
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	
		JScrollPane creditScrollPane = new JScrollPane(
			creditEditorPane,
			ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		creditScrollPane.setPreferredSize(new Dimension(400, 400));
		
		this.getContentPane().add(creditScrollPane, BorderLayout.NORTH);
		
		JPanel panelButton = new JPanel();
		panelButton.setBackground(SystemColor.control);
		getContentPane().add(panelButton, BorderLayout.SOUTH);
		panelButton.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JButton btnOk = new JButton("OK");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				CreditPanel.this.setVisible(false);
			}
		});
		btnOk.setActionCommand("OK");
		panelButton.add(btnOk);
		
		
		this.pack();
		this.setLocationRelativeTo(parent);
	}
}