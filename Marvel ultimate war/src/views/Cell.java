package views;

import javax.swing.JButton;

public class Cell extends JButton {
	
	public Cell() {
		super();
		//this.setText("ana fady");
		//this.setBackground(Color.BLACK); 
		//this.addActionListener(this);
	}
	
	public void setChamp(String name) {
		this.setText(name);
	}

}
