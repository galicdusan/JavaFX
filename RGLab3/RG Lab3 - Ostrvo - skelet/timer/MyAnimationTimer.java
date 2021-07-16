package timer;

import javafx.animation.AnimationTimer;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;
import objects.Cannon;
import objects.movable.Boat;
import objects.movable.MovableObject;
import objects.movable.weapon.BoatCannonBall;

import java.util.List;
import java.util.Random;

public class MyAnimationTimer extends AnimationTimer {
	private List<MovableObject> movableObjects;
	private MovableObject       weapon;
	public GameEventListener listener;
	public boolean gameOver;

	private Group root;
	private Group cannon;
	public void setCannon(Group cannon){
		this.cannon = cannon;
	}
	private int numShips = 4;
	private int currShips = 4;
	private double boat_w;
	private double boat_h;
	private double boat_d;
	private double boat_dist;
	private double boat_spd;
	private double island_r;
	private double delta;
	Camera[] boatCams;

	private double scene_w;
	private double scene_h;

	private int wave = 1;

	public MyAnimationTimer ( List<MovableObject> movableObjects, GameEventListener listener, Group root, int numShips, double boat_w, double boat_h,
							  double boat_d, double boat_dist, double boat_spd, double island_r, double delta, Camera[] boatCams, double scene_w, double scene_h) {
		this.movableObjects = movableObjects;
		this.listener = listener;
		this.root = root;
		this.numShips = numShips;
		this.currShips = numShips;

		this.boat_w = boat_w;
		this.boat_h = boat_h;
		this.boat_d = boat_d;
		this.boat_dist = boat_dist;
		this.boat_spd = boat_spd;
		this.island_r = island_r;
		this.delta = delta;
		this.boatCams = boatCams;

		this.scene_w = scene_w;
		this.scene_h = scene_h;
	}

	public void addMoveableObject(MovableObject obj){
		movableObjects.add(obj);
	}
	public void emptyList(){
		movableObjects.clear();
	}
	
	@Override public synchronized void handle ( long now ) {
		boolean islandHit = this.movableObjects.removeIf ( object -> object.update ( now  ) );
		
		if ( this.gameOver == false ) {
			if ( islandHit ) {

				int txt_counter = 0;
				Group ammo_root = ((Cannon)cannon).getAmmo_root();
				for(int i=0; i<ammo_root.getChildren().size(); i++){
					if (ammo_root.getChildren().get(i) instanceof Text) {
						txt_counter++;
						if (txt_counter == 2){
							int numOfBoatsDocked = 0;
							for (int j=0; j<root.getChildren().size(); j++){
								if (root.getChildren().get(j) instanceof Boat) {
									Boat boat = ((Boat)root.getChildren().get(j));
									if ( Math.abs( boat.getPosition().getTx() ) < 110 && Math.abs( boat.getPosition().getTz() ) < 110 )
										numOfBoatsDocked++;
								}
							}
							Text def_txt = ((Text) ammo_root.getChildren().get(i));
							def_txt.setText(String.valueOf(Integer.parseInt(def_txt.getText()) + numOfBoatsDocked));
							break;
						}
					}
				}

				int j = 0;
				while( j < root.getChildren().size() ){
					if (root.getChildren().get(j) instanceof Boat) {
						root.getChildren().remove(j);
						j--;
					}
					j++;
				}

				boat_spd += 0.1;
				currShips = numShips;
				for (int i = 0; i < numShips; ++i ) {
					Random rand = new Random();
					double angle = 360 * rand.nextDouble();
					Boat boat = new Boat (
							root,
							boat_w,
							boat_h,
							boat_d,
							Color.RED,
							boat_dist,
							angle,
							boat_spd,
							island_r,
							delta,
							boatCams[i]
					);

					root.getChildren ( ).addAll ( boat );
					movableObjects.add ( boat );
				}
				wave++;
				if (wave == 6) {
					this.listener.onGameWon();
					this.gameOver = true;
				}
//				this.listener.onGameLost ( );
//				this.gameOver = true;
			} else if ( this.weapon != null ) {

				boolean boatHit = this.movableObjects.removeIf ( object -> {
					return object.handleCollision ( this.weapon ) && (object instanceof Boat) ;
				} );

				if ( boatHit ) {
					this.weapon.onCollision ( );
					this.weapon = null;
					if ( this.movableObjects.size ( ) == 0 ) {
//						this.listener.onGameWon ( );
//						this.gameOver = true;
					}

					currShips--;
					if (currShips == 0){
						boat_spd += 0.1;
						currShips = numShips;
						for (int i = 0; i < numShips; ++i ) {
							Random rand = new Random();
							double angle = 360 * rand.nextDouble();
							Boat boat = new Boat (
									root,
									boat_w,
									boat_h,
									boat_d,
									Color.RED,
									boat_dist,
									angle,
									boat_spd,
									island_r,
									delta,
									boatCams[i]
							);

							root.getChildren ( ).addAll ( boat );
							movableObjects.add ( boat );
						}
						wave++;
						if (wave == 6) {
							this.listener.onGameWon();
							this.gameOver = true;
						}

						Cannon myCannon = ((Cannon)cannon);
						Group ammo_root = myCannon.getAmmo_root();
						Circle vrh_metka = new Circle(10,20,10);
						Rectangle caura_metka = new Rectangle(4, 20, 12, 15);
						vrh_metka.setScaleX(0.6);
						double position = 0.95;
						int added = 10;
						if (myCannon.remaining_ammo > 10)
							added = 20 - myCannon.remaining_ammo;
						for (int i=0; i<myCannon.remaining_ammo; i++){
							position -= 0.02;
						}
						for (int i=0; i<added; i++){
							Shape metak = Shape.union(vrh_metka, caura_metka);
							metak.setFill(Color.YELLOW);
							metak.getTransforms().addAll(
									new Translate(scene_w * position, scene_h * 0.95)
							);
							ammo_root.getChildren().addAll(metak);
							position-=0.02;
						}
						myCannon.remaining_ammo+= added;

					}

					int txt_counter = 0;
					Group ammo_root = ((Cannon)cannon).getAmmo_root();
					for(int i=0; i<ammo_root.getChildren().size(); i++){
						if (ammo_root.getChildren().get(i) instanceof Text) {
							txt_counter++;
							if (txt_counter == 1){
								Text def_txt = ((Text) ammo_root.getChildren().get(i));
								def_txt.setText(String.valueOf(Integer.parseInt(def_txt.getText()) + 1));
								break;
							}
						}
					}
				}
			}
		}
		
		if ( this.weapon != null ) {
			this.weapon.update ( now );
		}

		if (now % 10000 ==0 && this.gameOver == false){
			for (int i=0; i<movableObjects.size(); i++){
				if (movableObjects.get(i) instanceof Boat){
					BoatCannonBall boatCannonBall = new BoatCannonBall(
							root,
							((Boat) movableObjects.get(i)).getAngle(),
							((Boat) movableObjects.get(i)).getPosition().getTx(),
							((Boat) movableObjects.get(i)).getPosition().getTz()
					);
					root.getChildren().addAll(boatCannonBall);
					addMoveableObject(boatCannonBall);
				}
			}
		}

		boolean bulletHit = this.movableObjects.removeIf( object -> {
			if (object.getBoundsInParent().intersects( cannon.getBoundsInParent() ) && (object instanceof BoatCannonBall)){
				((Cannon)cannon).decreaseHealth();
				root.getChildren().remove(object);
			}
			return object.getBoundsInParent().intersects( cannon.getBoundsInParent() ) && (object instanceof BoatCannonBall);
		} );

	}
	
	public synchronized boolean canAddWeapon ( ) {
		return this.gameOver == false && this.weapon == null;
	}
	
	public synchronized void setWeapon ( MovableObject weapon ) {
		this.weapon = weapon;
	}
}
