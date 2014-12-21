package client.view.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import client.core.Core;
import client.core.ICoreClient;
import client.view.IViewClient;
import client.view.View;

/**
 * This class handles the user interactions from the view
 * @author Ammon
 *
 */
public class ViewController implements Initializable{
	
	private ICoreClient core;
	private IViewClient view;
	
	
	@FXML
	private GridPane competenceList;
	
	
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
	public void initialize(URL location, ResourceBundle resources) {
//		Parent root;
//		try {
//			root = FXMLLoader.load(View.class.getResource("/client/view/fxml/Competences.fxml"));
//			competenceList.add(root, 1, 0);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
