package server.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import server.core.Core;
import server.core.ICoreServer;

/**
 * This class displays the server view on the screen
 * @author Ammon
 *
 */
public class ViewController implements Initializable{
	
	private ICoreServer core;
	private IViewServer view;
	
	@FXML
	private Circle statusInd;
	@FXML
	private Label lblServerStatus;
	
	@FXML
	private Button btnStart;
	@FXML
	private Button btnStop;
	
	@FXML
	private TextField txtPort;
	
	
	/**
	 * The constructor looks for the singleton instances of the Core and View
	 */
	public ViewController(){
		this.core = (ICoreServer) Core.getInstance(Core.class);
		this.view = (IViewServer) View.getInstance(View.class);
	}
	
	
	private int parseServerPort(String text){
		try{
			return Integer.parseInt(text);
		} catch (Exception e){
			return -1;
		}
	}
	
	
	/*
	 * Button controller
	 */
	
	/**
	 * This method is called, when the start button is hit.
	 * It will start the server thread.
	 */
	@FXML
	public void btnStartClick(ActionEvent event){
		// start the server
		core.startServer();
		
		// check if the server is running
		if(core.isServerRunning()){
			// disable the start button
			btnStart.setDisable(true);
			
			// change the server status indicator
			statusInd.setFill(Color.GREEN);
			
			// change the displayed text
			lblServerStatus.setText("Server running (Port: "+core.getServerPort()+")");
		}
    }
	
	/**
	 * This method is called, when the stop button is hit.
	 * It will stop the server thread.
	 */
	@FXML
	public void btnStopClick(ActionEvent event){
		// stop the server
		core.stopServer();
		
		// check if the server has stopped
		if(!core.isServerRunning()){
			// enable the start button again
			btnStart.setDisable(false);
			
			// change the server status indicator
			statusInd.setFill(Color.RED);
			
			// change the displayed text
			lblServerStatus.setText("Server not running");
		}
	}
	
	/**
	 * This method is called when the text has changed
	 */
	@FXML
	public void txtPortChanged(InputMethodEvent event){
		System.out.println();
	}
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set the server port to default
		core.setServerPort(parseServerPort(txtPort.getText()));
		
		// add a change listener to the text field
		txtPort.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(!newValue) {
		            core.setServerPort(parseServerPort(txtPort.getText()));
		        }
		    }
		});
	}
}
