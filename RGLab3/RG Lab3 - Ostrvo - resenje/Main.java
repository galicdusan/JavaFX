import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import objects.Cannon;
import objects.movable.Boat;
import objects.movable.MovableObject;
import timer.GameEventListener;
import timer.MyAnimationTimer;

import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.ArrayList;
import java.util.List;

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
        public static final double CANNON_DEPTH       = 50;
        public static final double CANNON_VENT_LENGTH = 50;
        public static final double Y_SPEED            = -2;
        
        public static final double GRAVITY = 0.01;
    }
    
	private MyAnimationTimer timer;
	
	public static void main ( String[] args ) {
		launch ( args );
	}
	
	@Override public void onGameWon ( ) { }
	
	@Override public void onGameLost ( ) { }
	
	@Override
	public void start ( Stage primaryStage ) throws Exception {
		List<MovableObject> movableObjects = new ArrayList<> ( );
		Group               root           = new Group ( );
		Scene               scene          = new Scene ( root, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT, true, SceneAntialiasing.BALANCED );
		
		Box ocean = new Box ( Constants.OCEAN_WIDTH, Constants.OCEAN_HEIGHT, Constants.OCEAN_DEPTH );
		ocean.setMaterial ( new PhongMaterial ( Color.BLUE ) );
		root.getChildren ( ).addAll ( ocean );
		
		Cylinder island = new Cylinder ( Constants.ISLAND_RADIUS, Constants.ISLAND_HEIGHT );
		island.setMaterial ( new PhongMaterial ( Color.BROWN ) );
		root.getChildren ( ).addAll ( island );
		
		CameraController cameraController = new CameraController ( Constants.CAMERA_NEAR_CLIP, Constants.CAMERA_FAR_CLIP, scene );
		scene.addEventHandler ( KeyEvent.ANY, cameraController );
		
		Camera camera = cameraController.getFixedCamera ( );
		camera.setNearClip ( Constants.CAMERA_NEAR_CLIP );
		camera.setFarClip ( Constants.CAMERA_FAR_CLIP );
		camera.getTransforms ( ).addAll (
				new Rotate ( Constants.CAMERA_X_ANGLE, Rotate.X_AXIS ),
				new Translate ( 0, 0, Constants.CAMERA_Z )
		);
		
		for ( int i = 0; i < Constants.NUMBER_OF_BOATS; ++i ) {
			double angle = 360 * 1.0 / Constants.NUMBER_OF_BOATS * i;
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
					Constants.DELTA
			);
			
			root.getChildren ( ).addAll ( boat );
			movableObjects.add ( boat );
		}
		
		this.timer = new MyAnimationTimer ( movableObjects, this );
		
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
				cameraController.getCannonCamera ( )
		);
		root.getChildren ( ).addAll ( cannon );
		scene.addEventHandler ( MouseEvent.ANY, cannon );
		
		scene.setFill ( Color.LIGHTBLUE );
		scene.setCursor ( Cursor.NONE );
		primaryStage.setScene ( scene );
		primaryStage.setTitle ( Constants.TITLE );
		primaryStage.show ( );
		timer.start ( );
	}
}
