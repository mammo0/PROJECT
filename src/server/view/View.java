package server.view;

import global.ASingelton;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	
	private Stage primaryStage;
	
	
	
	
	// set the primary stage
	private void setPrimaryStage(Stage primaryStage){
		this.primaryStage = primaryStage;
	}
	
	
	
	
	@Override
	public Stage getPrimaryStage(){
		return primaryStage;
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
		private static View view;
		
		static{
			Frame.core = Core.getInstance(Core.class);
			Frame.view = View.getInstance(View.class);
		}
		
		
		public void setTray(Stage stage){
			SystemTray sTray = null;
			sTray = SystemTray.getSystemTray();
			// TODO set icon
			Image image = Toolkit.getDefaultToolkit().getImage("Mario-icon.png");
			
			ActionListener listenerShow = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							stage.show();
							stage.toFront();
						}
					});
				}
			};

			ActionListener listenerClose = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// stop the server
					core.stopServer();
					
					System.exit(0);
				}
			};

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					stage.toBack();
					stage.hide();
				}
			});
			
			
			PopupMenu popup = new PopupMenu();
			MenuItem showItem = new MenuItem("Öffnen");
			MenuItem exitItem = new MenuItem("Beenden");

			showItem.addActionListener(listenerShow);
			exitItem.addActionListener(listenerClose);

			popup.add(showItem);
			popup.add(exitItem);

			TrayIcon icon = new TrayIcon(image, "PROJECT", popup);

			try {
				sTray.add(icon);
			}
			catch (AWTException e) {
				System.err.println(e);
			}
		}
		
		
		@Override
		public void start(Stage primaryStage) {
			Parent root;
			try {
				root = FXMLLoader.load(View.class.getResource("View.fxml"));
	            primaryStage.setTitle("PROJECT Server");
	            primaryStage.setScene(new Scene(root));
	            primaryStage.setResizable(false);
	            
	            // set the primary stage
	            view.setPrimaryStage(primaryStage);
	            
	            if (SystemTray.isSupported()) {
	    			Platform.setImplicitExit(false);
	    			setTray(primaryStage);
	    		}else{
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
	    		}
	            
	            // show the frame
	            primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}