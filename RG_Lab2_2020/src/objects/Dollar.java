package objects;

import javafx.scene.Group;
import javafx.scene.text.Text;
import models.GameModel;

public class Dollar {
    private Group dolla;
    private Text numskor;
    public Dollar(Group dolla, Text numskor){
        this.dolla = dolla;
        this.numskor = numskor;
    }

    public void handleDollaCollisions() {
        if ( dolla.getBoundsInParent ( ).intersects ( GameModel.getInstance ( ).getPlayer ( ).getBoundsInParent ( ) ) ) {
            numskor.setText( String.valueOf(Integer.parseInt(numskor.getText())*2 ) );
            GameModel.getInstance().getRoot().getChildren().remove(dolla);
            GameModel.getInstance().removeDollar(this);
        }
    }
}
