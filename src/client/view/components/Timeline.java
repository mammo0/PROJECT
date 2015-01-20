package client.view.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * This class is the time line component for the results view
 * @author Ammon
 *
 */
public class Timeline extends ScrollPane {
	
	@FXML
	private StackPane stkTimeline;
	
	private Hashtable<Integer, ArrayList<ToggleButton>> quarterPanes;
	
	
	
	/**
	 * The Constructor
	 */
	public Timeline(int yearBeginn, int beginnQuarter, int yearEnd, int endQuarter) {
		// load the skill tab fxml
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/Timeline.fxml"));
		
		// apply this class as root and controller
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		// load the fxml
		try {
			fxmlLoader.load();
		} catch (IOException e) { 
		    e.printStackTrace();
		}
		
		quarterPanes = new Hashtable<Integer, ArrayList<ToggleButton>>();
		
		AnchorPane time = buildTimeline(yearBeginn, beginnQuarter, yearEnd, endQuarter);
		this.setMinHeight(110d);
		this.setMaxHeight(time.maxHeight(0d));
		stkTimeline.getChildren().add(time);
	}
	
	
	// this method generates the complete time line
	private AnchorPane buildTimeline(int yearBeginn, int beginnQuarter, int yearEnd, int endQuarter){
		AnchorPane timeline = new AnchorPane();
		
		HBox pane = new HBox();
		
		for(int i=yearBeginn;i<=yearEnd;i++){
			ArrayList<AnchorPane> quarters = new ArrayList<AnchorPane>();
			if(i==yearBeginn && i== yearEnd){
				for(int j=beginnQuarter;j<=endQuarter;j++){
					quarters.add(buildQuarter(j,i));
				}
			}else if(i==yearEnd){
				for(int j=1;j<=endQuarter;j++){
					quarters.add(buildQuarter(j,i));
				}
			}else if(i==yearBeginn){
				for(int j=beginnQuarter;j<=4;j++){
					quarters.add(buildQuarter(j,i));
				}
			}else{
				for(int j=1;j<=4;j++){
					quarters.add(buildQuarter(j,i));
				}
			}
			
			AnchorPane year = buildYear(i, quarters.toArray(new AnchorPane[quarters.size()]));
			if(i!=yearEnd)
				year.setStyle("-fx-border-color: transparent black transparent transparent;");
			pane.getChildren().add(year);
		}
		
		timeline.getChildren().add(pane);
		timeline.setMaxWidth(pane.minWidth(0d));
		AnchorPane.setTopAnchor(pane, 0d);
		AnchorPane.setBottomAnchor(pane, 0d);
		AnchorPane.setRightAnchor(pane, 0d);
		AnchorPane.setLeftAnchor(pane, 0d);
		
		return timeline;
	}
	
	
	// this method builds a year element of the time line
	private AnchorPane buildYear(int yearNumber, AnchorPane... quarters){
		AnchorPane year = new AnchorPane();
		year.setMinWidth(50d*quarters.length);
		year.setMaxWidth(50d*quarters.length);
		year.setStyle("-fx-border-color: transparent transparent transparent transparent;");
		
		VBox pane = new VBox();
		StackPane yearPane = new StackPane();
		Button btnYear = new Button(String.valueOf(yearNumber));
		btnYear.setFont(Font.font("", FontWeight.BOLD, 17.5));
		btnYear.setOnAction(this::btnYearClick);
		yearPane.getChildren().add(btnYear);
		pane.getChildren().add(yearPane);
		
		HBox quarterPane = new HBox();
		for(AnchorPane quarter : quarters){
			quarter.minWidthProperty().bind(year.widthProperty().divide(quarters.length));
			quarterPane.getChildren().add(quarter);
		}
		pane.getChildren().add(quarterPane);
		
		year.getChildren().add(pane);
		AnchorPane.setTopAnchor(pane, 0d);
		AnchorPane.setBottomAnchor(pane, 0d);
		AnchorPane.setRightAnchor(pane, 0d);
		AnchorPane.setLeftAnchor(pane, 0d);
		
		return year;
	}
	
	
	// this method builds a single quarter element of the time line
	private AnchorPane buildQuarter(int quarterNumber, int yearNumber){
		AnchorPane quarter = new AnchorPane();
		quarter.setMinWidth(50d);
		quarter.setMaxHeight(110d);
		
		VBox pane = new VBox();
		
		StackPane linePane = new StackPane();
		linePane.setMinHeight(30d);
		Line hLine = new Line();
		hLine.setStrokeWidth(2d);
		hLine.setStartX(0d);
		hLine.endXProperty().bind(linePane.widthProperty().subtract(2));
		Line vLine = new Line();
		vLine.setStartY(0d);
		vLine.endYProperty().bind(linePane.heightProperty().subtract(15));
		linePane.getChildren().addAll(hLine, vLine);
		
		pane.getChildren().add(linePane);
		
		StackPane buttonPane = new StackPane();
		ToggleButton btnQuarter = new ToggleButton("Q"+quarterNumber);
		btnQuarter.setOnAction(this::btnQuarterClick);
		if(quarterPanes.get(yearNumber) == null){
			ArrayList<ToggleButton> buttons = new ArrayList<ToggleButton>();
			quarterPanes.put(yearNumber, buttons);
		}
		ArrayList<ToggleButton> buttons = quarterPanes.get(yearNumber);
		buttons.add(btnQuarter);
		quarterPanes.put(yearNumber, buttons);
		buttonPane.getChildren().add(btnQuarter);
		pane.getChildren().add(buttonPane);
		
		quarter.getChildren().add(pane);
		AnchorPane.setTopAnchor(pane, 0d);
		AnchorPane.setBottomAnchor(pane, 0d);
		AnchorPane.setRightAnchor(pane, 0d);
		AnchorPane.setLeftAnchor(pane, 0d);
		
		return quarter;
	}
	
	
	
	// this method is called when a year toggle button is clicked
	private void btnYearClick(ActionEvent event){
		ArrayList<ToggleButton> buttons = quarterPanes.get(Integer.valueOf(((Button)event.getSource()).getText()));
		
		boolean allSelected = true;
		for(ToggleButton button : buttons){
			if(!button.isSelected())
				allSelected = false;
		}
		
		for(ToggleButton button : buttons){
			if(allSelected)
				button.setSelected(false);
			else
				button.setSelected(true);
		}
	}
	
	
	// this method is called when a quarter toggle button is clicked
	private void btnQuarterClick(ActionEvent event){
		
	}
}
