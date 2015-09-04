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
 * This class holds much of the game logic and keeps track of the actors.
 * 
 * @author D. Collin Bachi
 */
class RaceGame {
    public static final String TITLE = "Redneck Racer";
    private static int SCROLL_SPEED = 4;

    private Group root;
    private Scene myScene;
    private Truck myTruck;
    private Cop cop;
    private HumanController humanController;
    private AIController copController;
    private ImageView background;
    private Rectangle2D backgroundViewport;
    private Image tacoImage;
    private ImageView tacoBell;
    private Text scoreBoard;
    private Text displayText;
    private int distance = 0;
    private boolean crashed = false;

    private List<Integer> barricades = Arrays.asList(350, 25, 420, 500, 455, 25, 550, 65, 625, 105, 675, 225, 700, 275, 750, 45, 1000, 100, 1200, 600, 1400, 175, 1550, 600, 1600, 525, 1700, 400, 2300, 100, 2350, 275, 2375, 15, 2425, 600, 2475, 500, 2550, 325, 2700, 15);
    private ArrayList<Obstacle> barricadesList = new ArrayList<Obstacle>();
    private int barricadesIndex = 0;

    private List<Integer> powerups = Arrays.asList(385, 375, 455, 325, 550, 365, 575, 600, 1000, 700, 1100, 375, 1870, 400, 1950, 470, 2320, 580, 2370, 480, 2900, 400);
    private ArrayList<Powerup> powerupsList = new ArrayList<Powerup>();
    private int powerupsIndex = 0;


    /**
     * Returns name of the game.
     */
    public String getTitle () {
        return TITLE;
    }

    /**
     * Resets game after a loss.
     */
    private void reset(){
        myTruck.reset();
        myTruck.setX(800 / 2 - myTruck.getBoundsInLocal().getWidth() / 2);
        myTruck.setY(500 / 2  - myTruck.getBoundsInLocal().getHeight() / 2);
        cop.reset();
        cop.setX(myTruck.getX());
        cop.setY(myTruck.getY() + 2 * myTruck.getHeight() + 650);
        distance = 0;
        crashed = false;

        for (Obstacle o : barricadesList) removeActor(o);
        barricadesList = new ArrayList<Obstacle>();
        barricadesIndex = 0;

        for (Powerup p : powerupsList) removeActor(p);
        powerupsList = new ArrayList<Powerup>();
        powerupsIndex = 0;

        background.setViewport(new Rectangle2D(90, 35, 800, 626));
    }

    /**
     * Initialize game world
     */
    public Scene init (int width, int height) {
        // Create a scene graph to organize the scene
        root = new Group();
        myScene = new Scene(root, width, height, Color.WHITE);

        // Load background and set animation
        Image roadImage = new Image(getClass().getClassLoader().getResourceAsStream("road.jpg"));
        background = new ImageView(roadImage);
        backgroundViewport = new Rectangle2D(90, 35, 800, 626);
        background.setViewport(backgroundViewport);

        myTruck = new Truck();
        myTruck.setX(width / 2 - myTruck.getBoundsInLocal().getWidth() / 2);
        myTruck.setY(height / 2  - myTruck.getBoundsInLocal().getHeight() / 2);

        cop = new Cop(myTruck);
        cop.setHook(this);
        cop.setX(myTruck.getX());
        cop.setY(myTruck.getY() + 2 * myTruck.getHeight() + 650);

        scoreBoard = new Text(10, 50, "3189321m");
        displayText = new Text(10, 22, "Hi there");

        root.getChildren().add(background);
        root.getChildren().add(myTruck);
        root.getChildren().add(cop);
        root.getChildren().add(scoreBoard);
        root.getChildren().add(displayText);
       
        humanController = new HumanController(myTruck, myScene);
        copController = new AIController(cop, myScene, myTruck);
        return myScene;
    }

    /**
     * Update the game world -- Main game loop
     */
    public void step (double elapsedTime) {
        if (crashed) return;
        myTruck.step(elapsedTime);
        cop.step(elapsedTime);
        checkTacoBell();
        handleObstacles();
        handlePowerups();    
        triggerGameEvents();
        updateBackground();
    }

    /**
     * Updates the scrolling background.
     */
    public void updateBackground(){
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

    /**
     * Triggers game events sequentially, and updates text displays.
     */
    public void triggerGameEvents(){
        distance++;
        scoreBoard.setText(distance + "m");

        if (distance > 2500 && distance < 3500){
            displayText.setText("Lookout! POLICE!");
            copController.setMode("follow");
        }else if(distance>3500 && distance < 5000){
            displayText.setText("Take evasive manuevers!");
            copController.setMode("random");
            cop.setBonus(1.6);
        }else if(distance>5000){
            displayText.setText("I think you lost him. Woohoo!");
            copController.setMode("fallbehind");
        }
    }

    /**
     * Draws and updates barricades.
     */
    public void handleObstacles(){
        for (Obstacle b : barricadesList) b.step();
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

    }

    /**
     * Draws and updates powerups
     */
    public void handlePowerups(){
        for (Powerup p : powerupsList) p.step();
        if (powerupsIndex < powerups.size() && powerups.get(powerupsIndex)==distance){
            Powerup newPowerup = new Powerup(myTruck);
            newPowerup.setX(powerups.get(powerupsIndex+1));
            newPowerup.setY(-150);
            newPowerup.setVelocity(0, SCROLL_SPEED);
            newPowerup.setHook(this);
            powerupsList.add(newPowerup);
            root.getChildren().add(newPowerup);
            powerupsIndex+=2;
        }
    }

    /**
     * Checks for win state, and triggers necessary animations.
     */
    public void checkTacoBell(){
        if (distance>5300){
            displayText.setText("You Win!!!");
            if (tacoBell==null){
                tacoImage = new Image(getClass().getClassLoader().getResourceAsStream("Taco-Bell.jpg"));
                tacoBell = new ImageView(tacoImage);
                tacoBell.setX(0);
                tacoBell.setY(0-tacoBell.getBoundsInLocal().getHeight());
                root.getChildren().add(tacoBell);
            }
            if (tacoBell.getY() < 0) tacoBell.setY(tacoBell.getY()+1);
            myTruck.stop();
            if (SCROLL_SPEED > 0) SCROLL_SPEED--;
        }
    }

    /**
     * This method is called when an obstacle collides with the player
     */
    public void handleCollision(Actor a, Truck b){
        if (b.isInvincible()) return;
        b.gotoLabel("crash");
        crashed = true;
        KeyFrame crashFrame = new KeyFrame(Duration.millis(1000));
        Timeline crashAnimation = new Timeline();
        crashAnimation.setCycleCount(2);
        crashAnimation.getKeyFrames().add(crashFrame);
        crashAnimation.play();
        crashAnimation.setOnFinished( new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t){
                reset();
            }
        });
    }

    public void removeActor(Actor a){
        root.getChildren().remove(a);
    }

    /**
     * Returns the splash scene to the Main class
     */
    public Scene showSplash(int width, int height) {
        Group splashRoot = new Group();
        Scene mySplashScene = new Scene(splashRoot, width, height, Color.WHITE);
        Image splashImage = new Image(getClass().getClassLoader().getResourceAsStream("splash.jpg"));
        ImageView splash = new ImageView(splashImage);
        splashRoot.getChildren().add(splash);
        return mySplashScene;
    }
}
