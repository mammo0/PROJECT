package client.view.components;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * This class is the input mask of the resources
 * 
 * @author Ammon
 *
 */
public class ResourcePane extends AnchorPane {
	
	@FXML
	private TextField resourceName;
	@FXML
	private ChoiceBox<String> intExt;
	@FXML
	private TextField availability;
	@FXML
	private TextField skillAmount;
	
	
	
	/**
	 * The Constructor
	 */
	public ResourcePane() {
		// load the resource pane fxml
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/ResourcePane.fxml"));
		
		// apply this class as root and controller
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		// load the fxml
		try {
			fxmlLoader.load();
		} catch (IOException e) { 
		    e.printStackTrace();
		}
		
		// finalize the choice box
		intExt.getItems().addAll("Intern","Extern");
		intExt.getSelectionModel().selectFirst();
	}
	
	
	
	
	/**
	 * Get the resource name
	 * @return the resourceName
	 */
	public String getResourceName(){
		return resourceName.getText();
	}
	
	/**
	 * Is the resource internal or external
	 * @return boolean
	 */
	public boolean isIntern(){
		if(intExt.getSelectionModel().selectedItemProperty().get() == "Intern")
			return true;
		else
			return false;
	}
	
	/**
	 * Get the availability of a resource
	 * @return the availability
	 */
	public String getAvailability(){
		return availability.getText();
	}
	
	/**
	 * Get the skill amount
	 * @return the skillAmount
	 */
	public int getSkillAmount(){
		try{
			return Integer.valueOf(skillAmount.getText());
		}catch (Exception e){
			return -1;
		}
	}
}
