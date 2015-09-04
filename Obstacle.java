import javafx.scene.image.Image;

/**
 * Class for obstacles
 * 
 * @author D. Collin Bachi
 */
class Obstacle extends Actor {

	private double xvel = 0.0;
	private double yvel = 0.0;
	private Truck truck;
	private RaceGame raceGame;

	public Obstacle(Truck t){
		super(new Image(Obstacle.class.getClassLoader().getResourceAsStream("barricade.png")));
		truck = t;
	}

	public void setVelocity(double deltax, double deltay){
		xvel = deltax;
		yvel = deltay;
	}

	public void step(){
		this.setY(this.getY() + yvel);
		if (this.getBoundsInParent().intersects(truck.getBoundsInParent())) {
            raceGame.handleCollision(this, truck);
        }
        if (this.getY() > 900) raceGame.removeActor(this);
	}

	public void setHook(RaceGame rg){
		raceGame = rg;
	}

}