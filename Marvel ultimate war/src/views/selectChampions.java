package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import model.world.Champion;

public class selectChampions extends JFrame {
	
	private JPanel panel;
	private JTextArea info;
	
	public selectChampions() {
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); // terminate program when click x on window
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); // set sze of frame to full screen
		this.setVisible(true); // make frame visible
		this.validate(); // refresh frame

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(600, this.getHeight()));
		panel.setLayout(new GridLayout(3, 5));
		this.add(panel, BorderLayout.CENTER);

		info = new JTextArea();
		info.setFont(info.getFont().deriveFont(15f));
		info.setPreferredSize(new Dimension(400, this.getHeight()));
		info.setEditable(false);

		this.add(info, BorderLayout.EAST);

		this.revalidate();
		this.repaint();
	}

	public JPanel getPanel() {
		return panel;
	}

	public JTextArea getInfo() {
		return info;
	}
	
}
