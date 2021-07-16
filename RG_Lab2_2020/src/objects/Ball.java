package objects;


import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import models.GameModel;

public class Ball extends MovingGameObject {
	private static boolean turn = true;
	public static boolean invulnerable = false;

	//private GameModel model = GameModel.getInstance();
	private float ballSpeedX = 8;
	private float ballSpeedY = ballSpeedX * 1.4f;
	private boolean once = true;
	private boolean fake = false;
	private boolean beingHit = false;

	private Text numskor;
//	private Group dolla= null;
	public static final float PLAYER_WIDTH  = ( float ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.01 )*3f;
	public static final float PLAYER_HEIGHT = 1.5f * PLAYER_WIDTH;
	
	//private static final double BALL_DIAMETER = ( float ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.025 );
	private float diameter;

	public Ball ( Point2D position, Color color, float blspdx, float blspdy, float diameter, Text numskor ) {

		super ( position );

		if(Math.random() < 0.166667){
			fake = true;
		}

		this.ballSpeedX = blspdx;
		this.ballSpeedY = blspdy;
		this.diameter = diameter;
		this.numskor = numskor;

		{
			Circle ball = new Circle ( diameter );
			ball.setFill ( color );
			if(fake)
				ball.setOpacity(0.5);
			this.getChildren ( ).addAll ( ball );
		}

		// initialize speed
		{
			super.speedX = ballSpeedX;
			super.speedY = -ballSpeedY;
		}
	}
	
	@Override
	public void updatePosition ( ) {
		handleCollisions ( );
		position = new Point2D ( position.getX ( ) + speedX, position.getY ( ) + speedY );
		setTranslateX ( getTranslateX ( ) + speedX );
		setTranslateY ( getTranslateY ( ) + speedY );
		
		if ( speedY < 0 ) {
			speedY += 0.17;
		} else {
			speedY = ballSpeedY;
		}
	}
	
	@Override
	protected void handleCollisions ( ) {
		handleBorderCollisions ( );
		handlePlayerCollisions ( );
		handleBulletCollisions ( );
//		if (this.dolla != null)
//			handleDollaCollisions();
	}

/*	private void handleDollaCollisions() {
		if ( dolla.getBoundsInParent ( ).intersects ( GameModel.getInstance ( ).getPlayer ( ).getBoundsInParent ( ) ) ) {
			numskor.setText( String.valueOf(Integer.parseInt(numskor.getText())*2 ) );
			GameModel.getInstance().getRoot().getChildren().remove(dolla);
		}
	}
*/
	private void handleBulletCollisions ( ) {
		if ( GameModel.getInstance ( ).getWeapon ( ) == null ) {
			return;
		}
		if ( this.getBoundsInParent ( ) .intersects ( GameModel.getInstance ( ).getWeapon ( ).getBoundsInParent ( ) ) ) {
			GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
			GameModel.getInstance().getBalls().remove(this);

			Point2D bulletPos = GameModel.getInstance ( ).getWeapon ( ).position ;
			GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( GameModel.getInstance ( ).getWeapon ( ) );
			GameModel.getInstance ( ).setWeapon ( null );

			numskor.setText( String.valueOf(Integer.parseInt(numskor.getText()) + 5) );

			//0.166667
			double v;
			if(Math.random() < 0.166667) {
				if ((v=Math.random()) < 0.33) {
					Group dollar = new Group();
					Dollar dolla = new Dollar(dollar, numskor);
					GameModel.getInstance().addDollar(dolla);
					Rectangle bill = new Rectangle(20, 40);
					bill.setFill(Color.DARKGREEN);
					Text sign = new Text();
					sign.setFont(new Font(20));
					sign.setFill(Color.BLACK);
					sign.setText("$");
					sign.getTransforms().addAll(
							new Translate(5, 20)
					);
					dollar.getChildren().addAll(bill, sign);
					GameModel.getInstance().getRoot().getChildren().addAll(dollar);
					dollar.getTransforms().addAll(
							new Translate(position.getX(), position.getY())
					);
					Duration t = Duration.seconds(3);
					FadeTransition nestanak = new FadeTransition(t, dollar);
					nestanak.setFromValue(1.0);
					nestanak.setToValue(0.0);

					Duration t2 = Duration.seconds(5);
					TranslateTransition pad = new TranslateTransition(t2, dollar);
					pad.setFromY(0);
					pad.setToY(GameModel.getInstance().getSceneHeight());

					nestanak.play();
					pad.play();

					nestanak.setOnFinished(e -> {
						GameModel.getInstance().getRoot().getChildren().remove(dollar);
					});


					pad.setOnFinished(e -> {
						GameModel.getInstance().getRoot().getChildren().remove(dollar);
					});

				}
				else if (v < 0.66){
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
					Group stit = new Group();
					stit.getChildren().addAll(face, usta, cviker1, telo, sesir, obod, cviker2);
					stit.setOpacity(0.5);
					GameModel.getInstance().getRoot().getChildren().addAll(stit);
					stit.getTransforms().addAll(
							new Translate(position.getX(), position.getY())
					);

					Stit s = new Stit(stit);
					GameModel.getInstance().addStit(s);

					Duration t2 = Duration.seconds(5);
					TranslateTransition pad = new TranslateTransition(t2, stit);
					pad.setFromY(0);
					pad.setToY(GameModel.getInstance().getSceneHeight());
					pad.play();

				}
				else {
					Group sat = new Group();
					Circle smolCirc = new Circle(18);
					smolCirc.setFill(Color.WHITE);
					Circle bigCirc = new Circle(20);
					bigCirc.setFill(Color.RED);
					Path kazaljke = new Path();
					kazaljke.setStrokeWidth(3);
					kazaljke.getElements().addAll(
							new MoveTo(0,-14),
							new LineTo(0,0),
							new LineTo(8, 0)
					);
					sat.getChildren().addAll(bigCirc,smolCirc,  kazaljke);
					GameModel.getInstance().getRoot().getChildren().addAll(sat);
					sat.getTransforms().addAll(
							new Translate(position.getX(), position.getY())
					);

					Sat casovnik = new Sat(sat);
					GameModel.getInstance().addSat(casovnik);


					Duration t2 = Duration.seconds(5);
					TranslateTransition pad = new TranslateTransition(t2, sat);
					pad.setFromY(0);
					pad.setToY(GameModel.getInstance().getSceneHeight());
					pad.play();
				}
			}

			if (diameter > 20 && once) {
				float r = (float)Math.random();
				float g = (float)Math.random();
				float b = (float)Math.random();
				Color newColour = new Color(r,g,b, 1 );
//				Point2D bulletPos = GameModel.getInstance ( ).getWeapon ( ).position ;
				Point2D childPos2 =  new Point2D( bulletPos.getX()-this.diameter*0.7f-21, position.getY() );
				Point2D childPos1 =  new Point2D( bulletPos.getX()+this.diameter*0.7f+21, position.getY() );

				if (this.ballSpeedX < 0) this.ballSpeedX = -this.ballSpeedX;

				Ball child1 = new Ball(childPos1, newColour, this.ballSpeedX, this.ballSpeedY*1.1f, this.diameter*0.7f, numskor);
				Ball child2 = new Ball(childPos2, newColour, -this.ballSpeedX, this.ballSpeedY*1.1f, this.diameter*0.7f, numskor);
				GameModel.getInstance().getRoot().getChildren().addAll(child1, child2);
				GameModel.getInstance ( ).getBalls ( ).add ( child2 );
				GameModel.getInstance ( ).getBalls ( ).add ( child1 );
				once = false;
			}
			else numskor.setText( String.valueOf(Integer.parseInt(numskor.getText()) + 5) );

			if(GameModel.getInstance().getBalls().isEmpty())
				GameModel.getInstance ( ).setGameWon ( true );
		}
	}
	
	private void handlePlayerCollisions ( ) {
		if ( this.getBoundsInParent ( ).intersects ( GameModel.getInstance ( ).getPlayer ( ).getBoundsInParent ( ) ) ) {
			if (this.invulnerable == true){
				this.beingHit = true;
				this.invulnerable = false;
				Stit.shieldFade();
			}
			else if (!fake && GameModel.getInstance().lives==0)
				GameModel.getInstance ( ).setGameLost ( true );
			else if (!fake && GameModel.getInstance().lives>0 && !beingHit){
				this.beingHit =  true;
//				GameModel.getInstance().getPlayer().setOpacity(0.3);
				GameModel.getInstance().lives--;
				GameModel.getInstance().getOuterRoot().getChildren().remove( GameModel.getInstance().getLifeIcons()[  GameModel.getInstance().lives  ] );
			}
			else {
				if (fake) {
					GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
					GameModel.getInstance().getBalls().remove(this);
				}
				if (GameModel.getInstance().getBalls().isEmpty())
					GameModel.getInstance ( ).setGameWon(true);
			}
		}
		else {
			this.beingHit = false;
//			GameModel.getInstance().getPlayer().setOpacity(1.0);
		}

	}
	
	private void handleBorderCollisions ( ) {
		if ( position.getX ( ) - diameter < 0 || position.getX ( ) > GameModel.getInstance ( ).getSceneWidth ( ) - diameter ) {
			speedX = -speedX;
		}
		
		if ( position.getY ( ) - diameter < 0 || position.getY ( ) > GameModel.getInstance ( ).getSceneHeight ( ) - diameter ) {
			speedY = -speedY;
			//speedY = Math.signum(speedY) * GameModel.getInstance().ballSpeedY;
		}

		if (position.getY ( ) - diameter < 0){
			Text combo = new Text();
			combo.setFont(new Font(50));
			combo.setFill(Color.RED);
			combo.setText("Combo!");
			combo.getTransforms().addAll(
					new Translate(position.getX() + 30, 50)
			);
			FadeTransition nestanak = new FadeTransition(Duration.seconds(3), combo);
			nestanak.setFromValue(1.0);
			nestanak.setToValue(0.0);
			nestanak.play();
			if (turn) {
				numskor.setText( String.valueOf(Integer.parseInt(numskor.getText()) + 100) );
				GameModel.getInstance().getRoot().getChildren().addAll(combo);
			}
			turn = !turn;

			GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
			GameModel.getInstance().getBalls().remove(this);
		}
	}
}
