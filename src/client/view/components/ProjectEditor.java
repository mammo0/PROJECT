package client.view.components;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Hashtable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import client.view.IComponents;

/**
 * This class represents the project editor panel
 * 
 * @author Ammon
 *
 */
public class ProjectEditor extends TabPane implements IComponents {
	
	@FXML
	private Tab tabProject;
	@FXML
	private Tab tabSkills;
	@FXML
	private Tab tabResources;
	@FXML
	private Tab tabPhases;
	@FXML
	private Tab tabResults;
	
	@FXML
	private TextField txtProjectName;
	@FXML
	private TextField txtProjectResponsible;
	@FXML
	private TextArea txtProjectDescription;
	@FXML
	private Circle crlFinish;
	@FXML
	private Label lblTimeStamp;
	
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
		
		String viewCss = getClass().getResource("/client/view/css/View.css").toExternalForm();
		String projectEditorCss = getClass().getResource("/client/view/css/ProjectEditor.css").toExternalForm();
		this.getStylesheets().addAll(viewCss, projectEditorCss);
		
		
		// set up first screen
		tabProject.setGraphic(getTabHeader("/client/res/tabs/Home.png", "Projekt anlegen"));
		
		// add the second screen (skills)
		skillTab = new SkillTab();
		tabSkills.setContent(skillTab);
		tabSkills.setGraphic(getTabHeader("/client/res/tabs/KompetenzenAnlegen.png", "Kompetenz anlegen"));
		
		// add the third screen (resources)
		resourceTab = new ResourceTab();
		tabResources.setContent(resourceTab);
		tabResources.setGraphic(getTabHeader("/client/res/tabs/KompetenzenDefinieren.png", "Ressourenplanung"));
		
		// add the fourth screen (phases)
		phaseTab = new PhaseTab();
		tabPhases.setContent(phaseTab);
		tabPhases.setGraphic(getTabHeader("/client/res/tabs/Phasenplanung.png", "Phasenplanung"));
		
		// add the fifth screen (results)
		resultTab = new ResultTab();
		tabResults.setContent(resultTab);
		tabResults.setGraphic(getTabHeader("/client/res/tabs/Kalkulation.png", "Ergebnisse"));
		
		
		tabMinWidthProperty().bind(this.widthProperty().subtract(90).divide(5));
	}
	
	
	// generate the tab header with a picture and title
	private BorderPane getTabHeader(String imagePath, String tabTitle){
		BorderPane pane = new BorderPane();
		
		Image img = new Image(getClass().getResourceAsStream(imagePath));
		ImageView image = new ImageView();
		image.setPreserveRatio(true);
		image.setFitWidth(40);
		image.setImage(img);
		pane.setCenter(image);
		
		Label label = new Label(tabTitle);
		pane.setBottom(label);
		
		return pane;
	}
	
	

	/**
	 * This method is called when the resources tab is opened.
	 * It updates the resources view
	 */
	@FXML
	public void updateResources(){
		resourceTab.updateResources();
	}
	
	
	/**
	 * This method is called when the phases tab is opened.
	 * It updates the phases view
	 */
	@FXML
	public void updatePhases(){
		phaseTab.updatePhases();
	}
	
	
	
	/**
	 * This method displays the results on the result tab
	 */
	public void displayResults(boolean gotoTab){
		resultTab.displayResults();
		
		// go to the results tab
		if(gotoTab)
			this.getSelectionModel().select(tabResults);
	}
	
	
	
	/**
	 * Get a node by its name
	 * @param fxmlName the name of the node
	 * @return the node
	 */
	public Node getNode(String fxmlName){
		switch (fxmlName) {
			case "txtProjectName":
				return txtProjectName;
			case "txtProjectResponsible":
				return txtProjectResponsible;
			case "txtProjectDescription":
				return txtProjectDescription;

			default:
				return null;
		}
	}
	
	
	
	@Override
	public void clearAll(){
		txtProjectName.clear();
		txtProjectResponsible.clear();
		txtProjectDescription.clear();
		setProjectTimeStamp(null);
		
		skillTab.clearAll();
		resourceTab.clearAll();
		phaseTab.clearAll();
		resultTab.clearAll();
	}
	
	@Override
	public void disableWrite(boolean disable){
		txtProjectName.setEditable(!disable);
		txtProjectResponsible.setEditable(!disable);
		txtProjectDescription.setEditable(!disable);
		
		skillTab.disableWrite(disable);
		resourceTab.disableWrite(disable);
		phaseTab.disableWrite(disable);
	}


	@Override
	public String getProjectName() {
		return txtProjectName.getText();
	}
	
	@Override
	public void setProjectName(String projectName){
		txtProjectName.setText(projectName);
	}


	@Override
	public String getProjectResponsible() {
		return txtProjectResponsible.getText();
	}
	
	@Override
	public void setProjectResponsible(String projectResponsible){
		txtProjectResponsible.setText(projectResponsible);
	}


	@Override
	public String getProjectDescription() {
		return txtProjectDescription.getText();
	}
	
	@Override
	public void setProjectDescription(String projectDescription){
		txtProjectDescription.setText(projectDescription);
	}
	
	@Override
	public void setProjectTimeStamp(LocalDateTime timeStamp){
		if(timeStamp == null){
			crlFinish.setStyle("-fx-fill: #b11a3b");
			lblTimeStamp.setText("");
		}else{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd.MM.yyyy");
			lblTimeStamp.setText(timeStamp.format(formatter));
			
			// set the finish indicator
			crlFinish.setStyle("-fx-fill: green");
		}
	}
	
	@Override
	public Hashtable<String, Integer> getRealTimes(){
		return resultTab.getRealTimes();
	}


	
	@Override
	public ArrayList<SkillPane> getSkillPanes(){
		return skillTab.getSkillPanes();
	}
	
	@Override
	public SkillPane addSkillPane(){
		return skillTab.addSkillPane();
	}
	
	@Override
	public Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> getPhasePanes() {
		return phaseTab.getPhasePanes();
	}
	
	@Override
	public PhasePaneWrapper addPhasePaneWrapper(String phaseName, String parentName){
		return phaseTab.addPhasePaneWrapper(phaseName, parentName);
	}
	
	@Override
	public PhasePane addPhasePane(PhasePaneWrapper wrapper){
		return phaseTab.addPhasePane(wrapper);
	}

	@Override
	public ArrayList<ResourcePaneWrapper> getResourcePanes() {
		return resourceTab.getResourcePanes();
	}
	
	@Override
	public ResourcePane addResourcePane(int parentSkillID){
		return resourceTab.addResourcePane(parentSkillID);
	}
}