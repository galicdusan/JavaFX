package objects;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import models.GameModel;
import playerStates.MovingLeftState;
import playerStates.MovingRightState;
import playerStates.StandingState;
import playerStates.State;

public class Player extends MovingGameObject {
	//private GameModel model = GameModel.getInstance();
	private             State state         = new StandingState ( this );
	public static final float PLAYER_WIDTH  = ( float ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.01 )*3f;
	public static final float PLAYER_HEIGHT = 1.5f * PLAYER_WIDTH;
	public static final float PLAYER_SPEED  = 10;

	
	{
		// this is necessary in order for this class to respond to key events
		this.setFocusTraversable ( true );
	}
	
	{
		speedX = PLAYER_SPEED;
		speedY = 0;
		Group player = new Group();
		Rectangle player1 = new Rectangle ( PLAYER_WIDTH, PLAYER_HEIGHT );

		Circle face = new Circle(20);
		face.setFill(Color.BURLYWOOD);
		face.getTransforms().addAll(
				new Translate(28, 40)
		);

		Arc usta = new Arc(0,0,10,10, 180,180);
		usta.setScaleX(1);
	    usta.setScaleY(0.75);
	    usta.setFill(Color.BROWN);
	    usta.getTransforms().addAll(
	    			new Translate(28,57)
		);

	    Polygon cviker1 = new Polygon();
	    cviker1.getPoints().addAll(
	    		new Double[] {
	    				0.0,0.0,
						20.0,0.0,
						10.0, 10.0
				}
		);
	    cviker1.getTransforms().addAll(
	    		new Translate(9, 30)
		);

	    Path telo = new Path();
	    telo.getElements().addAll(
	    		new MoveTo(28, 60),
				new LineTo(0, PLAYER_HEIGHT),
				new CubicCurveTo(25, 60, 25, 90,  PLAYER_WIDTH, PLAYER_HEIGHT-4),
				new LineTo(28,60)
		);
		telo.setStroke(Color.RED);
	    telo.setFill(Color.RED);

	    Rectangle sesir = new Rectangle(30,25);
	    sesir.getTransforms().addAll(
	    		new Translate(12, 0)
		);
	    Rectangle obod = new Rectangle(PLAYER_WIDTH, 6);
	    obod.getTransforms().addAll(
	    		new Translate(0, 20)
		);

		Polygon cviker2 = new Polygon();
		cviker2.getPoints().addAll(
				new Double[] {
						0.0,0.0,
						20.0,0.0,
						10.0, 10.0
				}
		);
		cviker2.getTransforms().addAll(
				new Translate(27, 30)
		);

		Group[] lifeIcon = new Group[5];
		double pomeraj = 0;
		for (int i=0; i<5; i++) {
			lifeIcon[i] = new Group();

			Circle face1 = new Circle(20);
			face1.setFill(Color.BURLYWOOD);
			face1.getTransforms().addAll(
					new Translate(28, 40)
			);

			Arc usta1 = new Arc(0,0,10,10, 180,180);
			usta1.setScaleX(1);
			usta1.setScaleY(0.75);
			usta1.setFill(Color.BROWN);
			usta1.getTransforms().addAll(
					new Translate(28,57)
			);

			Polygon cviker11 = new Polygon();
			cviker11.getPoints().addAll(
					new Double[] {
							0.0,0.0,
							20.0,0.0,
							10.0, 10.0
					}
			);
			cviker11.getTransforms().addAll(
					new Translate(9, 30)
			);

			Rectangle sesir1 = new Rectangle(30,25);
			sesir1.getTransforms().addAll(
					new Translate(12, 0)
			);
			Rectangle obod1 = new Rectangle(PLAYER_WIDTH, 6);
			obod1.getTransforms().addAll(
					new Translate(0, 20)
			);

			Polygon cviker21 = new Polygon();
			cviker21.getPoints().addAll(
					new Double[] {
							0.0,0.0,
							20.0,0.0,
							10.0, 10.0
					}
			);
			cviker21.getTransforms().addAll(
					new Translate(27, 30)
			);


			lifeIcon[i].getChildren().addAll(face1, usta1, cviker11, cviker21, sesir1, obod1);
			lifeIcon[i].getTransforms().addAll(
					new Translate(-100+pomeraj, -70),
					new Scale(0.6, 0.6)
			);
			GameModel.getInstance().getOuterRoot().getChildren().addAll(lifeIcon[i]);

			pomeraj += 50;
		}
		GameModel.getInstance().setLifeIcons(lifeIcon);


		player1.setFill ( Color.LIGHTBLUE );
//		player.getChildren().addAll(player1);
		this.getChildren ( ).addAll ( player, face, usta, cviker1, cviker2, telo, sesir, obod );

		
		this.addEventFilter ( KeyEvent.KEY_PRESSED, event -> {
			switch ( event.getCode ( ) ) {
				case RIGHT:
					state = new MovingRightState ( GameModel.getInstance ( ).getPlayer ( ) );
					break;
				case LEFT:
					state = new MovingLeftState ( GameModel.getInstance ( ).getPlayer ( ) );
					break;
			}
		} );
		
		this.addEventFilter (
				KeyEvent.KEY_RELEASED, event -> {
					if ( event.getCode ( ) == KeyCode.LEFT || event.getCode ( ) == KeyCode.RIGHT ) {
						state = new StandingState ( GameModel.getInstance ( ).getPlayer ( ) );
					}
				}
		);
	}
	
	public Player ( Point2D position ) {
		super ( position );
	}
	
	@Override
	protected void handleCollisions ( ) {
		if ( position.getX ( ) < 0 || position.getX ( ) > GameModel.getInstance ( ).getSceneWidth ( ) - PLAYER_WIDTH ) {
			state = new StandingState ( this );
			if ( position.getX ( ) < 0 ) {
				setPosition ( new Point2D ( 0, getPosition ( ).getY ( ) ) );
				setTranslateX ( 0 );
			}
			
			if ( position.getX ( ) > GameModel.getInstance ( ).getSceneWidth ( ) - PLAYER_WIDTH ) {
				setPosition ( new Point2D ( GameModel.getInstance ( ).getSceneWidth ( ) - PLAYER_WIDTH, getPosition ( ).getY ( ) ) );
				setTranslateX ( GameModel.getInstance ( ).getSceneWidth ( ) - PLAYER_WIDTH );
			}
		}
	}
	
	@Override
	public void updatePosition ( ) {
		state.update ( );
		handleCollisions ( );
	}

	public void rightKey(){
		state = new MovingRightState ( GameModel.getInstance ( ).getPlayer ( ) );
	}
	public void leftKey(){
		state = new MovingLeftState ( GameModel.getInstance ( ).getPlayer ( ) );
	}
	public void releaseKey(){
			state = new StandingState ( GameModel.getInstance ( ).getPlayer ( ) );
	}

}
