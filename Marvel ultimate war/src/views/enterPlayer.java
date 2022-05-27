package views;



import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class enterPlayer implements ActionListener{
	
	private JFrame frame;
	private JPanel panel;
	private JLabel welcome;
	private JLabel welcome2;
	private JLabel firstLabel;
	private JLabel secondLabel;
	private JTextField firstText;
	private JTextField secondText;
	
	private JButton button;
	private JLabel fail;
	private JLabel fail2;
	
	
	public enterPlayer() {
		
		//setBounds (x, y, width, height)
		panel = new JPanel();
		frame = new JFrame();
		frame.setBounds(500,250, 500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(panel);
		
		panel.setLayout(null);
		
		welcome = new JLabel("Welcome to marvel ultimate war");
		welcome.setBounds(10, 20, 200, 25);
		panel.add(welcome);
		
		welcome2 = new JLabel("please enter your names");
		welcome2.setBounds(10,40,200,25);
		panel.add(welcome2);
		
		firstLabel = new JLabel("First Player");
		firstLabel.setBounds(10, 70, 100, 25);
		panel.add(firstLabel);
		
		firstText = new JTextField(20);
		firstText.setBounds(100, 70, 165, 25);
		panel.add(firstText);
		
		secondLabel = new JLabel("Second Player");
		secondLabel.setBounds(10, 100, 100, 25);
		panel.add(secondLabel);
		
		secondText = new JTextField(20);
		secondText.setBounds(100, 100, 165, 25);
		panel.add(secondText);
		
		button = new JButton("Enter");
		button.setBounds(30, 150, 80, 25);
		button.setFocusable(false);
		button.addActionListener(this);
		panel.add(button);
		
		fail = new JLabel("");
		fail.setBounds(30, 180, 165, 25);
		panel.add(fail);
		
		fail2 = new JLabel("");
		fail2.setBounds(30, 195, 190, 25);
		panel.add(fail2);
		
		
		
		
		
		frame.setVisible(true);
		//frame.validate();
		
		
	}
	
	public static void main(String[] args) {
		new enterPlayer();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String f = firstText.getText();
		String s = secondText.getText();
		
		if(f.equals("") || s.equals("")) {
			fail.setText("the name field was blank");
			fail2.setText("please enter players names again");
		}
		else {
			frame.dispose();
			try {
				new Controller(f,s);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
