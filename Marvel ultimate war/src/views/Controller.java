package views;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

import engine.Game;
import engine.Player;
import model.world.Champion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Controller implements ActionListener, MouseListener{
	private Player first;
	private Player second;
	private Game game;
	private board view;
	private selectChampions sc;
	
	private ArrayList<JButton> buttons;
	private int buttonCounter = 0;
	
	public Controller(String f, String s) throws IOException {
		first = new Player(f);
		second = new Player(s);
		game = new Game(first, second); 
		sc= new selectChampions();
		buttons = new ArrayList<JButton>();
		for(int i = 0; i < game.getAvailableChampions().size(); i++) {
			JButton btn = new JButton(game.getAvailableChampions().get(i).getName());
			//btn = new JButton(game.getAvailableChampions().get(i).getName());
			btn.setFocusable(false);
			btn.addMouseListener(this);
			btn.addActionListener(this);
			buttons.add(btn);
			sc.getPanel().add(btn);
			
			sc.getInfo().setText("select your champions\nthe first champion you select will be the leader of the team\n");
			
			sc.revalidate();
			sc.repaint();
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.buttonCounter++;
		JButton b = (JButton) e.getSource();
		// String x = b.getText();
		int x = buttons.indexOf(b);
		Champion c = getChamp(x);
		boolean exit = false;
		if (buttonCounter == 6) {
			exit = true;
		}
		if (buttonCounter < 4) {
			if (buttonCounter == 1) {
				game.getFirstPlayer().setLeader(c);
			}
			game.getFirstPlayer().getTeam().add(c);
			//b.disable();
			b.setVisible(false);
		} else {
			if (buttonCounter == 4) {
				game.getSecondPlayer().setLeader(c);
			}
			game.getSecondPlayer().getTeam().add(c);
			b.setVisible(false);
		}

		if (exit) {
			sc.dispose();
			new board();
		}

	}
	
	public Champion getChamp(int index) {
		for(int i = 0; i < game.getAvailableChampions().size(); i++) {
//			if(name.equals(game.getAvailableChampions().get(i).getName())) {
//				return game.getAvailableChampions().get(i);
//			}
			
			if(i == index) {
				return game.getAvailableChampions().get(i);
			}
			
		}
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		JButton b = (JButton) e.getSource();
		//String x = b.getText();
		int x = buttons.indexOf(b);
		Champion c = getChamp(x);
		if(buttons.indexOf(b) == 7) {
			sc.getInfo().append("Name: Iceman\nAttack Damage :" + c.getAttackDamage());
			
			sc.revalidate();
			sc.repaint();
		}
		if(buttons.indexOf(b) == 8) {
			sc.getInfo().append("Name: Ironman\nAttack Damage :" + c.getAttackDamage());
			
			sc.revalidate();
			sc.repaint();
		}
		else if(buttons.indexOf(b) == 9) {
			sc.getInfo().append("Name: Loki\nAttack Damage :" + c.getAttackDamage());
			
			sc.revalidate();
			sc.repaint();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		JButton b = (JButton) e.getSource();
		int x = buttons.indexOf(b);
		Champion c = getChamp(x);
		if(x == 7) {
			sc.getInfo().setText("select your champions\n");
		}
		else if(x == 8) {
			sc.getInfo().setText("select your champions\n");
		}
		else if(x == 9) {
			sc.getInfo().setText("select your champions\n");
		}
	}
	
}
