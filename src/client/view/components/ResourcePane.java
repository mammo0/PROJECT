package client.view.components;

import global.IExpandableNode;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * This class is the input mask of the resources
 * 
 * @author Ammon
 *
 */
public class ResourcePane extends AnchorPane implements IExpandableNode {
	
	@FXML
	private TextField txtResourceName;
	@FXML
	private ChoiceBox<String> cmbIntExt;
	@FXML
	private TextField txtAvailability;
	@FXML
	private TextField txtSkillAmount;
	
	
	
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
		cmbIntExt.getItems().addAll("Intern","Extern");
		cmbIntExt.getSelectionModel().selectFirst();
	}
	
	
	
	
	/**
	 * Get the resource name
	 * @return the resourceName
	 */
	public String getResourceName(){
		return txtResourceName.getText();
	}
	
	/**
	 * Is the resource internal or external
	 * @return boolean
	 */
	public boolean isIntern(){
		if(cmbIntExt.getSelectionModel().selectedItemProperty().get() == "Intern")
			return true;
		else
			return false;
	}
	
	/**
	 * Get the availability of a resource
	 * @return the availability
	 */
	public int getAvailability(){
		try{
			return Integer.valueOf(txtAvailability.getText());
		}catch (Exception e){
			return -1;
		}
	}
	
	/**
	 * Get the skill amount
	 * @return the skillAmount
	 */
	public int getSkillAmount(){
		try{
			return Integer.valueOf(txtSkillAmount.getText());
		}catch (Exception e){
			return -1;
		}
	}




	@Override
	public void setParentNode(Node parent) {}




	@Override
	public void removeNode() {}
}
