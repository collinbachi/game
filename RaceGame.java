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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


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

    private boolean crashed = false;


    /**
     * Returns name of the game.
     */
    public String getTitle () {
        return TITLE;
    }

    private void reset(){
        System.out.println("resetting...");
        distance = 0;
        barricades = Arrays.asList(350, 25, 420, 500, 455, 25);
        for (Obstacle o : barricadesList) removeActor(o);
        barricadesList = new ArrayList<Obstacle>();
        barricadesIndex = 0;
        crashed = false;
        background.setViewport(new Rectangle2D(90, 35, 800, 626));
        myTruck.reset();
        cop.reset();
        myTruck.setX(800 / 2 - myTruck.getBoundsInLocal().getWidth() / 2);
        myTruck.setY(500 / 2  - myTruck.getBoundsInLocal().getHeight() / 2);
        cop.setX(myTruck.getX());
        cop.setY(myTruck.getY() + 2 * myTruck.getHeight());
    }
    /**
     * Create the game's scene
     */
    public Scene init (int width, int height) {
        System.out.println("INITIALIZING");

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

        cop = new Cop(myTruck);
        cop.setHook(this);
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
        if (crashed) return;
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

    public void handleCollision(Actor a, Truck b){
        System.out.println("Collision!");
        b.gotoLabel("crash");
        crashed = true;
        KeyFrame crashFrame = new KeyFrame(Duration.millis(1000));
        Timeline crashAnimation = new Timeline();
        crashAnimation.setCycleCount(2);
        crashAnimation.getKeyFrames().add(crashFrame);
        crashAnimation.play();
        crashAnimation.setOnFinished( new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t){
                System.out.println("made it here?");
                reset();
            }
        });
    }

    public void removeActor(Actor a){
        root.getChildren().remove(a);
    }

    public Scene showSplash(int width, int height) {
        Group splashRoot = new Group();
        Scene mySplashScene = new Scene(splashRoot, width, height, Color.WHITE);
        // credz to Ian Guy for the cool painting
        Image splashImage = new Image(getClass().getClassLoader().getResourceAsStream("splash.jpg"));
        ImageView splash = new ImageView(splashImage);
        splashRoot.getChildren().add(splash);
        return mySplashScene;
    }
}
