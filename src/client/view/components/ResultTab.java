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
import client.core.CostTableModel;
import client.core.PDTableModel;
import client.core.QuarterTableModel;
import client.view.dialogs.Dialog;
import client.view.dialogs.DialogConfirmation;

public class ResultTab extends AnchorPane {
	
	@FXML
	private TableView<PDTableModel> tblPD;
	@FXML
	private TableColumn<PDTableModel, String> colSkillPD;
	@FXML
	private TableColumn<PDTableModel, Integer> colShouldPD;
	@FXML
	private TableColumn<PDTableModel, Integer> colIsPD;
	@FXML
	private TableColumn<PDTableModel, Integer> colIsPDInt;
	@FXML
	private TableColumn<PDTableModel, Integer> colIsPDExt;
	private ObservableList<PDTableModel> pdData;
	
	
	@FXML
	private TableView<CostTableModel> tblCost;
	@FXML
	private TableColumn<CostTableModel, String> colSkillCost;
	@FXML
	private TableColumn<CostTableModel, Float> colTotalCost;
	@FXML
	private TableColumn<CostTableModel, Float> colIntCost;
	@FXML
	private TableColumn<CostTableModel, Float> colExtCost;
	private ObservableList<CostTableModel> costData;
	
	
	@FXML
	private TableView<QuarterTableModel> tblQuarter;
	@FXML
	private TableColumn<QuarterTableModel, String> colSkillQuarter;
	@FXML
	private TableColumn<QuarterTableModel, Float> colIntCostQuarter;
	@FXML
	private TableColumn<QuarterTableModel, Float> colExtCostQuarter;
	@FXML
	private TableColumn<QuarterTableModel, Integer> colIntPDQuarter;
	@FXML
	private TableColumn<QuarterTableModel, Integer> colExtPDQuarter;
	private ObservableList<QuarterTableModel> quarterData;
	
	
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
		timeline = new Timeline(this);
		ancTimeline.getChildren().add(timeline);
		AnchorPane.setTopAnchor(timeline, 0d);
		AnchorPane.setBottomAnchor(timeline, 0d);
		AnchorPane.setRightAnchor(timeline, 0d);
		AnchorPane.setLeftAnchor(timeline, 0d);
		
		
		// Set up the pd table
		prepareTable(tblPD);
		PropertyValueFactory<PDTableModel, Integer> pdShould = new PropertyValueFactory<PDTableModel, Integer>("pdShould");
		PropertyValueFactory<PDTableModel, Integer> pdIs = new PropertyValueFactory<PDTableModel, Integer>("pdIs");
		
		colSkillPD.setCellValueFactory(
				new PropertyValueFactory<PDTableModel, String>("skillName")
			);
		colShouldPD.setCellValueFactory(pdShould);
		colShouldPD.setCellFactory(column -> {
		    return new TableCell<PDTableModel, Integer>() {
		        @Override
		        protected void updateItem(Integer item, boolean empty) {
		            super.updateItem(item, empty);

		            if (item == null || empty) {
		                setText(null);
		                setStyle("");
		            }else{
		            	int pdIsValue = pdIs.call(new CellDataFeatures<PDTableModel, Integer>(tblPD, colIsPD, pdData.get(getIndex()))).getValue();
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
		    return new TableCell<PDTableModel, Integer>() {
		        @Override
		        protected void updateItem(Integer item, boolean empty) {
		            super.updateItem(item, empty);

		            if (item == null || empty) {
		                setText(null);
		                setStyle("");
		            }else{
		            	int pdShouldValue = pdShould.call(new CellDataFeatures<PDTableModel, Integer>(tblPD, colShouldPD, pdData.get(getIndex()))).getValue();
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
			    new PropertyValueFactory<PDTableModel, Integer>("pdIsInt")
			);
		colIsPDExt.setCellValueFactory(
			    new PropertyValueFactory<PDTableModel, Integer>("pdIsExt")
			);
		
		// Set up the cost table
		prepareTable(tblCost);
		colSkillCost.setCellValueFactory(
				new PropertyValueFactory<CostTableModel, String>("skillName")
			);
		colTotalCost.setCellValueFactory(
			    new PropertyValueFactory<CostTableModel, Float>("costTotal")
			);
		colIntCost.setCellValueFactory(
			    new PropertyValueFactory<CostTableModel, Float>("costInt")
			);
		colExtCost.setCellValueFactory(
			    new PropertyValueFactory<CostTableModel, Float>("costExt")
			);
		
		// Set up the quarter table
		prepareTable(tblQuarter);
		colSkillQuarter.setCellValueFactory(
				new PropertyValueFactory<QuarterTableModel, String>("skillName")
			);
		colIntCostQuarter.setCellValueFactory(
				new PropertyValueFactory<QuarterTableModel, Float>("costInt")
			);
		colExtCostQuarter.setCellValueFactory(
				new PropertyValueFactory<QuarterTableModel, Float>("costExt")
			);
		colIntPDQuarter.setCellValueFactory(
				new PropertyValueFactory<QuarterTableModel, Integer>("pdInt")
			);
		colExtPDQuarter.setCellValueFactory(
				new PropertyValueFactory<QuarterTableModel, Integer>("pdExt")
			);
	}
	
	
	// this mehtod refreshes the pd result tab
	private void refreshPDResults(){
		pdData = core.getPDTable(tbnRisk.isSelected());
		tblPD.setItems(pdData);
	}
	
	
	@FXML
	private void tbnRiskClick(){
		if(tbnRisk.isSelected())
			tbnRisk.setText("Risikozuschlag AUS");
		else
			tbnRisk.setText("Risikozuschlag EIN");
		
		refreshPDResults();
	}
	
	
	// this method finishes the project
	@FXML
	private void btnFinishClick(){
		String message = "Möchten Sie das Projekt wirklich abschließen?\n"
				+ "Es kann danach nicht mehr bearbeitet werden.";
		DialogConfirmation confirmation = new DialogConfirmation("Projekt abschließen", message);
		if(!Dialog.showDialog(confirmation))
			return;
		
		finishable.set(false);
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
		for(TableColumn<Model, ?> column : table.getColumns()){
			if(!column.getColumns().isEmpty()){
				for(TableColumn<Model, ?> innerColumn : column.getColumns()){
					innerColumn.setCellFactory(columnFactory -> {
					    return newTableCellFactory();
					});
				}
			}else{
				column.setCellFactory(columnFactory -> {
				    return newTableCellFactory();
				});
			}
		}
	}
	
	// this method creates a specific table cell factory
	private <Model, Type> TableCell<Model, Type> newTableCellFactory(){
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
		refreshPDResults();
		
		costData = core.getCostTable();
		tblCost.setItems(costData);
		
		timeline.createTimeline(core.getProjectStartDate(), core.getProjectEndDate());
		refreshQuarterResults();
	}
	
	
	/**
	 * This method refreshes the quarter results tab
	 */
	public void refreshQuarterResults(){
		quarterData = core.getQuarterTable(timeline.getSelectedYearBegin(), timeline.getSelectedQuarterBegin(),
										   timeline.getSelectedYearEnd(), timeline.getSelectedQuarterEnd());
		tblQuarter.setItems(quarterData);
	}
}
