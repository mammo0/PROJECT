package client.view;

import global.ASingelton;

import java.io.IOException;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import client.core.ICoreClient;

/**
 * This class is the view for the client application
 * @author Ammon
 *
 */
public class View extends ASingelton implements IViewClient {
	
	private ICoreClient core;
	
	// the wrapper frame
	private JFrame frame;
	
	
	/**
	 * Constructor
	 */
	public View(ICoreClient core) {
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
		frame.setTitle("PROJECT Client");
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
		
		// set up the frame
		frame.validate();
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