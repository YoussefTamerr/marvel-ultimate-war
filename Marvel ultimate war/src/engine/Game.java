package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Dodge;
import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;
import model.effects.PowerUp;
import model.effects.Root;
import model.effects.Shield;
import model.effects.Shock;
import model.effects.Silence;
import model.effects.SpeedUp;
import model.effects.Stun;
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
	private final static int BOARDHEIGHT = 5;
	private final static int BOARDWIDTH = 5;
	public Game () {
		
	}
	
	public Game (Player first, Player second) throws Exception
	{
		this.firstPlayer = first;
		this.secondPlayer = second;
		board = new Object[5][5];
		firstLeaderAbilityUsed=false;
		secondLeaderAbilityUsed=false;
		availableChampions =new ArrayList<Champion>() ;
		availableAbilities =new ArrayList<Ability>() ;
		turnOrder=new PriorityQueue(6);
		
		placeChampions();
		placeCovers();
		//String filepath="/Marvel ultimate war/Abilities.csv";
		//String filepath = "C:\\Users\\youss\\git\\marvel-ultimate-war\\Marvel ultimate war\\Abilities.csv";
		//loadAbilities(filepath);
		//String filepathc="/Marvel ultimate war/Champions.csv";
		//String filepathc= "C:\\Users\\youss\\git\\marvel-ultimate-war\\Marvel ultimate war\\Champions.csv";
		//loadChampions(filepathc);
		
	}
	
	
	
	private void placeCovers() {
		
		//Cover[] cArr = new Cover[5];

		for (int i = 0; i < 5; i++) {
			int x =  (int) (Math.random() * 5);
			int y =  (int) (Math.random() * 3)+1;
			while (board[y][x] != null) { //|| (x == 4 && y == 4) 
					//|| (x == 0 && y == 0) 
					//|| (x == 4 && y == 0)
					//|| (x == 0 && y == 4)) {
				x = (int) (Math.random() * 5);
				y = (int) (Math.random() * 3)+1;

			}
			Cover c = new Cover(y, x);
			//cArr[i] = c;
			board[y][x] = c;

		}
	}


	private void placeChampions() { //////////////////////////////////////////////////
		
		//Champion ch1 = firstPlayer.getTeam().get(0);
	//	Champion ch2 = firstPlayer.getTeam().get(1);
		//Champion ch3 = firstPlayer.getTeam().get(2);
		ArrayList<Champion>team1= firstPlayer.getTeam();
		
		for(int i=0;i<team1.size();i++) {
		Champion ch1 = firstPlayer.getTeam().get(i);
		board[0][i+1] = team1.get(i);
		ch1.setLocation(new Point(0,i+1));
		//ch1.setLocation(new Point(1,0));
		//board[0][2] = ch2;
		//ch2.setLocation(new Point(2,0));
		//board[0][3] = ch3;
		//ch3.setLocation(new Point(3,0));
		}
		ArrayList<Champion>team2= secondPlayer.getTeam();
		for(int i=0;i<team2.size();i++) {
		Champion ch2 = secondPlayer.getTeam().get(i);
		board[4][i+1] = team2.get(i);
		ch2.setLocation(new Point(4,i+1));
		//ch1.setLocation(new Point(1,0));
		//board[0][2] = ch2;
		//ch2.setLocation(new Point(2,0));
		//board[0][3] = ch3;
		//ch3.setLocation(new Point(3,0));
		}

		//{Champion v1 = secondPlayer.getTeam().get(0);
		//Champion v2 = secondPlayer.getTeam().get(1);
		//Champion v3 = secondPlayer.getTeam().get(2);
		
		//board[4][1] = v1;
		//v1.setLocation(new Point(1,4));
		//board[4][2] = v2;
		//v2.setLocation(new Point(2,4));
		//board[4][3] = v3;
		//v3.setLocation(new Point(3,4));
		
		
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
	
	public static void loadChampions(String filePath) throws Exception//esm el ability hador el ability fa el avaialaibe abilities loop 3ala el availaible 3 abilities 
	{
		BufferedReader br= new BufferedReader(new FileReader(filePath));
		String line = "";
		while((line = br.readLine()) != null)
		{
			String[] s = line.split(",");
			Champion c = createChampion(s);
			availableChampions.add(c);
			
			
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
		String ability1=s[8];
		String ability2=s[9];
		String ability3=s[10];
		for(int f =0; f< availableAbilities.size();f++) {
		Ability ability=availableAbilities.get(f);
		if(ability.getName().equals(ability1)||ability.getName().equals(ability2)||ability.getName().equals(ability3))
			c.getAbilities().add(ability);
		}
		return c;
	}
	
	
	public static Ability createAbility(String[] s)
	{
		
		EffectType et =null;
		Effect f = null;
		switch(s[7]) {
		case "Disarm" :
			f = new Disarm(Integer.parseInt(s[8]));
			break;
		case "PowerUp" :
			f = new PowerUp(Integer.parseInt(s[8]));
			break;
		case "Shield" :
			f = new Shield(Integer.parseInt(s[8]));
			break;
		case "Silence" :
			f = new Silence(Integer.parseInt(s[8]));
			break;
		case "SpeedUp" :
			f = new SpeedUp(Integer.parseInt(s[8]));
			break;
		case "Embrace" :
			f = new Embrace(Integer.parseInt(s[8]));
			break;
		case "Root" :
			f = new Root(Integer.parseInt(s[8]));
			break;
		case "Shock" :
			f = new Shock(Integer.parseInt(s[8]));
			break;
		case "Dodge" :
			f = new Dodge(Integer.parseInt(s[8]));
			break;
		case "Stun" :
			f = new Stun(Integer.parseInt(s[8]));
			break;
		}
		/*AreaOfEffect aoe = null;
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
		
		}*/
		Ability a = null; /////rage3 3ala 7tet s[3] w s[4]//////////////
		switch (s[0]) {
		case "CC" :
			
			a = new CrowdControlAbility(s[1] , Integer.parseInt(s[2]), Integer.parseInt(s[4]), Integer.parseInt(s[3]), AreaOfEffect.valueOf(s[5]) ,Integer.parseInt(s[6]), f);
			break;
		case "DMG" :
			a = new DamagingAbility(s[1] , Integer.parseInt(s[2]), Integer.parseInt(s[4]), Integer.parseInt(s[3]), AreaOfEffect.valueOf(s[5]) ,Integer.parseInt(s[6]), Integer.parseInt(s[7]) );
			break;
		case "HEL" :
			a = new HealingAbility(s[1] , Integer.parseInt(s[2]), Integer.parseInt(s[4]), Integer.parseInt(s[3]), AreaOfEffect.valueOf(s[5]) ,Integer.parseInt(s[6]), Integer.parseInt(s[7]) ); 
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
	public static int getBoardheight() {
		return BOARDHEIGHT;
	}
	public static int getBoardwidth() {
		return BOARDWIDTH;
	}
	
	
}
