package client.view.controller;

import global.ASingelton;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import client.core.Core;
import client.core.ICoreClient;
import client.view.IViewClient;
import client.view.View;
import client.view.components.ProjectEditor;
import client.view.components.SkillPane;

/**
 * This class handles the user interactions from the view
 * @author Ammon
 *
 */
public class ViewController extends ASingelton implements Initializable{
	
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
		view.setViewController(this);
		
		projectEditor = new ProjectEditor();
	}
	
	
	
	/**
	 * Get the skill panes from the project editor
	 * @return the skill panes
	 */
	public ArrayList<SkillPane> getSkillPanes(){
		return projectEditor.getSkillPanes();
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
}
