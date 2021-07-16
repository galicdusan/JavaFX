package objects.movable;

import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Boat extends MovableObject {
	private float unit = 1;
	private double angle = 0;
	public double getAngle(){
		return angle;
	}

	public static class BoatDestination implements MovableObject.Destination {
		private Point3D destination;
		private double delta;
		
		public BoatDestination ( Point3D destination, double delta ) {
			this.destination = destination;
			this.delta = delta;
		}
		
		@Override public boolean reached ( double x, double y, double z ) {
			double dx = Math.abs ( this.destination.getX ( ) - x );
			double dy = Math.abs ( this.destination.getY ( ) - y );
			double dz = Math.abs ( this.destination.getZ ( ) - z );
			
			if ( dx <= this.delta && dy <= this.delta && dz <= this.delta ) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	private static Affine getPosition ( double angle, double distance ) {
		Affine identity = new Affine (  );
		
		identity.appendRotation ( angle, new Point3D ( 0, 0, 0 ), new Point3D ( 0, 1, 0 ) );
		identity.appendTranslation ( 0, 0, distance );
		
		return identity;
	}
	
	private static Point3D getSpeed ( double angle, double distance, double speed ) {
		Affine position = Boat.getPosition ( angle, distance );
		
		return new Point3D (
				-position.getTx ( ),
				-position.getTy ( ),
				-position.getTz ( )
		).normalize ( ).multiply ( speed );
	}
	
	private static BoatDestination getBoatDestination ( double angle, double zDestination, double delta ) {
		Affine position = Boat.getPosition ( angle, zDestination );
		Point3D destination = new Point3D (
				position.getTx ( ),
				position.getTy ( ),
				position.getTz ( )
		);
		
		return new BoatDestination ( destination, delta );
	}
	
	@Override public void onDestinationReached ( ) { }
	
	public Boat ( Group parent, double width, double height, double depth, Color color, double distance, double angle, double speed, double destination, double delta, Camera boatCam ) {
		super (
				parent,
				Boat.getPosition ( angle, distance ),
				Boat.getSpeed ( angle, distance, speed ),
				new Point3D ( 0, 0, 0 ),
				Boat.getBoatDestination ( angle, destination + depth / 2, delta )
		);

		this.angle = angle;
		TriangleMesh mojMesh = new TriangleMesh();

		float[] points = {
			7.5f*unit, 0, 0,
			-7.5f*unit, 0, 0,
			-7.5f*unit, -8*unit, 0,
			7.5f*unit, -8*unit, 0,
			0, -8*unit, 12*unit,
		};

		float[] tex = {
			0.5f, 0.5f
		};

		int[] faces = {
			0, 0, 2, 0, 1, 0,
			0, 0, 1, 0, 2, 0,
			0, 0, 3, 0, 2, 0,
			0, 0, 2, 0, 3, 0,
			2, 0, 3, 0, 4, 0,
			2, 0, 4, 0, 3, 0,
			0, 0, 4, 0, 3, 0,
			0, 0, 3, 0, 4, 0,
			0, 0, 1, 0, 4, 0,
			0, 0, 4, 0, 1, 0,
			1, 0, 2, 0, 4, 0,
			1, 0, 4, 0, 2, 0
		};

		mojMesh.getPoints().addAll(points);
		mojMesh.getTexCoords().addAll(tex);
		mojMesh.getFaces().addAll(faces);
		MeshView mesha = new MeshView();
		mesha.setMesh(mojMesh);
		mesha.setMaterial(new PhongMaterial(Color.LIGHTGRAY));
		mesha.getTransforms().addAll(
				new Rotate(180, Rotate.Y_AXIS),
				new Translate(0,0, 17.5*unit)
		);
		super.getChildren().addAll(mesha);

		Box cabin = new Box(10*unit, 7*unit, 14*unit);
		cabin.getTransforms().addAll(
				new Translate(0, (-3.5)*unit-8*unit, 7*unit)
		);
		cabin.setMaterial(new PhongMaterial(Color.GRAY));
		Box deck = new Box(15*unit, 8*unit, 35*unit);
		deck.getTransforms().addAll(
				new Translate(0,-4*unit,0)
		);
		deck.setMaterial(new PhongMaterial(Color.LIGHTGRAY));

		Box box = new Box ( width, height, depth );
		box.setMaterial (
				new PhongMaterial ( color )
		);
//		super.getChildren ( ).addAll ( box );
		super.getChildren().addAll(cabin,deck);

		boatCam.getTransforms().addAll(
				new Rotate(180, Rotate.Y_AXIS),
				new Translate(0,-10,0)
		);
		super.getChildren().addAll(boatCam);
	}

	public void onCollision ( ) {
		TranslateTransition tranzicija = new TranslateTransition(Duration.seconds(5), this);
		tranzicija.setByY(15*unit);
		tranzicija.play();
		tranzicija.setOnFinished(e->{
			this.parent.getChildren ( ).remove ( this );
		});
	}
}
