package me.kioo.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

public class ProgressDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;

	public ProgressDialog(Frame parent) {
		super(parent);
		this.setTitle("Update");
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setPreferredSize(new Dimension(200, 80));
		
		this.progressBar = new JProgressBar();
		getContentPane().add(this.progressBar, BorderLayout.CENTER);
		
		this.pack();
		this.setLocationRelativeTo(parent);
	}
	
	
	public void setValue(Integer value ) {
		this.progressBar.setValue(value);
	}
	
	public void refresh() {
		this.progressBar.setStringPainted(true);
	}
}