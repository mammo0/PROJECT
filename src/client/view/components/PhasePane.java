package client.view.components;

import global.IExpandableNode;

import java.io.IOException;
import java.util.ArrayList;

import model.project.Skill;
import client.core.Core;
import client.core.ICoreClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class PhasePane extends AnchorPane implements IExpandableNode {
	
	@FXML
	private ComboBox<String> cmbSkills;
	@FXML
	private TextField txtDuration;
	
	private ObservableList<String> skills;
	
	private ICoreClient core;
	private PhasePaneWrapper parent;
	
	private String oldSelectedSkill;
	
	
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
		cmbSkills.setOnAction(this::cmbChanged);
		
		// set the skill collection as data source to the combo box
		cmbSkills.setItems(skills);
	}
	
	
	// this method is called when the selection of the combo box has changed 
	private void cmbChanged(ActionEvent event){
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
	
	
	
	/**
	 * This method updates the skills in the drop downs
	 */
	public void updateCmbSkills(){
		ArrayList<String> newSkills = parent.getAvailableSkills();
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
			
			if(deleted && !oldSkill.equals(cmbSkills.getSelectionModel().getSelectedItem())){
				skills.remove(oldSkill);
				deleted = true;
				i--;
			}
		}
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
	 * Get the phase duration
	 * @return the duration
	 */
	public int getPhaseDuration(){
		try{
			return Integer.valueOf(txtDuration.getText());
		}catch (Exception e){
			return -1;
		}
	}


	
	@Override
	public void setParentNode(Node parent) {
		this.parent = (PhasePaneWrapper) parent;
		updateCmbSkills();
	}


	@Override
	public void removeNode() {
		parent.removeSkillInUse(cmbSkills.getSelectionModel().getSelectedItem());
		parent.updateSkillDropDown();
	}
}
