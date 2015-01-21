package client.view.controller;

import global.ASingelton;

import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.WritableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import client.core.Core;
import client.core.ICoreClient;
import client.view.IComponents;
import client.view.IViewClient;
import client.view.View;
import client.view.components.PhasePane;
import client.view.components.PhasePaneWrapper;
import client.view.components.ProjectEditor;
import client.view.components.ResourcePane;
import client.view.components.ResourcePaneWrapper;
import client.view.components.SettingsMenu;
import client.view.components.SkillPane;

/**
 * This class handles the user interactions from the view
 * @author Ammon
 *
 */
public class ViewController extends ASingelton implements Initializable, IComponents{
	
	private ICoreClient core;
	private IViewClient view;
	
	// the project editor pane
	private ProjectEditor projectEditor;
	// the settings menu
	private SettingsMenu settingsMenu;
	
	@FXML
	private AnchorPane editorPane;
	@FXML
	private ListView<String> lstProjects;
	
	@FXML
	private Label lblStatus;
	
	
	/**
	 * The constructor looks for the singleton instances of the Core and View
	 */
	public ViewController(){
		this.core = (ICoreClient) Core.getInstance(Core.class);
		this.view = (IViewClient) View.getInstance(View.class);
		
		projectEditor = new ProjectEditor();
		settingsMenu = new SettingsMenu();
	}
	
	
	// this method is called by the initialization of the frame
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// add the project editor to the main window
		editorPane.getChildren().add(projectEditor);
		AnchorPane.setTopAnchor(projectEditor, 0d);
		AnchorPane.setBottomAnchor(projectEditor, 0d);
		AnchorPane.setRightAnchor(projectEditor, 0d);
		AnchorPane.setLeftAnchor(projectEditor, 0d);
	}
	
	
	@FXML
	private void calculateProject(){
		if(core.calculateProject())
			projectEditor.displayResults(true);
	}
	
	@FXML
	private void openSettings(){
		AnchorPane root = (AnchorPane) view.getViewRootPane();
		root.getChildren().add(settingsMenu);
		AnchorPane.setTopAnchor(settingsMenu, 0d);
		AnchorPane.setBottomAnchor(settingsMenu, 0d);
		AnchorPane.setRightAnchor(settingsMenu, 0d);
		AnchorPane.setLeftAnchor(settingsMenu, 0d);
	}
	
	@FXML
	private void loadProject(){
		core.loadProject("Testprojekt");
	}
	
	@FXML
	private void saveProject(){
		core.saveProject();
	}
	
	
	private <Property> void newSimpleAnimation(WritableValue<Property> targetProperty, Property startValue, Property endValue, int duration){
		// set the start value if not null
		if (startValue != null)
			targetProperty.setValue(startValue);
		
		// start the animation
		Timeline timeline = new Timeline();
		KeyValue keyValue = new KeyValue(targetProperty, endValue);
		KeyFrame endFrame = new KeyFrame(Duration.seconds(duration), keyValue);
		timeline.getKeyFrames().add(endFrame);
		timeline.play();
	}
	
	
	/**
	 * This method marks a node on the view to set an input
	 * @param fxmlName the name of the node
	 */
	public void markNode(Node parent, String fxmlName){
		Node node = null;
		if(parent == null){
			node = projectEditor.getNode(fxmlName);
			projectEditor.getSelectionModel().select(0);
		}else if(parent instanceof SkillPane){
			node = ((SkillPane)parent).getNode(fxmlName);
			projectEditor.getSelectionModel().select(1);
		}else if(parent instanceof ResourcePane){
			node = ((ResourcePane)parent).getNode(fxmlName);
			projectEditor.getSelectionModel().select(2);
			Node resParent = parent.getParent();
			while(!(resParent instanceof ResourcePaneWrapper))
				resParent = resParent.getParent();
			((ResourcePaneWrapper) resParent).setExpanded(true);
		}else if(parent instanceof PhasePaneWrapper){
			node = ((PhasePaneWrapper)parent).getNode(fxmlName);
			projectEditor.getSelectionModel().select(3);
			((PhasePaneWrapper) parent).setExpanded(true);
		}else if(parent instanceof PhasePane){
			node = ((PhasePane)parent).getNode(fxmlName);
			projectEditor.getSelectionModel().select(3);
			Node phaParent = parent.getParent();
			while(!(phaParent instanceof PhasePaneWrapper))
				phaParent = phaParent.getParent();
			((PhasePaneWrapper) phaParent).setExpanded(true);
		}
		
		final Node finalNode = node;
		if(node != null){
			new Thread(){
				public void run(){
					while(!finalNode.focusedProperty().get()){
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Platform.runLater(new Runnable() {
						    @Override
						    public void run() {
						    	// TODO mark as red or something like that
						    	
						    	finalNode.requestFocus();
						    }
						});
					}
				}
			}.start();
		}
	}
	
	
	/**
	 * Display the results in the result tab
	 */
	public void displayResults(){
		projectEditor.displayResults(false);
	}
	
	
	/**
	 * This method displays a status text for a given period of time on the view
	 * @param status the status message
	 * @param displayTime the time how long the message is displayed
	 */
	public void setStatus(String status, int displayTime){
		lblStatus.setText(status);
		
		if(displayTime > 0){
			newSimpleAnimation(lblStatus.textProperty(), null, "", displayTime);
		}
	}
	
	
	/**
	 * This method updates the resources view
	 */
	public void updateResoources(){
		projectEditor.updateResources();
	}

	
	
	@Override
	public void clearAll(){
		projectEditor.clearAll();
	}


	@Override
	public String getProjectName() {
		return projectEditor.getProjectName();
	}
	
	@Override
	public void setProjectName(String projectName){
		projectEditor.setProjectName(projectName);
	}


	@Override
	public String getProjectResponsible() {
		return projectEditor.getProjectResponsible();
	}
	
	@Override
	public void setProjectResponsible(String projectResponsible){
		projectEditor.setProjectResponsible(projectResponsible);
	}


	@Override
	public String getProjectDescription() {
		return projectEditor.getProjectDescription();
	}
	
	@Override
	public void setProjectDescription(String projectDescription){
		projectEditor.setProjectDescription(projectDescription);
	}

	

	@Override
	public ArrayList<SkillPane> getSkillPanes(){
		return projectEditor.getSkillPanes();
	}
	
	@Override
	public SkillPane addSkillPane(){
		return projectEditor.addSkillPane();
	}
	
	@Override
	public Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> getPhasePanes() {
		return projectEditor.getPhasePanes();
	}
	
	@Override
	public PhasePaneWrapper addPhasePaneWrapper(String phaseName, int index, String parentName){
		return projectEditor.addPhasePaneWrapper(phaseName, index, parentName);
	}
	
	@Override
	public PhasePane addPhasePane(PhasePaneWrapper wrapper){
		return projectEditor.addPhasePane(wrapper);
	}

	@Override
	public ArrayList<ResourcePaneWrapper> getResourcePanes() {
		return projectEditor.getResourcePanes();
	}
	
	@Override
	public ResourcePane addResourcePane(int parentSkillID){
		return projectEditor.addResourcePane(parentSkillID);
	}
}
