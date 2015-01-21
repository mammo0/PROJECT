package client.view;

import global.ASingelton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import client.core.ICoreClient;
import client.view.components.PhasePane;
import client.view.components.PhasePaneWrapper;
import client.view.components.ResourcePane;
import client.view.components.ResourcePaneWrapper;
import client.view.components.SkillPane;
import client.view.controller.ViewController;

/**
 * This class is the view for the client application
 * @author Ammon
 *
 */
public class View extends ASingelton implements IViewClient {
	
	private ICoreClient core;
	
	private ViewController viewController;
	
	private Node viewRootPane;
	
	
	/**
	 * Constructor
	 */
	public View(ICoreClient core) {
		this.core = core;
	}
	
	// this method sets the view controller
	private void setViewController(ViewController viewController) {
		this.viewController = viewController;
		core.guiInitialized();
	}
	
	// this method sets the root pane of the view
	private void setViewRootPane(Node viewRootPane){
		this.viewRootPane = viewRootPane;
	}
	
	@Override
	public Node getViewRootPane() {
		return viewRootPane;
	}
	
	
	@Override
	public void showFrame(){
		Application.launch(Frame.class, "");
	}
	
	@Override
	public void setStatus(String status, int displayTime) {
		viewController.setStatus(status, displayTime);
	}
	
	@Override
	public void markNode(Node parent, String fxmlName){
		viewController.markNode(parent, fxmlName);
	}
	
	@Override
	public void displayResults(){
		viewController.displayResults();
	}
	
	
	
	/**
	 * This inner class shows the frame
	 * @author Ammon
	 *
	 */
	public static class Frame extends Application {
		
		private static View view;
		
		// get the view instance
		static{
			view = View.getInstance(View.class);
		}
		
		
		
		@Override
		public void start(Stage primaryStage) {
			Parent root;
			try {
				root = FXMLLoader.load(View.class.getResource("fxml/View.fxml"));
				Stage stage = new Stage();
	            stage.setTitle("PROJECT Client");
	            stage.setScene(new Scene(root));
	            stage.show();
	            view.setViewController(ViewController.getInstance(ViewController.class));
	            view.setViewRootPane(root);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	@Override
	public void clearAll(){
		viewController.clearAll();
	}
	
	
	@Override
	public String getProjectName() {
		return viewController.getProjectName();
	}
	
	@Override
	public void setProjectName(String projectName){
		viewController.setProjectName(projectName);
	}


	@Override
	public String getProjectResponsible() {
		return viewController.getProjectResponsible();
	}
	
	@Override
	public void setProjectResponsible(String projectResponsible){
		viewController.setProjectResponsible(projectResponsible);
	}


	@Override
	public String getProjectDescription() {
		return viewController.getProjectDescription();
	}
	
	@Override
	public void setProjectDescription(String projectDescription){
		viewController.setProjectDescription(projectDescription);
	}

	
	
	@Override
	public ArrayList<SkillPane> getSkillPanes(){
		return viewController.getSkillPanes();
	}
	
	@Override
	public SkillPane addSkillPane(){
		return viewController.addSkillPane();
	}
	
	@Override
	public Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> getPhasePanes() {
		return viewController.getPhasePanes();
	}
	
	@Override
	public PhasePaneWrapper addPhasePaneWrapper(String phaseName, int index, String parentName){
		return viewController.addPhasePaneWrapper(phaseName, index, parentName);
	}
	
	@Override
	public PhasePane addPhasePane(PhasePaneWrapper wrapper){
		return viewController.addPhasePane(wrapper);
	}

	@Override
	public ArrayList<ResourcePaneWrapper> getResourcePanes() {
		return viewController.getResourcePanes();
	}
	
	@Override
	public ResourcePane addResourcePane(int parentSkillID){
		return viewController.addResourcePane(parentSkillID);
	}
}