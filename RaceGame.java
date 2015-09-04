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
import javafx.scene.text.Text;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;


/**
 * Separate the game code from some of the boilerplate code.
 * 
 * @author D. Collin Bachi
 */
class RaceGame {
    public static final String TITLE = "Ridin' Dirty";
    private static double SPEED_CONSTANT = 60.0;
    private static int SCROLL_SPEED = 4;

    private Scene myScene;
    private Truck myTruck;
    private Cop cop;
    private HumanController humanController;
    private AIController copController;
    private ImageView background;
    private Rectangle2D backgroundViewport;

    private int distance = 0;
    private List<Integer> barricades = Arrays.asList(350, 25, 420, 500, 455, 25);
    private ArrayList<Obstacle> barricadesList = new ArrayList<Obstacle>();
    private int barricadesIndex = 0;
    private Image barricadeImage;
    private Text scoreBoard;

    private Group root;

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
        root = new Group();
        // Create a place to see the shapes
        myScene = new Scene(root, width, height, Color.WHITE);
        // Load background and set animation
        Image roadImage = new Image(getClass().getClassLoader().getResourceAsStream("road.jpg"));
        background = new ImageView(roadImage);
        backgroundViewport = new Rectangle2D(90, 35, 800, 626);
        background.setViewport(backgroundViewport);
        /*Animation animation = new SpriteAnimation(
                background,
                Duration.millis(750),
                726, 1,
                0, -1,
                800, 500
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play(); */

        myTruck = new Truck();
        // x and y represent the top left corner, so center it
        myTruck.setX(width / 2 - myTruck.getBoundsInLocal().getWidth() / 2);
        myTruck.setY(height / 2  - myTruck.getBoundsInLocal().getHeight() / 2);

        cop = new Cop();
        cop.setX(myTruck.getX());
        cop.setY(myTruck.getY() + 2 * myTruck.getHeight());

        barricadeImage = new Image(getClass().getClassLoader().getResourceAsStream("barricade.jpg"));

        scoreBoard = new Text(10, 50, "3189321m");
        // order added to the group is the order in whuch they are drawn
        root.getChildren().add(background);
        root.getChildren().add(myTruck);
        root.getChildren().add(cop);
        root.getChildren().add(scoreBoard);
       
        humanController = new HumanController(myTruck, myScene);
        copController = new AIController(cop, myScene);
        return myScene;
    }

    /**
     * Update the game world
     */
    public void step (double elapsedTime) {
        // update attributes
        myTruck.step(elapsedTime);
        cop.step(elapsedTime);
        
        for (Obstacle b : barricadesList) b.step();

        distance++;
        scoreBoard.setText(distance + "m");

        if (barricadesIndex < barricades.size() && barricades.get(barricadesIndex)==distance){
            Obstacle newBarricade = new Obstacle(myTruck);
            newBarricade.setX(barricades.get(barricadesIndex+1));
            newBarricade.setY(-150);
            newBarricade.setVelocity(0, SCROLL_SPEED);
            newBarricade.setHook(this);
            barricadesList.add(newBarricade);
            root.getChildren().add(newBarricade);
            barricadesIndex+=2;
        }

        // Horribly implemented
        if (background.viewportProperty().getValue().getMinY() > 5){
            background.setViewport(new Rectangle2D(
                    background.viewportProperty().getValue().getMinX(),
                    background.viewportProperty().getValue().getMinY()-SCROLL_SPEED,
                    background.viewportProperty().getValue().getWidth(),
                    background.viewportProperty().getValue().getHeight()
                ));
        }else{
            background.setViewport(new Rectangle2D(
                    background.viewportProperty().getValue().getMinX(),
                    background.viewportProperty().getValue().getMinY()+558,
                    background.viewportProperty().getValue().getWidth(),
                    background.viewportProperty().getValue().getHeight()
                ));
        }
    }

    public void handleCollision(Actor a, Actor b){
        System.out.println("Collision!");
    }
}
