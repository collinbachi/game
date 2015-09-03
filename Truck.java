import javafx.scene.image.Image;


/**
 * Class for main truck
 * 
 * @author D. Collin Bachi
 */
class Truck extends Actor {

	private static double TRUCK_SPEED = 60.0;
	private double xvel = 0.0;
	private double yvel = 0.0;

	public Truck(){
		super(new Image(Truck.class.getClassLoader().getResourceAsStream("truck.png")));
		Image left = new Image(getClass().getClassLoader().getResourceAsStream("left.png"));
		Image right = new Image(getClass().getClassLoader().getResourceAsStream("right.png"));
		addFrameWithLabel(left, "left");
		addFrameWithLabel(right, "right");
	}

	public void addVelocity(double deltax, double deltay){
		xvel += deltax * TRUCK_SPEED;
		yvel += deltay * TRUCK_SPEED;
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
	}

}