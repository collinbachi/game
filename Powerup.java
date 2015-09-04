import javafx.scene.image.Image;


/**
 * Class for powerups
 * 
 * @author D. Collin Bachi
 */
class Powerup extends Actor {

	private double xvel = 0.0;
	private double yvel = 0.0;
	private Truck truck;
	private RaceGame raceGame;
	private boolean givenMyGift = false;

	public Powerup(Truck t){
		super(new Image(Powerup.class.getClassLoader().getResourceAsStream("powerup.png")));
		truck = t;
	}

	public void setVelocity(double deltax, double deltay){
		xvel = deltax;
		yvel = deltay;
	}

	public void step(){
		this.setY(this.getY() + yvel);
		if (this.getBoundsInParent().intersects(truck.getBoundsInParent())) {
			if (!givenMyGift) truck.powerup();
			givenMyGift = true;
            raceGame.removeActor(this);
        }
        if (this.getY() > 900) raceGame.removeActor(this);
	}

	public void setHook(RaceGame rg){
		raceGame = rg;
	}

}