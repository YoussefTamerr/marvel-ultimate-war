package views;

import java.io.IOException;
import java.util.ArrayList;

import engine.Game;
import engine.Player;
import engine.PriorityQueue;
import exceptions.AbilityUseException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughResourcesException;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Effect;
import model.world.Champion;
import model.world.Cover;
import model.world.Hero;
import model.world.Villain;



public class StartGame {
	
	private static Button [][]buttonLoc;
	private static Game game;
	private static Group root;
	

	public static Scene createStartGame(Game g) {
		game = g;
		root = new Group();
//		GridPane yarab =new GridPane();
//		
//		yarab.setPadding(new Insets(10));
//		yarab.setLayoutX(440);
//		yarab.setHgap(10);
//		yarab.setVgap(10);
//		buttonLoc= new Button[5][5];
//		for (int r = 0; r < 5; r++) {
//	        for (int c = 0; c < 5; c++) {
//	            Button button = new Button();
////	            button.setScaleX(5);
////	            button.setScaleY(5);
//	            button.setFocusTraversable(false); 
//	            button.setPrefSize(120, 120);
//	            buttonLoc[r][c]=button;
//	            yarab.add(button,c,r);
//	            
//	        }
//	    }
		setupText();
		setupBoard();
		
		
//		root.getChildren().add(yarab);
		Scene s = new Scene(root);
		//yarab.requestFocus();
		
		
		
		return s;
	}
	
	public static Label getAliveChamp(Player p) {
		boolean first = true;
		Label l = new Label();
		for(int i = 0; i<p.getTeam().size();i++) {
			if(first) {
				l.setText("current team: "+p.getTeam().get(i).getName());
				first = false;
			}
			else {
				String temp = l.getText();
				l.setText(temp +", "+ p.getTeam().get(i).getName());
			}
		}
		return l;
	}
	
	public static void setupText() {
		
		root.getChildren().clear();
		
		VBox leftUpper = new VBox();
		leftUpper.setPrefSize(300, 50);
		leftUpper.setBackground(Background.fill(Color.WHITE));
	    //v.getChildren().add(l);
		leftUpper.setLayoutX(5);
		leftUpper.setLayoutY(10);
		
		// left labels
		
		Label name1 = new Label("First player name: " + game.getFirstPlayer().getName());
		Label team1 = getAliveChamp(game.getFirstPlayer());
		Label leaderAbility = new Label("leader ability: " +(game.isFirstLeaderAbilityUsed() ? "used":"not used"));
		
		leftUpper.getChildren().add(name1);
		leftUpper.getChildren().add(team1);
		leftUpper.getChildren().add(leaderAbility);


		
		VBox leftMiddle = new VBox();
		leftMiddle.setLayoutX(5);
		leftMiddle.setLayoutY(80);
		//leftUpper.setBackground(Background.fill(Color.LAVENDER));
		
		HBox firstTeam = new HBox();
		firstTeam.setBackground(Background.fill(Color.BEIGE));
		leftMiddle.getChildren().add(firstTeam);
		
		for(int i = 0; i < game.getFirstPlayer().getTeam().size();i++) {
			
			int index = champIndex(game.getFirstPlayer().getTeam().get(i));
			//buttonLoc[count][j].setText(c.getName());
			Image o = SelectChampions.getView().get(index);
			ImageView v = new ImageView(o);
			v.setFitWidth(50);
		    v.setFitHeight(70);
		    v.setPreserveRatio(true);
			
			VBox fTeam = new VBox();
			fTeam.setBackground(Background.fill(Color.WHITE));
			
			Label n = new Label("Name: "+game.getFirstPlayer().getTeam().get(i).getName());
			Label h = new Label("current HP: "+game.getFirstPlayer().getTeam().get(i).getCurrentHP()+"/"+game.getFirstPlayer().getTeam().get(i).getMaxHP());
			Label m = new Label("current mana: " + game.getFirstPlayer().getTeam().get(i).getMana()+", Speed: " + game.getFirstPlayer().getTeam().get(i).getSpeed());
			//Label s = new Label("speed: " + game.getFirstPlayer().getTeam().get(i).getSpeed());
			Label maxA = new Label("Action points: " + game.getFirstPlayer().getTeam().get(i).getCurrentActionPoints()+"/"+game.getFirstPlayer().getTeam().get(i).getMaxActionPointsPerTurn());
			Label aD = new Label("Attack Damage: " + game.getFirstPlayer().getTeam().get(i).getAttackDamage()+", Attack range: " + game.getFirstPlayer().getTeam().get(i).getAttackRange());
			//Label aR = new Label("attack range: " + game.getFirstPlayer().getTeam().get(i).getAttackRange());
			Label t = new Label();
			if(game.getFirstPlayer().getTeam().get(i) instanceof Hero) {
				t.setText("type: Hero");
			}
			else if(game.getFirstPlayer().getTeam().get(i) instanceof Villain) {
				t.setText("type: Villain");
			}
			else {
				t.setText("type: AntiHero");
			}
			fTeam.getChildren().add(n);
			if(game.getFirstPlayer().getTeam().get(i) == game.getCurrentChampion()) {
				//n.setText("Name: "+game.getFirstPlayer().getTeam().get(i).getName()+" This Champion's Turn !!!!");
				Label chT = new Label(" This Champion's Turn !!!!");
				fTeam.getChildren().add(chT);
			}
//			else {
//				fTeam.getChildren().add(n);
//			}
			if(game.getFirstPlayer().getTeam().get(i) == game.getFirstPlayer().getLeader()) {
				fTeam.getChildren().add(new Label("The Leader"));
			}
			
			fTeam.getChildren().add(v);
			fTeam.getChildren().add(h);
			fTeam.getChildren().add(m);
			//leftMiddle.getChildren().add(s);
			fTeam.getChildren().add(maxA);
			fTeam.getChildren().add(aD);
			//leftMiddle.getChildren().add(aR);
			fTeam.getChildren().add(t);
			Label con = new Label("Condition: "+game.getFirstPlayer().getTeam().get(i).getCondition().name());
			fTeam.getChildren().add(con);
			Label aE1 = new Label("Applied Effects : ");
			fTeam.getChildren().add(aE1);
			for(Effect e : game.getFirstPlayer().getTeam().get(i).getAppliedEffects()) {
				Label aE = new Label("Name: "+e.getName()+", Duration: "+e.getDuration());
				fTeam.getChildren().add(aE);
			}
			if(i == 1) {
				VBox space = new VBox();
				space.setBackground(Background.fill(Color.WHITE));
				space.setPrefSize(20, firstTeam.getHeight());
				firstTeam.getChildren().add(space);
			}
			if(i == 2) {
				leftMiddle.getChildren().add(fTeam);
			}
			else 
				firstTeam.getChildren().add(fTeam);
		}
		
		
		
		VBox rightUpper = new VBox();
		rightUpper.setPrefSize(300, 50);
		rightUpper.setBackground(Background.fill(Color.WHITE));
	    //v.getChildren().add(l);
		rightUpper.setLayoutX(1100);
		rightUpper.setLayoutY(10);
		
		
		
		// right labels
		
		Label name2 = new Label("Second player name: " + game.getSecondPlayer().getName());
		Label team2 = getAliveChamp(game.getSecondPlayer());
		Label leaderAbility2 = new Label("leader ability: " +(game.isSecondLeaderAbilityUsed() ? "used":"not used"));
		
		rightUpper.getChildren().add(name2);
		rightUpper.getChildren().add(team2);
		rightUpper.getChildren().add(leaderAbility2);
		
		
		
		VBox rightMiddle = new VBox();
		rightMiddle.setLayoutX(1100);
		rightMiddle.setLayoutY(80);
		
		HBox secondTeam = new HBox();
		secondTeam.setBackground(Background.fill(Color.BEIGE));
		rightMiddle.getChildren().add(secondTeam);
		
		for(int i = 0; i < game.getSecondPlayer().getTeam().size();i++) {
			
			int index = champIndex(game.getSecondPlayer().getTeam().get(i));
			//buttonLoc[count][j].setText(c.getName());
			Image o = SelectChampions.getView().get(index);
			ImageView v = new ImageView(o);
			v.setFitWidth(50);
		    v.setFitHeight(70);
		    v.setPreserveRatio(true);
			
			VBox sTeam = new VBox();
			sTeam.setBackground(Background.fill(Color.WHITE));
			
			Label n = new Label("Name: "+game.getSecondPlayer().getTeam().get(i).getName());
			Label h = new Label("current HP: "+game.getSecondPlayer().getTeam().get(i).getCurrentHP()+"/"+game.getSecondPlayer().getTeam().get(i).getMaxHP());
			Label m = new Label("current mana: " + game.getSecondPlayer().getTeam().get(i).getMana()+", Speed: " + game.getSecondPlayer().getTeam().get(i).getSpeed());
			//Label s = new Label("speed: " + game.getFirstPlayer().getTeam().get(i).getSpeed());
			Label maxA = new Label("Action points: " + game.getSecondPlayer().getTeam().get(i).getCurrentActionPoints()+"/"+game.getSecondPlayer().getTeam().get(i).getMaxActionPointsPerTurn());
			Label aD = new Label("Attack Damage: " + game.getSecondPlayer().getTeam().get(i).getAttackDamage()+", Attack range: " + game.getSecondPlayer().getTeam().get(i).getAttackRange());
			//Label aR = new Label("attack range: " + game.getFirstPlayer().getTeam().get(i).getAttackRange());
			Label t = new Label();
			if(game.getSecondPlayer().getTeam().get(i) instanceof Hero) {
				t.setText("type: Hero");
			}
			else if(game.getSecondPlayer().getTeam().get(i) instanceof Villain) {
				t.setText("type: Villain");
			}
			else {
				t.setText("type: AntiHero");
			}
			sTeam.getChildren().add(n);
			if(game.getSecondPlayer().getTeam().get(i) == game.getCurrentChampion()) {
				//n.setText("Name: "+game.getFirstPlayer().getTeam().get(i).getName()+" This Champion's Turn !!!!");
				Label chT = new Label(" This Champion's Turn !!!!");
				sTeam.getChildren().add(chT);
			}
//			else {
//				fTeam.getChildren().add(n);
//			}
			if(game.getSecondPlayer().getTeam().get(i) == game.getSecondPlayer().getLeader()) {
				sTeam.getChildren().add(new Label("The Leader"));
			}
			
			sTeam.getChildren().add(v);
			sTeam.getChildren().add(h);
			sTeam.getChildren().add(m);
			//leftMiddle.getChildren().add(s);
			sTeam.getChildren().add(maxA);
			sTeam.getChildren().add(aD);
			//leftMiddle.getChildren().add(aR);
			sTeam.getChildren().add(t);
			Label con = new Label("Condition: "+game.getSecondPlayer().getTeam().get(i).getCondition().name());
			sTeam.getChildren().add(con);
			Label aE1 = new Label("Applied Effects : ");
			sTeam.getChildren().add(aE1);
			for(Effect e : game.getSecondPlayer().getTeam().get(i).getAppliedEffects()) {
				Label aE = new Label("Name: "+e.getName()+", Duration: "+e.getDuration());
				sTeam.getChildren().add(aE);
			}
			if(i == 1) {
				VBox space = new VBox();
				space.setBackground(Background.fill(Color.WHITE));
				space.setPrefSize(20, firstTeam.getHeight());
				secondTeam.getChildren().add(space);
			}
			if(i == 2) {
				rightMiddle.getChildren().add(sTeam);
			}
			else 
				secondTeam.getChildren().add(sTeam);
		}
		
		Button con = new Button("Controls");
		Tooltip t = new Tooltip("You move using WASD buttons\nAttack with the arrow buttons\nUse T key to end your turn\nUse L key to use the leader ability\nPress on the ability buttons to use them ");
		con.setTooltip(t);
		con.setPrefSize(100, 30);
		rightMiddle.getChildren().add(new Label(""));
		rightMiddle.getChildren().add(con);
		
		
		VBox leftDown = new VBox();
		leftDown.setPrefSize(300, 300);
		leftDown.setBackground(Background.fill(Color.WHITE));
	    //v.getChildren().add(l);
		leftDown.setLayoutX(25);
		leftDown.setLayoutY(670);
		Label k = new Label("Champions turn order");
		k.setFont(Font.font("italic", 15));
		leftDown.getChildren().add(k);
		
		PriorityQueue pq = new PriorityQueue(game.getTurnOrder().size());
		//PriorityQueue temp = new PriorityQueue(6);
		int size = game.getTurnOrder().size();
		for(int i = 0; i<size && !game.getTurnOrder().isEmpty();i++) {
			pq.insert(game.getTurnOrder().peekMin());
			Champion b = (Champion) game.getTurnOrder().remove();
			Label x = new Label(b.getName()+" current action points: " + b.getCurrentActionPoints());
			leftDown.getChildren().add(x);
			
		}
		for(int i =0;i<size && !pq.isEmpty();i++) {
			game.getTurnOrder().insert(pq.remove());
		}
		
		HBox down = new HBox();
		down.setPrefSize(1000, 200);
		down.setBackground(Background.fill(Color.WHITE));
		down.setLayoutX(380);
		down.setLayoutY(670);
		
		
		
		
		Button[][] b = new Button[5][5];
		
		
		boolean first = true;
		for(int i = 0; i<game.getCurrentChampion().getAbilities().size();i++) {
			Label fLine = new Label();
					
			if(first) {
				first = false;
				fLine.setText(game.getCurrentChampion().getName()+" abilities:");
			}
			else {
				fLine.setText("");
			}
			Ability q = game.getCurrentChampion().getAbilities().get(i);
			//Label qN = new Label("Ability name: "+q.getName());
			Button qN = new Button(q.getName());
			qN.setFocusTraversable(false);
			Control.executeAbility(qN, q, game);

			Label qT = new Label();
			if(q instanceof DamagingAbility) {
				qT.setText("Type: Damaging ability, Damage amount: "+((DamagingAbility)q).getDamageAmount());
			}
			else if(q instanceof HealingAbility) {
				qT.setText("Type: Healing ability, Heal amount: "+((HealingAbility)q).getHealAmount());
			}
			else {
				qT.setText("Type: Crowd Control ability, \nEffect name: "+((CrowdControlAbility)q).getEffect().getName()+". Duration:"+((CrowdControlAbility)q).getEffect().getDuration());
			}
			Label qAreaOfEff = new Label("Area of effect: " + q.getCastArea().name() +", Cast range: "+q.getCastRange());
			Label cool = new Label("Current cooldown: "+q.getCurrentCooldown()+", Base cooldown: "+ q.getBaseCooldown());
			Label ma = new Label("Mana cost: "+ q.getManaCost()+", Action points cost: "+q.getRequiredActionPoints());
			
			
			VBox down1 = new VBox();
			down1.setPrefSize(250, 200);
			down1.setBackground(Background.fill(Color.WHITE));
			//down1.setLayoutX(440);
			//down.setLayoutY(670);
			
			down1.getChildren().add(fLine);
			down1.getChildren().add(qN);
			down1.getChildren().add(qT);
			down1.getChildren().add(qAreaOfEff);
			down1.getChildren().add(cool);
			down1.getChildren().add(ma);
			down1.getChildren().add(new Label(""));
			
			down.getChildren().add(down1);
			
//			rightDown.getChildren().add(qN);
//			rightDown.getChildren().add(qT);
//			rightDown.getChildren().add(qAreaOfEff);
//			rightDown.getChildren().add(cool);
//			rightDown.getChildren().add(ma);
//			rightDown.getChildren().add(new Label(""));
		}
		
		
		
		root.getChildren().add(leftUpper);
		root.getChildren().add(leftMiddle);
		
		root.getChildren().add(leftDown);
		root.getChildren().add(down);
		
		root.getChildren().add(rightUpper);
		root.getChildren().add(rightMiddle);
		
	}
	
	
	/*public static void setupTextt() {
		/*VBox left = new VBox();
		left.setBackground(Background.fill(Color.BEIGE));
		left.setPrefSize(300, 600);
		left.setLayoutX(25);
		left.setLayoutY(50);
		
		
		
		
		
		
		VBox leftUpper = new VBox();
		leftUpper.setPrefSize(300, 50);
		leftUpper.setBackground(Background.fill(Color.AQUA));
	    //v.getChildren().add(l);
		leftUpper.setLayoutX(25);
		leftUpper.setLayoutY(10);
		
		// left labels
		
		Label name1 = new Label("First player name: " + game.getFirstPlayer().getName());
		Label team1 = getAliveChamp(game.getFirstPlayer());
		Label leaderAbility = new Label("leader ability: " +(game.isFirstLeaderAbilityUsed() ? "used":"not used"));
		
		leftUpper.getChildren().add(name1);
		leftUpper.getChildren().add(team1);
		leftUpper.getChildren().add(leaderAbility);
		
		VBox leftMiddle = new VBox();
		leftMiddle.setPrefSize(300, 400);
		leftMiddle.setLayoutX(25);
		leftMiddle.setLayoutY(60);
		leftMiddle.setBackground(Background.fill(Color.BEIGE));
		leftMiddle.getChildren().add(new Label(""));
		leftMiddle.getChildren().add(new Label("Team details:"));
		for(int i = 0; i < game.getFirstPlayer().getTeam().size();i++) {
			
//			Champion c = game.getFirstPlayer().getTeam().get(i);
//			int index = champIndex(c);
//			//buttonLoc[count][j].setText(c.getName());
//			Image o = SelectChampions.getView().get(index);
//			ImageView v = new ImageView(o);
//			v.setFitWidth(50);
//		    v.setFitHeight(70);
//		    v.setPreserveRatio(true);
			
			
			Label n = new Label("Name: "+game.getFirstPlayer().getTeam().get(i).getName());
			//Label h = new Label("current HP: "+game.getFirstPlayer().getTeam().get(i).getCurrentHP());
			Label m = new Label("current mana: " + game.getFirstPlayer().getTeam().get(i).getMana()+", Speed: " + game.getFirstPlayer().getTeam().get(i).getSpeed());
			//Label s = new Label("speed: " + game.getFirstPlayer().getTeam().get(i).getSpeed());
			Label maxA = new Label("max action points per turn: " + game.getFirstPlayer().getTeam().get(i).getMaxActionPointsPerTurn()+", current HP: "+game.getFirstPlayer().getTeam().get(i).getCurrentHP());
			Label aD = new Label("Attack Damage: " + game.getFirstPlayer().getTeam().get(i).getAttackDamage()+", Attack range: " + game.getFirstPlayer().getTeam().get(i).getAttackRange());
			//Label aR = new Label("attack range: " + game.getFirstPlayer().getTeam().get(i).getAttackRange());
			Label t = new Label();
			if(game.getFirstPlayer().getTeam().get(i) instanceof Hero) {
				t.setText("type: Hero");
			}
			else if(game.getFirstPlayer().getTeam().get(i) instanceof Villain) {
				t.setText("type: Villain");
			}
			else {
				t.setText("type: AntiHero");
			}
			if(game.getFirstPlayer().getTeam().get(i) == game.getCurrentChampion()) {
				n.setText("Name: "+game.getFirstPlayer().getTeam().get(i).getName()+" This Champion's Turn !!!!");
				leftMiddle.getChildren().add(n);
			}
			else {
				leftMiddle.getChildren().add(n);
			}
			if(game.getFirstPlayer().getTeam().get(i) == game.getFirstPlayer().getLeader()) {
				leftMiddle.getChildren().add(new Label("The Leader"));
			}
			
			//leftMiddle.getChildren().add(v);
			//leftMiddle.getChildren().add(h);
			leftMiddle.getChildren().add(m);
			//leftMiddle.getChildren().add(s);
			leftMiddle.getChildren().add(maxA);
			leftMiddle.getChildren().add(aD);
			//leftMiddle.getChildren().add(aR);
			leftMiddle.getChildren().add(t);
			Label aE1 = new Label("Applied Effects : ");
			leftMiddle.getChildren().add(aE1);
			for(Effect e : game.getFirstPlayer().getTeam().get(i).getAppliedEffects()) {
				Label aE = new Label("Name: "+e.getName()+", Duration: "+e.getDuration());
				leftMiddle.getChildren().add(aE);
			}
			Label space = new Label("");
			leftMiddle.getChildren().add(space);
			
		}
		
		
		VBox rightUpper = new VBox();
		rightUpper.setPrefSize(300, 50);
		rightUpper.setBackground(Background.fill(Color.AQUA));
	    //v.getChildren().add(l);
		rightUpper.setLayoutX(1200);
		rightUpper.setLayoutY(10);
		
		
		
		// right labels
		
		Label name2 = new Label("Second player name: " + game.getSecondPlayer().getName());
		Label team2 = getAliveChamp(game.getSecondPlayer());
		Label leaderAbility2 = new Label("leader ability: " +(game.isSecondLeaderAbilityUsed() ? "used":"not used"));
		
		rightUpper.getChildren().add(name2);
		rightUpper.getChildren().add(team2);
		rightUpper.getChildren().add(leaderAbility2);
		
		
		
		VBox rightMiddle = new VBox();
		rightMiddle.setPrefSize(300, 400);
		rightMiddle.setLayoutX(1200);
		rightMiddle.setLayoutY(60);
		rightMiddle.setBackground(Background.fill(Color.BEIGE));
		rightMiddle.getChildren().add(new Label(""));
		rightMiddle.getChildren().add(new Label("Team details:"));
		for(int i = 0; i < game.getSecondPlayer().getTeam().size();i++) {
			
//			//Champion c = game.getSecondPlayer().getTeam().get(i);
//			int index = champIndex(game.getSecondPlayer().getTeam().get(i));
//			//buttonLoc[count][j].setText(c.getName());
//			Image o = SelectChampions.getView().get(index);
//			ImageView v = new ImageView(o);
//			v.setFitWidth(50);
//		    v.setFitHeight(70);
//		    v.setPreserveRatio(true);
			
			Label n = new Label("Name: "+game.getSecondPlayer().getTeam().get(i).getName());
			//Label h = new Label("current HP: "+game.getFirstPlayer().getTeam().get(i).getCurrentHP());
			Label m = new Label("Current mana: " + game.getSecondPlayer().getTeam().get(i).getMana()+", Speed: " + game.getSecondPlayer().getTeam().get(i).getSpeed());
			//Label s = new Label("speed: " + game.getFirstPlayer().getTeam().get(i).getSpeed());
			Label maxA = new Label("Max action points per turn: " + game.getSecondPlayer().getTeam().get(i).getMaxActionPointsPerTurn()+", current HP: "+game.getSecondPlayer().getTeam().get(i).getCurrentHP());
			Label aD = new Label("Attack Damage: " + game.getSecondPlayer().getTeam().get(i).getAttackDamage()+", Attack range: " + game.getSecondPlayer().getTeam().get(i).getAttackRange());
			//Label aR = new Label("attack range: " + game.getFirstPlayer().getTeam().get(i).getAttackRange());
			Label t = new Label();
			if(game.getSecondPlayer().getTeam().get(i) instanceof Hero) {
				t.setText("type: Hero");
			}
			else if(game.getSecondPlayer().getTeam().get(i) instanceof Villain) {
				t.setText("type: Villain");
			}
			else {
				t.setText("type: AntiHero");
			}
			
			
			
			if(game.getSecondPlayer().getTeam().get(i) == game.getCurrentChampion()) {
				n.setText("Name: "+game.getSecondPlayer().getTeam().get(i).getName()+" This Champion's Turn !!!!");
				rightMiddle.getChildren().add(n);
			}
			else {
				rightMiddle.getChildren().add(n);
			}
			if(game.getSecondPlayer().getTeam().get(i) == game.getSecondPlayer().getLeader()) {
				rightMiddle.getChildren().add(new Label("The Leader"));
			}
			//rightMiddle.getChildren().add(v);
			//leftMiddle.getChildren().add(h);
			rightMiddle.getChildren().add(m);
			//leftMiddle.getChildren().add(s);
			rightMiddle.getChildren().add(maxA);
			rightMiddle.getChildren().add(aD);
			//leftMiddle.getChildren().add(aR);
			rightMiddle.getChildren().add(t);
			Label aE1 = new Label("Applied Effects : ");
			rightMiddle.getChildren().add(aE1);
			for(Effect e : game.getSecondPlayer().getTeam().get(i).getAppliedEffects()) {
				Label aE = new Label("Name: "+e.getName()+", Duration: "+e.getDuration());
				rightMiddle.getChildren().add(aE);
			}
			Label space = new Label("");
			rightMiddle.getChildren().add(space);
			
		}
		
		
		
		VBox leftDown = new VBox();
		leftDown.setPrefSize(300, 300);
		leftDown.setBackground(Background.fill(Color.WHITE));
	    //v.getChildren().add(l);
		leftDown.setLayoutX(25);
		leftDown.setLayoutY(670);
		Label k = new Label("Champions turn order");
		leftDown.getChildren().add(k);
		
		PriorityQueue pq = new PriorityQueue(game.getTurnOrder().size());
		//PriorityQueue temp = new PriorityQueue(6);
		int size = game.getTurnOrder().size();
		for(int i = 0; i<size && !game.getTurnOrder().isEmpty();i++) {
			pq.insert(game.getTurnOrder().peekMin());
			Champion b = (Champion) game.getTurnOrder().remove();
			Label x = new Label(b.getName()+" current action points: " + b.getCurrentActionPoints());
			leftDown.getChildren().add(x);
			
		}
		for(int i =0;i<size && !pq.isEmpty();i++) {
			game.getTurnOrder().insert(pq.remove());
		}
		
		
		VBox rightDown = new VBox();
		rightDown.setPrefSize(300, 400);
		rightDown.setLayoutX(1200);
		rightDown.setLayoutY(500);
		rightDown.setBackground(Background.fill(Color.PINK));
		
		Label ti = new Label(game.getCurrentChampion().getName()+" abilities: ");
		rightDown.getChildren().add(ti);
		
		HBox down = new HBox();
		down.setPrefSize(1000, 200);
		down.setBackground(Background.fill(Color.FUCHSIA));
		down.setLayoutX(380);
		down.setLayoutY(670);
		
		
		boolean first = true;
		for(int i = 0; i<game.getCurrentChampion().getAbilities().size();i++) {
			Label fLine = new Label();
					
			if(first) {
				first = false;
				fLine.setText(game.getCurrentChampion().getName()+" abilities:");
			}
			else {
				fLine.setText("");
			}
			Ability q = game.getCurrentChampion().getAbilities().get(i);
			//Label qN = new Label("Ability name: "+q.getName());
			Button qN = new Button(q.getName());
			Control.executeAbility(qN, q, game);

			Label qT = new Label();
			if(q instanceof DamagingAbility) {
				qT.setText("Type: Damaging ability, Damage amount: "+((DamagingAbility)q).getDamageAmount());
			}
			else if(q instanceof HealingAbility) {
				qT.setText("Type: Healing ability, Heal amount: "+((HealingAbility)q).getHealAmount());
			}
			else {
				qT.setText("Type: Crowd Control ability, \nEffect name: "+((CrowdControlAbility)q).getEffect().getName()+". Duration:"+((CrowdControlAbility)q).getEffect().getDuration());
			}
			Label qAreaOfEff = new Label("Area of effect: " + q.getCastArea().name() +", Cast range: "+q.getCastRange());
			Label cool = new Label("Current cooldown: "+q.getCurrentCooldown()+", Base cooldown: "+ q.getBaseCooldown());
			Label ma = new Label("Mana cost: "+ q.getManaCost()+", Action points cost: "+q.getRequiredActionPoints());
			
			
			VBox down1 = new VBox();
			down1.setPrefSize(250, 200);
			down1.setBackground(Background.fill(Color.AQUA));
			//down1.setLayoutX(440);
			//down.setLayoutY(670);
			
			down1.getChildren().add(fLine);
			down1.getChildren().add(qN);
			down1.getChildren().add(qT);
			down1.getChildren().add(qAreaOfEff);
			down1.getChildren().add(cool);
			down1.getChildren().add(ma);
			down1.getChildren().add(new Label(""));
			
			down.getChildren().add(down1);
			
//			rightDown.getChildren().add(qN);
//			rightDown.getChildren().add(qT);
//			rightDown.getChildren().add(qAreaOfEff);
//			rightDown.getChildren().add(cool);
//			rightDown.getChildren().add(ma);
//			rightDown.getChildren().add(new Label(""));
		}
		
		
		//rightUpper.requestFocus();
		//left.getChildren().add(leftUpper);
		root.getChildren().add(rightUpper);
		root.getChildren().add(leftUpper);
		root.getChildren().add(leftMiddle);
		root.getChildren().add(rightMiddle);
		root.getChildren().add(leftDown);
		//root.getChildren().add(rightDown);
		root.getChildren().add(down);
		
	}*/

	public static void setupBoard() {
		
		GridPane yarab =new GridPane();
		
		yarab.setPadding(new Insets(10));
		yarab.setLayoutX(440);
		yarab.setHgap(10);
		yarab.setVgap(10);
		buttonLoc= new Button[5][5];
		for (int r = 0; r < 5; r++) {
	        for (int c = 0; c < 5; c++) {
	            Button button = new Button();
//	            button.setScaleX(5);
//	            button.setScaleY(5);
	            button.setFocusTraversable(false); 
	            button.setPrefSize(120, 120);
	            buttonLoc[r][c]=button;
	            yarab.add(button,c,r);
	            
	        }
	    }
		
		
		for(int i = 0; i < 5;i++) {
			for(int j = 0;j<5;j++) {
				//buttonLoc [i][j].setText("");
				buttonLoc[i][j].setGraphic(null);
				buttonLoc[i][j].setTooltip(null);
				//buttonLoc[i][j].setBackground(Background.fill(Color.BLACK));
			}
		}
		int count =4;
		Object [][] loc= game.getBoard();
		for(int i = 0; i < 5;i++) {
			for(int j = 0;j<5;j++) {
				if(loc[i][j] instanceof Champion) {
					Champion c = (Champion) loc[i][j];
					int index = champIndex(c);
					//buttonLoc[count][j].setText(c.getName());
					Image o = SelectChampions.getView().get(index);
					ImageView v = new ImageView(o);
					v.setFitWidth(80);
				    v.setFitHeight(100);
				    v.setPreserveRatio(true);
					buttonLoc[count][j].setGraphic(v);
					
				}
				else if(loc[i][j] instanceof Cover) {
					//buttonLoc[count][j].setText("cover:" + ((Cover) loc[i][j]).getLocation().x +","+((Cover) loc[i][j]).getLocation().y);
					Image img15 = new Image("views/assets/cover.png");
				    ImageView v15 = new ImageView(img15);
				    v15.setFitWidth(80);
				    v15.setFitHeight(100);
				    v15.setPreserveRatio(true);
				    
					buttonLoc[count][j].setGraphic(v15);
					
					Tooltip tt = new Tooltip("HP: " + ((Cover)loc[i][j]).getCurrentHP());
					buttonLoc[count][j].setTooltip(tt);
					//buttonLoc[count][j].setText("HP: "+((Cover)loc[i][j]).getCurrentHP());
					//buttonLoc[count][j].setTextAlignment(TextAlignment.CENTER);
				}
				
			}
			count--;
		}
		root.getChildren().add(yarab);
		yarab.requestFocus();
	}
	
	public static int champIndex(Champion c) {
		for(int i = 0; i< game.getAvailableChampions().size();i++) {
			if(c == game.getAvailableChampions().get(i)) {
				return i;
			}
		}
		return 0;
	}

	public static Button[][] getButtonLoc() {
		return buttonLoc;
	}

//	public static void setSingleFunc(Ability q) {
//		final Stage pop = new Stage();
//           // dialog.initModality(Modality.APPLICATION_MODAL);
//            //dialog.initOwner(primaryStage);
//        VBox v = new VBox(20);
//        v.getChildren().add(new Text("Please select the cell that you want to cast the ability upon"));
//        Scene dialogScene = new Scene(v, 300, 200);
//        pop.setScene(dialogScene);
//        pop.show();
//		//Button[][] buttonLoc = StartGame.getButtonLoc();
//		int count = 4;
//		for(int i = 0;i<5;i++) {
//			for(int j = 0;j<5;j++) {
//				//final int x=count;
//				//final int y=j;
//				buttonLoc[i][j].addEventFilter(MouseEvent.MOUSE_CLICKED, e1 -> {
//					try {
//						 
//						game.castAbility(q, count, j);
//					} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException
//							| CloneNotSupportedException e2) {
//						final Stage pop1 = new Stage();
//			               // dialog.initModality(Modality.APPLICATION_MODAL);
//			                //dialog.initOwner(primaryStage);
//			                VBox v1 = new VBox(20);
//			                v1.getChildren().add(new Text(e2.getMessage()));
//			                Scene dialogScene1 = new Scene(v, 300, 200);
//			                pop1.setScene(dialogScene1);
//			                pop1.show();
//					}
//				});
//			}
//			count--;
//		}
//		
//	}
	
	public static int getButtonLocationX(Button button) {
		int count = 4;
		for(int i = 0;i<buttonLoc.length;i++) {
			for(int j = 0;j<buttonLoc[i].length;j++) {
				if(buttonLoc[i][j] == button) {
					return count;
				}
			}
			count--;
		}
		return 0;
	}
	
	public static int getButtonLocationY(Button button) {
		//int count = 4;
		for(int i = 0;i<5;i++) {
			for(int j = 0;j<5;j++) {
				if(buttonLoc[i][j] == button) {
					return j;
				}
			}
			//count++;
		}
		return 0;
	}
	
	

}
