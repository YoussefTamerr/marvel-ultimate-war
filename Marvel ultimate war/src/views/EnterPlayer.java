package views;

import java.io.IOException;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.PopupWindow;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class EnterPlayer {
	
	public static Scene createEnterPlayer() {
		
		Pane root = new Pane();
		
		
        Image img = new Image("views/assets/baa.jpg");
        BackgroundImage bImg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bGround = new Background(bImg);
        root.setBackground(bGround);

		
        Image image = new Image("views/assets/bbTitle.png");
		ImageView imageView = new ImageView(image);
		imageView.setX(480);
		imageView.setY(0);
		

		
		
		
		
		TextField first = new TextField();
		//first.setFocusTraversable(false);
		first.setScaleX(1.2);
		first.setScaleY(1.2);
		first.setPromptText("First Player Name");
		first.setLayoutX(680);
		first.setLayoutY(400);
		
		TextField second = new TextField();
		//second.setFocusTraversable(false);
		second.setScaleX(1.2);
		second.setScaleY(1.2);
		second.setPromptText("Second Player Name");
		second.setLayoutX(680);
		second.setLayoutY(450);
		
		Button b = new Button("Start Game");
		b.setLayoutX(720);
		b.setLayoutY(500);
		//b.setFocusTraversable(false);
		b.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
			String f = first.getText();
			String s = second.getText();
			if(f.equals("") || s.equals("")) {
				final Stage pop = new Stage();
               // dialog.initModality(Modality.APPLICATION_MODAL);
                //dialog.initOwner(primaryStage);
                VBox v = new VBox(20);
                v.getChildren().add(new Text("The name field was blank"));
                Scene dialogScene = new Scene(v, 300, 200);
                pop.setScene(dialogScene);
                pop.show();
				
			}
			else {
				try {
					Control.onSelectChampions(f,s);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
		Text text = new Text();
		text.setText("hi");
		text.setX(50);
		text.setY(50);
		text.setFont(Font.font("Verdana",50));
		text.setFill(Color.LIMEGREEN);
		
		
		Line line = new Line();
		line.setStartX(200);
		line.setStartY(200);
		line.setEndX(500);
		line.setEndY(500);
		
		

		
		//root.getChildren().add(text);
		//root.getChildren().add(line);
		root.getChildren().add(first);
		root.getChildren().add(second);
		root.getChildren().add(imageView);
		root.getChildren().add(b);
		//root.getChildren().add(dis);
		
		Scene scene = new Scene(root, Color.rgb(24, 25, 26));
		
		root.requestFocus();
		
		return scene;
	}

}
