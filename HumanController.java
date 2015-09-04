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
	private boolean right_down = false;
	private boolean left_down = false;
	private boolean up_down = false;
	private boolean down_down = false;

	public HumanController(Truck t, Scene s){
		truck = t;
		scene = s;
		scene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
        scene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));
	}

	private void handleKeyPress (KeyCode code) {
        switch (code) {
            case RIGHT:
                if (!right_down) truck.addVelocity(1,0);
                right_down = true;
                break;
            case LEFT:
                if (!left_down) truck.addVelocity(-1,0);
                left_down = true;
                break;
            case UP:
                if (!up_down) truck.addVelocity(0,-1); 
                up_down = true;
                break;
            case DOWN:
                if (!down_down) truck.addVelocity(0,1); 
                down_down = true;
                break;
            case SPACE:
                 truck.cheat();
                 break;
            default:
                // do nothing
        }
    }

	private void handleKeyRelease (KeyCode code) {
        switch (code) {
            case RIGHT:
                truck.addVelocity(-1,0);
                right_down = false;
                break;
            case LEFT:
                truck.addVelocity(1,0);
                left_down = false;
                break;
            case UP:
                truck.addVelocity(0,1);
                up_down = false;
                break;
            case DOWN:
                truck.addVelocity(0,-1); 
                down_down = false;
                break;
            default:
                // do nothing
        }
    }

}