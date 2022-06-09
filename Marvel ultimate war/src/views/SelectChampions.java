package views;

import java.io.IOException;
import java.util.ArrayList;

import engine.Game;
import engine.Player;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.world.Champion;

public class SelectChampions {
	
	private static int buttonCounter = 0;
	private static String firstName;
	private static String secondName;
	private static ArrayList<Image> view;
	
	public static Scene createSelectChampions(String f, String s) throws IOException {
		
//		Group root = new Group();
//		Scene scene = new Scene(root,Color.ANTIQUEWHITE);
//		return scene;
		
		firstName = f;
		secondName = s;
		
		
		Game.loadAbilities("Abilities.csv");
		Game.loadChampions("Champions.csv");
		
		Player first = new Player(f);
		Player second = new Player(s);
		
		ArrayList<Button> buttons = new ArrayList<Button>();
		
		Group root = new Group();
		
		GridPane grid = new GridPane();
	    grid.setPadding(new Insets(10));
	    grid.setHgap(10);
	    grid.setVgap(10);
	    
	    VBox v = new VBox();
	    v.setPrefSize(500, 600);
	   // v.setBackground(Background.fill(Color.BISQUE));
	    //v.getChildren().add(l);
	    v.setLayoutX(1000);
	    v.setLayoutY(50);
	    
	    Label l = new Label();
	    l.setFont(Font.font("italic", 15));
	    l.setText(f+" please select your champions");
	    v.getChildren().add(l);
	    for (int r = 0; r < 3; r++) {
	        for (int c = 0; c < 5; c++) {
	            int number = 3 * r + c;
	            Button button = new Button();
//	            button.setScaleX(5);
//	            button.setScaleY(5);
	            button.setPrefSize(150, 200);
	            button.addEventFilter(MouseEvent.MOUSE_ENTERED, e->{
	            	int x = buttons.indexOf(button);
	            	Champion ch = Game.getAvailableChampions().get(x);
	            	/*if(buttonCounter == 0) {
	        			v.getChildren().add(new Label(firstName + " please select your leader"));
	        		}
	        		else if(buttonCounter == 1) {
	        			v.getChildren().add(new Label(secondName + " please select your leader"));
	        		}
	        		else if(buttonCounter % 2 == 0)
	        			v.getChildren().add(new Label(firstName + " please select your champions"));
	        		else {
	        			v.getChildren().add(new Label(secondName + " please select your champions"));
	        		}*/
	            	Label n = new Label("Name:"+ch.getName());
	            	n.setFont(Font.font("italic", 15));
	            	v.getChildren().add(n);
	            	
	            	Label n1 = new Label("Damage:" + ch.getAttackDamage());
	            	n1.setFont(Font.font("italic", 15));
	            	v.getChildren().add(n1);
	            	
	            	Label n2 = new Label("Attack Range:" + ch.getAttackRange());
	            	n2.setFont(Font.font("italic", 15));
	            	v.getChildren().add(n2);
	            	
	            	Label n3 = new Label("HP:" + ch.getMaxHP());
	            	n3.setFont(Font.font("italic", 15));
	            	v.getChildren().add(n3);
	            	
	            	Label n4 = new Label("Mana:" + ch.getMana());
	            	n4.setFont(Font.font("italic", 15));
	            	v.getChildren().add(n4);
	            	
	            	Label n5 = new Label("Speed:" + ch.getSpeed());
	            	n5.setFont(Font.font("italic", 15));
	            	v.getChildren().add(n5);
	            	
	            	Label n6 = new Label("Action points per turn:" + ch.getMaxActionPointsPerTurn());
	            	n6.setFont(Font.font("italic", 15));
	            	v.getChildren().add(n6);
	            	
	            	Label n7 = new Label("Abilities:"+ ch.getAbilities().get(0).getName()+", "+ch.getAbilities().get(1).getName()+", "+ch.getAbilities().get(2).getName());
	            	n7.setFont(Font.font("italic", 15));
	            	v.getChildren().add(n7);
	            	
	            	
	            	
	            	
	            	//"\nAbilities:"+curr.getAbilities().get(0).getName()+", "+curr.getAbilities().get(1).getName()+", "+curr.getAbilities().get(2).getName());
	            });
	            button.addEventFilter(MouseEvent.MOUSE_EXITED, e->{
	            	v.getChildren().clear();
	            	if(buttonCounter == 0) {
	            		Label temp = new Label();
	            	    temp.setFont(Font.font("italic", 15));
	            	    temp.setText(f+" please select your champions");
	            	    v.getChildren().add(temp);
	            	}
	            	else if(buttonCounter == 1) {
	            		Label temp = new Label();
	            	    temp.setFont(Font.font("italic", 15));
	            	    temp.setText(s+" please select your champions");
	            	    v.getChildren().add(temp);
	            	}
	            	else if(buttonCounter %2 == 0) {
	            		Label temp = new Label();
	            	    temp.setFont(Font.font("italic", 15));
	            	    temp.setText(f+" please select your champions");
	            	    v.getChildren().add(temp);
	            	} else if(buttonCounter %2 != 0) {
	            		Label temp = new Label();
	            	    temp.setFont(Font.font("italic", 15));
	            	    temp.setText(s+" please select your champions");
	            	    v.getChildren().add(temp);
	            	}
	            });
	            button.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
	            	buttonCounter++;
	            	int x = buttons.indexOf(button);
	            	Champion ch = Game.getAvailableChampions().get(x);
	            	if(buttonCounter == 1) {
	            		//first.setLeader(ch);
	            		first.getTeam().add(ch);
	            		button.setDisable(true);
	            		
	            	}
	            	else if(buttonCounter == 2) {
	            		//second.setLeader(ch);
	            		second.getTeam().add(ch);
	            		button.setDisable(true);
	            	}
	            	else if(buttonCounter %2 != 0) {
	            		first.getTeam().add(ch);
	            		button.setDisable(true);
	            	}
	            	else if(buttonCounter %2 == 0) {
	            		second.getTeam().add(ch);
	            		button.setDisable(true);
	            	}
	            	
	            	if(buttonCounter == 6) {
	            		try {
							Game g = new Game(first,second);
							Control.onSelectLeader(g);
		            		//Control.onStartGame(g);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

	            	}
	            });
	            buttons.add(button);
	            grid.add(button, c, r);
	            
	        }
	    }
	    view = new ArrayList<Image>();
	    Image img1 = new Image("views/assets/captain america.png");
//	    //ImageView v1 = new ImageView(img1);
//	    v1.setFitWidth(100);
//	    v1.setFitHeight(150);
//	    v1.setPreserveRatio(true);
	    view.add(img1);
	    
	    Image img2 = new Image("views/assets/deadpool.png");
	    ImageView v2 = new ImageView(img2);
	    v2.setFitWidth(100);
	    v2.setFitHeight(150);
	    v2.setPreserveRatio(true);
	    view.add(img2);
	    
	    Image img3 = new Image("views/assets/dr strange.png");
	    ImageView v3 = new ImageView(img3);
	    v3.setFitWidth(100);
	    v3.setFitHeight(150);
	    v3.setPreserveRatio(true);
	    view.add(img3);
	    
	    Image img4 = new Image("views/assets/electro.png");
	    ImageView v4 = new ImageView(img4);
	    v4.setFitWidth(100);
	    v4.setFitHeight(150);
	    v4.setPreserveRatio(true);
	    view.add(img4);
	    
	    Image img5 = new Image("views/assets/ghost rider.png");
	    ImageView v5 = new ImageView(img5);
	    v5.setFitWidth(100);
	    v5.setFitHeight(150);
	    v5.setPreserveRatio(true);
	    view.add(img5);
	    
	    Image img6 = new Image("views/assets/hela.png");
	    ImageView v6 = new ImageView(img6);
	    v6.setFitWidth(100);
	    v6.setFitHeight(150);
	    v6.setPreserveRatio(true);
	    view.add(img6);
	    
	    Image img7 = new Image("views/assets/hulk.png");
	    ImageView v7 = new ImageView(img7);
	    v7.setFitWidth(100);
	    v7.setFitHeight(150);
	    v7.setPreserveRatio(true);
	    view.add(img7);
	    
	    Image img8 = new Image("views/assets/iceman.png");
	    ImageView v8 = new ImageView(img8);
	    v8.setFitWidth(100);
	    v8.setFitHeight(150);
	    v8.setPreserveRatio(true);
	    view.add(img8);
	    
	    Image img9 = new Image("views/assets/ironman.png");
	    ImageView v9 = new ImageView(img9);
	    v9.setFitWidth(100);
	    v9.setFitHeight(150);
	    v9.setPreserveRatio(true);
	    view.add(img9);
	    
	    Image img10 = new Image("views/assets/loki.png");
	    ImageView v10 = new ImageView(img10);
	    v10.setFitWidth(100);
	    v10.setFitHeight(150);
	    v10.setPreserveRatio(true);
	    view.add(img10);
	    
	    Image img11 = new Image("views/assets/quicksilver.png");
	    ImageView v11 = new ImageView(img11);
	    v11.setFitWidth(100);
	    v11.setFitHeight(150);
	    v11.setPreserveRatio(true);
	    view.add(img11);
	    
	    
	    Image img12 = new Image("views/assets/spiderman.png");
	    ImageView v12 = new ImageView(img12);
	    v12.setFitWidth(100);
	    v12.setFitHeight(150);
	    v12.setPreserveRatio(true);
	    view.add(img12);
	    
	    Image img13 = new Image("views/assets/thor.png");
	    ImageView v13 = new ImageView(img13);
	    v13.setFitWidth(100);
	    v13.setFitHeight(150);
	    v13.setPreserveRatio(true);
	    view.add(img13);
	    
	    Image img14 = new Image("views/assets/venom.png");
	    ImageView v14 = new ImageView(img14);
	    v14.setFitWidth(100);
	    v14.setFitHeight(150);
	    v14.setPreserveRatio(true);
	    view.add(img14);
	    
	    Image img15 = new Image("views/assets/yellow jacket.png");
	    ImageView v15 = new ImageView(img15);
	    v15.setFitWidth(100);
	    v15.setFitHeight(150);
	    v15.setPreserveRatio(true);
	    view.add(img15);
	    
	    
	    for(int i = 0; i<Game.getAvailableChampions().size();i++) {
	    	
	    	//buttons.get(i).setText(Game.getAvailableChampions().get(i).getName());
	    	ImageView temp = new ImageView(view.get(i));
	    	temp.setFitWidth(100);
		    temp.setFitHeight(150);
		    temp.setPreserveRatio(true);
	    	buttons.get(i).setGraphic(temp);
	    }
	    
	    
	    
	    
	    

	    root.getChildren().add(grid);
	    root.getChildren().add(v);
	    

	    return new Scene(root);
		
	}

	public static ArrayList<Image> getView() {
		return view;
	}
	
	
	
	/*public void resetTextSelection() {
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
	}*/
	
	

}
