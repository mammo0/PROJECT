package client.view.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import client.view.IViewClient;
import client.view.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class HelpMenu extends AnchorPane {
	
	private IViewClient view;
	
	@FXML
	private TextArea txtHelpText;
	
	
	public HelpMenu(){
		// load the project editor fxml
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/HelpMenu.fxml"));
		
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
		
		File helpFile = new File("res/help/Readme.txt");
		if(helpFile.canRead()){
			BufferedReader reader;
			StringBuilder helpText = new StringBuilder();
			try {
				reader = new BufferedReader(new FileReader(helpFile));
				String line = null;
				while ((line = reader.readLine()) != null) {
					helpText.append(line);
					helpText.append("\n");
				}
				reader.close();
				
				txtHelpText.setText(helpText.toString());
			} catch (IOException e) {
				
			}
		}
	}
	
	
	@FXML
	private void closeHelpMenu(){
		((AnchorPane)view.getViewRootPane()).getChildren().remove(this);
	}
}
