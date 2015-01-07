package client.view;

import global.ASingelton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import client.core.ICoreClient;
import client.view.components.PhasePaneWrapper;
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
	
	
	/**
	 * Constructor
	 */
	public View(ICoreClient core) {
		this.core = core;
	}
	
	// this method sets the view controller
	private void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}
	
	
	
	@Override
	public void showFrame(){
		Application.launch(Frame.class, "");
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	@Override
	public ArrayList<SkillPane> getSkillPanes(){
		return viewController.getSkillPanes();
	}

	
	@Override
	public String getProjectName() {
		return viewController.getProjectName();
	}


	@Override
	public String getProjectResponsible() {
		return viewController.getProjectResponsible();
	}


	@Override
	public String getProjectDescription() {
		return viewController.getProjectDescription();
	}

	@Override
	public Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> getPhasePanes() {
		return viewController.getPhasePanes();
	}

	@Override
	public ArrayList<ResourcePaneWrapper> getResourcePanes() {
		return viewController.getResourcePanes();
	}
}