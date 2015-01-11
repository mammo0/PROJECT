package client.view.components;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import client.core.Core;
import client.core.ICoreClient;
import client.view.ITester;
import client.view.InputTester;
import model.project.Skill;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class PhasePaneWrapper extends TitledPane implements ITester {
	
	@FXML
	private DatePicker datProjectBegin;
	@FXML
	private DatePicker datProjectEnd;
	@FXML
	private TextField txtRiskFactor;
	@FXML
	private AnchorPane ancPhaseList;
	
	private PhaseTab parent;
	
	private ExpandableTable<PhasePane> phaseTable;
	private TextField phaseName;
	private Button btnRemovePhase;
	
	private boolean subPhase;
	
	private ArrayList<String> availableSkills;
	private ArrayList<String> skillsInUse;
	
	private ICoreClient core;
	
	
	/**
	 * The Constructor
	 */
	public PhasePaneWrapper(PhaseTab parent){
		this(parent, false);
	}
	
	public PhasePaneWrapper(PhaseTab parent, boolean subPhase) {
		// load the project editor fxml
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/PhasePaneWrapper.fxml"));
		
		// apply this class as root and controller
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		// load the fxml
		try {
			fxmlLoader.load();
		} catch (IOException e) { 
		    e.printStackTrace();
		}
		
		this.parent = parent;
		this.subPhase = subPhase;
		
		availableSkills = new ArrayList<String>();
		skillsInUse = new ArrayList<String>();
		
		core = Core.getInstance(Core.class);
		
		// build the titled pane header
		phaseName = new TextField();
		phaseName.setPromptText("Phasenname");
		
		btnRemovePhase = new Button("-");
		btnRemovePhase.setOnAction(this::btnRemovePhaseClick);
		
        GridPane header = new GridPane();
        header.add(phaseName, 0, 0);
        header.add(btnRemovePhase, 1, 0);
        GridPane.setConstraints(phaseName, 0, 0, 1, 1, HPos.LEFT, VPos.CENTER, Priority.SOMETIMES, Priority.NEVER, Insets.EMPTY);
        GridPane.setConstraints(btnRemovePhase, 1, 0, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.SOMETIMES, Priority.NEVER, Insets.EMPTY);
        
        // check if the wrapper contains a sub phase
        if(subPhase){
        	setPadding(new Insets(0d, 0d, 0d, 50d));
        	header.minWidthProperty().bind(this.widthProperty().subtract(90));
        }else
        	header.minWidthProperty().bind(this.widthProperty().subtract(40));
        
        // apply the header
        setGraphic(header);
		
		
		// initialize the expandable table and add it to the anchor pane
		phaseTable = new ExpandableTable<PhasePane>(PhasePane.class, this);
		ancPhaseList.getChildren().add(phaseTable);
		AnchorPane.setTopAnchor(phaseTable, 0d);
		AnchorPane.setBottomAnchor(phaseTable, 0d);
		AnchorPane.setRightAnchor(phaseTable, 0d);
		AnchorPane.setLeftAnchor(phaseTable, 0d);
		
		// check that the dates are correct
		datProjectBegin.valueProperty().addListener(new ChangeListener<LocalDate>(){
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				if(newValue != null && (getPhaseEnd() == null || getPhaseEnd().isBefore(newValue)))
					datProjectEnd.setValue(newValue);
			}
		});
		datProjectEnd.valueProperty().addListener(new ChangeListener<LocalDate>(){
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				if(getPhaseBegin() == null)
					datProjectBegin.setValue(newValue);
				else if(newValue != null && newValue.isBefore(getPhaseBegin()))
					datProjectEnd.setValue(getPhaseBegin());
			}
		});
		
		// add an input tester to the text field
		txtRiskFactor.textProperty().addListener(new InputTester(this, txtRiskFactor));
	}
	
	
	private void btnRemovePhaseClick(ActionEvent event){
		if(subPhase)
			parent.removeSubPhase(this);
		else
			parent.removeMainPhase(this);
	}
	
	
	
	@Override
	public boolean checkInput(Node node) {
		if(node.equals(txtRiskFactor)){
			if(txtRiskFactor.getText().isEmpty())
				return true;
			
			try{
				int input = Integer.valueOf(txtRiskFactor.getText());
				if(input < 0)
					return false;
				else
					return true;
			}catch (Exception e){
				return false;
			}
		}else
			return false;
	}
	
	
	
	/**
	 * This method updates all drop downs of the expandable table
	 */
	public void updateSkillDropDown(){
		for(PhasePane pane : phaseTable.getContents()){
			pane.updateCmbSkills();
		}
	}
	
	
	/**
	 * Get all available skills
	 * @return the available skills
	 */
	public ArrayList<String> getAvailableSkills(){
		availableSkills.clear();
		
		ArrayList<Skill> skills = core.getSkills();
		for(Skill skill : skills){
			if(!skillsInUse.contains(skill.getSkillName()))
				availableSkills.add(skill.getSkillName());
		}
		
		return availableSkills;
	}
	
	
	/**
	 * Add a skill to the in use list
	 * @param skillName the name of the skill
	 */
	public void addSkillInUse(String skillName){
		skillsInUse.add(skillName);
	}
	
	/**
	 * Remove a skill from the in use list
	 * @param skillName the name of the skill
	 */
	public void removeSkillInUse(String skillName){
		if(skillsInUse.contains(skillName))
			skillsInUse.remove(skillName);
	}
	
	
	
	/**
	 * Get the phase name
	 * @return the phase name
	 */
	public String getPhaseName(){
		return phaseName.getText();
	}
	
	
	/**
	 * Get the project begin date
	 * @return the begin date
	 */
	public LocalDate getPhaseBegin(){
		return datProjectBegin.getValue();
	}
	
	
	/**
	 * Get the project end date
	 * @return the end date
	 */
	public LocalDate getPhaseEnd(){
		return datProjectEnd.getValue();
	}
	
	
	/**
	 * Get the risk factor for this phase
	 * @return the risk factor
	 */
	public int getRiskFactor(){
		try{
			return Integer.valueOf(txtRiskFactor.getText());
		}catch (Exception e){
			return -1;
		}
	}
	
	
	/**
	 * Get the phase panes
	 * @return the phase panes
	 */
	public ArrayList<PhasePane> getPhasePanes(){
		return phaseTable.getContents();
	}
}
