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

	public AIController(Cop c, Scene s){
		cop = c;
		scene = s;

		// sets the AI's loop
        KeyFrame frame = new KeyFrame(Duration.millis(1000),
                                      e -> this.step());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
	}

	private void step(){
		int opt = (int) Math.floor(Math.random() * 8);
		if (Math.random() < .8) opt = -1; //Do nothing most of the time
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
        }
        if (cop.getX() > scene.getX() + scene.getWidth() - cop.getWidth()){
        	cop.setVelocity(-1, 0);
        }
        if (cop.getY() < scene.getY()){
        	cop.setVelocity(0, 1);
        }
        if (cop.getY() > scene.getY() + scene.getHeight() - cop.getHeight()){
        	cop.setVelocity(0, -1);
        }
	}
}