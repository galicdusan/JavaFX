package grafika_lab;

import javafx.application.Application;

import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class TransformacijePogleda extends Application {

    private Camera kamera;
    private ParallelCamera paralelnaKamera;
    private PerspectiveCamera perspektivnaKamera;

    private Box kutija1;
    private Box kutija2;
    private Box kutija3;

    private double ugao = 0;

    private Scene scena;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group koren = new Group();

        Rectangle pravougaonik = new Rectangle(-100, -100, 200, 200);
        pravougaonik.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
        pravougaonik.setFill(Color.GREEN);

        PhongMaterial material1 = new PhongMaterial(Color.RED);
        PhongMaterial material2 = new PhongMaterial(Color.GREEN);
        PhongMaterial material3 = new PhongMaterial(Color.BLUE);

        kutija1 = new Box(20, 20, 20);
        kutija1.setMaterial(material1);
        kutija2 = new Box(30, 30, 30);
        kutija2.setMaterial(material2);
        kutija3 = new Box(40, 40, 40);
        kutija3.setMaterial(material3);

        transformisiKutije();

        koren.getChildren().addAll(pravougaonik, kutija1, kutija2, kutija3);

        perspektivnaKamera = new PerspectiveCamera(true);
        perspektivnaKamera.setFieldOfView(90);
        perspektivnaKamera.setVerticalFieldOfView(false);
        perspektivnaKamera.setNearClip(0.1);
        perspektivnaKamera.setFarClip(3000);
        paralelnaKamera = new ParallelCamera();

        kamera = perspektivnaKamera;
        transformisiKamere(-45);

        scena = new Scene(koren, 700, 700, true);
        scena.setCamera(kamera);
        scena.setOnKeyPressed(e -> onKeyPressed(e));
        primaryStage.setScene(scena);
        primaryStage.setTitle("Transformacije pogleda");
        primaryStage.show();
    }

    private void transformisiKutiju(Box box, double x, double y, double z, double ugao) {
        box.getTransforms().setAll(new Translate(x, y, z));
        box.getTransforms().addAll(new Rotate(ugao, Rotate.X_AXIS));
    }

    private void transformisiKutije() {
        transformisiKutiju(kutija1, 40, -40, 80, ugao);
        transformisiKutiju(kutija2, 80, -40, -40, ugao);
        transformisiKutiju(kutija3, 60, -40, 20, ugao);
    }

    private void transformisiKamere(double ugao) {
        paralelnaKamera.getTransforms().setAll(new Rotate(ugao, Rotate.Y_AXIS), new Translate(0, -100, -100), new Rotate(-45, Rotate.X_AXIS));
        paralelnaKamera.getTransforms().add(new Translate(-350, -350, 0));

        perspektivnaKamera.getTransforms().setAll(new Rotate(ugao, Rotate.Y_AXIS), new Translate(0, -100, -100), new Rotate(-45, Rotate.X_AXIS));
    }

    private void onKeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case LEFT:
            case RIGHT:
                if (kamera == paralelnaKamera) {
                    kamera = perspektivnaKamera;
                } else {
                    kamera = paralelnaKamera;
                }
                scena.setCamera(kamera);
                break;
            case UP:
                ugao++;
                transformisiKutije();
                break;
            case DOWN:
                ugao--;
                transformisiKutije();
                break;
            case DIGIT1:
                transformisiKamere(-45);
                break;
            case DIGIT2:
                transformisiKamere(-135);
                break;
            case DIGIT3:
                transformisiKamere(-225);
                break;
            case DIGIT4:
                transformisiKamere(-315);
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
