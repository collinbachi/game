import javafx.scene.image.Image;


/**
 * Class for cop car
 * 
 * @author D. Collin Bachi
 */
class Cop extends Actor {

	private static double COP_SPEED = 75.0;
	private double xvel = 0.0;
	private double yvel = 0.0;
	private Truck truck;
	private RaceGame raceGame;

	public void reset(){
		COP_SPEED = 75.0;
	    xvel = 0.0;
	  	yvel = 0.0;
	}

	public Cop(Truck t){
		super(new Image(Cop.class.getClassLoader().getResourceAsStream("straightcop.png")));
		Image left = new Image(getClass().getClassLoader().getResourceAsStream("leftcop.png"));
		Image right = new Image(getClass().getClassLoader().getResourceAsStream("rightcop.png"));
		addFrameWithLabel(left, "left");
		addFrameWithLabel(right, "right");
		truck = t;
	}

	public void setVelocity(double deltax, double deltay){
		xvel = deltax * COP_SPEED;
		yvel = deltay * COP_SPEED;
	}

	public void setHook(RaceGame rg){
		raceGame = rg;
	}

	public void step(double elapsedTime){
		this.setX(this.getX() + xvel * elapsedTime);
		this.setY(this.getY() + yvel * elapsedTime);
		if (xvel > 0){
			this.gotoLabel("right");
		}else if (xvel == 0){
			this.gotoLabel("default");
		}else if (xvel < 0){
			this.gotoLabel("left");
		}

		if (this.getBoundsInParent().intersects(truck.getBoundsInParent())) {
            raceGame.handleCollision(this, truck);
        }
	}

}