package client.view.components;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import client.core.Core;
import client.core.CostTableModel;
import client.core.ICoreClient;
import client.core.OverflowTableModel;
import client.core.PDTableModel;
import client.core.QuarterTableModel;
import client.view.IViewClient;
import client.view.View;
import client.view.dialogs.Dialog;
import client.view.dialogs.DialogConfirmation;
import client.view.dialogs.DialogWarning;

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
	private TableColumn<PDTableModel, TextField> colRealPD;
	private ObservableList<PDTableModel> pdData;
	
	
	@FXML
	private TableView<OverflowTableModel> tblOverflow;
	@FXML
	private TableColumn<OverflowTableModel, String> colSkillOverflow;
	@FXML
	private TableColumn<OverflowTableModel, Integer> colOverflowPD;
	private ObservableList<OverflowTableModel> overflowData;
	
	
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
	private Button btnExportCSV;
	
	@FXML
	private ToggleButton tbnRisk;
	@FXML
	private ToggleButton tbnOverflow;
	
	@FXML
	private VBox vbxPDTables;
	
	@FXML
	private TabPane tabResult;
	
	private Timeline timeline;
	
	private BooleanProperty finishable;
	
	private ArrayList<TableCell<?, ?>> errorCells;
	
	
	private ICoreClient core;
	private IViewClient view;
	
	
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
		
		String viewCss = getClass().getResource("/client/view/css/View.css").toExternalForm();
		String resultTabCss = getClass().getResource("/client/view/css/ResultTab.css").toExternalForm();
		this.getStylesheets().addAll(viewCss, resultTabCss);
		
		this.core = Core.getInstance(Core.class);
		this.view = View.getInstance(View.class);
		
		vbxPDTables.getChildren().remove(tblOverflow);
		
		errorCells = new ArrayList<TableCell<?,?>>();
		
		finishable = new SimpleBooleanProperty(this, "finishable", false);
		btnFinish.disableProperty().bind(finishable.not());
		btnExportCSV.disableProperty().bind(finishable.not());
		
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
		            		setStyle("-fx-background-color: #b11a3b; -fx-text-fill: white;");
		            		errorCells.add(this);
		            		finishable.set(false);
		            	}else if(getIndex() == pdData.size()-1){
		            		setStyle("-fx-background-color: #b2b2b2");
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
		            		setStyle("-fx-background-color: #b11a3b; -fx-text-fill: white;");
		            		finishable.set(false);
		            		errorCells.add(this);
		            	}else if(getIndex() == pdData.size()-1){
		            		setStyle("-fx-background-color: #b2b2b2");
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
		colRealPD = new TableColumn<PDTableModel, TextField>("Ist-Zeiten");
		colRealPD.setMaxWidth(100000d);
		colRealPD.setCellFactory(column -> {return new TableCell<PDTableModel, TextField>(){
			@Override
	        protected void updateItem(TextField item, boolean empty) {
				super.updateItem(item, empty);
				
				if (item == null || empty) {
					setStyle("");
				}else{
	            	// mark the last line in the table
					if(getIndex() == pdData.size()-1)
	            		setStyle("-fx-background-color: #b2b2b2");
				}
				
	            if (item != null || empty) {
                    setGraphic(item);
	            }
			}
		};});
		colRealPD.setCellValueFactory(
			    new PropertyValueFactory<PDTableModel, TextField>("pdReal")
			);
		
		// Set up the overflow table
		prepareTable(tblOverflow);
		colSkillOverflow.setCellValueFactory(
				new PropertyValueFactory<OverflowTableModel, String>("skillName")
			);
		colOverflowPD.setCellValueFactory(
				new PropertyValueFactory<OverflowTableModel, Integer>("pdOverflow")
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
		
		tabResult.tabMinWidthProperty().bind(tabResult.widthProperty().subtract(120).divide(3));
	}
	
	
	// this mehtod refreshes the pd result tab
	private void refreshPDResults(){
		pdData = core.getPDTable(tbnRisk.isSelected());
		
		if(core.isProjectFinished() && !tblPD.getColumns().contains(colRealPD)){
			tblPD.getColumns().add(colRealPD);
		}else if(!core.isProjectFinished())
			tblPD.getColumns().remove(colRealPD);
		
		RealTextField.updateSum();
		
		tblPD.setItems(pdData);
	}
	
	
	@FXML
	private void btnExportCSVClick(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialFileName(core.getLoadedProjectName());
		  
        // Set extension filter
		ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV Dateien (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        
        if(core.isProjectFinished())
        	core.exportCSV(fileChooser.showSaveDialog(view.getViewPrimaryStage()));
        else{
        	String message = "Das Projekt kann nicht exportiert werden, wenn es noch nicht gemeldet wurde.\n"
        			+ "Bitte melden Sie zuerst das Projekt.";
        	DialogWarning warning = new DialogWarning("Exportieren des Projekts", message);
        	Dialog.showDialog(warning);
        }
	}
	
	
	@FXML
	private void tbnRiskClick(){
		if(tbnRisk.isSelected())
			tbnRisk.setText("Risikozuschlag AUS");
		else
			tbnRisk.setText("Risikozuschlag EIN");
		
		if(pdData != null && !pdData.isEmpty())
			refreshPDResults();
	}
	
	
	@FXML
	private void tbnOverflowClick(){
		if(tbnOverflow.isSelected()){
			tbnOverflow.setText("Überschüsse AUS");
			vbxPDTables.getChildren().add(tblOverflow);
		}else{
			tbnOverflow.setText("Überschüsse EIN");
			vbxPDTables.getChildren().remove(tblOverflow);
		}
	}
	
	
	// this method finishes the project
	@FXML
	private void btnFinishClick(){
		if(core.isProjectFinished())
			return;
		
		String message = "Möchten Sie das Projekt wirklich abschließen?\n"
				+ "Es kann danach nicht mehr bearbeitet werden.";
		DialogConfirmation confirmation = new DialogConfirmation("Projekt abschließen", message);
		if(!Dialog.showDialog(confirmation))
			return;
		
		if(core.finishProject())
			tblPD.getColumns().add(colRealPD);
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
	            		setStyle("-fx-background-color: #b2b2b2");
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
		if(pdData != null){
			pdData.clear();
			RealTextField.clearTextFields();
		}
		if(overflowData != null)
			overflowData.clear();
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
		
		overflowData = core.getOverflowTable();
		tblOverflow.setItems(overflowData);
		
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
	
	
	/**
	 * Get the real times from the result screen after the project is finished
	 * @return the real times per skill
	 */
	public Hashtable<String, Integer> getRealTimes(){
		if(pdData != null){
			Hashtable<String, Integer> realTimes = new Hashtable<String, Integer>();
			
			for(PDTableModel model : pdData){
				int realPD;
				if(!((TextField)model.getPdReal()).getText().isEmpty()){
					realPD = Integer.parseInt(((TextField)model.getPdReal()).getText());
					realTimes.put(model.getSkillName(), realPD);
				}		
			}
			
			return realTimes;
		}
		return null;
	}
}
