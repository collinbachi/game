import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * This is the main program, it is basically boilerplate to create
 * an animated scene.
 * 
 * @author D. Collin Bachi
 */
public class Main extends Application {
    public static final int SIZE = 800;
    public static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    private RaceGame myGame;


    /**
     * Set things up at the beginning.
     */
    @Override
    public void start (Stage s) {
        // create your own game here
        myGame = new RaceGame();
        s.setTitle(myGame.getTitle());

        Scene splashScene = myGame.showSplash(800, 500);
        s.setScene(splashScene);
        s.show();

        // sets the game's loop
        KeyFrame splashFrame = new KeyFrame(Duration.millis(1000));
        Timeline splashAnimation = new Timeline();
        splashAnimation.setCycleCount(2);
        splashAnimation.getKeyFrames().add(splashFrame);
        splashAnimation.play();
        splashAnimation.setOnFinished( new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t){
                // attach game to the stage and display it
                Scene scene = myGame.init(800, 500);
                s.setScene(scene);
                s.show();

                // sets the game's loop
                KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                                              e -> myGame.step(SECOND_DELAY));
                Timeline animation = new Timeline();
                animation.setCycleCount(Timeline.INDEFINITE);
                animation.getKeyFrames().add(frame);
                animation.play();
            }
        });

    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
