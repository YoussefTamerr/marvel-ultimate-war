package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.awt.Color;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class cellsPanel extends JPanel {

	public cellsPanel() {
		this.setPreferredSize(new Dimension(600, this.getHeight()));
		this.setLayout(new GridLayout(5,5));
		for(int i = 0; i<5; i++) {
			for(int j = 0; j<5; j++) {
				JButton b = new JButton();
				
				try {
					Image img = ImageIO.read(getClass().getClassLoader().getResource(
							"views/acids/vegeta.png"));
					b.setIcon((new ImageIcon(img)));
					b.setFocusable(false); // removes the border around the image
				} catch (IOException ex) {
				}
				
				b.setText("hi");
				b.setVerticalTextPosition(AbstractButton.TOP);
				//b.setBackground(Color.WHITE);
				this.add(b);
			}
		}
	}
	
}
