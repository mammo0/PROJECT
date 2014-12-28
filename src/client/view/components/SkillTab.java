package client.view.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class SkillTab extends AnchorPane {
	@FXML
	private ScrollPane scrSkillList;
	private GridPane skillList;
	private Collection<ColumnConstraints> columnConstraints;
	private int skillListRowCount;
	private Button btnSkillAdd;
	private Hashtable<Integer, SkillPane> skillPanes;
	private Hashtable<Button, Integer> btnSkillRemoves;
	
	/**
	 * The Constructor
	 */
	public SkillTab() {
		// load the project editor fxml
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
		
		initializeSkillView();
	}
	
	
	// initialize the skill view
	private void initializeSkillView(){
		skillList = new GridPane();
		
		// set the column constraints
		columnConstraints = new ArrayList<ColumnConstraints>();
		ColumnConstraints col1 = new ColumnConstraints();
	    col1.setPrefWidth(25d);
	    col1.setHgrow(Priority.NEVER);
	    col1.setHalignment(HPos.CENTER);
	    columnConstraints.add(col1);
	    ColumnConstraints col2 = new ColumnConstraints();
	    col2.setHgrow(Priority.ALWAYS);
	    col2.setHalignment(HPos.CENTER);
	    columnConstraints.add(col2);
	    ColumnConstraints col3 = new ColumnConstraints();
	    col3.setPrefWidth(25d);
	    col3.setHgrow(Priority.NEVER);
	    col3.setHalignment(HPos.CENTER);
	    columnConstraints.add(col3);
	    skillList.getColumnConstraints().addAll(columnConstraints);
		
		skillListRowCount = 0;
		skillPanes = new Hashtable<Integer, SkillPane>();
		btnSkillRemoves = new Hashtable<Button, Integer>();
		btnSkillAdd = new Button("+");
		btnSkillAdd.setOnAction(this::btnSkillAddClick);
		skillListAddRow();
		
		// add the grid pane to the scroll pane
		scrSkillList.setContent(skillList);
	}
	
	// this methods adds a row to the skills view
	private void skillListAddRow(){
		SkillPane sP = new SkillPane();
		skillPanes.put(skillListRowCount, sP);
		
		Button btnSkillRemove = new Button("-");
		btnSkillRemove.setOnAction(this::btnSkillRemoveClick);
		btnSkillRemoves.put(btnSkillRemove, skillListRowCount);
		
		// move the skill add button one row down
		if(skillListRowCount > 0)
			skillList.getChildren().remove(btnSkillAdd);
		skillList.add(btnSkillAdd, 0, skillListRowCount);
		
		// add the skill pane and the remove button
		skillList.add(sP, 1, skillListRowCount);
		skillList.add(btnSkillRemove, 2, skillListRowCount);
		
		// increase the row counter
		skillListRowCount++;
	}
	
	// this method is called by the add button
	private void btnSkillAddClick(ActionEvent event){
		skillListAddRow();
	}
	
	// this method is called by the remove button
	private void btnSkillRemoveClick(ActionEvent event){
		int deleteRowIndex = btnSkillRemoves.get(event.getSource());
		
		// return if only one row is left
		if(skillListRowCount == 1){
			return;
		}
		
		// initialize a new grid pane
		GridPane tempGrid = new GridPane();
		tempGrid.getColumnConstraints().addAll(columnConstraints);
		
		// clear the hashtables (will be rebuilt)
		btnSkillRemoves.remove(event.getSource());
		skillPanes.clear();
		
		// special case on deleting the first line
		boolean deleteFirstRow = false;
		if (deleteRowIndex == 0)
			deleteFirstRow = true;
		
		// iterate over the old pane
		int childCounter = 0;
		int rowIndex = 0;
		int temp = 0;
		while(skillList.getChildren().size() > childCounter){
			Node child = skillList.getChildren().get(childCounter);
			
			if(deleteFirstRow){
				deleteFirstRow = false;
				rowIndex--;
			}
			
			// check if this row should be ignored
			if(GridPane.getRowIndex(child) != deleteRowIndex){
				if(temp != GridPane.getRowIndex(child)){
					temp = GridPane.getRowIndex(child);
					rowIndex++;
				}
				
				// add the child to the temporary grid pane
				tempGrid.add(child, GridPane.getColumnIndex(child), rowIndex);
				
				// rebuild the hashtables
				if(child.getClass().equals(SkillPane.class)){
					skillPanes.put(rowIndex, (SkillPane) child);
				}
				if(btnSkillRemoves.containsKey(child)){
					btnSkillRemoves.replace((Button) child, rowIndex);
				}
			}else{
				childCounter++;
			}
		}
		
		// check if the last row should be removed
		if(deleteRowIndex == skillListRowCount-1){
			tempGrid.add(btnSkillAdd, 0, deleteRowIndex-1);
		}
		
		// apply the temporary grid pane
		skillList = tempGrid;
		scrSkillList.setContent(skillList);
		
		// decrease the row counter
		skillListRowCount--;
	}
	
	
	
	/**
	 * This method returns all entered skills
	 * @return the skills
	 */
	public ArrayList<SkillPane> getSkills(){
		return (ArrayList<SkillPane>) skillPanes.values();
	}
	
	
	
	public class SkillPane extends AnchorPane {
		
		@FXML
		private TextField skillName;
		@FXML
		private TextField dayRateInt;
		@FXML
		private TextField dayRateExt;
		
		
		
		public SkillPane() { 
		     FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/SkillPane.fxml"));
		     
		     // apply this class as root and controller
		     fxmlLoader.setRoot(this);
		     fxmlLoader.setController(this);
		     
		     // load the fxml
		     try {
		    	 fxmlLoader.load();
		     } catch (IOException e) { 
		         e.printStackTrace();
		     }
	    }
		
		// This method casts a String to a float
		private float castStringToFloat(String input){
			try{
				return Float.valueOf(input);
			}catch (Exception e){
				return Float.NaN;
			}
		}



		/**
		 * Get the name of the skill
		 * @return the skillName
		 */
		public String getSkillName() {
			return skillName.getText();
		}

		/**
		 * Get the internal day rate of the skill
		 * @return the dayRateInt
		 */
		public float getDayRateInt() {
			return castStringToFloat(dayRateInt.getText());
		}

		/**
		 * Get the external day rate of the skill
		 * @return the dayRateExt
		 */
		public float getDayRateExt() {
			return castStringToFloat(dayRateExt.getText());
		}
	}
}
