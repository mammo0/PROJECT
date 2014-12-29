package client.view.components;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.layout.AnchorPane;
import model.project.Skill;
import client.core.Core;
import client.core.ICoreClient;

/**
 * This class represents the tab where the resources are entered
 * 
 * @author Ammon
 *
 */
public class ResourceTab extends AnchorPane {
	
	private ICoreClient core;
	
	private ArrayList<ResourcePaneWrapper> wrapperPanes;
	
	@FXML
	private Accordion accSkillList;
	
	
	/**
	 * The Constructor
	 */
	public ResourceTab() {
		// load the resource tab fxml
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/ResourceTab.fxml"));
		
		// apply this class as root and controller
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		// load the fxml
		try {
			fxmlLoader.load();
		} catch (IOException e) { 
		    e.printStackTrace();
		}
		
		// get the core instance
		this.core = Core.getInstance(Core.class);
		
		this.wrapperPanes = new ArrayList<ResourcePaneWrapper>();
	}
	
	
	// this method test all wrapper panes if this skill is already there
	private boolean skillHasPane(Skill skill){
		for(ResourcePaneWrapper pane : wrapperPanes){
			if(pane.getSkill().getSkillID() == skill.getSkillID())
				return true;
		}
		
		return false;
	}
	
	
	
	/**
	 * This method updates the resources
	 */
	public void updateResources(){
		// get the entered skills
		ArrayList<Skill> skills = core.getSkills();
		
		// return if no skills are entered
		if(skills == null)
			return;
		
		// define a temporary accordion pane and wrapper list
		Accordion tempAcc = new Accordion();
		ArrayList<ResourcePaneWrapper> tempRPW = new ArrayList<ResourcePaneWrapper>();
		
		// iterate over all skills
		for(Skill skill : skills){
			// check if the skill is already added to the pane
			if(skillHasPane(skill))
				continue;
			
			ResourcePaneWrapper resWrapper = new ResourcePaneWrapper(skill);
			tempAcc.getPanes().add(resWrapper);
			tempRPW.add(resWrapper);
		}
		
		// clear the old accordion and wrapper list and apply to the new one
		wrapperPanes = tempRPW;
		accSkillList.getPanes().clear();
		accSkillList.getPanes().addAll(tempAcc.getPanes());
		
		// disable the accordion pane if it has no children
		if(accSkillList.getPanes().isEmpty())
			accSkillList.setDisable(true);
		else
			accSkillList.setDisable(false);
	}
}
