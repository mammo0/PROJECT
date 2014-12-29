package client.view;

import global.ASingelton;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import client.core.ICoreClient;
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
		this.viewController = ViewController.getInstance(ViewController.class);
	}
	
	
	@Override
	public void showFrame(){
		Application.launch(Frame.class, "");
		this.viewController = ViewController.getInstance(ViewController.class);
	}
	
	
	
	@Override
	public void setViewController(ViewController viewController) {
		this.viewController = viewController;
	}
	
	
	
	@Override
	public ArrayList<SkillPane> getSkillPanes(){
		return viewController.getSkillPanes();
	}
	
	
	
	/**
	 * This inner class shows the frame
	 * @author Ammon
	 *
	 */
	public static class Frame extends Application {
		
//		private static IViewClient view;
//		
//		static{
//			Frame.view = (IViewClient) View.getInstance(View.class);
//		}
		
		
		@Override
		public void start(Stage primaryStage) {
			Parent root;
			try {
				root = FXMLLoader.load(View.class.getResource("fxml/View.fxml"));
				Stage stage = new Stage();
	            stage.setTitle("PROJECT Client");
	            stage.setScene(new Scene(root));
	            stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}