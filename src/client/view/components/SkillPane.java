package client.view.components;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import client.view.IExpandableNode;
import client.view.ITester;
import client.view.InputTester;

/**
 * This class is the input mask of the skills
 * 
 * @author Ammon
 *
 */
public class SkillPane extends AnchorPane implements IExpandableNode, ITester {
	
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
	    
	    // add an input tester to the text fields
	    dayRateInt.textProperty().addListener(new InputTester(this, dayRateInt));
	    dayRateExt.textProperty().addListener(new InputTester(this, dayRateExt));
    }
	
	// This method casts a String to a float
	private float castStringToFloat(String input){
		try{
			return Float.valueOf(input);
		}catch (Exception e){
			return 0f;
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

	
	@Override
	public void setParentNode(Node parent) {}

	@Override
	public void removeNode() {}

	@Override
	public boolean checkInput(Node node) {
		if(node.equals(dayRateInt) || node.equals(dayRateExt)){
			TextField txtField = (TextField) node;
			if(txtField.getText().isEmpty())
				return true;
			
			try{
				Float.valueOf(txtField.getText());
				return true;
			}catch (Exception e){
				return false;
			}
		}else
			return false;
	}
}