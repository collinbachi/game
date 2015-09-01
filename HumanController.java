import javafx.scene.input.KeyCode;
import javafx.scene.Scene;


/**
 * Handles keyboard/mouse input from user
 * 
 * @author D. Collin Bachi
 */
class HumanController {

	private Truck truck;
	private Scene scene;

	public HumanController(Truck t, Scene s){
		truck = t;
		scene = s;
		myScene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
        myScene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));
	}

	private void handleKeyPress (KeyCode code) {
        switch (code) {
            case RIGHT:
                BOUNCER_SPEED = new Tuple(SPEED_CONSTANT,BOUNCER_SPEED.y);
                myBouncer.gotoLabel("right");
                break;
            case LEFT:
                BOUNCER_SPEED = new Tuple(-SPEED_CONSTANT,BOUNCER_SPEED.y);
                myBouncer.gotoLabel("left");
                break;
            case UP:
                BOUNCER_SPEED = new Tuple(BOUNCER_SPEED.x,-SPEED_CONSTANT); 
                myBouncer.gotoLabel("default");
                break;
            case DOWN:
                BOUNCER_SPEED = new Tuple(BOUNCER_SPEED.x,SPEED_CONSTANT); 
                myBouncer.gotoLabel("default");
                break;
            default:
                // do nothing
        }
    }

}