package client.view.components;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import client.core.Core;
import client.core.ICoreClient;
import client.core.costTableModel;
import client.core.pdTableModel;
import client.core.quarterTableModel;

public class ResultTab extends AnchorPane {
	
	@FXML
	private TableView<pdTableModel> tblPD;
	@FXML
	private TableColumn<pdTableModel, String> colSkillPD;
	@FXML
	private TableColumn<pdTableModel, Integer> colShouldPD;
	@FXML
	private TableColumn<pdTableModel, Integer> colIsPD;
	@FXML
	private TableColumn<pdTableModel, Integer> colIsPDInt;
	@FXML
	private TableColumn<pdTableModel, Integer> colIsPDExt;
	private ObservableList<pdTableModel> pdData;
	
	
	@FXML
	private TableView<costTableModel> tblCost;
	@FXML
	private TableColumn<costTableModel, String> colSkillCost;
	@FXML
	private TableColumn<costTableModel, Float> colTotalCost;
	@FXML
	private TableColumn<costTableModel, Float> colIntCost;
	@FXML
	private TableColumn<costTableModel, Float> colExtCost;
	private ObservableList<costTableModel> costData;
	
	
	@FXML
	private TableView<quarterTableModel> tblQuarter;
	@FXML
	private TableColumn<quarterTableModel, String> colSkillQuarter;
	@FXML
	private TableColumn<quarterTableModel, Float> colIntCostQuarter;
	@FXML
	private TableColumn<quarterTableModel, Float> colExtCostQuarter;
	@FXML
	private TableColumn<quarterTableModel, Integer> colIntPDQuarter;
	@FXML
	private TableColumn<quarterTableModel, Integer> colExtPDQuarter;
	private ObservableList<quarterTableModel> quarterData;
	
	
	@FXML
	private AnchorPane ancTimeline;
	
	@FXML
	private ToggleButton tbnRisk;
	
	private Timeline timeline;
	
	
	private ICoreClient core;
	
	
	/**
	 * The constructor
	 */
	public ResultTab() { 
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/ResultTab.fxml"));
		
		// apply this class as root and controller
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		// load the fxml
		try {
			 fxmlLoader.load();
		} catch (IOException e) { 
		     e.printStackTrace();
		}
		
		this.core = Core.getInstance(Core.class);
		
		
		// add the time line
		timeline = new Timeline(2014, 1, 2015, 4);
		ancTimeline.getChildren().add(timeline);
		AnchorPane.setTopAnchor(timeline, 0d);
		AnchorPane.setBottomAnchor(timeline, 0d);
		AnchorPane.setRightAnchor(timeline, 0d);
		AnchorPane.setLeftAnchor(timeline, 0d);
		
		
		// Set up the pd table
		colSkillPD.setCellValueFactory(
				new PropertyValueFactory<pdTableModel, String>("skillName")
			);
		colShouldPD.setCellValueFactory(
			    new PropertyValueFactory<pdTableModel, Integer>("pdShould")
			);
		colIsPD.setCellValueFactory(
			    new PropertyValueFactory<pdTableModel, Integer>("pdIs")
			);
		colIsPDInt.setCellValueFactory(
			    new PropertyValueFactory<pdTableModel, Integer>("pdIsInt")
			);
		colIsPDExt.setCellValueFactory(
			    new PropertyValueFactory<pdTableModel, Integer>("pdIsExt")
			);
		
		// Set up the cost table
		colSkillCost.setCellValueFactory(
				new PropertyValueFactory<costTableModel, String>("skillName")
			);
		colTotalCost.setCellValueFactory(
			    new PropertyValueFactory<costTableModel, Float>("costTotal")
			);
		colIntCost.setCellValueFactory(
			    new PropertyValueFactory<costTableModel, Float>("costInt")
			);
		colExtCost.setCellValueFactory(
			    new PropertyValueFactory<costTableModel, Float>("costExt")
			);
		
		// Set up the quarter table
		colSkillQuarter.setCellValueFactory(
				new PropertyValueFactory<quarterTableModel, String>("skillName")
			);
		colIntCostQuarter.setCellValueFactory(
				new PropertyValueFactory<quarterTableModel, Float>("costInt")
			);
		colExtCostQuarter.setCellValueFactory(
				new PropertyValueFactory<quarterTableModel, Float>("costExt")
			);
		colIntPDQuarter.setCellValueFactory(
				new PropertyValueFactory<quarterTableModel, Integer>("pdInt")
			);
		colExtPDQuarter.setCellValueFactory(
				new PropertyValueFactory<quarterTableModel, Integer>("pdExt")
			);
	}
	
	
	@FXML
	private void tbnRiskClick(){
		if(tbnRisk.isSelected())
			tbnRisk.setText("Risikozuschlag AUS");
		else
			tbnRisk.setText("Risikozuschlag EIN");
		
//		displayResults();
	}
	
	
	/**
	 * This method displays the results in the tables
	 */
	public void displayResults(){
		pdData = core.getPDTable(tbnRisk.isSelected());
		tblPD.setItems(pdData);
		
		costData = core.getCostTable();
		tblCost.setItems(costData);
		
//		quarterData = core.getQuarterTable();
//		tblQuarter.setItems(quarterData);
	}
}
