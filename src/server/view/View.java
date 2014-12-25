package server.view;

import global.ASingelton;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.core.Core;
import server.core.ICoreServer;

/**
 * This class is the view for the server application
 * @author Ammon
 *
 */
public class View extends ASingelton implements IViewServer {
	
	private ICoreServer core;
	
	
	/**
	 * Constructor
	 */
	public View(ICoreServer core) {
		this.core = core;
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
		
		private static ICoreServer core;
		
		static{
			Frame.core = (ICoreServer) Core.getInstance(Core.class);
		}
		
		
		@Override
		public void start(Stage primaryStage) {
			Parent root;
			try {
				root = FXMLLoader.load(View.class.getResource("View.fxml"));
	            primaryStage.setTitle("PROJECT Server");
	            primaryStage.setScene(new Scene(root));
	            primaryStage.setResizable(false);
	            
	            // handle the window close event
	            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	                @Override
	                public void handle(WindowEvent t) {
	                	// stop the server
	                	core.stopServer();
	                	
	                	// exit the application
	                    Platform.exit();
	                    System.exit(0);
	                }
	            });
	            
	            // show the frame
	            primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}