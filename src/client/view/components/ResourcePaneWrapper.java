package client.view.components;

import java.io.IOException;
import java.util.ArrayList;

import model.project.Skill;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

/**
 * This class is a wrapper class for the {@link ResourcePane}
 * 
 * @author Ammon
 *
 */
public class ResourcePaneWrapper extends TitledPane {
	
	@FXML
	private AnchorPane ancResourceList;
	
	private Skill skill;
	
	private ExpandableTable<ResourcePane> resourceTable;
	
	
	
	/**
	 * The Constructor
	 */
	public ResourcePaneWrapper(Skill skill) {
		// load the resource pane wrapper fxml
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/ResourcePaneWrapper.fxml"));
		
		// apply this class as root and controller
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		// load the fxml
		try {
			fxmlLoader.load();
		} catch (IOException e) { 
		    e.printStackTrace();
		}
		
		this.skill = skill;
		
		setText(skill.getSkillName());
		
		// initialize the expandable table and add it to the anchor pane
		resourceTable = new ExpandableTable<ResourcePane>(ResourcePane.class);
		ancResourceList.getChildren().add(resourceTable);
		AnchorPane.setTopAnchor(resourceTable, 0d);
		AnchorPane.setBottomAnchor(resourceTable, 0d);
		AnchorPane.setRightAnchor(resourceTable, 0d);
		AnchorPane.setLeftAnchor(resourceTable, 0d);
	}
	
	
	/**
	 * Get the skill corresponding to the wrapper pane
	 * @return the skill
	 */
	public Skill getSkill(){
		return skill;
	}
	
	
	/**
	 * This method returns all entered resources
	 * @return the resources
	 */
	public ArrayList<ResourcePane> getResources(){
		return resourceTable.getContents();
	}
}