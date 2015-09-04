import javafx.scene.Scene;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Controls AI vehicles
 * 
 * @author D. Collin Bachi
 */
class AIController {

	private Cop cop;
	private Scene scene;
	private boolean right_down = false;
	private boolean left_down = false;
	private boolean up_down = false;
	private boolean down_down = false;
    private String mode = "hide";
    private Truck truck;
    private int followDist = 150;

	public AIController(Cop c, Scene s, Truck t){
		cop = c;
		scene = s;
        truck = t;
		// sets the AI's loop
        KeyFrame frame = new KeyFrame(Duration.millis(100),
                                      e -> this.step());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
	}

    public void setMode(String s){
        mode = s;
    }

	private void step(){
        // Hide, random, strike, tail
        if (mode.equals("hide")) hide();
		if (mode.equals("random")) randomDecision();
        if (mode.equals("follow")) follow(150);
        if (mode.equals("fallbehind")){
            followDist+=8;
            follow(followDist);
        }
	}

    private void follow(int dist){
        driveTo((int)truck.getX(), (int)truck.getY() + dist);
    }

    private void hide(){
        driveTo((int)truck.getX(), (int)truck.getY() + 650);
    }

    private void driveTo(int x, int y){
        if (cop.getX() < x - cop.getWidth()/2){
            cop.addVelocity(1, 0);

        }
        if (cop.getX() > x){
            cop.addVelocity(-1, 0);       

        }
        if (Math.abs(cop.getX() - x) < 5){
            cop.setX(x);
            cop.setXVel(0);
        }
        if (cop.getY() < y){
            cop.addVelocity(0, 1);

        }
        if (cop.getY() > y){
            cop.addVelocity(0, -1);
        }
        if (Math.abs(cop.getY() - y) < 5){
            cop.setY(y);
            cop.setYVel(0);
        }
    }

	private void randomDecision(){
		int opt = (int) Math.floor(Math.random() * 8);
        if (Math.random() < .01){
            driveTo((int)(truck.getX() + (Math.random() - .5)*650), (int)truck.getY());
        }
		if (Math.random() < .95) opt = -1; //Do nothing most of the time
		switch (opt) {
            case 0:
                cop.setVelocity(-1,0);
                break;
            case 1:
                cop.setVelocity(-1,-1);
                break;
            case 2:
                cop.setVelocity(0,-1);
                break;
            case 3:
                cop.setVelocity(1,-1);
                break;
            case 4:
                cop.setVelocity(1,0); 
                break;
            case 5:
                cop.setVelocity(1,1); 
                break;
            case 6:
                cop.setVelocity(0,1); 
                break;
            case 7:
                cop.setVelocity(-1,1);
                break;
            default:
                // do nothing
        }
        //Check for collisions, and reverse if so
        if (cop.getX() < scene.getX()){
        	cop.setVelocity(1, 0);
        	System.out.println("Cop reverse");

        }
        if (cop.getX() > scene.getX() + scene.getWidth() - cop.getWidth()){
        	cop.setVelocity(-1, 0);       
        	System.out.println("Cop reverse");

        }
        if (cop.getY() < scene.getY()){
        	cop.setVelocity(0, 1);
        	System.out.println("Cop reverse");

        }
        if (cop.getY() > scene.getY() + scene.getHeight() - cop.getHeight()){
        	cop.setVelocity(0, -1);
        	System.out.println("Cop reverse");

        }
	}
}