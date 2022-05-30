package views;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

import engine.Game;
import engine.Player;
import model.world.Champion;
import model.world.Cover;
import model.world.Hero;
import model.world.Villain;

import static engine.Game.getAvailableChampions;

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
	private String firstName;
	private String secondName;
	private Champion curr;
	
	private ArrayList<JButton> buttons;
	private int buttonCounter = 0;
	
	public Controller(String f, String s) throws IOException  {
		Game.loadAbilities("Abilities.csv");
		Game.loadChampions("Champions.csv");
		firstName = f;
		secondName = s;
		first = new Player(f);
		second = new Player(s);
//		game = new Game(first, second); 
//		curr = game.getCurrentChampion();
		sc= new selectChampions();
		buttons = new ArrayList<JButton>();
		for(int i = 0; i < getAvailableChampions().size(); i++) {
			JButton btn = new JButton(getAvailableChampions().get(i).getName());
			//btn = new JButton(game.getAvailableChampions().get(i).getName());
			btn.setFocusable(false);
			btn.addMouseListener(this);
			btn.addActionListener(this);
			buttons.add(btn);
			sc.getPanel().add(btn);
			
			//sc.getInfo().setText(firstName +"please select your leader\nthe first champion you select will be the leader of the team\n");
			resetTextSelection();
			
			sc.revalidate();
			sc.repaint();
		}
		
	}
	
	public void resetTextSelection() {
		if(buttonCounter == 0) {
			this.sc.getInfo().setText(firstName + " please select your leader");
		}
		else if(buttonCounter == 1) {
			this.sc.getInfo().setText(secondName + " please select your leader");
		}
		else if(buttonCounter % 2 == 0)
			this.sc.getInfo().setText(firstName + " please select your champions");
		else {
			this.sc.getInfo().setText(secondName + " please select your champions");
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
		if (buttonCounter % 2 != 0) {
			if (buttonCounter == 1) {
				first.setLeader(c);
			}
			first.getTeam().add(c);
			//b.disable();
			b.setVisible(false);
		} else {
			if (buttonCounter == 4) {
				second.setLeader(c);
			}
			second.getTeam().add(c);
			b.setVisible(false);
		}

		if (exit) {
			sc.dispose();
			view = new board();
//			game.placeChampions();
			try {
				game = new Game(first, second);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			curr = game.getCurrentChampion();
			setupBoard(view);
		}

	}
	
	public void setupBoard(board v) {
		Object[][] loc = game.getBoard();
		Cell[][] buttonLoc = v.getCellsPanel().getButtonGrid();
		int count = 4;
		for(int i = 0; i < 5;i++) {
			for(int j = 0;j<5;j++) {
				if(loc[i][j] instanceof Champion) {
					Champion c = (Champion) loc[i][j];
					buttonLoc[count][j].setText(c.getName());
				}
				else if(loc[i][j] instanceof Cover) {
					buttonLoc[count][j].setChamp("cover:" + ((Cover) loc[i][j]).getLocation().x +","+((Cover) loc[i][j]).getLocation().y);
				}
				
			}
			count--;
		}
		resetTextGame();
		
	}

	public void resetTextGame() {
		view.getInfo().setText("Player 1:"+firstName+" Player 2:"+secondName+"\n");
		view.getInfo().append("its "+ curr.getName() +" turn!!!");
		if(curr instanceof Hero) {
			view.getInfo().append("\nType: Hero");
		} else if(curr instanceof Villain) {
			view.getInfo().append("\nType: Villain");
		} else {
			view.getInfo().append("\nType: AntiHero");
		}
		
		view.getInfo().append("\nName:" + curr.getName() +"\nDamage:" + curr.getAttackDamage()+ "\nRange:" + curr.getAttackRange()
		+ "\nHP:" + curr.getCurrentHP() + "\nMana:" + curr.getMana() +"\nSpeed:"+ curr.getSpeed() + "\nAction points per turn:"+curr.getMaxActionPointsPerTurn() 
		+"\nAbilities:"+curr.getAbilities().get(0).getName()+", "+curr.getAbilities().get(1).getName()+", "+curr.getAbilities().get(2).getName());
		
		
	}

	public Champion getChamp(int index) {
		for(int i = 0; i < getAvailableChampions().size(); i++) {
//			if(name.equals(game.getAvailableChampions().get(i).getName())) {
//				return game.getAvailableChampions().get(i);
//			}
			
			if(i == index) {
				return getAvailableChampions().get(i);
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
		
		JButton b = (JButton) e.getSource();
		//String x = b.getText();
		int x = buttons.indexOf(b);
		Champion c = getChamp(x);
		
		sc.getInfo().append("\nName:" + c.getName() +"\nDamage:" + c.getAttackDamage()+ "\nRange:" + c.getAttackRange()
		+ "\nHP:" + c.getCurrentHP() + "\nMana:" + c.getMana() +"\nSpeed:"+ c.getSpeed() + "\nAction points per turn:"+c.getMaxActionPointsPerTurn() 
		+"\nAbilities:"+c.getAbilities().get(0).getName()+", "+c.getAbilities().get(1).getName()+", "+c.getAbilities().get(2).getName());
		
		/*if(buttons.indexOf(b) == 7) {
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
		}*/
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
		resetTextSelection();
		
		/*JButton b = (JButton) e.getSource();
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
		}*/
	}
	
}
