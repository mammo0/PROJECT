package server.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import server.core.ICore;

/*
 * This class displays the server view on the screen
 */
public class ViewController implements Initializable{
	
	private static ICore core;
	
	@FXML
	private Circle statusInd;
	
	
	
	/*
	 * Getter / Setter
	 */
	
	/*
	 * This method sets the core object
	 */
	public static void setCore(ICore core){
		ViewController.core = core;
	}
	
	
	
	/*
	 * Button controller
	 */
	
	/*
	 * This method is called, when the start button is hit.
	 * It will start the server thread.
	 */
	public void btnStartClick(ActionEvent event){
		// change the color only if the server is running
		if(core.isServerRunning())
			statusInd.setFill(Color.GREEN);
    }
	
	/*
	 * This method is called, when the stop button is hit.
	 * It will stop the server thread.
	 */
	public void btnStopClick(ActionEvent event){
		statusInd.setFill(Color.RED);
	}
	
	
	
	@Override
	// this method does nothing but has to be implemented
	public void initialize(URL location, ResourceBundle resources) {}
}
