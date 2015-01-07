package client.view.controller;

import global.ASingelton;

import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import client.core.Core;
import client.core.ICoreClient;
import client.view.IComponents;
import client.view.IViewClient;
import client.view.View;
import client.view.components.PhasePaneWrapper;
import client.view.components.ProjectEditor;
import client.view.components.ResourcePaneWrapper;
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
	
	@FXML
	private AnchorPane editorPane;
	
	
	/**
	 * The constructor looks for the singleton instances of the Core and View
	 */
	public ViewController(){
		this.core = (ICoreClient) Core.getInstance(Core.class);
		this.view = (IViewClient) View.getInstance(View.class);
		
		projectEditor = new ProjectEditor();
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
		core.calculateProject();
	}

	
	
	@Override
	public ArrayList<SkillPane> getSkillPanes(){
		return projectEditor.getSkillPanes();
	}


	@Override
	public String getProjectName() {
		return projectEditor.getProjectName();
	}



	@Override
	public String getProjectResponsible() {
		return projectEditor.getProjectResponsible();
	}



	@Override
	public String getProjectDescription() {
		return projectEditor.getProjectDescription();
	}


	@Override
	public Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> getPhasePanes() {
		return projectEditor.getPhasePanes();
	}


	@Override
	public ArrayList<ResourcePaneWrapper> getResourcePanes() {
		return projectEditor.getResourcePanes();
	}
}
