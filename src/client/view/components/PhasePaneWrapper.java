package client.view.components;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Stack;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import model.project.Skill;
import client.core.Core;
import client.core.ICoreClient;
import client.view.ITester;
import client.view.InputTester;

public class PhasePaneWrapper extends TitledPane implements ITester {
	
	@FXML
	private DatePicker datPhaseBegin;
	@FXML
	private DatePicker datPhaseEnd;
	@FXML
	private TextField txtRiskFactor;
	@FXML
	private AnchorPane ancPhaseList;
	
	@FXML
	private TableView<?> tblPhase;
	@FXML
	private TableColumn<?, ?> colSkill;
	@FXML
	private TableColumn<?, ?> colDuration;
	
	private PhaseTab parent;
	
	private ExpandableTable<PhasePane> phaseTable;
	private TextField txtPhaseName;
	private Button btnRemovePhase;
	
	private boolean subPhase;
	
	private ArrayList<String> availableSkills;
	private ArrayList<String> skillsInUse;
	
	private ICoreClient core;
	
	private boolean noDateCheck;
	
	private boolean writeProtected;
	
	private static Stack<Runnable> runnableStack;
	
	
	static{
		runnableStack = new Stack<Runnable>();
	}
	
	
	
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
		
		SimpleDoubleProperty tableWidth = new SimpleDoubleProperty();
		tableWidth.bind(tblPhase.widthProperty().subtract(4).divide(2));
		colSkill.prefWidthProperty().bind(tableWidth);
		colDuration.prefWidthProperty().bind(tableWidth);
		
		final PhasePaneWrapper This = this;
		
		this.parent = parent;
		this.subPhase = subPhase;
		
		availableSkills = new ArrayList<String>();
		skillsInUse = new ArrayList<String>();
		
		core = Core.getInstance(Core.class);
		
		// build the titled pane header
		txtPhaseName = new TextField();
		txtPhaseName.setPromptText("Phasenname");
		
		btnRemovePhase = new Button("-");
		btnRemovePhase.setOnAction(this::btnRemovePhaseClick);
		
        GridPane header = new GridPane();
        header.add(txtPhaseName, 0, 0);
        header.add(btnRemovePhase, 1, 0);
        GridPane.setConstraints(txtPhaseName, 0, 0, 1, 1, HPos.LEFT, VPos.CENTER, Priority.SOMETIMES, Priority.NEVER, Insets.EMPTY);
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
		datPhaseBegin.valueProperty().addListener(new ChangeListener<LocalDate>(){
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				if(newValue != null){
					Runnable run = new Runnable() {
						@Override
						public void run() {
							boolean temp = noDateCheck;
							if(!noDateCheck && !parent.checkPhaseDates(This, newValue, null)){
								noDateCheck = true;
								datPhaseBegin.setValue(oldValue);
							}else if(temp)
								noDateCheck = false;
							
							runnableStack.pop();
						}
					};
					runnableStack.push(run);
					Platform.runLater(run);
				}
			}
		});
		datPhaseBegin.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                       
                        if (!empty && writeProtected) {
                            setDisable(true);
                        }else if(!empty && !writeProtected){
                        	setDisable(false);
                        }
                    }
                };
            }
        });
		datPhaseEnd.valueProperty().addListener(new ChangeListener<LocalDate>(){
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				if(newValue != null){
					Runnable run = new Runnable() {
						@Override
						public void run() {
							boolean temp = noDateCheck;
							if(!noDateCheck && !parent.checkPhaseDates(This, null, newValue)){
								noDateCheck = true;
								datPhaseEnd.setValue(oldValue);
							}else if(temp)
								noDateCheck = false;
							
							runnableStack.pop();
						}
					};
					runnableStack.push(run);
					Platform.runLater(run);
				}
			}
		});
		datPhaseEnd.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                       
                        if (!empty && writeProtected) {
                            setDisable(true);
                        }else if(!empty && !writeProtected){
                        	setDisable(false);
                        }
                    }
                };
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
	
	
	
	/**
	 * Get the current size of the runnable stack
	 * @return the size
	 */
	public static int getRunnableStackSize(){
		return runnableStack.size();
	}
	
	
	
	
	/**
	 * Get a node by its name
	 * @param fxmlName the name of the node
	 * @return the node
	 */
	public Node getNode(String fxmlName){
		switch (fxmlName) {
			case "datPhaseBegin":
				return datPhaseBegin;
			case "datPhaseEnd":
				return datPhaseEnd;
			case "txtRiskFactor":
				return txtRiskFactor;
			case "txtPhaseName":
				return txtPhaseName;

			default:
				return null;
		}
	}
	
	
	/**
	 * Disable or enable the writing for a project
	 */
	public void disableWrite(boolean disable){
		btnRemovePhase.setVisible(!disable);
		
		writeProtected = disable;
		datPhaseBegin.setEditable(!disable);
		datPhaseEnd.setEditable(!disable);
		txtPhaseName.setEditable(!disable);
		txtRiskFactor.setEditable(!disable);
		
		phaseTable.disableWrite(disable);
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
			pane.disableListener(true);
			pane.updateCmbSkills();
			pane.disableListener(false);
		}
	}
	
	
	// get all available skills
	private ArrayList<String> getAvailableSkills(){
		availableSkills.clear();
		
		ArrayList<Skill> skills = core.getSkills();
		for(Skill skill : skills){
			if(!skillsInUse.contains(skill.getSkillName()))
				availableSkills.add(skill.getSkillName());
		}
		
		return availableSkills;
	}
	
	
	/**
	 * Get all available skills for a given pane
	 * @param pane the pane
	 * @param newSkillName the new skill name
	 * @return the available skills
	 */
	public ArrayList<String> getAvailableSkills(PhasePane pane, String newSkillName){
		ArrayList<String> available = new ArrayList<String>(getAvailableSkills());
		if(newSkillName != null)
			available.add(newSkillName);
		
		return available;
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
		return txtPhaseName.getText();
	}
	
	/**
	 * Set the phase name
	 * @param phaseName
	 */
	public void setPhaseName(String phaseName){
		txtPhaseName.setText(phaseName);
	}
	
	
	/**
	 * Get the phase begin date
	 * @return the begin date
	 */
	public LocalDate getPhaseBegin(){
		return datPhaseBegin.getValue();
	}
	
	/**
	 * Set the phase begin date
	 * @param phaseBeginn the begin date
	 */
	public void setPhaseBegin(LocalDate phaseBeginn){
		setPhaseBegin(phaseBeginn, false);
	}
	/**
	 *  Set the phase begin date
	 * @param phaseBeginn the begin date
	 * @param noDateCheck if the value should be checked
	 */
	public void setPhaseBegin(LocalDate phaseBeginn, boolean noDateCheck){
		this.noDateCheck = noDateCheck;
		datPhaseBegin.setValue(phaseBeginn);
	}
	
	
	/**
	 * Get the phase end date
	 * @return the end date
	 */
	public LocalDate getPhaseEnd(){
		return datPhaseEnd.getValue();
	}
	
	/**
	 * Set the phase end date
	 * @param phaseEnd the end date
	 */
	public void setPhaseEnd(LocalDate phaseEnd){
		setPhaseEnd(phaseEnd, false);
	}
	/**
	 * Set the phase end date
	 * @param phaseEnd the end date
	 * @param noDateCheck if the value should be checked
	 */
	public void setPhaseEnd(LocalDate phaseEnd, boolean noDateCheck){
		this.noDateCheck = noDateCheck;
		datPhaseEnd.setValue(phaseEnd);
	}
	
	
	/**
	 * Get the risk factor for this phase
	 * @return the risk factor
	 */
	public int getRiskFactor(){
		try{
			return Integer.valueOf(txtRiskFactor.getText());
		}catch (Exception e){
			return 0;
		}
	}
	
	/**
	 * Set the risk factor for this phase
	 * @param riskFactor
	 */
	public void setRiskFactor(int riskFactor){
		txtRiskFactor.setText(String.valueOf(riskFactor));
	}
	
	
	/**
	 * Returns true if it is a sub phase
	 * @return
	 */
	public boolean isSubPhase(){
		return subPhase;
	}
	
	
	/**
	 * Get the phase panes
	 * @return the phase panes
	 */
	public ArrayList<PhasePane> getPhasePanes(){
		return phaseTable.getContents();
	}
	
	/**
	 * Add a new phase pane
	 * @return the new phase pane
	 */
	public PhasePane addPhasePane(){
		return phaseTable.addNewContentLine();
	}
}
