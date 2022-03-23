package engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import model.abilities.Ability;
import model.world.Champion;
import model.world.Cover;

public class Game {
	
	private Player firstPlayer;
	private Player secondPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private Object[][] board;
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private PriorityQueue turnOrder;
	private static int BOARDHEIGHT;
	private static int BOARDWIDTH;
	
	public Game (Player first, Player second)
	{
		firstPlayer = first;
		secondPlayer = second;
		board = new Object[5][5];
		placeChampions();
		placeCovers();
	}
	
	
	
	private void placeCovers() {
		
		Cover[] cArr = new Cover[5];
		
		for(int i = 0; i<5; i++)
		{
			int x = (int)Math.random()*4;
			int y = (int)Math.random()*4;
		while((board[x][y] != null) || (x== 4 && y==4) || (x== 0 && y==0) || (x== 4 && y==0) || (x== 0 && y==4))
		{
			 x = (int)Math.random()*4;
				 y = (int)Math.random()*4;
				 
			}
			Cover c = new Cover(x,y);
			cArr[i] = c;
			board[x][y] = c;

		}
	}


	private void placeChampions() {
		Champion o1 = new Champion();
		Champion o2 = new Champion();
		Champion o3 = new Champion();
		
		Champion v1 = new Champion();
		Champion v2 = new Champion();
		Champion v3 = new Champion();
		
		board[0][1] = o1;
		board[0][2] = o2;
		board[0][3] = o3;
		
		board[4][1] = v1;
		board[4][2] = v2;
		board[4][3] = v3;
		
		
	}
	
	public static void loadAbilities(String filePath) throws Exception {
		
		BufferedReader br= new BufferedReader(new FileReader(filePath));
		String line = "";
		while((line = br.readLine()) != null)
		{
			//availableAbilities = line.split(",");
			//availableAbilities.add((Ability)line.split(","));
		}
		
	}
	
	public static void loadChampions(String filePath) throws Exception
	{
		BufferedReader br= new BufferedReader(new FileReader(filePath));
	}



	public Player getFirstPlayer() {
		return firstPlayer;
	}
	public Player getSecondPlayer() {
		return secondPlayer;
	}
	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}
	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}
	public Object[][] getBoard() {
		return board;
	}
	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}
	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}
	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}
	public static int getBOARDHEIGHT() {
		return BOARDHEIGHT;
	}
	public static int getBOARDWIDTH() {
		return BOARDWIDTH;
	}
	
	
}
