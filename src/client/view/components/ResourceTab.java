package client.view.components;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.layout.AnchorPane;
import model.project.Skill;
import client.core.Core;
import client.core.ICoreClient;
import client.view.IComponents;

/**
 * This class represents the tab where the resources are entered
 * 
 * @author Ammon
 *
 */
public class ResourceTab extends AnchorPane implements IComponents {
	
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
		
		// define a temporary wrapper list
		ArrayList<ResourcePaneWrapper> wrappersToAdd = new ArrayList<ResourcePaneWrapper>();
		
		// iterate over all skills
		for(Skill skill : skills){
			// check if the skill is already added to the pane
			if(skillHasPane(skill))
				continue;
			
			// add a new resource wrapper
			ResourcePaneWrapper resWrapper = new ResourcePaneWrapper(skill);
			wrappersToAdd.add(resWrapper);
		}
		
		// add all newly created wrappers to the accordion
		wrapperPanes.addAll(wrappersToAdd);
		accSkillList.getPanes().addAll(wrappersToAdd);
		
		// remove the no longer needed wrappers
		boolean deleted = true;
		for(int i=0;i<wrapperPanes.size();i++){
			ResourcePaneWrapper pane = wrapperPanes.get(i);
			for(Skill skill : skills){
				if(pane.getSkill().getSkillID() == skill.getSkillID()){
					deleted = false;
					break;
				}
			}
			
			if(deleted){
				accSkillList.getPanes().remove(pane);
				wrapperPanes.remove(pane);
				i--;
			}
			deleted = true;
		}
		
		// disable the accordion pane if it has no children
		if(accSkillList.getPanes().isEmpty())
			accSkillList.setDisable(true);
		else
			accSkillList.setDisable(false);
	}

	
	
	@Override
	public void clearAll(){
		for(ResourcePaneWrapper wrapper : wrapperPanes){
			wrapper.clearAll();
		}
//		accSkillList.getPanes().clear();
//		updateResources();
	}
	
	@Override
	public void disableWrite(boolean disable){
		for(ResourcePaneWrapper resourcePane : wrapperPanes){
			resourcePane.disableWrite(disable);
		}
	}

	
	@Override
	public ArrayList<SkillPane> getSkillPanes() {
		return  null;
	}
	
	@Override
	public SkillPane addSkillPane(){
		return null;
	}
	
	@Override
	public Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> getPhasePanes() {
		return null;
	}
	
	@Override
	public PhasePaneWrapper addPhasePaneWrapper(String phaseName, int index, String parentName){
		return null;
	}
	
	@Override
	public PhasePane addPhasePane(PhasePaneWrapper wrapper){
		return null;
	}

	@Override
	public ArrayList<ResourcePaneWrapper> getResourcePanes() {
		return wrapperPanes;
	}
	
	@Override
	public ResourcePane addResourcePane(int parentSkillID){
		updateResources();
		
		for(ResourcePaneWrapper pane : wrapperPanes){
			if(pane.getSkill().getSkillID() == parentSkillID)
				return pane.addResourcePane();
		}
		
		return null;
	}

	@Override
	public String getProjectName() {
		return null;
	}
	
	@Override
	public void setProjectName(String projectName){}

	@Override
	public String getProjectResponsible() {
		return null;
	}
	
	@Override
	public void setProjectResponsible(String projectResponsible){}

	@Override
	public String getProjectDescription() {
		return null;
	}
	
	@Override
	public void setProjectDescription(String projectDescription){}
	
	@Override
	public void setProjectTimeStamp(LocalDateTime timeStamp){}
}
