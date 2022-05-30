package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class board extends JFrame {

	private cellsPanel CellsPanel;
	private JTextArea info;

	public board() {
		super();
		this.setTitle("Marvel Ultimate War");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); // terminate program when click x on window
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); // set sze of frame to full screen
		this.setVisible(true); // make frame visible
		this.validate(); // refresh frame

		CellsPanel = new cellsPanel();
		this.add(CellsPanel, BorderLayout.CENTER);

		info = new JTextArea();
		info.setPreferredSize(new Dimension(400, this.getHeight()));
		info.setEditable(false);
		info.setFont(info.getFont().deriveFont(15f));

		this.add(info, BorderLayout.EAST);

		this.revalidate();
		this.repaint();

	}
	
	

	public cellsPanel getCellsPanel() {
		return CellsPanel;
	}



	public JTextArea getInfo() {
		return info;
	}



	public static void main(String[] args) {
		new board();
	}
}
