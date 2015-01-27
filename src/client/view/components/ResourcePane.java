package client.view.components;

import java.io.IOException;

import client.view.IExpandableNode;
import client.view.ITester;
import client.view.InputTester;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public class ResourcePane extends AnchorPane implements IExpandableNode, ITester {
	
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
		cmbIntExt.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.equals("Extern")){
					txtAvailability.setText(String.valueOf(100));
					txtAvailability.setEditable(false);
				}else if(newValue.equals("Intern") && oldValue.equals("Extern")){
					txtAvailability.setEditable(true);
					txtAvailability.setText("");
				}
			}
		});
		
		// add an input tester to the text fields
		txtAvailability.textProperty().addListener(new InputTester(this, txtAvailability));
		txtSkillAmount.textProperty().addListener(new InputTester(this, txtSkillAmount));
	}
	
	
	@Override
	public boolean checkInput(Node node) {
		if(node.equals(txtAvailability) || node.equals(txtSkillAmount)){
			TextField txtField = (TextField) node;
			if(txtField.getText().isEmpty())
				return true;
			
			try{
				int input = Integer.valueOf(txtField.getText());
				if(input < 0)
					return false;
				else if(input > 100 && node.equals(txtAvailability))
					return false;
				else
					return true;
			}catch (Exception e){
				return false;
			}
		}else
			return false;
	}
	
	
	
	@Override
	public Node getNode(String fxmlName) {
		switch (fxmlName) {
			case "txtResourceName":
				return txtResourceName;
			case "cmbIntExt":
				return cmbIntExt;
			case "txtAvailability":
				return txtAvailability;
			case "txtSkillAmount":
				return txtSkillAmount;
	
			default:
				return null;
		}
	}
	
	
	
	
	/**
	 * Get the resource name
	 * @return the resourceName
	 */
	public String getResourceName(){
		return txtResourceName.getText();
	}
	
	/**
	 * Set the resource name
	 * @param resourceName
	 */
	public void setResourceName(String resourceName){
		txtResourceName.setText(resourceName);
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
	 * Set the internal flag
	 * @param intern
	 */
	public void setIntern(boolean intern){
		if(intern)
			cmbIntExt.getSelectionModel().select("Intern");
		else
			cmbIntExt.getSelectionModel().select("Extern");
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
	 * Set the availability of a resource
	 * @param availability
	 */
	public void setAvailability(int availability){
		txtAvailability.setText(String.valueOf(availability));
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
	
	/**
	 * Set the skill amount
	 * @param skillAmount
	 */
	public void setSkillAmount(int skillAmount){
		txtSkillAmount.setText(String.valueOf(skillAmount));
	}




	@Override
	public void setParentNode(Node parent) {}

	@Override
	public void removeNode() {}
	
	@Override
	public boolean isEmpty() {
		if(txtResourceName.getText().isEmpty() && txtAvailability.getText().isEmpty() && txtSkillAmount.getText().isEmpty())
			return true;
		else
			return false;
	}


	@Override
	public void disableWrite(boolean disable) {
		txtResourceName.setEditable(!disable);
		cmbIntExt.setMouseTransparent(disable);
		txtAvailability.setEditable(!disable);
		txtSkillAmount.setEditable(!disable);
	}
}
