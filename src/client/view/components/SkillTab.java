package client.view.components;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import client.view.IComponents;

/**
 * This class represents the tab where the skills are entered
 * 
 * @author Ammon
 *
 */
public class SkillTab extends AnchorPane implements IComponents {
	
	@FXML
	private AnchorPane ancSkillList;
	
	@FXML
	private TableView<?> tblSkill;
	@FXML
	private TableColumn<?, ?> colSkill;
	@FXML
	private TableColumn<?, ?> colInt;
	@FXML
	private TableColumn<?, ?> colExt;
	
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
		
		SimpleDoubleProperty tableWidth = new SimpleDoubleProperty();
		tableWidth.bind(tblSkill.widthProperty().subtract(4).divide(3));
		colSkill.prefWidthProperty().bind(tableWidth);
		colInt.prefWidthProperty().bind(tableWidth);
		colExt.prefWidthProperty().bind(tableWidth);
		
		// initialize the expandable table and add it to the anchor pane
		skillTable = new ExpandableTable<SkillPane>(SkillPane.class, this);
		ancSkillList.getChildren().add(skillTable);
		AnchorPane.setTopAnchor(skillTable, 0d);
		AnchorPane.setBottomAnchor(skillTable, 0d);
		AnchorPane.setRightAnchor(skillTable, 0d);
		AnchorPane.setLeftAnchor(skillTable, 0d);
	}
	
	
	
	/**
	 * This method returns an array list with all skill names
	 * @param except
	 * @return all skill names
	 */
	public ArrayList<String> getAllSkillNames(String except){
		ArrayList<String> names = new ArrayList<String>();
		
		for(SkillPane skill : skillTable.getContents()){
			names.add(skill.getSkillName());
		}
		
		// remove the ecept string
		names.remove(except);
		
		return names;
	}
	
	
	
	@Override
	public void clearAll(){
		skillTable.clearAll();
	}
	
	@Override
	public void disableWrite(boolean disable){
		skillTable.disableWrite(disable);
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
	
	@Override
	public Hashtable<String, Integer> getRealTimes(){
		return null;
	}

	
	
	
	@Override
	public ArrayList<SkillPane> getSkillPanes() {
		return skillTable.getContents();
	}
	
	@Override
	public SkillPane addSkillPane(){
		return skillTable.addNewContentLine();
	}
	
	@Override
	public Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> getPhasePanes() {
		return null;
	}
	
	@Override
	public PhasePaneWrapper addPhasePaneWrapper(String phaseName, String parentName){
		return null;
	}
	
	@Override
	public PhasePane addPhasePane(PhasePaneWrapper wrapper){
		return null;
	}

	@Override
	public ArrayList<ResourcePaneWrapper> getResourcePanes() {
		return null;
	}
	
	@Override
	public ResourcePane addResourcePane(int parentSkillID){
		return null;
	}
}
