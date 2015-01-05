package client.view.components;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import client.view.IComponents;

/**
 * This class represents the project editor panel
 * 
 * @author Ammon
 *
 */
public class ProjectEditor extends TabPane implements IComponents {
	
	@FXML
	private Tab tabSkills;
	@FXML
	private Tab tabResources;
	@FXML
	private Tab tabPhases;
	@FXML
	private Tab tabResults;
	
	private SkillTab skillTab;
	private ResourceTab resourceTab;
	private PhaseTab phaseTab;
	private ResultTab resultTab;
	
	
	/**
	 * The Constructor
	 */
	public ProjectEditor() {
		// load the project editor fxml
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/ProjectEditor.fxml"));
		
		// apply this class as root and controller
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		// load the fxml
		try {
			fxmlLoader.load();
		} catch (IOException e) { 
		    e.printStackTrace();
		}
		
		// add the second screen (skills)
		skillTab = new SkillTab();
		tabSkills.setContent(skillTab);
		
		// add the third screen (resources)
		resourceTab = new ResourceTab();
		tabResources.setContent(resourceTab);
		
		// add the fourth screen (phases)
		phaseTab = new PhaseTab();
		tabPhases.setContent(phaseTab);
		
		// add the fifth screen (results)
		resultTab = new ResultTab();
		tabResults.setContent(resultTab);
	}
	
	
	// this method is called when the resources tab is opened
	// it updates the resources view
	@FXML
	private void updateResources(){
		resourceTab.updateResources();
	}
	
	// this method is called when the phases tab is opened
	// it updates the phases view
	@FXML
	private void updatePhases(){
		phaseTab.updatePhases();
	}
	
	
	
	
	@Override
	public ArrayList<SkillPane> getSkillPanes(){
		return skillTab.getSkillPanes();
	}
}