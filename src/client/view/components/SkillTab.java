package client.view.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import client.view.IComponents;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 * This class represents the tab where the skills are entered
 * 
 * @author Ammon
 *
 */
public class SkillTab extends AnchorPane implements IComponents {
	
	@FXML
	private AnchorPane ancSkillList;
	
	private ExpandableTable<SkillPane> skillTable;
	
	
	/**
	 * The Constructor
	 */
	public SkillTab() {
		// load the skill tab fxml
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/SkillTab.fxml"));
		
		// apply this class as root and controller
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		// load the fxml
		try {
			fxmlLoader.load();
		} catch (IOException e) { 
		    e.printStackTrace();
		}
		
		// initialize the expandable table and add it to the anchor pane
		skillTable = new ExpandableTable<SkillPane>(SkillPane.class);
		ancSkillList.getChildren().add(skillTable);
		AnchorPane.setTopAnchor(skillTable, 0d);
		AnchorPane.setBottomAnchor(skillTable, 0d);
		AnchorPane.setRightAnchor(skillTable, 0d);
		AnchorPane.setLeftAnchor(skillTable, 0d);
	}
	
	
	@Override
	public ArrayList<SkillPane> getSkillPanes() {
		return skillTable.getContents();
	}

	@Override
	public String getProjectName() {
		return null;
	}

	@Override
	public String getProjectResponsible() {
		return null;
	}

	@Override
	public String getProjectDescription() {
		return null;
	}

	@Override
	public Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> getPhasePanes() {
		return null;
	}

	@Override
	public ArrayList<ResourcePaneWrapper> getResourcePanes() {
		return null;
	}
}
