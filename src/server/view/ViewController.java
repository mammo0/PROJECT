package server.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import server.core.Core;
import server.core.ICoreServer;

/*
 * This class displays the server view on the screen
 */
public class ViewController implements Initializable{
	
	private ICoreServer core;
	private IViewServer view;
	
	@FXML
	private Circle statusInd;
	
	/*
	 * The constructor looks for the singleton instances of the Core and View
	 */
	public ViewController(){
		this.core = (ICoreServer) Core.getInstance(Core.class);
		this.view = (IViewServer) View.getInstance(View.class);
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
