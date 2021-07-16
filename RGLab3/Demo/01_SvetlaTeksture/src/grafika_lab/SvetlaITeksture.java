package grafika_lab;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class SvetlaITeksture extends Application {
    MeshView piramida;
    PointLight svetlo;

    double ugao;

    Group koren;

    @Override
    public void start(Stage primaryStage) throws Exception{
        koren = new Group();
        PerspectiveCamera kamera = new PerspectiveCamera(true);
        kamera.setNearClip(0.1);
        kamera.setFarClip(1000);
        kamera.setVerticalFieldOfView(true);
        kamera.setFieldOfView(60);
        kamera.getTransforms().setAll(new Rotate(-45, Rotate.Y_AXIS), new Translate(0, -50, -50), new Rotate(-45, Rotate.X_AXIS));

        Image tekstura = new Image("ETF.bmp");
        PhongMaterial materijalPiramida = new PhongMaterial();
        materijalPiramida.setDiffuseMap(tekstura);

        piramida = crtajPiramidu();
        piramida.setMaterial(materijalPiramida);

        PhongMaterial materijalLopta = new PhongMaterial(Color.BLUE);
        materijalLopta.setSpecularColor(Color.WHITE);

        Sphere lopta = new Sphere(5);
        lopta.getTransforms().add(new Translate(0, -10, -20));
        lopta.setMaterial(materijalLopta);

        svetlo = new PointLight(Color.WHITE);
        svetlo.getTransforms().setAll(new Rotate(ugao, Rotate.Y_AXIS), new Translate(0, -15, 60));
        svetlo.getScope().addAll(piramida, lopta);
        
        koren.getChildren().addAll(piramida, lopta, svetlo);

        Scene scene = new Scene(koren, 700, 700, true);
        scene.setFill(Color.BLACK);
        scene.setCamera(kamera);
        scene.setOnKeyPressed(e -> onKeyPressed(e));
        primaryStage.setTitle("Teksturiranje i svetlo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MeshView crtajPiramidu() {
        float[] points = new float[] {
                0, -10, 0,
                10, 0, -10,
                10, 0, 10,
                0, -10, 0,
                10, 0, 10,
                -10, 0, 10,
                0, -10, 0,
                -10, 0, 10,
                -10, 0, -10,
                0, -10, 0,
                -10, 0, -10,
                10, 0, -10
        };
        float[] textCoords = new float[]{
                0.5f, 0,
                0, 1,
                1, 1
        };
        int[] faces = new int[] {
                0, 0,
                1, 1,
                2, 2,
                3, 0,
                4, 1,
                5, 2,
                6, 0,
                7, 1,
                8, 2,
                9, 0,
                10, 1,
                11, 2
        };


        TriangleMesh triangleMesh = new TriangleMesh();
        triangleMesh.getPoints().addAll(points);
        triangleMesh.getTexCoords().addAll(textCoords);
        triangleMesh.getFaces().addAll(faces);

        MeshView meshView = new MeshView();
        meshView.setMesh(triangleMesh);
        meshView.setCullFace(CullFace.NONE);
        return meshView;
    }

    private void onKeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case LEFT:
                piramida.setRotationAxis(Rotate.Y_AXIS);
                piramida.setRotate(piramida.getRotate() - 3);
                break;
            case RIGHT:
                piramida.setRotationAxis(Rotate.Y_AXIS);
                piramida.setRotate(piramida.getRotate() + 3);
                break;
            case UP:
                svetlo.getTransforms().setAll(new Rotate(ugao+=3, Rotate.Y_AXIS), new Translate(0, -15, 60));
                break;
            case DOWN:
                svetlo.getTransforms().setAll(new Rotate(ugao-=3, Rotate.Y_AXIS), new Translate(0, -15, 60));
                break;
            case L:
                if (koren.getChildren().contains(svetlo)) koren.getChildren().remove(svetlo);
                else koren.getChildren().add(svetlo);
                break;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
