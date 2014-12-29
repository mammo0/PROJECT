package client.view.components;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * This class is the input mask of the skills
 * 
 * @author Ammon
 *
 */
public class SkillPane extends AnchorPane {
	
	@FXML
	private TextField skillName;
	@FXML
	private TextField dayRateInt;
	@FXML
	private TextField dayRateExt;
	
	
	/**
	 * The constructor
	 */
	public SkillPane() { 
	     FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/SkillPane.fxml"));
	     
	     // apply this class as root and controller
	     fxmlLoader.setRoot(this);
	     fxmlLoader.setController(this);
	     
	     // load the fxml
	     try {
	    	 fxmlLoader.load();
	     } catch (IOException e) { 
	         e.printStackTrace();
	     }
    }
	
	// This method casts a String to a float
	private float castStringToFloat(String input){
		try{
			return Float.valueOf(input);
		}catch (Exception e){
			return Float.NaN;
		}
	}



	/**
	 * Get the name of the skill
	 * @return the skillName
	 */
	public String getSkillName() {
		return skillName.getText();
	}

	/**
	 * Get the internal day rate of the skill
	 * @return the dayRateInt
	 */
	public float getDayRateInt() {
		return castStringToFloat(dayRateInt.getText());
	}

	/**
	 * Get the external day rate of the skill
	 * @return the dayRateExt
	 */
	public float getDayRateExt() {
		return castStringToFloat(dayRateExt.getText());
	}
}