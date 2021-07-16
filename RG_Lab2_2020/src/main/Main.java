package main;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.GameModel;
import objects.*;
import objects.weapons.Bullet;



import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static objects.Player.PLAYER_HEIGHT;
import static objects.Player.PLAYER_WIDTH;

public class Main extends Application {
	
	private AnimationTimer timer;
	public static Timeline timeline;
	public static Text timeLeft;
	
	@Override
	public void start ( Stage primaryStage ) throws Exception {
		Group outerRoot = new Group();
		Scene outerScene = new Scene(outerRoot, GameModel.getInstance ( ).getSceneWidth ( )*1.3, GameModel.getInstance ( ).getSceneHeight ( )*1.3);
		outerRoot.getTransforms().addAll(
				new Translate(200,100)
		);
		GameModel.getInstance().setOuterRoot(outerRoot);

		Image bgOuter = new Image("/resources/okolina.jpg");
		ImagePattern bgPattern = new ImagePattern(bgOuter, 0,0,1,1,true);
		outerScene.setFill(bgPattern);

		Group root = new Group ( );
		primaryStage.setTitle ( "Deoba mehura" );
		SubScene scene = new SubScene ( root, GameModel.getInstance ( ).getSceneWidth ( ), GameModel.getInstance ( ).getSceneHeight ( ) );
		primaryStage.setScene ( outerScene);
		outerRoot.getChildren().addAll(scene);

		// disable resizing and maximize button
		primaryStage.setResizable ( false );
		primaryStage.sizeToScene ( );

		Text numskor = new Text();
		numskor.setFont(new Font(50));
		numskor.setFill(Color.RED);
		numskor.setText("0");
		numskor.getTransforms().addAll(
				//50
				new Translate(1350,-30)
		);

		// 200 600
		Ball ball = new Ball ( new Point2D ( 200, 600 ), Color.RED, 5.7f, 8, ( float ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.025 ), numskor );
		GameModel.getInstance ( ).getBalls ( ).add ( ball );
		
		Player player = new Player ( new Point2D ( 100, GameModel.getInstance ( ).getSceneHeight ( ) - PLAYER_HEIGHT ) );
		GameModel.getInstance ( ).setPlayer ( player );
		
		root.getChildren ( ).addAll ( new Background ( ), ball, player );
		GameModel.getInstance ( ).setRoot ( root );

		Text skor = new Text();
		skor.setFont(new Font(50));
		skor.setFill(Color.RED);
		skor.setText("Score:");
		skor.getTransforms().addAll(
				new Translate(1200,-30)
		);
		outerRoot.getChildren().addAll(skor);
		outerRoot.getChildren().addAll(numskor);

		int time_w = (int)GameModel.getInstance().getSceneWidth();
		Rectangle timeBox = new Rectangle(time_w, 50);
		timeBox.setFill(Color.RED);
		timeBox.getTransforms().addAll(
				new Translate(0, 810)
		);
		outerRoot.getChildren().addAll(timeBox);

		this.timeLeft = new Text();
		timeLeft.setFont(new Font(50));
		timeLeft.setFill(Color.BLUE);
		timeLeft.setText(String.valueOf(60));
		timeLeft.getTransforms().addAll(
				new Translate(650,850)
		);
		outerRoot.getChildren().addAll(timeLeft);

		this.timeline = new Timeline();

		for( int i=59; i>=0; i--){
			KeyValue k = new KeyValue(timeLeft.textProperty(), String.valueOf(i));
			KeyValue k2 = new KeyValue(timeBox.widthProperty(), i*time_w/60);
			Duration t = Duration.seconds(60-i);
			timeline.getKeyFrames().addAll(
				new KeyFrame( t, k, k2 )
			);

		}
		timeline.play();
		timeline.setOnFinished(e->{
			GameModel.getInstance ( ).setGameLost ( true );
		});

		//scene
		outerScene.setOnKeyPressed ( event -> {
			if ( event.getCode ( ) == KeyCode.SPACE ) {
				Bullet bullet = new Bullet ( player.getPosition ( ) .add ( 0.5 * PLAYER_WIDTH, 0 ) );
				root.getChildren ( ).remove ( GameModel.getInstance ( ).getWeapon ( ) );
				GameModel.getInstance ( ).setWeapon ( bullet );
				root.getChildren ( ).addAll ( bullet );
			} else if (event.getCode() == KeyCode.LEFT)
				player.leftKey();
			else if (event.getCode() == KeyCode.RIGHT)
				player.rightKey();
		} );
		outerScene.setOnKeyReleased(event ->{
			if ( event.getCode ( ) == KeyCode.LEFT || event.getCode ( ) == KeyCode.RIGHT )
				player.releaseKey();
		});


		primaryStage.show ( );

		timer = new AnimationTimer ( ) {
			@Override
			public void handle ( long l ) {
				if ( GameModel.getInstance ( ).isGameLost ( ) || GameModel.getInstance ( ).isGameWon ( ) ) {
					timer.stop ( );

					Text endgame = new Text();
					endgame.setFont(new Font(100));
					endgame.getTransforms().addAll(
							new Translate(GameModel.getInstance().getSceneWidth()/3, GameModel.getInstance().getSceneHeight()/2)
					);
					if (GameModel.getInstance ( ).isGameLost ( )) {
						endgame.setFill(Color.RED);
						endgame.setText("YOU LOST!");
					}
					else {
						endgame.setFill(Color.GREEN);
						endgame.setText("YOU WON!");
					}
					root.getChildren().addAll(endgame);
				}
				for ( Ball ball : GameModel.getInstance ( ).getBalls ( ) ) {
					ball.updatePosition ( );
				}
				
				GameModel.getInstance ( ).getPlayer ( ).updatePosition ( );
				if ( GameModel.getInstance ( ).getWeapon ( ) != null ) {
					GameModel.getInstance ( ).getWeapon ( ).updatePosition ( );
				}

				for (Dollar dolla: GameModel.getInstance().getDollas()){
					dolla.handleDollaCollisions();
				}

				for (Stit stit: GameModel.getInstance().getStitovi()){
					stit.handleStitCollisions();
				}

				for (Sat sat: GameModel.getInstance().getSatovi()){
					sat.handleSatCollisions();
				}
				
			}
		};
		timer.start ( );
	}
	
	public static void main ( String[] args ) {
		launch ( args );
	}
}
