import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.geometry.Rectangle2D;
import javafx.animation.Animation;
import javafx.util.Duration;


/**
 * Separate the game code from some of the boilerplate code.
 * 
 * @author D. Collin Bachi
 */
class RaceGame {
    public static final String TITLE = "Ridin' Dirty";
    public static final int KEY_INPUT_SPEED = 5;
    private static final double GROWTH_RATE = 1.1;
    private static double SPEED_CONSTANT = 60.0;
    private static Tuple BOUNCER_SPEED = new Tuple(0,0);

    private Scene myScene;
    private Truck myBouncer;
    HumanController humanController;
    private ImageView background;
    private Rectangle myTopBlock;
    private Rectangle myBottomBlock;


    /**
     * Returns name of the game.
     */
    public String getTitle () {
        return TITLE;
    }

    /**
     * Create the game's scene
     */
    public Scene init (int width, int height) {
        // Create a scene graph to organize the scene
        Group root = new Group();
        // Create a place to see the shapes
        myScene = new Scene(root, width, height, Color.WHITE);
        // Make some shapes and set their properties
        Image roadImage = new Image(getClass().getClassLoader().getResourceAsStream("road.jpg"));
        background = new ImageView(roadImage);
        Rectangle2D viewportRect = new Rectangle2D(90, 35, 800, 500);
        background.setViewport(viewportRect);


        Animation animation = new SpriteAnimation(
                background,
                Duration.millis(250),
                500, 1,
                0, -1,
                800, 500
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();


        //Image image = new Image(getClass().getClassLoader().getResourceAsStream("truck.png"));
        //myBouncer = new ImageView(image);
        myBouncer = new Truck();
        // x and y represent the top left corner, so center it
        myBouncer.setX(width / 2 - myBouncer.getBoundsInLocal().getWidth() / 2);
        myBouncer.setY(height / 2  - myBouncer.getBoundsInLocal().getHeight() / 2);
        myTopBlock = new Rectangle(width / 2 - 25, height / 2 - 100, 50, 50);
        myTopBlock.setFill(Color.RED);
        myBottomBlock = new Rectangle(width / 2 - 25, height / 2 + 50, 50, 50);
        myBottomBlock.setFill(Color.BISQUE);
        // order added to the group is the order in whuch they are drawn
        root.getChildren().add(background);
        root.getChildren().add(myBouncer);
        root.getChildren().add(myTopBlock);
        root.getChildren().add(myBottomBlock);
        // Respond to input
        /*myScene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
        myScene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));
        myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));*/
        humanController = new HumanController(myBouncer, myScene);
        return myScene;
    }

    /**
     * Change properties of shapes to animate them
     * 
     * Note, there are more sophisticated ways to animate shapes,
     * but these simple ways work too.
     */
    public void step (double elapsedTime) {
        // update attributes
        myBouncer.step(elapsedTime);
        /*myBouncer.setX(myBouncer.getX() + BOUNCER_SPEED.x * elapsedTime);
        myBouncer.setY(myBouncer.getY() + BOUNCER_SPEED.y * elapsedTime);
	   if (myBouncer.getX()+myBouncer.getBoundsInLocal().getWidth() > myScene.getWidth() || myBouncer.getX() < 0){
		  BOUNCER_SPEED = new Tuple(BOUNCER_SPEED.x * -1, BOUNCER_SPEED.y * -1);
	   }*/
        myTopBlock.setRotate(myBottomBlock.getRotate() - 1);
        myBottomBlock.setRotate(myBottomBlock.getRotate() + 1);
        
        // check for collisions
        // with shapes, can check precisely
        Shape intersect = Shape.intersect(myTopBlock, myBottomBlock);
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            myTopBlock.setFill(Color.MAROON);
        }
        else {
            myTopBlock.setFill(Color.RED);
        }
        // with images can only check bounding box
        if (myBottomBlock.getBoundsInParent().intersects(myBouncer.getBoundsInParent())) {
            myBottomBlock.setFill(Color.BURLYWOOD);
        }
        else {
            myBottomBlock.setFill(Color.BISQUE);
        }
    }


    // What to do each time a key is pressed
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

    private void handleKeyRelease (KeyCode code) {
        switch (code) {
            case RIGHT:
                BOUNCER_SPEED = new Tuple(0,BOUNCER_SPEED.y);
                break;
            case LEFT:
                BOUNCER_SPEED = new Tuple(0,BOUNCER_SPEED.y);
                break;
            case UP:
                BOUNCER_SPEED = new Tuple(BOUNCER_SPEED.x,0);
                break;
            case DOWN:
                BOUNCER_SPEED = new Tuple(BOUNCER_SPEED.x,0);
                break;
            default:
                // do nothing
        }
    }

    // What to do each time a key is pressed
    private void handleMouseInput (double x, double y) {
        if (myBottomBlock.contains(x, y)) {
            myBottomBlock.setScaleX(myBottomBlock.getScaleX() * GROWTH_RATE);
            myBottomBlock.setScaleY(myBottomBlock.getScaleY() * GROWTH_RATE);
        }
    }
}
