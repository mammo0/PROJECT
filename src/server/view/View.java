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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;

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
		
		
		private void showFrame(Stage stage){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					stage.show();
					stage.toFront();
				}
			});
		}
		
		private void closeFrame(){
			// stop the server
			core.stopServer();
			
			// exit the application
            Platform.exit();
            System.exit(0);
		}
		
		
		public void setTray(Stage stage){
			SystemTray sTray = null;
			sTray = SystemTray.getSystemTray();
			Image image = null;
			InputStream is = getClass().getResourceAsStream("/server/res/icons/icon.png");
			try{
				if (is != null)
					{
					byte[] buffer = new byte[0];
					byte[] tmpbuf = new byte[1024];
					while (true)
					{
						int len = is.read(tmpbuf);
						if (len <= 0) {
						break;
					}
					byte[] newbuf = new byte[buffer.length + len];
					System.arraycopy(buffer, 0, newbuf, 0, buffer.length);
					System.arraycopy(tmpbuf, 0, newbuf, buffer.length, len);
					buffer = newbuf;
					}
					//create image
					image = Toolkit.getDefaultToolkit().createImage(buffer);
					is.close();
				}
			}catch (IOException e){}
			
			ActionListener listenerShow = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					showFrame(stage);
				}
			};

			ActionListener listenerClose = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					closeFrame();
				}
			};

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					if(core.isServerRunning()){
						stage.toBack();
						stage.hide();
					}else{
						closeFrame();
					}
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
			icon.setImageAutoSize(true);
			icon.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e) {}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() > 1){
						showFrame(stage);
					}
				}
			});

			try {
				sTray.add(icon);
			}
			catch (AWTException e) {}
		}
		
		
		@Override
		public void start(Stage primaryStage) {
			Parent root;
			try {
				root = FXMLLoader.load(View.class.getResource("View.fxml"));
	            primaryStage.setTitle("PROJECT Server");
	            primaryStage.setScene(new Scene(root));
	            primaryStage.setResizable(false);
	            primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/server/res/icons/icon.png")));
	            
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
		                	closeFrame();
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