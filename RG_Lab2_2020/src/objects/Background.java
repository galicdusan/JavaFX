package objects;


import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import models.GameModel;

public class Background extends GameObject {
	public Background ( ) {
		Rectangle background = new Rectangle (
				GameModel.getInstance ( ).getSceneWidth ( ),
				GameModel.getInstance ( ).getSceneHeight ( )
		);

		Stop[] tacke = new Stop[] { new Stop(0, Color.YELLOW), new Stop(1, Color.BLACK)  };
		background.setFill ( new LinearGradient(0.5, 0, 0.5, 1, true, CycleMethod.NO_CYCLE,  tacke));
		
		this.getChildren ( ).addAll ( background );
	}
}
