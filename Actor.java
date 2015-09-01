import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.HashMap;


/**
 * Parent class for cars and powerups
 * 
 * @author D. Collin Bachi
 */
class Actor extends ImageView {

	private HashMap<String, Image> frames;

	public Actor(Image img){
		super(img);
		this.frames = new HashMap<String, Image>();
		this.frames.put("default", img);
	}

	/*public Actor(String s){
		super(new Image(getResourceAsStream(s)));
		this.frames = new HashMap<String, Image>();
		this.frames.put("default", img);
	}*/

	public void addFrameWithLabel(Image img, String label){
		this.frames.put(label, img);
	}

	public void gotoLabel(String label){
		if (this.frames.containsKey(label)){
			setImage(this.frames.get(label));
		}else{
			setImage(this.frames.get("default"));
		}
	}

}