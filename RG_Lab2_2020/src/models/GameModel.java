package models;

import javafx.scene.Group;
import objects.*;
import objects.weapons.Weapon;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameModel {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final double SCENE_SCALE_FACTOR = 0.7;
    private float sceneWidth = (float) (screenSize.getWidth() * SCENE_SCALE_FACTOR);
    private float sceneHeight = (float) (screenSize.getHeight() * SCENE_SCALE_FACTOR);
    
    private static GameModel thisInstance = null;

    private CopyOnWriteArrayList<Ball> balls = new CopyOnWriteArrayList<>();
    private Player player;
    private Weapon weapon;
    private boolean gameLost;
    private boolean gameWon;
    private Group root;
    private Group outerRoot;
    public int lives = 5;

    private Group[] lifeIcons;

    private CopyOnWriteArrayList<Dollar> dollas = new CopyOnWriteArrayList<Dollar>();
    private CopyOnWriteArrayList<Stit> stitovi = new CopyOnWriteArrayList<Stit>();
    private CopyOnWriteArrayList<Sat> satovi = new CopyOnWriteArrayList<Sat>();

    public static GameModel getInstance() {
        if (thisInstance == null) {
            thisInstance = new GameModel();
        }
        return thisInstance;
    }

    public CopyOnWriteArrayList<Sat> getSatovi() {return satovi;}

    public void addSat(Sat sat){ satovi.add(sat); }

    public void removeSat(Sat sat) {satovi.remove(sat);}

    public CopyOnWriteArrayList<Stit> getStitovi() {return stitovi;}

    public void addStit(Stit stit){ stitovi.add(stit); }

    public void removeStit(Stit stit) {stitovi.remove(stit);}

    public void setLifeIcons(Group[] lifeicons){ this.lifeIcons = lifeicons; }

    public Group[] getLifeIcons() {return this.lifeIcons;}

    public Group getOuterRoot(){ return outerRoot;}

    public void setOuterRoot(Group outerRoot){ this.outerRoot = outerRoot; }

    public void addDollar(Dollar dolla){
        dollas.add(dolla);
    }

    public void removeDollar(Dollar dolla){
        dollas.remove(dolla);
    }

    public CopyOnWriteArrayList<Dollar> getDollas() {return dollas;}

    public float getSceneWidth() {
        return sceneWidth;
    }

    public float getSceneHeight() {
        return sceneHeight;
    }

    public double getScreenWidth() {
        return screenSize.getWidth();
    }

    public double getScreenHeight() {
        return screenSize.getHeight();
    }

    public void setGameLost(boolean gameLost) {
        this.gameLost = gameLost;
    }

    public boolean isGameLost() {
        return gameLost;
    }

    public CopyOnWriteArrayList<Ball> getBalls() {
        return balls;
    }

    public void setBalls(CopyOnWriteArrayList<Ball> balls) {
        this.balls = balls;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Group getRoot() {
        return root;
    }

    public void setRoot(Group root) {
        this.root = root;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    public Dimension getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Dimension screenSize) {
        this.screenSize = screenSize;
    }

}
