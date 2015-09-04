import javafx.scene.image.Image;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Class for main truck
 * 
 * @author D. Collin Bachi
 */
class Truck extends Actor {

	private final double TRUCK_SPEED = 90.0;
	private double bonus = 1;
	private double xvel = 0.0;
	private double yvel = 0.0;
	private boolean stopping = false;
	private boolean invincible = false;

	public void reset(){
		bonus = 1;
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

	public void cheat(){
		invincible = true;
	}
	public boolean isInvincible(){
		return invincible;
	}

	public void powerup(){
		if (bonus==1) bonus = 1.5;
			else bonus += 1;
		System.out.println("powerup");
		Timer timer = new Timer();
		timer.schedule( new TimerTask(){
			public void run(){
				bonus = 1;
				System.out.println("no more power");
			}
		}, 7000);
		System.out.println(bonus);
	}

	public void stop(){
		if (stopping) return;
		stopping=true;
		gotoLabel("straight");
		xvel=0;
		yvel=-90;
	}

	public void addVelocity(double deltax, double deltay){
		if (stopping) return;
		xvel += deltax * TRUCK_SPEED;
		yvel += deltay * TRUCK_SPEED;
	}

	public void step(double elapsedTime){
		if (stopping){
			yvel += 6;
			if (yvel>0) yvel=0;
		}
		this.setX(this.getX() + xvel * elapsedTime * bonus);
		this.setY(this.getY() + yvel * elapsedTime * bonus);
		if (xvel > 0){
			this.gotoLabel("right");
		}else if (xvel == 0){
			this.gotoLabel("default");
		}else if (xvel < 0){
			this.gotoLabel("left");
		}
	}

}