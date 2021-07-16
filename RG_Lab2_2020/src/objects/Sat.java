package objects;

import javafx.scene.Group;
import javafx.util.Duration;
import main.Main;
import models.GameModel;

public class Sat {
    private Group sat;
    public Sat(Group sat){
        this.sat = sat;
    }

    public void handleSatCollisions() {
        if (sat.getBoundsInParent().intersects(GameModel.getInstance().getPlayer().getBoundsInParent())) {
            int current = Integer.parseInt(Main.timeLeft.getText());
            if (current > 50) current = 50;
            current = (50-current);
            Main.timeline.jumpTo(Duration.seconds(current) );

            GameModel.getInstance().getRoot().getChildren().remove(sat);
            GameModel.getInstance().removeSat(this);
        }
    }
}
