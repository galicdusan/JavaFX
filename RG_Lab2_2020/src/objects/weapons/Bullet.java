package objects.weapons;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;
import javafx.scene.transform.Translate;
import models.GameModel;

public class Bullet extends Weapon {
	public static final float  BULLET_SPEED    = -13;
	public static final double BULLET_DIAMETER = GameModel.getInstance ( ).getSceneWidth ( ) * 0.004;
	
	{
		Group bullet = new Group();
//		Circle bullet = new Circle ( BULLET_DIAMETER );
		Polygon vrh = new Polygon();
		vrh.getPoints().addAll(
				new Double[] {
						0.0,0.0,
						10.0,-20.32,
						20.0, 0.0
				}
		);
		vrh.setFill ( Color.BLUE );
		bullet.getChildren().addAll(vrh);

//		Line kanap = new Line(10, 0, 10, 1000);
		Group kanap = new Group();
		kanap.getTransforms().addAll(
			new Translate(10,0)
		);
		double xPoc = 0, yPoc = 0, sign=1;
		for (int i=0; i<66; i++) {
			QuadCurve deoKanapa = new QuadCurve(xPoc, yPoc, xPoc - 10* sign, yPoc + 7.5, xPoc, yPoc + 15);
			deoKanapa.setStroke(Color.DARKRED);
			deoKanapa.setFill(null);
			kanap.getChildren().addAll(deoKanapa);

			sign = sign*(-1);
			yPoc += 15;
		}

//		kanap.setStroke(Color.RED);
		bullet.getChildren().addAll(kanap);

		this.getChildren ( ).addAll ( bullet );
	}
	
	public Bullet ( Point2D position ) {
		super ( position );
	}
	
	public Bullet ( ) { }
	
	// initialize speed
	{
		super.speedX = 0;
		super.speedY = BULLET_SPEED;
	}
}
