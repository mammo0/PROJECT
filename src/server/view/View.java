package server.view;

import java.io.IOException;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.swing.JFrame;

import server.core.ICore;

@SuppressWarnings("serial")
public class View extends JFrame implements IView {
	
	// This method is invoked on JavaFX thread
	private static void initFX(JFXPanel fxPanel) {
		try {
			Parent root = FXMLLoader.load(View.class.getResource("View.fxml"));
			fxPanel.setScene(new Scene(root));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	

	
	/**
	 * Constructor
	 */
	public View(ICore core) {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(228, 137);
		
		final JFXPanel fxPanel = new JFXPanel();
		getContentPane().add(fxPanel);
        // apply the View.fxml
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
        });
       
        
        // apply the core to the view controller
        ViewController.setCore(core);
        
        // show the frame
        setVisible(true);
	}
}