package objects;

import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import objects.movable.weapon.CannonBall;
import timer.MyAnimationTimer;

import java.util.Random;

import static java.lang.Integer.parseInt;

public class Cannon extends Group implements EventHandler<MouseEvent> {
	private double           sceneWidth;
	private double           sceneHeight;
	private Rotate           rotateX;
	private Rotate           rotateY;
	private MyAnimationTimer timer;
	private double           width;
	private Color            cannonColor;
	private double           height;
	private double           ySpeed;
	private double           gravity;
	private Group            root;
	private double           ventHeight;

	private Camera djuleCam;
	private Group ammo_root;
	private Rectangle health_bar;
	private Text health_text;
	public int remaining_ammo = 20;

	public Group getAmmo_root(){
		return ammo_root;
	}

	public Rectangle getHealth_bar(){
		return health_bar;
	}

	public void decreaseHealth(){
		Random rand = new Random();
		if(rand.nextInt(4) == 3) {
			if (health_bar.getWidth() > 0) {
				health_bar.setWidth(health_bar.getWidth() - 2);
				health_text.setText(String.valueOf(Integer.parseInt(health_text.getText()) - 1));
			}
		}
		if(health_bar.getWidth() == 0) {
			timer.listener.onGameLost();
			timer.gameOver = true;
		}
	}
	
	public Cannon ( Group root, double width, double height, double depth, double islandHeight, double ventHeight, Color cannonColor, double sceneWidth, double sceneHeight, double ySpeed, double gravity, MyAnimationTimer timer, Camera mojCam, Camera djuleCam, Group ammo_root ) {
		this.root = root;

		this.djuleCam = djuleCam;
		this.ammo_root = ammo_root;
		for(int i=0; i<ammo_root.getChildren().size(); i++){
			if (ammo_root.getChildren().get(i) instanceof Rectangle)
				health_bar = ((Rectangle) ammo_root.getChildren().get(i));
			else if (ammo_root.getChildren().get(i) instanceof Text)
				health_text = ((Text) ammo_root.getChildren().get(i));
		}
		
		Group cannon = new Group ( );
		super.getChildren ( ).addAll ( cannon );

		////////////////////////////////////////

		double Tunit = height/4;
		float Tunitf = (float)Tunit;
		Box telo = new Box (3*Tunit, 8*Tunit, 2*Tunit);
		telo.getTransforms().addAll(
				new Translate(0,-2*Tunit,-1*Tunit )
		);
		telo.setMaterial( new PhongMaterial(Color.GREEN) );
//		root.getChildren().addAll(telo);
		cannon.getChildren().addAll(telo);

		TriangleMesh mojMesh = new TriangleMesh();

		float[] points = {
			0, 0, 0,
			0,0,2*Tunitf,
			-3*Tunitf,0,0,
			-3*Tunitf,0,2*Tunitf,
			0,-2*Tunitf,4*Tunitf,
			-3*Tunitf,-2*Tunitf,4*Tunitf,
			0,-8*Tunitf,0,
			-3*Tunitf,-8*Tunitf,0
		};

		float[] tex = {
				0.5f, 0.5f
		};

		int[] faces = {
			0, 0, 1, 0, 6, 0,
			0, 0, 6, 0, 1, 0,
			1, 0, 4, 0, 6, 0,
			1, 0, 6, 0, 4, 0,
			2, 0, 7, 0, 3, 0,
			2, 0, 3, 0, 7, 0,
			3, 0, 7, 0, 5, 0,
			3, 0, 5, 0, 7, 0,
			0, 0, 2, 0, 1, 0,
			0, 0, 1, 0, 2, 0,
			1, 0, 2, 0, 3, 0,
			1, 0, 3, 0, 2, 0,
			0, 0, 6, 0, 2, 0,
			0, 0, 2, 0, 6, 0,
			2, 0, 6, 0, 7, 0,
			2, 0, 7, 0, 6, 0,
			4, 0, 5, 0, 6, 0,
			4, 0, 6, 0, 5, 0,
			5, 0, 7, 0, 6, 0,
			5, 0, 6, 0, 7, 0,
			1, 0, 3, 0, 4, 0,
			1, 0, 4, 0, 3, 0,
			3, 0, 5, 0, 4, 0,
			3, 0, 4, 0, 5, 0
		};

		mojMesh.getPoints().addAll(points);
		mojMesh.getTexCoords().addAll(tex);
		mojMesh.getFaces().addAll(faces);
		MeshView mesha = new MeshView();
		mesha.setMesh(mojMesh);
		mesha.setMaterial(new PhongMaterial(Color.GREEN));
		mesha.getTransforms().addAll(
				new Translate(1.5*Tunit,2*Tunit, 0)
		);
		cannon.getChildren().addAll(mesha);

		///////////////////////////////////////
		
		Box podium = new Box ( width, height, depth );
		podium.setMaterial ( new PhongMaterial ( cannonColor ) );
		cannon.getChildren ( ).addAll ( podium );
		
		Cylinder vent1 = new Cylinder ( width / 2, 8*Tunit );
		vent1.setMaterial ( new PhongMaterial ( cannonColor ) );
		Cylinder endBit = new Cylinder(width/2+2, Tunit);
		endBit.setMaterial( new PhongMaterial(Color.GREEN) );
		endBit.getTransforms().addAll(
				new Translate(0, -3.5*Tunit, 0)
		);
		Group vent = new Group();
		vent.getChildren().addAll(
				vent1, endBit
		);

		this.rotateX = new Rotate ( );
		this.rotateY = new Rotate ( );
		this.rotateX.setAxis ( Rotate.X_AXIS );
		this.rotateY.setAxis ( Rotate.Y_AXIS );
		
		cannon.getTransforms ( ).addAll (
				new Translate ( 0, -( height + islandHeight ) / 2, 0 ),
				this.rotateY
		);
		vent.getTransforms ( ).addAll (
				new Translate ( 0, -height / 2, 0 ),
				this.rotateX,
				new Translate ( 0, -ventHeight / 2, 0 )
		);
		cannon.getChildren ( ).addAll ( vent );
		
		this.sceneHeight = sceneHeight;
		this.sceneWidth = sceneWidth;
		this.timer = timer;
		this.width = width;
		this.cannonColor = cannonColor;
		this.height = height;
		this.ySpeed = ySpeed;
		this.gravity = gravity;
		this.ventHeight = ventHeight;
//////////////////////////////////
		cannon.getChildren().addAll(mojCam);
	//////////////////////////////////
	}
	
	@Override public void handle ( MouseEvent event ) {
		if ( MouseEvent.MOUSE_MOVED.equals ( event.getEventType ( ) ) ) {
			double xRatio = event.getSceneX ( ) / this.sceneWidth;
			double yRatio = event.getSceneY ( ) / this.sceneHeight;
			
			this.rotateX.setAngle ( -120 * yRatio );
			this.rotateY.setAngle ( 360 * xRatio );
		} else if ( MouseEvent.MOUSE_PRESSED.equals ( event.getEventType ( ) ) && this.timer.canAddWeapon ( ) ) {

			if (ammo_root.getChildren().isEmpty())
				return;
			ammo_root.getChildren().remove( ammo_root.getChildren().size()-1 );
			remaining_ammo--;

			CannonBall cannonBall = new CannonBall (
					root,
					this.width / 2,
					this.cannonColor,
					this.height,
					this.ventHeight,
					this.rotateX.getAngle ( ),
					this.rotateY.getAngle ( ),
					this.ySpeed,
					this.gravity,
					timer,
					djuleCam
			);
			root.getChildren ( ).addAll ( cannonBall );
		}
	}
}
