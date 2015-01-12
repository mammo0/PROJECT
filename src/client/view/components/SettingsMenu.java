package client.view.components;

import java.io.IOException;

import client.core.Core;
import client.core.ICoreClient;
import client.view.ITester;
import client.view.IViewClient;
import client.view.InputTester;
import client.view.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class SettingsMenu extends GridPane implements ITester {
	
	@FXML
	private TextField txtServerAddress;
	@FXML
	private TextField txtServerPort;
	
	private IViewClient view;
	private ICoreClient core;
	
	
	
	/**
	 * The Constructor
	 */
	public SettingsMenu() {
		// load the project editor fxml
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/SettingsMenu.fxml"));
		
		// apply this class as root and controller
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		// load the fxml
		try {
			fxmlLoader.load();
		} catch (IOException e) { 
		    e.printStackTrace();
		}
		
		this.view = View.getInstance(View.class);
		this.core = Core.getInstance(Core.class);
		
		txtServerAddress.setText(core.getServerAddress());
		txtServerPort.setText(String.valueOf(core.getServerPort()));
		
		txtServerPort.textProperty().addListener(new InputTester(this, txtServerPort));
	}
	
	
	@FXML
	private void closeSettings(){
		((AnchorPane)view.getViewRootPane()).getChildren().remove(this);
	}
	
	@FXML
	private void saveSettings(){
		int port = Integer.valueOf(txtServerPort.getText());
		core.saveSettings(txtServerAddress.getText(), port);
		
		closeSettings();
	}


	@Override
	public boolean checkInput(Node node) {
		if(node.equals(txtServerPort)){
			TextField txtField = (TextField) node;
			if(txtField.getText().isEmpty())
				return true;
			
			try{
				int input = Integer.valueOf(txtField.getText());
				if(input < 0)
					return false;
				else if(input > 65535)
					return false;
				else
					return true;
			}catch (Exception e){
				return false;
			}
		}else
			return false;
	}
}
