package server.view;

import java.io.File;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
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
	
	private String path;
	
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
	
	@FXML
	private TextField txtPath;
	
	
	/**
	 * The constructor looks for the singleton instances of the Core and View
	 */
	public ViewController(){
		this.core = (ICoreServer) Core.getInstance(Core.class);
		this.view = (IViewServer) View.getInstance(View.class);
		
		this.path = core.getProjectDirectory();
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
	 * This method is called, when the path chooser button is hit.
	 * It opens a directory chooser to select a directory for the project files.
	 */
	@FXML
	private void btnPathChooserClick(ActionEvent event){
		// show the directory chooser
		DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(view.getPrimaryStage());
        
        if(selectedDirectory != null){
        	txtPath.setText(selectedDirectory.getAbsolutePath() + File.separator);
        	core.setProjectDirectory(selectedDirectory.getAbsolutePath());
        }
	}
	
	
	/**
	 * This method is called, when the save settings button is clicked.
	 * It saves the port and path settings to an external file.
	 */
	@FXML
	private void btnSaveSettingsClick(ActionEvent event){
		core.saveSettings(txtPath.getText(), parseServerPort(txtPort.getText()));
	}
	
	
	/**
	 * This method is called, when the start button is hit.
	 * It will start the server thread.
	 */
	@FXML
	private void btnStartClick(ActionEvent event){
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
	private void btnStopClick(ActionEvent event){
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
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set the server port to default
		this.txtPort.setText(String.valueOf(core.getServerPort()));
		
		// add a change listener to the text field
		txtPort.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if(!newValue) {
		            core.setServerPort(parseServerPort(txtPort.getText()));
		        }
		    }
		});
		
		// set the project path
		txtPath.setText(path);
	}
}
