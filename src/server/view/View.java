package server.view;

import java.awt.Container;
import java.awt.Insets;
import java.io.IOException;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import server.ASingelton;
import server.core.ICoreServer;

/*
 * This class is the view for the server application
 */
public class View extends ASingelton implements IViewServer {
	
	private ICoreServer core;
	
	private JFrame frame;
	
	/**
	 * Constructor
	 */
	public View(ICoreServer core) {
		this.core = core;
		
		generateFrame();
	}
	
	
	// This method is invoked on JavaFX thread
	private static void initFX(JFXPanel fxPanel) {
		try {
			Parent root = FXMLLoader.load(View.class.getResource("View.fxml"));
			fxPanel.setScene(new Scene(root));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	
	// This method builds the JFrame and adds the JFXPanel
	private void generateFrame(){
		frame = new JFrame();
		frame.setTitle("PROJECT Server");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(100, 100);
		
		final JFXPanel fxPanel = new JFXPanel();
		frame.add(fxPanel);
        // apply the View.fxml
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
		frame.pack();
	}
	
	
	@Override
	public void showFrame(){
        // show the frame
		SwingUtilities.invokeLater(new Runnable() {
	        @Override
	        public void run() {
	        	frame.setVisible(true); 
	        }
	    });
        
	}
}