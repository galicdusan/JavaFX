package objects;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.util.Duration;
import models.GameModel;

public class Stit {
    private boolean once = true;
    private Group stit;
    private static Timeline invulnerable;
    public Stit(Group stit){
        this.stit = stit;
    }

    public void handleStitCollisions() {
        if ( stit.getBoundsInParent ( ).intersects ( GameModel.getInstance ( ).getPlayer ( ).getBoundsInParent ( ) ) ) {

            Ball.invulnerable = true;
            this.invulnerable = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(GameModel.getInstance ( ).getPlayer ( ).opacityProperty(), 0.3)),
                    new KeyFrame(Duration.seconds(10), new KeyValue(GameModel.getInstance ( ).getPlayer ( ).opacityProperty(), 0.3))
            );
            invulnerable.play();
            invulnerable.setOnFinished(event->{
                shieldFade();
            });

            GameModel.getInstance().getRoot().getChildren().remove(stit);
            GameModel.getInstance().removeStit(this);
        }
    }

    public static void shieldFade(){
            invulnerable.stop();
            Timeline reverting = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(GameModel.getInstance ( ).getPlayer ( ).opacityProperty(), 0.3)),
                    new KeyFrame(Duration.seconds(1), new KeyValue(GameModel.getInstance ( ).getPlayer ( ).opacityProperty(), 1)),
                    new KeyFrame(Duration.seconds(2), new KeyValue(GameModel.getInstance ( ).getPlayer ( ).opacityProperty(), 0.3)),
                    new KeyFrame(Duration.seconds(3), new KeyValue(GameModel.getInstance ( ).getPlayer ( ).opacityProperty(), 1)),
                    new KeyFrame(Duration.seconds(4), new KeyValue(GameModel.getInstance ( ).getPlayer ( ).opacityProperty(), 0.3)),
                    new KeyFrame(Duration.seconds(5), new KeyValue(GameModel.getInstance ( ).getPlayer ( ).opacityProperty(), 1))
            );
            reverting.play();
    }

}
