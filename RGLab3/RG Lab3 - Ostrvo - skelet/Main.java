import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import objects.Cannon;
import objects.movable.Boat;
import objects.movable.MovableObject;
import objects.movable.weapon.BoatCannonBall;
import objects.movable.weapon.CannonBall;
import timer.GameEventListener;
import timer.MyAnimationTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application implements GameEventListener {
    private static class Constants {
        public static final String TITLE = "Island defense";
        
        public static final double CAMERA_NEAR_CLIP = 0.1;
	    public static final double CAMERA_FAR_CLIP  = 100000;
	    public static final double CAMERA_X_ANGLE   = -45;
	    public static final double CAMERA_Z         = -2000;
        
        public static final double SCENE_WIDTH  = 800;
        public static final double SCENE_HEIGHT = 800;
        
        public static final double OCEAN_WIDTH  = 10000;
        public static final double OCEAN_DEPTH  = 10000;
        public static final double OCEAN_HEIGHT = 2;
        
        public static final double ISLAND_RADIUS  = 100;
        public static final double ISLAND_HEIGHT = 6;
        
        public static final double DELTA = 0.1;
        
        public static final int    NUMBER_OF_BOATS = 4;
        public static final double BOAT_WIDTH      = 10;
        public static final double BOAT_HEIGHT     = 10;
        public static final double BOAT_DEPTH      = 20;
        public static final double BOAT_DISTANCE   = 500;
        public static final double BOAT_SPEED      = 0.1;
        
        public static final double CANNON_WIDTH       = 10;
        public static final double CANNON_HEIGHT      = 50;
        public static final double CANNON_DEPTH       = 10;
        public static final double CANNON_VENT_LENGTH = 50;
        public static final double Y_SPEED            = -2;
        
        public static final double GRAVITY = 0.01;
    }
    
	private MyAnimationTimer timer;
    private Group overlayy;

    private boolean minimapOn = true;
    private void setMinimapOn(boolean val){
    	minimapOn = val;
	}

	public static void main ( String[] args ) {
		launch ( args );
	}
	
	@Override public void onGameWon ( ) {
		Text youLost = new Text("YOU WON");
		youLost.setFill(Color.GREEN);
		youLost.setFont(new Font(150));
		youLost.getTransforms().addAll(
				new Translate(Constants.SCENE_WIDTH*0.07, Constants.SCENE_HEIGHT*0.5)
		);
		overlayy.getChildren().addAll(youLost);
	}
	
	@Override public void onGameLost ( ) {
    	Text youLost = new Text("YOU LOST");
    	youLost.setFill(Color.RED);
    	youLost.setFont(new Font(150));
    	youLost.getTransforms().addAll(
    			new Translate(Constants.SCENE_WIDTH*0.07, Constants.SCENE_HEIGHT*0.5)
		);
		overlayy.getChildren().addAll(youLost);
	}
	
	@Override
	public void start ( Stage primaryStage ) throws Exception {
		List<MovableObject> movableObjects = new ArrayList<> ( );

		Group globalRoot = new Group();
		Scene globalScene = new Scene( globalRoot, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT, true, SceneAntialiasing.BALANCED );
		Group               root           = new Group ( );
		SubScene               scene          = new SubScene ( root, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT, true, SceneAntialiasing.BALANCED );
		
		Box ocean = new Box ( Constants.OCEAN_WIDTH, Constants.OCEAN_HEIGHT, Constants.OCEAN_DEPTH );
		ocean.setMaterial ( new PhongMaterial () );
		root.getChildren ( ).addAll ( ocean );
		((PhongMaterial)ocean.getMaterial()).setDiffuseMap(new Image("Resources/untitled.png"));


		Cylinder island = new Cylinder ( Constants.ISLAND_RADIUS, Constants.ISLAND_HEIGHT );
		island.setMaterial ( new PhongMaterial ( Color.BROWN ) );
		root.getChildren ( ).addAll ( island );
		
		Camera camera = new PerspectiveCamera ( true );
		camera.setNearClip ( Constants.CAMERA_NEAR_CLIP );
		camera.setFarClip ( Constants.CAMERA_FAR_CLIP );
		camera.getTransforms ( ).addAll (
				new Rotate ( Constants.CAMERA_X_ANGLE, Rotate.X_AXIS ),
				new Translate ( 0, 0, Constants.CAMERA_Z )
		);
		scene.setCamera ( camera );

		Camera birdView = new PerspectiveCamera(true);
		birdView.setNearClip(0.1);
		birdView.setFarClip(1000);
		birdView.getTransforms().addAll(
			new Translate(0,-1000,0),
			new Rotate(-90, Rotate.X_AXIS)
		);

		Group upper_root = new Group(root);
		SubScene podscena = new SubScene( upper_root, Constants.SCENE_WIDTH*0.35, Constants.SCENE_HEIGHT*0.35, true,  SceneAntialiasing.BALANCED);
		podscena.getTransforms().addAll(
				new Translate(0, Constants.SCENE_HEIGHT*0.65)
		);
		podscena.setCamera(birdView);
		globalRoot.getChildren().addAll(podscena);
		globalRoot.getChildren().addAll(scene);
		podscena.toFront();

		Group ammo_root = new Group();
		SubScene ammo_overlay = new SubScene (ammo_root, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
		overlayy = ammo_root;

		Text health_text = new Text("100");
		health_text.setFont(new Font(20));
		health_text.setFill(Color.WHITE);
		health_text.getTransforms().addAll(
				new Translate(Constants.SCENE_WIDTH*0.787, Constants.SCENE_HEIGHT * 0.93)
		);
		Rectangle health_bar = new Rectangle (0,0, 200, 40);
		health_bar.setFill(Color.RED);
		health_bar.getTransforms().addAll(
				new Translate(Constants.SCENE_WIDTH*0.7, Constants.SCENE_HEIGHT*0.9)
		);
		ammo_root.getChildren().addAll(health_bar);

		Rectangle cabin = new Rectangle(25,20, 10, 10);
		cabin.setFill(Color.DIMGREY);
		Polygon pramac = new Polygon();
		pramac.getPoints().addAll(
				new Double[]{
						5.0, 30.0, 15.0, 30.0, 15.0, 40.0
				}
		);
		Rectangle tijelo = new Rectangle(15, 30, 25, 10);
		pramac.setFill(Color.LIGHTGRAY);
		tijelo.setFill(Color.LIGHTGRAY);
		Group boat_icon = new Group(cabin, pramac, tijelo);
		boat_icon.getTransforms().addAll(
				new Translate(Constants.SCENE_WIDTH*0.9,Constants.SCENE_HEIGHT*0.84)
		);
		ammo_root.getChildren().addAll(boat_icon);

		Rectangle cabin1 = new Rectangle(25,20, 10, 10);
		cabin1.setFill(Color.DIMGREY);
		Polygon pramac1 = new Polygon();
		pramac1.getPoints().addAll(
				new Double[]{
						5.0, 30.0, 15.0, 30.0, 15.0, 40.0
				}
		);
		Rectangle tijelo1 = new Rectangle(15, 30, 25, 10);
		pramac1.setFill(Color.LIGHTGRAY);
		tijelo1.setFill(Color.LIGHTGRAY);
		Group defeated_boat = new Group(cabin1, pramac1, tijelo1);
		Rectangle redRect = new Rectangle(5, 40);
		redRect.getTransforms().addAll(
				new Translate(36, 12),
				new Rotate(45)
		);
		redRect.setFill(Color.RED);
		defeated_boat.getChildren().addAll(redRect);
		defeated_boat.getTransforms().addAll(
				new Translate( Constants.SCENE_WIDTH*0.9, Constants.SCENE_HEIGHT*0.8)
		);
		ammo_root.getChildren().addAll(defeated_boat);

		Text defeated_ships_txt = new Text("0");
		defeated_ships_txt.setFill(Color.WHITE);
		defeated_ships_txt.setFont( new Font(20) );
		defeated_ships_txt.getTransforms().addAll(
				new Translate(Constants.SCENE_WIDTH*0.97, Constants.SCENE_HEIGHT*0.85)
		);
		ammo_root.getChildren().addAll(defeated_ships_txt);

		Text arrived_ships_txt = new Text("0");
		arrived_ships_txt.setFill(Color.WHITE);
		arrived_ships_txt.setFont( new Font(20) );
		arrived_ships_txt.getTransforms().addAll(
				new Translate( Constants.SCENE_WIDTH*0.97, Constants.SCENE_HEIGHT*0.89 )
		);
		ammo_root.getChildren().addAll(arrived_ships_txt);

		ammo_root.getChildren().addAll(health_text);

		Circle vrh_metka = new Circle(10,20,10);
		Rectangle caura_metka = new Rectangle(4, 20, 12, 15);
		vrh_metka.setScaleX(0.6);
		double position = 0.95;
		for(int i=0; i<20; i++) {
			Shape metak = Shape.union(vrh_metka, caura_metka);
			metak.setFill(Color.YELLOW);
			metak.getTransforms().addAll(
					new Translate(Constants.SCENE_WIDTH * position, Constants.SCENE_HEIGHT * 0.95)
			);
			ammo_root.getChildren().addAll(metak);
			position-=0.02;
		}

		globalRoot.getChildren().addAll(ammo_overlay);

		Camera[] boatCams = new Camera[4];
		for(int i=0; i<Constants.NUMBER_OF_BOATS; i++){
			boatCams[i] = new PerspectiveCamera(true);
			boatCams[i].setNearClip(0.1);
			boatCams[i].setFarClip(1000);
		}

		for ( int i = 0; i < Constants.NUMBER_OF_BOATS; ++i ) {
			Random rand = new Random();
			double angle = 360 * rand.nextDouble();
//			System.out.println(angle);
			Boat boat = new Boat (
					root,
					Constants.BOAT_WIDTH,
					Constants.BOAT_HEIGHT,
					Constants.BOAT_DEPTH,
					Color.RED,
					Constants.BOAT_DISTANCE,
					angle,
					Constants.BOAT_SPEED,
					Constants.ISLAND_RADIUS,
					Constants.DELTA,
					boatCams[i]
			);

			root.getChildren ( ).addAll ( boat );
			movableObjects.add ( boat );
		}

		this.timer = new MyAnimationTimer ( movableObjects, this, root, Constants.NUMBER_OF_BOATS,
				Constants.BOAT_WIDTH, Constants.BOAT_HEIGHT, Constants.BOAT_DEPTH, Constants.BOAT_DISTANCE, Constants.BOAT_SPEED, Constants.ISLAND_RADIUS,
				Constants.DELTA ,boatCams, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);

		////////////////////////////////////////
		PerspectiveCamera mojCam = new PerspectiveCamera(true);
		mojCam.setNearClip(0.1);
		mojCam.setFarClip(1000);
		mojCam.getTransforms().addAll(
				new Translate(0,-(Constants.CANNON_HEIGHT+60),-100)
				,new Rotate(-20, Rotate.X_AXIS)
		);

		PerspectiveCamera djuleCam = new PerspectiveCamera(true);
		djuleCam.setNearClip(0.1);
		djuleCam.setFarClip(1000);

		
		Cannon cannon = new Cannon (
				root,
				Constants.CANNON_WIDTH,
				Constants.CANNON_HEIGHT,
				Constants.CANNON_DEPTH,
				Constants.ISLAND_HEIGHT,
				Constants.CANNON_VENT_LENGTH,
				Color.GREEN,
				Constants.SCENE_WIDTH,
				Constants.SCENE_HEIGHT,
				Constants.Y_SPEED,
				Constants.GRAVITY,
				this.timer,
				mojCam,
				djuleCam,
				ammo_root
		);
		root.getChildren ( ).addAll ( cannon );
		timer.setCannon(cannon);
	//	scene.addEventHandler ( MouseEvent.ANY, cannon );
		globalScene.addEventHandler(MouseEvent.ANY, cannon);



		EventHandler<KeyEvent>  r1 = event -> {
			if(event.getCode() == KeyCode.DIGIT5){
				scene.setCamera(mojCam);
			} else if (event.getCode() == KeyCode.DIGIT0) {
				scene.setCamera(camera);
			}  else if (event.getCode() == KeyCode.DIGIT1) {
				scene.setCamera(boatCams[0]);
			}  else if (event.getCode() == KeyCode.DIGIT2) {
				scene.setCamera(boatCams[1]);
			}  else if (event.getCode() == KeyCode.DIGIT3) {
				scene.setCamera(boatCams[2]);
			}  else if (event.getCode() == KeyCode.DIGIT4) {
				scene.setCamera(boatCams[3]);
			} else if (event.getCode() == KeyCode.LEFT) {
				double x = scene.getCamera().getTranslateX();
				scene.getCamera().setTranslateX(x + 5);
			} else if (event.getCode() == KeyCode.RIGHT) {
				double x = scene.getCamera().getTranslateX();
				scene.getCamera().setTranslateX(x - 5);
			} else if (event.getCode() == KeyCode.UP) {
				double y = scene.getCamera().getTranslateY();
				scene.getCamera().setTranslateY(y - 5);
			} else if (event.getCode() == KeyCode.DOWN) {
				double y = scene.getCamera().getTranslateY();
				scene.getCamera().setTranslateY(y + 5);
			} else if (event.getCode() == KeyCode.PAGE_UP) {
				double z = scene.getCamera().getTranslateZ();
				scene.getCamera().setTranslateZ(z - 5);
			} else if (event.getCode() == KeyCode.PAGE_DOWN) {
				double z = scene.getCamera().getTranslateZ();
				scene.getCamera().setTranslateZ(z + 5);
			} else if (event.getCode() == KeyCode.SPACE) {
				if(CannonBall.getImaDjule())
					scene.setCamera(djuleCam);
				else
					scene.setCamera(mojCam);
			} else if (event.getCode() == KeyCode.T) {
				if(minimapOn){
					podscena.toBack();
					setMinimapOn(false);
				}
				else {
					podscena.toFront();
					setMinimapOn(true);
				}
			}
		};
		globalScene.addEventHandler(KeyEvent.KEY_PRESSED, r1);
	//	scene.setFocusTraversable(true);
	//	scene.addEventHandler(KeyEvent.KEY_PRESSED, r1);

		////////////////////////////////////
		
		scene.setFill ( Color.LIGHTBLUE );
		scene.setCursor ( Cursor.NONE );
		podscena.setCursor(Cursor.NONE);



		primaryStage.setScene ( globalScene );
		primaryStage.setTitle ( Constants.TITLE );
		primaryStage.show ( );
		timer.start ( );

	}
}
