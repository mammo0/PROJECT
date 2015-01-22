package client.view.components;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
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
	private Button btnFinish;
	@FXML
	private ToggleButton tbnRisk;
	
	private Timeline timeline;
	
	private BooleanProperty finishable;
	
	private ArrayList<TableCell<?, ?>> errorCells;
	
	
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
		
		errorCells = new ArrayList<TableCell<?,?>>();
		
		finishable = new SimpleBooleanProperty(this, "finishable", false);
		btnFinish.disableProperty().bind(finishable.not());
		
		// add the time line
		timeline = new Timeline(2014, 1, 2015, 4);
		ancTimeline.getChildren().add(timeline);
		AnchorPane.setTopAnchor(timeline, 0d);
		AnchorPane.setBottomAnchor(timeline, 0d);
		AnchorPane.setRightAnchor(timeline, 0d);
		AnchorPane.setLeftAnchor(timeline, 0d);
		
		
		// Set up the pd table
		prepareTable(tblPD);
		PropertyValueFactory<pdTableModel, Integer> pdShould = new PropertyValueFactory<pdTableModel, Integer>("pdShould");
		PropertyValueFactory<pdTableModel, Integer> pdIs = new PropertyValueFactory<pdTableModel, Integer>("pdIs");
		
		colSkillPD.setCellValueFactory(
				new PropertyValueFactory<pdTableModel, String>("skillName")
			);
		colShouldPD.setCellValueFactory(pdShould);
		colShouldPD.setCellFactory(column -> {
		    return new TableCell<pdTableModel, Integer>() {
		        @Override
		        protected void updateItem(Integer item, boolean empty) {
		            super.updateItem(item, empty);

		            if (item == null || empty) {
		                setText(null);
		                setStyle("");
		            }else{
		            	int pdIsValue = pdIs.call(new CellDataFeatures<pdTableModel, Integer>(tblPD, colIsPD, pdData.get(getIndex()))).getValue();
		            	if(pdIsValue<item){
		            		setStyle("-fx-background-color: red; -fx-text-fill: white;");
		            		errorCells.add(this);
		            		finishable.set(false);
		            	}else if(getIndex() == pdData.size()-1){
		            		setStyle("-fx-background-color: lightgrey");
		            	}else{
		            		setStyle("");
		            		if(errorCells.contains(this) || errorCells.isEmpty()){
		            			errorCells.remove(this);
		            			finishable.set(true);
		            		}
		            	}
                        setText(String.valueOf(item));
                    }
		        }
		    };
		});
		colIsPD.setCellValueFactory(pdIs);
		colIsPD.setCellFactory(column -> {
		    return new TableCell<pdTableModel, Integer>() {
		        @Override
		        protected void updateItem(Integer item, boolean empty) {
		            super.updateItem(item, empty);

		            if (item == null || empty) {
		                setText(null);
		                setStyle("");
		            }else{
		            	int pdShouldValue = pdShould.call(new CellDataFeatures<pdTableModel, Integer>(tblPD, colShouldPD, pdData.get(getIndex()))).getValue();
		            	if(pdShouldValue>item){
		            		setStyle("-fx-background-color: red; -fx-text-fill: white;");
		            		finishable.set(false);
		            		errorCells.add(this);
		            	}else if(getIndex() == pdData.size()-1){
		            		setStyle("-fx-background-color: lightgrey");
		            	}else{
		            		setStyle("");
		            		if(errorCells.contains(this) || errorCells.isEmpty()){
		            			errorCells.remove(this);
		            			if(!core.isProjectFinished())
		            				finishable.set(true);
		            		}
		            	}
                        setText(String.valueOf(item));
                    }
		        }
		    };
		});
		colIsPDInt.setCellValueFactory(
			    new PropertyValueFactory<pdTableModel, Integer>("pdIsInt")
			);
		colIsPDExt.setCellValueFactory(
			    new PropertyValueFactory<pdTableModel, Integer>("pdIsExt")
			);
		
		// Set up the cost table
		prepareTable(tblCost);
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
	
	
	// this method finishes the project
	@FXML
	private void btnFinishClick(){
		finishable.set(false);
		
		// TODO confirm dialog
		
		core.finishProject();
	}
	
	
	// prepare the table
	private <Model, Type> void prepareTable(TableView<Model> table){
		// exclude the last line from the sorting
		table.sortPolicyProperty().set(t -> {
		    Comparator<Model> comparator = (r1, r2) -> 
		         r1 == table.getItems().get(table.getItems().size()-1) ? 1 //TOTAL at the bottom
		       : r2 == table.getItems().get(table.getItems().size()-1) ? -1 //TOTAL at the bottom
		       : t.getComparator() == null ? 0 //no column sorted: don't change order
		       : t.getComparator().compare(r1, r2); //columns are sorted: sort accordingly
		    FXCollections.sort(t.getItems(), comparator);
		    return true;
		});
		
		// iterate over all columns
		for(TableColumn<Model, ?> columns : table.getColumns()){
			columns.setCellFactory(column -> {
			    return new TableCell<Model, Type>() {
			        @Override
			        protected void updateItem(Type item, boolean empty) {
			            super.updateItem(item, empty);
	
			            if (item == null || empty) {
			                setText(null);
			                setStyle("");
			            }else{
			            	// mark the last line in the table
			            	if(getIndex() == pdData.size()-1)
			            		setStyle("-fx-background-color: lightgrey");
			            	else
			            		setStyle("");
			            	
			            	// display the floats with only two decimals
			            	if(item instanceof Float){
			            		NumberFormat formatter = NumberFormat.getCurrencyInstance();
			            		setText(formatter.format(item));
			            		return;
			            	}
			            	
	                        setText(String.valueOf(item));
	                    }
			        }
			    };
			});
		}
	}
	
	
	/**
	 * Clear all inputs
	 */
	public void clearAll(){
		if(pdData != null)
			pdData.clear();
		if(costData != null)
			costData.clear();
		if(quarterData != null)
			quarterData.clear();
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
