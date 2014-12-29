package client.view.components;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * This class represents the project editor panel
 * 
 * @author Ammon
 *
 */
public class ProjectEditor extends TabPane {
	
	@FXML
	private Tab tabSkills;
	@FXML
	private Tab tabResources;
	
	private SkillTab skillTab;
	private ResourceTab resourceTab;
	
	
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
	}
	
	
	// this method is called when the resources tab is opened
	// it calls updates the resources view
	@FXML
	private void updateResources(){
		resourceTab.updateResources();
	}
	
	
	
	
	/**
	 * Get the skill panes from the project editor
	 * @return the skill panes
	 */
	public ArrayList<SkillPane> getSkillPanes(){
		return skillTab.getSkills();
	}
}