package client.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import client.core.Core;
import client.core.ICoreClient;

/**
 * This class handles the user interactions from the view
 * @author Ammon
 *
 */
public class ViewController implements Initializable{
	
	private ICoreClient core;
	private IViewClient view;
	
	
	/**
	 * The constructor looks for the singleton instances of the Core and View
	 */
	public ViewController(){
		this.core = (ICoreClient) Core.getInstance(Core.class);
		this.view = (IViewClient) View.getInstance(View.class);
	}
	
	
	/*
	 * Button controller
	 */
	
	
	
	@Override
	// this method does nothing but has to be implemented
	public void initialize(URL location, ResourceBundle resources) {}
}
