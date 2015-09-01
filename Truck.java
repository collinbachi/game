import javafx.scene.image.Image;


/**
 * Class for main truck
 * 
 * @author D. Collin Bachi
 */
class Truck extends Actor {

	public Truck(){
		super(new Image(Truck.class.getClassLoader().getResourceAsStream("truck.png")));
		Image left = new Image(getClass().getClassLoader().getResourceAsStream("left.png"));
		Image right = new Image(getClass().getClassLoader().getResourceAsStream("right.png"));
		addFrameWithLabel(left, "left");
		addFrameWithLabel(right, "right");
	}

	/*public static Truck create(){
		Image straight = new Image(getClass().getClassLoader().getResourceAsStream("truck.png"));
		Truck();
	}*/

}