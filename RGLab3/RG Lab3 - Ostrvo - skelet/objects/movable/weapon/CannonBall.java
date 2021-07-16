package objects.movable.weapon;

import javafx.animation.*;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import objects.Cannon;
import objects.movable.MovableObject;
import timer.MyAnimationTimer;

public class CannonBall extends Weapon {
	private static boolean imaDjule = false;
	public static boolean getImaDjule(){
		return imaDjule;
	}

	public static class CannonBallDestination implements MovableObject.Destination {
		private double radius;
		Group root;
		
		public CannonBallDestination ( double radius, Group root ) {
			this.radius = radius;
			this.root = root;
		}
		
		@Override public boolean reached ( double x, double y, double z ) {

			if (y>=0){
				imaDjule = false;

				Cylinder test = new Cylinder(15, 30, 15);
				test.setMaterial( new PhongMaterial(Color.BLUE));
				test.getTransforms().addAll(
						new Translate(x,y+15,z)
				);
				root.getChildren().addAll(test);

				Timeline clrChg1 = new Timeline(
						new KeyFrame(Duration.ZERO, new KeyValue(   ((PhongMaterial)test.getMaterial()).diffuseColorProperty(), Color.BLUE   )),
						new KeyFrame(Duration.seconds(1), new KeyValue(   ((PhongMaterial)test.getMaterial()).diffuseColorProperty(), Color.WHITE, Interpolator.EASE_IN   )),
						new KeyFrame(Duration.seconds(2), new KeyValue(   ((PhongMaterial)test.getMaterial()).diffuseColorProperty(), Color.BLUE, Interpolator.EASE_OUT   ))
				);
				clrChg1.play();

				TranslateTransition tranzicija = new TranslateTransition(Duration.seconds(1), test);
				tranzicija.setByY(-30);
				tranzicija.play();
				tranzicija.setOnFinished(e->{
					TranslateTransition tranzicija2 = new TranslateTransition(Duration.seconds(1), test);
					tranzicija2.setByY(35);
					tranzicija2.play();
				});
			}

			return y >= 0;
		}
	}
	
	private static Point3D getSpeed ( double ySpeed, double xAngle, double yAngle ) {
		Point3D speedVector = new Point3D ( 0, ySpeed, 0 );
		Rotate rotateX = new Rotate ( xAngle, Rotate.X_AXIS );
		Rotate rotateY = new Rotate ( yAngle, Rotate.Y_AXIS );
		speedVector = rotateX.transform ( speedVector );
		speedVector = rotateY.transform ( speedVector );
		return speedVector;
	}
	
	private static Affine getPosition ( double cannonHeight, double ventHeight, double xAngle, double yAngle ) {
		Affine identity = new Affine ( );
		
		identity.appendRotation ( yAngle, Point3D.ZERO, Rotate.Y_AXIS );
		identity.appendTranslation ( 0, -cannonHeight, 0 );
		identity.appendRotation ( xAngle, Point3D.ZERO, Rotate.X_AXIS );
		identity.appendTranslation ( 0, -ventHeight, 0 );
		
		return identity;
	}
	
	public CannonBall ( Group root, double radius, Color color, double cannonHeight, double ventHeight, double xAngle, double yAngle, double ySpeed, double gravity, MyAnimationTimer timer, Camera djuleCam ) {
		super (
				root,
				CannonBall.getPosition ( cannonHeight, ventHeight, xAngle, yAngle ),
				CannonBall.getSpeed ( ySpeed, xAngle, yAngle ),
				new Point3D ( 0, gravity, 0 ),
				new CannonBallDestination ( radius, root ),
				timer
		);
		imaDjule = true;

		Sphere ball = new Sphere (radius );
		ball.setMaterial ( new PhongMaterial ( color ) );
		super.getChildren ( ).addAll ( ball );

		super.getChildren().addAll(djuleCam);
	}
}
