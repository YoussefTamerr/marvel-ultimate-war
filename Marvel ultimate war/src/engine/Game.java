package engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Effect;
import model.effects.EffectType;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Cover;
import model.world.Hero;
import model.world.Villain;

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

		for (int i = 0; i < 5; i++) {
			int x = (int) Math.random() * 4;
			int y = (int) Math.random() * 4;
			while ((board[x][y] != null) || (x == 4 && y == 4) || (x == 0 && y == 0) || (x == 4 && y == 0)
					|| (x == 0 && y == 4)) {
				x = (int) Math.random() * 4;
				y = (int) Math.random() * 4;

			}
			Cover c = new Cover(x, y);
			cArr[i] = c;
			board[x][y] = c;

		}
	}


	private void placeChampions() { 
		
		
		board[0][1] = firstPlayer.getTeam().get(0);
		board[0][2] = firstPlayer.getTeam().get(1);
		board[0][3] = firstPlayer.getTeam().get(2);
		
		board[4][1] = secondPlayer.getTeam().get(0);
		board[4][2] = secondPlayer.getTeam().get(1);
		board[4][3] = secondPlayer.getTeam().get(2);
		
		
	}
	
	public static void loadAbilities(String filePath) throws Exception { 
		
		
		BufferedReader br= new BufferedReader(new FileReader(filePath));
		String line = "";
		while((line = br.readLine()) != null)
		{
			String[] values = line.split(",");
			Ability ab = createAbility(values);
			availableAbilities.add(ab);
			
		}
		
	}
	
	public static void loadChampions(String filePath) throws Exception
	{
		BufferedReader br= new BufferedReader(new FileReader(filePath));
		String line = "";
		while((line = br.readLine()) != null)
		{
			String[] values = line.split(",");
			Champion ch = createChampion(values);
			availableChampions.add(ch);
			
		}
		
	}
	
	public static Champion createChampion(String[] s)
	{
		Champion c = null;
		switch(s[0]) {
		case "H" :
			c = new Hero(s[1], Integer.parseInt(s[2]), Integer.parseInt(s[3]), Integer.parseInt(s[4]), Integer.parseInt(s[5]), Integer.parseInt(s[6]), Integer.parseInt(s[7]));
			break;
		case "A" :
			c = new AntiHero(s[1], Integer.parseInt(s[2]), Integer.parseInt(s[3]), Integer.parseInt(s[4]), Integer.parseInt(s[5]), Integer.parseInt(s[6]), Integer.parseInt(s[7]));
			break;
		case "V" :
			c = new Villain(s[1], Integer.parseInt(s[2]), Integer.parseInt(s[3]), Integer.parseInt(s[4]), Integer.parseInt(s[5]), Integer.parseInt(s[6]), Integer.parseInt(s[7]));
			break;
		}
		return c;
	}
	
	public static Ability createAbility(String[] s)
	{
		
		EffectType et =null;
		switch(s[7]) {
		case "Disarm" :
			et = EffectType.DEBUFF;
			break;
		case "PowerUp" :
			et = EffectType.BUFF;
			break;
		case "Shield" :
			et = EffectType.BUFF;
			break;
		case "Silence" :
			et = EffectType.DEBUFF;
			break;
		case "SpeedUp" :
			et = EffectType.BUFF;
			break;
		case "Embrace" :
			et = EffectType.BUFF;
			break;
		case "Root" :
			et = EffectType.DEBUFF;
			break;
		case "Shock" :
			et = EffectType.DEBUFF;
			break;
		case "Dodge" :
			et = EffectType.BUFF;
			break;
		case "Stun" :
			et = EffectType.DEBUFF;
			break;
		}
		AreaOfEffect aoe = null;
		switch(s[5]) {
		case "SELFTARGET" :
			aoe = AreaOfEffect.SELFTARGET;
			break;
		case "SINGLETARGET" :
			aoe = AreaOfEffect.SINGLETARGET;
			break;
		case "TEAMTARGET" :
			aoe = AreaOfEffect.TEAMTARGET;
			break;
		case "DIRECTIONAL" :
			aoe = AreaOfEffect.DIRECTIONAL;
			break;
		case "SURROUND" :
			aoe = AreaOfEffect.SURROUND;
			break;
		
		}
		Ability a = null;
		switch (s[0]) {
		case "CC" :
			Effect e = new Effect(s[7], Integer.parseInt(s[8]), et );
			a = new CrowdControlAbility(s[1] , Integer.parseInt(s[2]), Integer.parseInt(s[4]), Integer.parseInt(s[3]), aoe ,Integer.parseInt(s[6]), e);
			break;
		case "DMG" :
			a = new DamagingAbility(s[1] , Integer.parseInt(s[2]), Integer.parseInt(s[4]), Integer.parseInt(s[3]), aoe ,Integer.parseInt(s[6]), Integer.parseInt(s[7]) );
			break;
		case "HEL" :
			a = new HealingAbility(s[1] , Integer.parseInt(s[2]), Integer.parseInt(s[4]), Integer.parseInt(s[3]), aoe ,Integer.parseInt(s[6]), Integer.parseInt(s[7]) ); 
			break;
		}
		return a;

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
