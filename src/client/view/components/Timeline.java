package client.view.components;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

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
	
	private ArrayList<ButtonQuarter> quarterButtons;
	
	private ResultTab parent;
	
	
	
	/**
	 * The Constructor
	 */
	public Timeline(ResultTab parent) {
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
		
		this.parent = parent;
		
		quarterButtons = new ArrayList<ButtonQuarter>();
	}
	
	
	// this method generates the complete time line
	private AnchorPane buildTimeline(int yearBegin, int beginQuarter, int yearEnd, int endQuarter){
		AnchorPane timeline = new AnchorPane();
		
		HBox pane = new HBox();
		
		for(int i=yearBegin;i<=yearEnd;i++){
			ArrayList<AnchorPane> quarters = new ArrayList<AnchorPane>();
			if(i==yearBegin && i== yearEnd){
				for(int j=beginQuarter;j<=endQuarter;j++){
					quarters.add(buildQuarter(j,i));
				}
			}else if(i==yearEnd){
				for(int j=1;j<=endQuarter;j++){
					quarters.add(buildQuarter(j,i));
				}
			}else if(i==yearBegin){
				for(int j=beginQuarter;j<=4;j++){
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
		btnYear.setMinHeight(32d);
		if(quarters.length == 1)
			btnYear.setFont(Font.font("Times New Roman", FontWeight.BOLD, 13));
		else
			btnYear.setFont(Font.font("Times New Roman", FontWeight.BOLD, 17.5));
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
		ButtonQuarter btnQuarter = new ButtonQuarter(quarterNumber, yearNumber);
		btnQuarter.setOnAction(this::btnQuarterClick);
		quarterButtons.add(btnQuarter);
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
		boolean allSelected = true;
		for(ButtonQuarter button : quarterButtons){
			if(button.getYear() == Integer.valueOf(((Button)event.getSource()).getText()))
				if(!button.isSelected())
					allSelected = false;
		}
		
		boolean firstRun = true;
		for(ButtonQuarter button : quarterButtons){
			if(button.getYear() == Integer.valueOf(((Button)event.getSource()).getText())){
				if(allSelected){
					button.setSelected(false);
					if(firstRun == true){
						firstRun = false;
						btnQuarterClick(new ActionEvent(button, null));
					}
				}else{
					if(firstRun == true){
						firstRun = false;
						btnQuarterClick(new ActionEvent(button, null));
					}
					button.setSelected(true);
				}
				
			}
		}
		
		parent.refreshQuarterResults();
	}
	
	
	// get the quarter buttons between two other quarter buttons
	private ArrayList<ButtonQuarter> getButtonsBetween(ButtonQuarter start, ButtonQuarter end){
		ArrayList<ButtonQuarter> buttons = new ArrayList<ButtonQuarter>();
		
		for(ButtonQuarter button : quarterButtons){
			if((start.getQuarterYear().isBefore(button.getQuarterYear()) && button.getQuarterYear().isBefore(end.getQuarterYear()) ||
					(start.getQuarterYear().isAfter(button.getQuarterYear()) && button.getQuarterYear().isAfter(end.getQuarterYear())))){
				buttons.add(button);
			}
		}
		
		return buttons;
	}
	
	// get a quarter button by its quarter and year number
	private ButtonQuarter getQuarterButton(int quarter, int year){
		for(ButtonQuarter button : quarterButtons){
			if(button.getYear() == year && button.getQuarter() == quarter)
				return button;
		}
		return null;
	}
	
	// get the group size after a selected button
	private int getGroupSize(ButtonQuarter startButton){
		ButtonQuarter after = null;
		if(startButton.getQuarter() == 4)
			after = getQuarterButton(1, startButton.getYear()+1);
		else
			after = getQuarterButton(startButton.getQuarter()+1, startButton.getYear());
		
		if(after != null && after.isSelected())
			return 1+ getGroupSize(after);
		else
			return 1;
	}
	
	
	// this method is called when a quarter toggle button is clicked
	private void btnQuarterClick(ActionEvent event){
		ButtonQuarter btnQuarter = (ButtonQuarter) event.getSource();
		
		// select buttons between
		ArrayList<ButtonQuarter> buttons = null;
		for(ButtonQuarter button : quarterButtons){
			if((button.isSelected() && buttons == null) || (button.isSelected() && !button.equals(btnQuarter) && buttons != null && !buttons.contains(button))){
				buttons = getButtonsBetween(button, btnQuarter);
			}
		}
		if(buttons != null)
			for(ButtonQuarter button : buttons){
				button.setSelected(true);
			}
		
		// keep the greatest group of at least a pair
		int maxGroupSize = 0;
		ButtonQuarter startButton = null;
		ButtonQuarter endButton = null;
		for(int i=0;i<quarterButtons.size();i++){
			ButtonQuarter earliest = quarterButtons.get(i);
			if(earliest.isSelected()){
				int groupSize = getGroupSize(earliest);
				
				if(groupSize > maxGroupSize){
					startButton = earliest;
					endButton = quarterButtons.get(i+groupSize-1);
					maxGroupSize = groupSize;
					
					i = i+groupSize-1;
				}
			}
		}
		
		if(startButton != null && endButton != null){
			ArrayList<ButtonQuarter> group = getButtonsBetween(startButton, endButton);
			group.add(startButton);
			group.add(endButton);
			for(ButtonQuarter button : quarterButtons){
				if(!group.contains(button))
					button.setSelected(false);
			}
		}
		
		if(event.getTarget() != null)
			parent.refreshQuarterResults();
	}
	
	
	
	/**
	 * Create a new time line
	 * @param startDate
	 * @param endDate
	 */
	public void createTimeline(LocalDate startDate, LocalDate endDate){
		int beginnQuarter = ((startDate.getMonthValue() -1) /3) +1;
		int endQuarter = ((endDate.getMonthValue() -1) /3) +1;
		
		createTimeline(startDate.getYear(), beginnQuarter, endDate.getYear(), endQuarter);
	}
	
	/**
	 * Create a new time line
	 * @param yearBeginn
	 * @param beginnQuarter
	 * @param yearEnd
	 * @param endQuarter
	 */
	public void createTimeline(int yearBeginn, int beginnQuarter, int yearEnd, int endQuarter){
		quarterButtons.clear();
		stkTimeline.getChildren().clear();
		
		AnchorPane time = buildTimeline(yearBeginn, beginnQuarter, yearEnd, endQuarter);
		this.setMinHeight(110d);
		this.setMaxHeight(time.maxHeight(0d));
		stkTimeline.getChildren().add(time);	
	}
	
	
	
	/**
	 * Get the selected start year
	 * @return the selected year
	 */
	public int getSelectedYearBegin(){
		for(ButtonQuarter button : quarterButtons){
			if(button.isSelected())
				return button.getYear();
		}
		return -1;
	}
	
	/**
	 * Get the selected start quarter
	 * @return the selected quarter
	 */
	public int getSelectedQuarterBegin(){
		for(ButtonQuarter button : quarterButtons){
			if(button.isSelected())
				return button.getQuarter();
		}
		return -1;
	}
	
	/**
	 * Get the selected end year
	 * @return the selected year
	 */
	public int getSelectedYearEnd(){
		for(int i=quarterButtons.size()-1;i>=0;i--){
			if(quarterButtons.get(i).isSelected())
				return quarterButtons.get(i).getYear();
		}
		return -1;
	}
	
	/**
	 * Get the selected end quarter
	 * @return the selected quarter
	 */
	public int getSelectedQuarterEnd(){
		for(int i=quarterButtons.size()-1;i>=0;i--){
			if(quarterButtons.get(i).isSelected())
				return quarterButtons.get(i).getQuarter();
		}
		return -1;
	}
	
	
	
	// own class for the quarter toggle buttons
	private class ButtonQuarter extends ToggleButton{
		
		private int year;
		private int quarter;
		
		public ButtonQuarter(int quarter, int year){
			super("Q"+quarter);
			this.year = year;
			this.quarter = quarter;
		}
		
		
		/**
		 * Get the year number
		 * @return the year
		 */
		public int getYear(){
			return year;
		}
		
		/**
		 * Get the quarter number
		 * @return the quarter
		 */
		public int getQuarter(){
			return quarter;
		}
		
		public LocalDate getQuarterYear(){
			return LocalDate.of(year, quarter*3, 1);
		}
	}
}
