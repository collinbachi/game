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
    private static double SPEED_CONSTANT = 60.0;

    private Scene myScene;
    private Truck myTruck;
    private HumanController humanController;
    private ImageView background;


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
        // Load background and set animation
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

        myTruck = new Truck();
        // x and y represent the top left corner, so center it
        myTruck.setX(width / 2 - myTruck.getBoundsInLocal().getWidth() / 2);
        myTruck.setY(height / 2  - myTruck.getBoundsInLocal().getHeight() / 2);
        // order added to the group is the order in whuch they are drawn
        root.getChildren().add(background);
        root.getChildren().add(myTruck);
       
        humanController = new HumanController(myTruck, myScene);
        return myScene;
    }

    /**
     * Update the game world
     */
    public void step (double elapsedTime) {
        // update attributes
        myTruck.step(elapsedTime);
    }
}
