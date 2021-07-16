package objects.movable.weapon;

import javafx.animation.*;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import objects.movable.MovableObject;
import timer.MyAnimationTimer;

public class BoatCannonBall extends MovableObject {

    public static class CannonBallDestination implements MovableObject.Destination {
        private double radius;
        Group root;

        public CannonBallDestination ( double radius, Group root ) {
            this.radius = radius;
            this.root = root;
        }

        @Override public boolean reached ( double x, double y, double z ) {

            if (y<=0){
            }

            return (y<=0);
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

    private static Affine getPosition (double boatHeight, double angle, double distance_x, double distance_z) {
        Affine identity = new Affine ( );

//        identity.appendRotation ( angle, Point3D.ZERO, Rotate.Y_AXIS );
        identity.appendTranslation(distance_x, 0, distance_z);
        identity.appendTranslation ( 0, -boatHeight, 0 );

        return identity;
    }

    public BoatCannonBall (Group root, double angle, double distance_x, double distance_z ) {
        super (
                root,
                BoatCannonBall.getPosition ( 15, angle, distance_x, distance_z ),
                BoatCannonBall.getSpeed (0, 0, 0 ),
                new Point3D ( -Math.sin(Math.toRadians(angle)), 0, -Math.cos(Math.toRadians(angle)) ),
//                new Point3D ( 0, 0, 0 ),
                new CannonBall.CannonBallDestination( 3, root )
        );

        Sphere ball = new Sphere (3 );
        ball.setMaterial ( new PhongMaterial ( Color.RED ) );
        super.getChildren ( ).addAll ( ball );
    }
}
