package client.view.components;

import java.io.IOException;
import java.util.ArrayList;

import model.project.Skill;
import client.core.Core;
import client.core.ICoreClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

public class PhasePane extends AnchorPane {
	
	@FXML
	private ComboBox<String> cmbSkills;
	
	private ObservableList<String> skills;
	
	private ICoreClient core;
	
	
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
		
		// update the combo boxes
		updateCmbSkills();
		
		// set the skill collection as data source to the combo box
		cmbSkills.setItems(skills);
	}
	
	
	// this method test all skills if this skill is already there
	private boolean skillExists(Skill newSkill){
		for(String oldSkill : skills){
			if(oldSkill.equals(newSkill.getSkillName()))
				return true;
		}
		
		return false;
	}
	
	
	
	/**
	 * This method updates the skills in the drop downs
	 */
	public void updateCmbSkills(){
		ArrayList<Skill> newSkills = core.getSkills();
		ArrayList<String> tempSkills = new ArrayList<String>();
		
		for(Skill skill : newSkills){
			// check if the skill is already added to the drop down
			if(skillExists(skill))
				continue;
			
			// add a new skill
			tempSkills.add(skill.getSkillName());
		}
		
		// add all newly created skills to the old ones
		skills.addAll(tempSkills);
		
		// remove the no longer needed skills
		boolean deleted = true;
		for(int i=0;i<skills.size();i++){
			String oldSkill = skills.get(i);
			for(Skill newSkill : newSkills){
				if(oldSkill.equals(newSkill.getSkillName())){
					deleted = false;
					break;
				}
			}
			
			if(deleted){
				skills.remove(oldSkill);
				deleted = true;
				i--;
			}
		}
	}
}
