package client.view.components;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class PhasePaneWrapper extends TitledPane {
	
	@FXML
	private AnchorPane ancPhaseList;
	
	private ExpandableAccordion parent;
	
	private ExpandableTable<PhasePane> phaseTable;
	private TextField phaseName;
	private Button btnRemovePhase;
	
	private boolean subPhase;
	
	
	/**
	 * The Constructor
	 */
	public PhasePaneWrapper(ExpandableAccordion parent){
		this(parent, false);
	}
	
	public PhasePaneWrapper(ExpandableAccordion parent, boolean subPhase) {
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
		phaseTable = new ExpandableTable<PhasePane>(PhasePane.class);
		ancPhaseList.getChildren().add(phaseTable);
		AnchorPane.setTopAnchor(phaseTable, 0d);
		AnchorPane.setBottomAnchor(phaseTable, 0d);
		AnchorPane.setRightAnchor(phaseTable, 0d);
		AnchorPane.setLeftAnchor(phaseTable, 0d);
	}
	
	
	private void btnRemovePhaseClick(ActionEvent event){
		if(subPhase)
			parent.removeSubPhase(this);
		else
			parent.removeMainPhase(this);
	}
	
	
	
	/**
	 * This method updates all drop downs of the expandable table
	 */
	public void updateSkillDropDown(){
		for(PhasePane pane : phaseTable.getContents()){
			pane.updateCmbSkills();
		}
	}
}