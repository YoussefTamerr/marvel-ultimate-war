package views;

import java.io.IOException;

import engine.Game;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.world.Direction;

public class Control extends Application {
	
	private static Scene scene;
	private static Stage stage = new Stage();
	private static Game g;
	
	private static int countX = 0;
	private static int countY = 0;
	private static int countXboard = 4;
	private static boolean oneTime;
	
	public static Scene endGame(int x) {
		Group root = new Group();
		Image image = new Image("views/assets/thanos.gif");
		ImageView imageView = new ImageView(image);
		imageView.setX(520);
		imageView.setY(250);
		if(x == 0) {
			Text text = new Text();
			text.setText(g.getFirstPlayer().getName()+" wins!!!!");
			text.setX(50);
			text.setY(50);
			text.setFont(Font.font("Verdana",50));
			text.setFill(Color.BLACK);
			text.setX(620);
			text.setY(100);
			root.getChildren().add(text);
		}
		else {
			Text text = new Text();
			text.setText(g.getSecondPlayer().getName()+" wins!!!!");
			text.setX(50);
			text.setY(50);
			text.setFont(Font.font("Verdana",50));
			text.setFill(Color.BLACK);
			text.setX(620);
			text.setY(100);
			root.getChildren().add(text);
		}
		
		root.getChildren().add(imageView);
		
		return new Scene(root);
	}
	
	
	public static void onEnterPlayer() {
		scene = EnterPlayer.createEnterPlayer();
		stage.setScene(scene);
	}
	
	public static void onSelectChampions(String f, String s) throws IOException {
		scene = SelectChampions.createSelectChampions(f, s);
		stage.setScene(scene);
	}
	
	public static void onStartGame(Game game) {
		g = game;
		scene = StartGame.createStartGame(g);
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent arg0) {
				switch (arg0.getCode()) {
				case W:
					try {
						g.move(Direction.UP);
						
					} catch (NotEnoughResourcesException | UnallowedMovementException e) {
						final Popup pop = new Popup();
			               // dialog.initModality(Modality.APPLICATION_MODAL);
			                //dialog.initOwner(primaryStage);
			                VBox v = new VBox(20);
			                Text m = new Text(e.getMessage());
			                m.setFill(Color.WHITE);
			                m.setFont(Font.font("italic", 15));
			                v.getChildren().add(m);
			                v.setPrefSize(450, 300);
			                v.setBackground(Background.fill(Color.BLACK));
			                Scene dialogScene = new Scene(v, 300, 200);
			                pop.getContent().add(v);
			                pop.setAutoHide(true);
			                pop.show(stage);  
					}
					StartGame.setupText();
					StartGame.setupBoard();
					
					if(g.checkGameOver() == g.getFirstPlayer()) {
						scene = endGame(0);
						stage.setScene(scene);
					}
					else if(g.checkGameOver() == g.getSecondPlayer()){
						scene = endGame(1);
						stage.setScene(scene);
					}
					
					break;
				case S:
					try {
						g.move(Direction.DOWN);
						
					} catch (NotEnoughResourcesException | UnallowedMovementException e) {
							final Popup pop = new Popup();
			               // dialog.initModality(Modality.APPLICATION_MODAL);
			                //dialog.initOwner(primaryStage);
			                VBox v = new VBox(20);
			                Text m = new Text(e.getMessage());
			                m.setFill(Color.WHITE);
			                m.setFont(Font.font("italic", 15));
			                v.getChildren().add(m);
			                v.setPrefSize(450, 300);
			                v.setBackground(Background.fill(Color.BLACK));
			                Scene dialogScene = new Scene(v, 300, 200);
			                pop.getContent().add(v);
			                pop.setAutoHide(true);
			                pop.show(stage); 
					}
					StartGame.setupText();
					StartGame.setupBoard();
					
					if(g.checkGameOver() == g.getFirstPlayer()) {
						scene = endGame(0);
						stage.setScene(scene);
					}
					else if(g.checkGameOver() == g.getSecondPlayer()){
						scene = endGame(1);
						stage.setScene(scene);
					}
					
					break;
				case A:
					try {
						g.move(Direction.LEFT);
						
					} catch (NotEnoughResourcesException | UnallowedMovementException e) {
						final Popup pop = new Popup();
			               // dialog.initModality(Modality.APPLICATION_MODAL);
			                //dialog.initOwner(primaryStage);
			                VBox v = new VBox(20);
			                Text m = new Text(e.getMessage());
			                m.setFill(Color.WHITE);
			                m.setFont(Font.font("italic", 15));
			                v.getChildren().add(m);
			                v.setPrefSize(450, 300);
			                v.setBackground(Background.fill(Color.BLACK));
			                Scene dialogScene = new Scene(v, 300, 200);
			                pop.getContent().add(v);
			                pop.setAutoHide(true);
			                pop.show(stage); 
					}
					StartGame.setupText();
					StartGame.setupBoard();
					
					if(g.checkGameOver() == g.getFirstPlayer()) {
						scene = endGame(0);
						stage.setScene(scene);
					}
					else if(g.checkGameOver() == g.getSecondPlayer()){
						scene = endGame(1);
						stage.setScene(scene);
					}
					
					break;
				case D:
					try {
						g.move(Direction.RIGHT);
						
					} catch (NotEnoughResourcesException | UnallowedMovementException e) {
						final Popup pop = new Popup();
			               // dialog.initModality(Modality.APPLICATION_MODAL);
			                //dialog.initOwner(primaryStage);
			                VBox v = new VBox(20);
			                Text m = new Text(e.getMessage());
			                m.setFill(Color.WHITE);
			                m.setFont(Font.font("italic", 15));
			                v.getChildren().add(m);
			                v.setPrefSize(450, 300);
			                v.setBackground(Background.fill(Color.BLACK));
			                Scene dialogScene = new Scene(v, 300, 200);
			                pop.getContent().add(v);
			                pop.setAutoHide(true);
			                pop.show(stage); 
					}
					StartGame.setupText();
					StartGame.setupBoard();
					
					if(g.checkGameOver() == g.getFirstPlayer()) {
						scene = endGame(0);
						stage.setScene(scene);
					}
					else if(g.checkGameOver() == g.getSecondPlayer()){
						scene = endGame(1);
						stage.setScene(scene);
					}
					
					break;
				case T:
					g.endTurn();
					StartGame.setupText();
					StartGame.setupBoard();
					
					if(g.checkGameOver() == g.getFirstPlayer()) {
						scene = endGame(0);
						stage.setScene(scene);
					}
					else if(g.checkGameOver() == g.getSecondPlayer()){
						scene = endGame(1);
						stage.setScene(scene);
					}
					
					break;
				case L:
					try {
						g.useLeaderAbility();
						
					} catch (LeaderNotCurrentException | LeaderAbilityAlreadyUsedException e2) {
						final Popup pop = new Popup();
			               // dialog.initModality(Modality.APPLICATION_MODAL);
			                //dialog.initOwner(primaryStage);
			                VBox v = new VBox(20);
			                Text m = new Text(e2.getMessage());
			                m.setFill(Color.WHITE);
			                m.setFont(Font.font("italic", 15));
			                v.getChildren().add(m);
			                v.setPrefSize(450, 300);
			                v.setBackground(Background.fill(Color.BLACK));
			                Scene dialogScene = new Scene(v, 300, 200);
			                pop.getContent().add(v);
			                pop.setAutoHide(true);
			                pop.show(stage); 
					}
					StartGame.setupText();
					StartGame.setupBoard();
					
					if(g.checkGameOver() == g.getFirstPlayer()) {
						scene = endGame(0);
						stage.setScene(scene);
					}
					else if(g.checkGameOver() == g.getSecondPlayer()){
						scene = endGame(1);
						stage.setScene(scene);
					}
					
					break;
				case UP:
					try {
						g.attack(Direction.UP);
						
					} catch (Exception e1) {
						final Popup pop = new Popup();
			               // dialog.initModality(Modality.APPLICATION_MODAL);
			                //dialog.initOwner(primaryStage);
			                VBox v = new VBox(20);
			                Text m = new Text(e1.getMessage());
			                m.setFill(Color.WHITE);
			                m.setFont(Font.font("italic", 15));
			                v.getChildren().add(m);
			                v.setPrefSize(450, 300);
			                v.setBackground(Background.fill(Color.BLACK));
			                Scene dialogScene = new Scene(v, 300, 200);
			                pop.getContent().add(v);
			                pop.setAutoHide(true);
			                pop.show(stage);  
					}
					StartGame.setupText();
					StartGame.setupBoard();
					
					if(g.checkGameOver() == g.getFirstPlayer()) {
						scene = endGame(0);
						stage.setScene(scene);
					}
					else if(g.checkGameOver() == g.getSecondPlayer()){
						scene = endGame(1);
						stage.setScene(scene);
					}
					
					break;
				case LEFT:
					try {
						g.attack(Direction.LEFT);

					} catch (Exception e1) {
						final Popup pop = new Popup();
			               // dialog.initModality(Modality.APPLICATION_MODAL);
			                //dialog.initOwner(primaryStage);
			                VBox v = new VBox(20);
			                Text m = new Text(e1.getMessage());
			                m.setFill(Color.WHITE);
			                m.setFont(Font.font("italic", 15));
			                v.getChildren().add(m);
			                v.setPrefSize(450, 300);
			                v.setBackground(Background.fill(Color.BLACK));
			                Scene dialogScene = new Scene(v, 300, 200);
			                pop.getContent().add(v);
			                pop.setAutoHide(true);
			                pop.show(stage); 
					}
					StartGame.setupText();
					StartGame.setupBoard();
					
					if(g.checkGameOver() == g.getFirstPlayer()) {
						scene = endGame(0);
						stage.setScene(scene);
					}
					else if(g.checkGameOver() == g.getSecondPlayer()){
						scene = endGame(1);
						stage.setScene(scene);
					}
					
					break;
				case DOWN:
					try {
						g.attack(Direction.DOWN);
						
					} catch (Exception e1) {
						final Popup pop = new Popup();
			               // dialog.initModality(Modality.APPLICATION_MODAL);
			                //dialog.initOwner(primaryStage);
			                VBox v = new VBox(20);
			                Text m = new Text(e1.getMessage());
			                m.setFill(Color.WHITE);
			                m.setFont(Font.font("italic", 15));
			                v.getChildren().add(m);
			                v.setPrefSize(450, 300);
			                v.setBackground(Background.fill(Color.BLACK));
			                Scene dialogScene = new Scene(v, 300, 200);
			                pop.getContent().add(v);
			                pop.setAutoHide(true);
			                pop.show(stage);  
					}
					StartGame.setupText();
					StartGame.setupBoard();
					
					if(g.checkGameOver() == g.getFirstPlayer()) {
						scene = endGame(0);
						stage.setScene(scene);
					}
					else if(g.checkGameOver() == g.getSecondPlayer()){
						scene = endGame(1);
						stage.setScene(scene);
					}
					
					break;
				case RIGHT:
					try {
						g.attack(Direction.RIGHT);
						
					} catch (Exception e1) {
						final Popup pop = new Popup();
			               // dialog.initModality(Modality.APPLICATION_MODAL);
			                //dialog.initOwner(primaryStage);
			                VBox v = new VBox(20);
			                Text m = new Text(e1.getMessage());
			                m.setFill(Color.WHITE);
			                m.setFont(Font.font("italic", 15));
			                v.getChildren().add(m);
			                v.setPrefSize(450, 300);
			                v.setBackground(Background.fill(Color.BLACK));
			                Scene dialogScene = new Scene(v, 300, 200);
			                pop.getContent().add(v);
			                pop.setAutoHide(true);
			                pop.show(stage); 
					}
					StartGame.setupText();
					StartGame.setupBoard();
					
					if(g.checkGameOver() == g.getFirstPlayer()) {
						scene = endGame(0);
						stage.setScene(scene);
					}
					else if(g.checkGameOver() == g.getSecondPlayer()){
						scene = endGame(1);
						stage.setScene(scene);
					}
					
					break;
				/*case E:
					scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

						@Override
						public void handle(KeyEvent e) {
							switch(e.getCode()) {
							case W:
								try {
									g.attack(Direction.UP);
								} catch (NotEnoughResourcesException | ChampionDisarmedException
										| InvalidTargetException e1) {
									final Stage pop = new Stage();
						               // dialog.initModality(Modality.APPLICATION_MODAL);
						                //dialog.initOwner(primaryStage);
						                VBox v = new VBox(20);
						                v.getChildren().add(new Text(e1.getMessage()));
						                Scene dialogScene = new Scene(v, 300, 200);
						                pop.setScene(dialogScene);
						                pop.show(); 
								}
								StartGame.setupBoard();
								break;
							case A:
								try {
									g.attack(Direction.LEFT);
								} catch (NotEnoughResourcesException | ChampionDisarmedException
										| InvalidTargetException e1) {
									final Stage pop = new Stage();
						               // dialog.initModality(Modality.APPLICATION_MODAL);
						                //dialog.initOwner(primaryStage);
						                VBox v = new VBox(20);
						                v.getChildren().add(new Text(e1.getMessage()));
						                Scene dialogScene = new Scene(v, 300, 200);
						                pop.setScene(dialogScene);
						                pop.show(); 
								}
								StartGame.setupBoard();
								break;
							case S:
								try {
									g.attack(Direction.DOWN);
								} catch (NotEnoughResourcesException | ChampionDisarmedException
										| InvalidTargetException e1) {
									final Stage pop = new Stage();
						               // dialog.initModality(Modality.APPLICATION_MODAL);
						                //dialog.initOwner(primaryStage);
						                VBox v = new VBox(20);
						                v.getChildren().add(new Text(e1.getMessage()));
						                Scene dialogScene = new Scene(v, 300, 200);
						                pop.setScene(dialogScene);
						                pop.show(); 
								}
								StartGame.setupBoard();
								break;
							case D:
								try {
									g.attack(Direction.RIGHT);
								} catch (NotEnoughResourcesException | ChampionDisarmedException
										| InvalidTargetException e1) {
									final Stage pop = new Stage();
						               // dialog.initModality(Modality.APPLICATION_MODAL);
						                //dialog.initOwner(primaryStage);
						                VBox v = new VBox(20);
						                v.getChildren().add(new Text(e1.getMessage()));
						                Scene dialogScene = new Scene(v, 300, 200);
						                pop.setScene(dialogScene);
						                pop.show(); 
								}
								StartGame.setupBoard();
								break;
							default:
								break;
							
							}
							
						}});
					break;*/
				default:
					break;
					
				}
					
				
			}
			
		});
		stage.setScene(scene);
	}

	@Override
	public void start(Stage gg) throws Exception {

		Image icon = new Image("views/assets/marvel.png");
		stage.getIcons().add(icon);
		stage.setTitle("Marvel Ultimate War");

		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		stage.setX(bounds.getMinX());
		stage.setY(bounds.getMinY());
		stage.setWidth(bounds.getWidth());
		stage.setHeight(bounds.getHeight());
		
		stage.setResizable(false);
		onEnterPlayer();
		stage.setMaximized(true);
		//stage.setScene(scene);
		stage.show();
		
	}
	public static void main(String[] args) {
		launch(args);
	}

	public static void executeAbility(Button qN, Ability q, Game game) {
		g = game;
		qN.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			if (q.getCastArea() == AreaOfEffect.SURROUND || q.getCastArea() == AreaOfEffect.TEAMTARGET
					|| q.getCastArea() == AreaOfEffect.SELFTARGET) {
				try {
					game.castAbility(q);
					
					
				} catch (NotEnoughResourcesException | AbilityUseException | CloneNotSupportedException e1) {
					final Popup pop = new Popup();
		               // dialog.initModality(Modality.APPLICATION_MODAL);
		                //dialog.initOwner(primaryStage);
		                VBox v = new VBox(20);
		                Text m = new Text(e1.getMessage());
		                m.setFill(Color.WHITE);
		                m.setFont(Font.font("italic", 15));
		                v.getChildren().add(m);
		                v.setPrefSize(450, 300);
		                v.setBackground(Background.fill(Color.BLACK));
		                Scene dialogScene = new Scene(v, 300, 200);
		                pop.getContent().add(v);
		                pop.setAutoHide(true);
		                pop.show(stage); 
				}
				
				StartGame.setupText();
				StartGame.setupBoard();
				
				if(g.checkGameOver() == g.getFirstPlayer()) {
					scene = endGame(0);
					stage.setScene(scene);
				}
				else if(g.checkGameOver() == g.getSecondPlayer()){
					scene = endGame(1);
					stage.setScene(scene);
				}
				
			}
			else if(q.getCastArea() == AreaOfEffect.DIRECTIONAL) {
				final Stage popp = new Stage();
	               // dialog.initModality(Modality.APPLICATION_MODAL);
	                //dialog.initOwner(primaryStage);
	                VBox vv = new VBox(20);
	                final ComboBox<String> DirectionComboBox = new ComboBox<String>();
	                DirectionComboBox.setPromptText("Directions");
	                DirectionComboBox.getItems().addAll(
	                    "UP",
	                    "RIGHT",
	                    "LEFT",
	                    "DOWN"  
	                );
	                
	                vv.getChildren().add(new Text("Please choose a direction "));
	                vv.getChildren().add(DirectionComboBox);
	                DirectionComboBox.setOnAction((event) -> {
	                	int selectedIndex = DirectionComboBox.getSelectionModel().getSelectedIndex();
	                	switch (selectedIndex) {
						case 0:
							try {
								game.castAbility(q, Direction.UP);
							} catch (Exception e1) {
								final Popup pop = new Popup();
					               // dialog.initModality(Modality.APPLICATION_MODAL);
					                //dialog.initOwner(primaryStage);
					                VBox v = new VBox(20);
					                Text m = new Text(e1.getMessage());
					                m.setFill(Color.WHITE);
					                m.setFont(Font.font("italic", 15));
					                v.getChildren().add(m);
					                v.setPrefSize(450, 300);
					                v.setBackground(Background.fill(Color.BLACK));
					                Scene dialogScene = new Scene(v, 300, 200);
					                pop.getContent().add(v);
					                pop.setAutoHide(true);
					                pop.show(stage); 
							}
							
							StartGame.setupText();
							StartGame.setupBoard();
							
							popp.close();
							
							
							if(g.checkGameOver() == g.getFirstPlayer()) {
								scene = endGame(0);
								stage.setScene(scene);
							}
							else if(g.checkGameOver() == g.getSecondPlayer()){
								scene = endGame(1);
								stage.setScene(scene);
							}
							
							break;
						case 2:
							try {
								game.castAbility(q, Direction.LEFT);
							} catch (Exception e1) {
								final Popup pop = new Popup();
					               // dialog.initModality(Modality.APPLICATION_MODAL);
					                //dialog.initOwner(primaryStage);
					                VBox v = new VBox(20);
					                Text m = new Text(e1.getMessage());
					                m.setFill(Color.WHITE);
					                m.setFont(Font.font("italic", 15));
					                v.getChildren().add(m);
					                v.setPrefSize(450, 300);
					                v.setBackground(Background.fill(Color.BLACK));
					                Scene dialogScene = new Scene(v, 300, 200);
					                pop.getContent().add(v);
					                pop.setAutoHide(true);
					                pop.show(stage); 
							}
							StartGame.setupText();
							StartGame.setupBoard();
							
							popp.close();
							
							
							if(g.checkGameOver() == g.getFirstPlayer()) {
								scene = endGame(0);
								stage.setScene(scene);
							}
							else if(g.checkGameOver() == g.getSecondPlayer()){
								scene = endGame(1);
								stage.setScene(scene);
							}
							
							break;
						case 3:
							try {
								game.castAbility(q, Direction.DOWN);
							} catch (Exception e1) {
								final Popup pop = new Popup();
					               // dialog.initModality(Modality.APPLICATION_MODAL);
					                //dialog.initOwner(primaryStage);
					                VBox v = new VBox(20);
					                Text m = new Text(e1.getMessage());
					                m.setFill(Color.WHITE);
					                m.setFont(Font.font("italic", 15));
					                v.getChildren().add(m);
					                v.setPrefSize(450, 300);
					                v.setBackground(Background.fill(Color.BLACK));
					                Scene dialogScene = new Scene(v, 300, 200);
					                pop.getContent().add(v);
					                pop.setAutoHide(true);
					                pop.show(stage); 
							}
							StartGame.setupText();
							StartGame.setupBoard();
							
							popp.close();
							
							
							if(g.checkGameOver() == g.getFirstPlayer()) {
								scene = endGame(0);
								stage.setScene(scene);
							}
							else if(g.checkGameOver() == g.getSecondPlayer()){
								scene = endGame(1);
								stage.setScene(scene);
							}
							
							break;
						case 1:
							try {
								game.castAbility(q, Direction.RIGHT);
							} catch (Exception e1) {
								final Popup pop = new Popup();
					               // dialog.initModality(Modality.APPLICATION_MODAL);
					                //dialog.initOwner(primaryStage);
					                VBox v = new VBox(20);
					                Text m = new Text(e1.getMessage());
					                m.setFill(Color.WHITE);
					                m.setFont(Font.font("italic", 15));
					                v.getChildren().add(m);
					                v.setPrefSize(450, 300);
					                v.setBackground(Background.fill(Color.BLACK));
					                Scene dialogScene = new Scene(v, 300, 200);
					                pop.getContent().add(v);
					                pop.setAutoHide(true);
					                pop.show(stage); 
							}
							StartGame.setupText();
							StartGame.setupBoard();
							
							popp.close();
							
							
							if(g.checkGameOver() == g.getFirstPlayer()) {
								scene = endGame(0);
								stage.setScene(scene);
							}
							else if(g.checkGameOver() == g.getSecondPlayer()){
								scene = endGame(1);
								stage.setScene(scene);
							}
							
							break;
						default:
							break;
						}

	                });
	                    
	                /*scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

						@Override
						public void handle(KeyEvent arg0) {
							switch (arg0.getCode()) {
							case W:
								try {
									game.castAbility(q, Direction.UP);
								} catch (Exception e) {
									final Stage pop = new Stage();
						               // dialog.initModality(Modality.APPLICATION_MODAL);
						                //dialog.initOwner(primaryStage);
						                VBox v = new VBox(20);
						                v.getChildren().add(new Text(e.getMessage()));
						                Scene dialogScene = new Scene(v, 300, 200);
						                pop.setScene(dialogScene);
						                pop.show();
								}
								
								StartGame.setupText();
								StartGame.setupBoard();
								
								
								if(g.checkGameOver() == g.getFirstPlayer()) {
									scene = endGame(0);
									stage.setScene(scene);
								}
								else if(g.checkGameOver() == g.getSecondPlayer()){
									scene = endGame(1);
									stage.setScene(scene);
								}
								
								break;
							case A:
								try {
									game.castAbility(q, Direction.LEFT);
								} catch (Exception e) {
									final Stage pop = new Stage();
						               // dialog.initModality(Modality.APPLICATION_MODAL);
						                //dialog.initOwner(primaryStage);
						                VBox v = new VBox(20);
						                v.getChildren().add(new Text(e.getMessage()));
						                Scene dialogScene = new Scene(v, 300, 200);
						                pop.setScene(dialogScene);
						                pop.show();
								}
								StartGame.setupText();
								StartGame.setupBoard();
								
								
								if(g.checkGameOver() == g.getFirstPlayer()) {
									scene = endGame(0);
									stage.setScene(scene);
								}
								else if(g.checkGameOver() == g.getSecondPlayer()){
									scene = endGame(1);
									stage.setScene(scene);
								}
								
								break;
							case S:
								try {
									game.castAbility(q, Direction.DOWN);
								} catch (Exception e) {
									final Stage pop = new Stage();
						               // dialog.initModality(Modality.APPLICATION_MODAL);
						                //dialog.initOwner(primaryStage);
						                VBox v = new VBox(20);
						                v.getChildren().add(new Text(e.getMessage()));
						                Scene dialogScene = new Scene(v, 300, 200);
						                pop.setScene(dialogScene);
						                pop.show();
								}
								StartGame.setupText();
								StartGame.setupBoard();
								
								
								if(g.checkGameOver() == g.getFirstPlayer()) {
									scene = endGame(0);
									stage.setScene(scene);
								}
								else if(g.checkGameOver() == g.getSecondPlayer()){
									scene = endGame(1);
									stage.setScene(scene);
								}
								
								break;
							case D:
								try {
									game.castAbility(q, Direction.RIGHT);
								} catch (Exception e) {
									final Stage pop = new Stage();
						               // dialog.initModality(Modality.APPLICATION_MODAL);
						                //dialog.initOwner(primaryStage);
						                VBox v = new VBox(20);
						                v.getChildren().add(new Text(e.getMessage()));
						                Scene dialogScene = new Scene(v, 300, 200);
						                pop.setScene(dialogScene);
						                pop.show();
								}
								StartGame.setupText();
								StartGame.setupBoard();
								
								
								if(g.checkGameOver() == g.getFirstPlayer()) {
									scene = endGame(0);
									stage.setScene(scene);
								}
								else if(g.checkGameOver() == g.getSecondPlayer()){
									scene = endGame(1);
									stage.setScene(scene);
								}
								
								break;
							default:
								break;
							}
							
						}
	                	
	                });*/
	                Scene dialogScene = new Scene(vv, 300, 200);
	                popp.setScene(dialogScene);
	                popp.show();
			}
			
			else if(q.getCastArea() == AreaOfEffect.SINGLETARGET) {
				//StartGame.setSingleFunc(q);
				final Stage pop = new Stage();
	               // dialog.initModality(Modality.APPLICATION_MODAL);
	                //dialog.initOwner(primaryStage);
	            VBox v = new VBox(20);
	            v.getChildren().add(new Text("Please select the cell \nthat you want to cast the ability upon"));
	            Scene dialogScene = new Scene(v, 300, 200);
                pop.setScene(dialogScene);
                pop.show();
				Button[][] buttonLoc = StartGame.getButtonLoc();
				//int count = 4;
				oneTime = false;
				for(int i = 0;i<5;i++) {
					if(oneTime)
						break;
					for(int j = 0;j<5;j++) {
						final int x=StartGame.getButtonLocationX(buttonLoc[i][j]);
						final int y=StartGame.getButtonLocationY(buttonLoc[i][j]);
						buttonLoc[i][j].addEventFilter(MouseEvent.MOUSE_CLICKED, e1 -> {
							try {
								
								game.castAbility(q,x, y);
								oneTime = true;
								
							} catch (NotEnoughResourcesException | AbilityUseException | InvalidTargetException
									| CloneNotSupportedException e2) {
								final Popup pop1 = new Popup();
					               // dialog.initModality(Modality.APPLICATION_MODAL);
					                //dialog.initOwner(primaryStage);
					                VBox v1 = new VBox(20);
					                Text m = new Text(e2.getMessage());
					                m.setFill(Color.WHITE);
					                m.setFont(Font.font("italic", 15));
					                v1.getChildren().add(m);
					                v1.setPrefSize(450, 300);
					                v1.setBackground(Background.fill(Color.BLACK));
					               // Scene dialogScene = new Scene(v1, 300, 200);
					                pop1.getContent().add(v1);
					                pop1.setAutoHide(true);
					                pop1.show(stage); 
							}
							StartGame.setupText();
							StartGame.setupBoard();
							
							if(g.checkGameOver() == g.getFirstPlayer()) {
								scene = endGame(0);
								stage.setScene(scene);
							}
							else if(g.checkGameOver() == g.getSecondPlayer()){
								scene = endGame(1);
								stage.setScene(scene);
							}
							
						});
						if(oneTime)
							break;
					}
				}
			}
		});
		
	}
	
	/*root.getChildren().add(b);
		root.getChildren().add(t);
		
		Stage stage = new Stage();
		Scene s = new Scene(root);
		
//		stage.setWidth(800);
//		stage.setHeight(800);
		
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		stage.setX(bounds.getMinX());
		stage.setY(bounds.getMinY());
		stage.setWidth(bounds.getWidth());
		stage.setHeight(bounds.getHeight());
		
		stage.setResizable(false);
		stage.setMaximized(true);
		stage.setScene(s);
		stage.show();
	 */
	
	//--module-path "C:\Users\youss\Downloads\openjfx-18.0.1_windows-x64_bin-sdk\javafx-sdk-18.0.1\lib" --add-modules javafx.controls,javafx.fxml


	public static void onSelectLeader(Game g2) {
		scene = SelectLeader.createSelectLeader(g2);
		stage.setScene(scene);
		
	}

	
	
	
	
	

}
