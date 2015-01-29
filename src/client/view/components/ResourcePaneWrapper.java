package client.view.components;

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import model.project.Skill;

/**
 * This class is a wrapper class for the {@link ResourcePane}
 * 
 * @author Ammon
 *
 */
public class ResourcePaneWrapper extends TitledPane {
	
	@FXML
	private AnchorPane ancResourceList;
	
	@FXML
	private TableView<?> tblResource;
	@FXML
	private TableColumn<?, ?> colResource;
	@FXML
	private TableColumn<?, ?> colIntExt;
	@FXML
	private TableColumn<?, ?> colAvailability;
	@FXML
	private TableColumn<?, ?> colAmount;
	
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
		
		SimpleDoubleProperty tableWidth = new SimpleDoubleProperty();
		tableWidth.bind(tblResource.widthProperty().subtract(4).divide(4));
		colResource.prefWidthProperty().bind(tableWidth);
		colIntExt.prefWidthProperty().bind(tableWidth);
		colAvailability.prefWidthProperty().bind(tableWidth);
		colAmount.prefWidthProperty().bind(tableWidth);
		
		this.skill = skill;
		
		setText(skill.getSkillName());
		
		// initialize the expandable table and add it to the anchor pane
		resourceTable = new ExpandableTable<ResourcePane>(ResourcePane.class, this);
		ancResourceList.getChildren().add(resourceTable);
		AnchorPane.setTopAnchor(resourceTable, 0d);
		AnchorPane.setBottomAnchor(resourceTable, 0d);
		AnchorPane.setRightAnchor(resourceTable, 0d);
		AnchorPane.setLeftAnchor(resourceTable, 0d);
	}
	

	/**
	 * Clear all inputs
	 */
	public void clearAll(){
		resourceTable.clearAll();
	}
	
	
	/**
	 * Disable or enable the writing for a project
	 */
	public void disableWrite(boolean disable){
		resourceTable.disableWrite(disable);
	}
	
	
	/**
	 * Get the skill corresponding to the wrapper pane
	 * @return the skill
	 */
	public Skill getSkill(){
		return skill;
	}
	
	
	/**
	 * This method returns all resource panes
	 * @return the resources
	 */
	public ArrayList<ResourcePane> getResourcePanes(){
		return resourceTable.getContents();
	}
	
	
	/**
	 * Add a new resource pane
	 * @return the new resource pane
	 */
	public ResourcePane addResourcePane(){
		return resourceTable.addNewContentLine();
	}
}
