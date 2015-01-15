package client.view.components;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import client.view.IExpandableNode;
import client.view.ITester;
import client.view.InputTester;
import client.view.controller.ViewController;

/**
 * This class is the input mask of the skills
 * 
 * @author Ammon
 *
 */
public class SkillPane extends AnchorPane implements IExpandableNode, ITester {
	
	@FXML
	private TextField txtSkillName;
	@FXML
	private TextField txtDayRateInt;
	@FXML
	private TextField txtDayRateExt;
	
	
	private boolean skillNameChanged;
	
	private ViewController viewController;
	
	
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
	    
	    viewController = ViewController.getInstance(ViewController.class);
	    
	    
	    // update the third screen (resources) if a new skill is added
	    txtSkillName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue != null && !newValue.equals(oldValue))
					skillNameChanged = true;
			}
		});
	    txtSkillName.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue && skillNameChanged){
					viewController.updateResoources();
					skillNameChanged = false;
				}
			}
		});
	    
	    // add an input tester to the text fields
	    txtDayRateInt.textProperty().addListener(new InputTester(this, txtDayRateInt));
	    txtDayRateExt.textProperty().addListener(new InputTester(this, txtDayRateExt));
    }
	
	// This method casts a String to a float
	private float castStringToFloat(String input){
		try{
			return Float.valueOf(input);
		}catch (Exception e){
			return 0f;
		}
	}
	
	
	@Override
	public Node getNode(String fxmlName) {
		switch (fxmlName) {
			case "txtSkillName":
				return txtSkillName;
			case "txtDayRateInt":
				return txtDayRateInt;
			case "txtDayRateExt":
				return txtDayRateExt;
			default:
				return null;
		}
	}



	/**
	 * Get the name of the skill
	 * @return the skillName
	 */
	public String getSkillName() {
		return txtSkillName.getText();
	}

	/**
	 * Get the internal day rate of the skill
	 * @return the dayRateInt
	 */
	public float getDayRateInt() {
		return castStringToFloat(txtDayRateInt.getText());
	}

	/**
	 * Get the external day rate of the skill
	 * @return the dayRateExt
	 */
	public float getDayRateExt() {
		return castStringToFloat(txtDayRateExt.getText());
	}

	
	@Override
	public void setParentNode(Node parent) {}

	@Override
	public void removeNode() {}

	@Override
	public boolean checkInput(Node node) {
		if(node.equals(txtDayRateInt) || node.equals(txtDayRateExt)){
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