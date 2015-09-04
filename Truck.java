import javafx.scene.image.Image;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Class for main truck
 * 
 * @author D. Collin Bachi
 */
class Truck extends Actor {

	private final double TRUCK_SPEED = 60.0;
	private int bonus = 0;
	private double xvel = 0.0;
	private double yvel = 0.0;

	public void reset(){
		bonus = 0;
	    xvel = 0.0;
	  	yvel = 0.0;
	}

	public Truck(){
		super(new Image(Truck.class.getClassLoader().getResourceAsStream("truck.png")));
		Image left = new Image(getClass().getClassLoader().getResourceAsStream("left.png"));
		Image right = new Image(getClass().getClassLoader().getResourceAsStream("right.png"));
		Image crash = new Image(getClass().getClassLoader().getResourceAsStream("crash.png"));
		addFrameWithLabel(left, "left");
		addFrameWithLabel(right, "right");
		addFrameWithLabel(crash, "crash");
	}

	public void powerup(){
		bonus = 35;
		System.out.println("powerup");
		Timer timer = new Timer();
		timer.schedule( new TimerTask(){
			public void run(){
				bonus = 0;
				if (xvel > TRUCK_SPEED) xvel = TRUCK_SPEED;
				if (xvel < -TRUCK_SPEED) xvel = -TRUCK_SPEED;
				if (yvel > TRUCK_SPEED) yvel = TRUCK_SPEED;
				if (yvel < -TRUCK_SPEED) yvel = -TRUCK_SPEED;
				System.out.println("no more power");
			}
		}, 4000);
		System.out.println(bonus);
	}

	public void addVelocity(double deltax, double deltay){
		xvel += deltax * (TRUCK_SPEED + bonus);
		yvel += deltay * (TRUCK_SPEED + bonus);
	}

	public void setXVel(double newx){
		xvel = newx;
	}

	public void setYVel(double newy){
		yvel = newy;
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