package client.view.components;

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.project.Skill;
import client.core.Core;
import client.core.ICoreClient;
import client.view.IExpandableNode;
import client.view.ITester;
import client.view.InputTester;

public class PhasePane extends AnchorPane implements IExpandableNode, ITester {
	
	@FXML
	private ComboBox<String> cmbSkills;
	@FXML
	private TextField txtDuration;
	
	private ObservableList<String> skills;
	
	private ICoreClient core;
	private PhasePaneWrapper parent;
	
	private String oldSelectedSkill;
	
	private boolean noListener;
	
	
	/**
	 * The Constructor
	 */
	public PhasePane() {
		// load the project editor fxml
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/PhasePane.fxml"));
		
		// apply this class as root and controller
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		// load the fxml
		try {
			fxmlLoader.load();
		} catch (IOException e) { 
		    e.printStackTrace();
		}
		
		this.core = Core.getInstance(Core.class);
		
		// initialize the skill collection
		skills = FXCollections.observableArrayList();
		
		// add an event handler to listen for changes
		cmbSkills.valueProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if(newValue != null && !noListener)
	            	cmbChanged();
	        }    
		});
		
		// set the skill collection as data source to the combo box
		cmbSkills.setItems(skills);
		
		// add an input tester to the text field
		txtDuration.textProperty().addListener(new InputTester(this, txtDuration));
	}
	
	
	// this method is called when the selection of the combo box has changed 
	private void cmbChanged(){
		// update the in use skills
		parent.removeSkillInUse(oldSelectedSkill);
		parent.addSkillInUse(cmbSkills.getSelectionModel().getSelectedItem());
		this.oldSelectedSkill = cmbSkills.getSelectionModel().getSelectedItem();
		parent.updateSkillDropDown();
	}
	
	
	// this method test all skills if this skill is already there
	private boolean skillExists(String newSkill){
		for(String oldSkill : skills){
			if(oldSkill.equals(newSkill))
				return true;
		}
		
		return false;
	}
	
	
	@Override
	public Node getNode(String fxmlName) {
		switch (fxmlName) {
			case "cmbSkills":
				return cmbSkills;
			case "txtDuration":
				return txtDuration;
	
			default:
				return null;
		}
	}
	
	
	
	@Override
	public boolean checkInput(Node node) {
		if(node.equals(txtDuration)){
			if(txtDuration.getText().isEmpty())
				return true;
			
			try{
				int input = Integer.valueOf(txtDuration.getText());
				if(input < 0)
					return false;
				else
					return true;
			}catch (Exception e){
				return false;
			}
		}else
			return false;
	}
	
	
	/**
	 * This method updates the skills in the drop downs
	 */
	public void updateCmbSkills(){
		ArrayList<String> newSkills = parent.getAvailableSkills(this, oldSelectedSkill);
		ArrayList<String> tempSkills = new ArrayList<String>();
		
		for(String skill : newSkills){
			// check if the skill is already added to the drop down
			if(skillExists(skill))
				continue;
			
			// add a new skill
			tempSkills.add(skill);
		}
		
		// add all newly created skills to the old ones
		skills.addAll(tempSkills);
		
		// remove the no longer needed skills
		boolean deleted = true;
		for(int i=0;i<skills.size();i++){
			String oldSkill = skills.get(i);
			for(String newSkill : newSkills){
				if(oldSkill.equals(newSkill)){
					deleted = false;
					break;
				}
			}
			
			if(deleted){
				skills.remove(oldSkill);
				i--;
			}
			deleted = true;
		}
		
		cmbSkills.getSelectionModel().select(oldSelectedSkill);
	}
	
	
	
	/**
	 * Disable/Enable the listener on the combo box
	 * @param disable
	 */
	public void disableListener(boolean disable){
		this.noListener = disable;
	}
	
	
	
	/**
	 * Get the selected skill id for this phase
	 * @return the selected skill id
	 */
	public int getPhaseSkillId(){
		for(Skill skill : core.getSkills()){
			if(skill.getSkillName().equals(cmbSkills.getSelectionModel().getSelectedItem())){
				return skill.getSkillID();
			}
		}
		
		return -1;
	}
	
	/**
	 * Set the selected skill for this phase
	 * @param skillId the skill id
	 */
	public void setPhaseSkillId(int skillId){
		for(Skill skill : core.getSkills()){
			if(skill.getSkillID() == skillId){
				cmbSkills.getSelectionModel().select(skill.getSkillName());
				break;
			}
		}
	}
	
	
	/**
	 * Get the skill duration
	 * @return the duration
	 */
	public int getPhaseDuration(){
		try{
			return Integer.valueOf(txtDuration.getText());
		}catch (Exception e){
			return -1;
		}
	}
	
	/**
	 * Set the skill duration
	 * @param duration
	 */
	public void setPhaseDuration(int duration){
		txtDuration.setText(String.valueOf(duration));
	}


	
	@Override
	public void setParentNode(Node parent) {
		this.parent = (PhasePaneWrapper) parent;
		if(!noListener)
			updateCmbSkills();
	}


	@Override
	public void removeNode() {
		parent.removeSkillInUse(cmbSkills.getSelectionModel().getSelectedItem());
		parent.updateSkillDropDown();
	}
	
	
	@Override
	public boolean isEmpty() {
		if(txtDuration.getText().isEmpty() && cmbSkills.getSelectionModel().isEmpty())
			return true;
		else
			return false;
	}


	@Override
	public void disableWrite(boolean disable) {
		cmbSkills.setMouseTransparent(disable);
		txtDuration.setEditable(!disable);
	}
}
