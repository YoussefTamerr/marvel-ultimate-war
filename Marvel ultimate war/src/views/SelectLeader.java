package views;

import java.util.ArrayList;

import engine.Game;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.world.Champion;

public class SelectLeader {
	private static int co = 0;

	public static Scene createSelectLeader(Game g2) {
		Group root = new Group();
		VBox left = new VBox();
		left.setLayoutX(10);
		left.setLayoutY(0);
		left.setPrefSize(700, 1000);
		Label l1 = new Label(g2.getFirstPlayer().getName()+" please select your leader");
		l1.setFont(Font.font("Verdana",25));
		left.getChildren().add(l1);
		
		HBox but = new HBox();
		but.setPrefSize(600, 200);
		
		ArrayList<Image> v = SelectChampions.getView();
		ArrayList<Button> bf = new ArrayList<Button>();
		for(int i = 0; i< g2.getFirstPlayer().getTeam().size();i++) {
			Button b = new Button();
			b.setPrefSize(690, 250);
			bf.add(b);
			left.getChildren().add(new Label(""));
			Champion c = g2.getFirstPlayer().getTeam().get(i);
			int x = Game.getAvailableChampions().indexOf(c);
			ImageView v13 = new ImageView(v.get(x));
		    v13.setFitWidth(100);
		    v13.setFitHeight(150);
		    v13.setPreserveRatio(true);
			b.setGraphic(v13);
			b.addEventFilter(MouseEvent.MOUSE_CLICKED, e1 -> {
				co++;
				g2.getFirstPlayer().setLeader(c);
				for(int j = 0; j<bf.size();j++) {
					bf.get(j).setDisable(true);
				}
				if(co == 2) {
					Control.onStartGame(g2);
				}
			});
			
			left.getChildren().add(b);	
		}
		
		
		//left.getChildren().add(but);
		
		VBox right = new VBox();
		right.setLayoutX(830);
		right.setLayoutY(0);
		right.setPrefSize(700, 1000);
		Label l2 = new Label(g2.getSecondPlayer().getName()+" please select your leader");
		l2.setFont(Font.font("Verdana",25));
		right.getChildren().add(l2);
		
		HBox but1 = new HBox();
		but1.setPrefSize(600, 200);
		
		//ArrayList<Image> v = SelectChampions.getView();
		ArrayList<Button> bs = new ArrayList<Button>();
		for(int i = 0; i< g2.getSecondPlayer().getTeam().size();i++) {
			Button b = new Button();
			b.setPrefSize(690, 250);
			bs.add(b);
			right.getChildren().add(new Label(""));
			Champion c = g2.getSecondPlayer().getTeam().get(i);
			int x = Game.getAvailableChampions().indexOf(c);
			ImageView v13 = new ImageView(v.get(x));
		    v13.setFitWidth(100);
		    v13.setFitHeight(150);
		    v13.setPreserveRatio(true);
			b.setGraphic(v13);
			b.addEventFilter(MouseEvent.MOUSE_CLICKED, e1 -> {
				co++;
				g2.getSecondPlayer().setLeader(c);
				for(int j = 0; j<bs.size();j++) {
					bs.get(j).setDisable(true);
				}
				if(co == 2) {
					Control.onStartGame(g2);
				}
			});
			
			right.getChildren().add(b);	
		}
		
		left.getChildren().add(but);
		right.getChildren().add(but1);
		
		root.getChildren().add(left);
		root.getChildren().add(right);
		
		return new Scene(root);
		
		
	}

}
