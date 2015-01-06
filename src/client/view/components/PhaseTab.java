package client.view.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;

public class PhaseTab extends ScrollPane {
	
	@FXML
	private Accordion accPhaseList;
	
	private ArrayList<PhasePaneWrapper> mainPhases;
	private Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> subPhases;
	
	private AddPhaseWrapper addMain;
	
	
	
	/**
	 * The Constructor
	 */
	public PhaseTab() {
		// load the project editor fxml
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/PhaseTab.fxml"));
		
		// apply this class as root and controller
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		// load the fxml
		try {
			fxmlLoader.load();
		} catch (IOException e) { 
		    e.printStackTrace();
		}
        
		mainPhases = new ArrayList<PhasePaneWrapper>();
		subPhases = new Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>>();
		
		
		// new main phase adder
		addMain = new AddPhaseWrapper(this, "Neue Hauptphase");
		accPhaseList.getPanes().add(addMain);
	}
	
	
	// this method checks if a main phase has sub phases
	// if so, the main phases gets uneditable
	private void checkMainPhases(){
		for(PhasePaneWrapper main : mainPhases){
			if(subPhases.containsKey(main))
				main.setCollapsible(false);
			else if(!main.isCollapsible())
				main.setCollapsible(true);
		}
	}
	
	
	
	
	/**
	 * This method adds a new main phase
	 * @param from the add pane wrapper calling this method
	 */
	public void addMainPhase(AddPhaseWrapper from){
		// create the new main phase
		PhasePaneWrapper main = new PhasePaneWrapper(this);
		
		// new sub phase adder below
		AddPhaseWrapper addSub = new AddPhaseWrapper(this, "Neue Subphase", main);
		// new main phase adder below the sub phase adder
		AddPhaseWrapper addMain = new AddPhaseWrapper(this, "Neue Hauptphase");
		
		int index = accPhaseList.getPanes().indexOf(from)+1;
		
		accPhaseList.getPanes().add(index, main);
		accPhaseList.getPanes().add(index+1, addSub);
		accPhaseList.getPanes().add(index+2, addMain);
		
		mainPhases.add(main);
	}

	
	/**
	 * This method adds a new sub phase
	 * @param from the add pane wrapper calling this method
	 * @param main the main phase belonging to this sub phase
	 */
	public void addSubPhase(AddPhaseWrapper from, PhasePaneWrapper main){
		// create the new sub phase
		PhasePaneWrapper sub = new PhasePaneWrapper(this, true);
		
		int index = accPhaseList.getPanes().indexOf(from);
		
		accPhaseList.getPanes().add(index, sub);
		
		ArrayList<PhasePaneWrapper> tempSubPhases = subPhases.get(mainPhases.get(mainPhases.indexOf(main)));
		if(tempSubPhases == null)
			tempSubPhases = new ArrayList<PhasePaneWrapper>();
		tempSubPhases.add(sub);
		
		subPhases.put(mainPhases.get(mainPhases.indexOf(main)), tempSubPhases);
		
		this.checkMainPhases();
	}
	
	
	/**
	 * This method removes the main phase
	 * @param phase the main phase to remove
	 */
	public void removeMainPhase(PhasePaneWrapper phase){
		int indexBeginn = accPhaseList.getPanes().indexOf(phase);
		
		int indexEnd;
		try{
			indexEnd = subPhases.get(phase).size() + indexBeginn;
		}catch (Exception e){
			indexEnd = indexBeginn;
		}
		
		accPhaseList.getPanes().remove(indexBeginn, indexEnd+3);
		
		subPhases.remove(phase);
		mainPhases.remove(phase);
	}
	
	
	/**
	 * This method removes the sub phase
	 * @param phase the sub phase to remove
	 */
	public void removeSubPhase(PhasePaneWrapper phase){
		int index = accPhaseList.getPanes().indexOf(phase);
		accPhaseList.getPanes().remove(index);
		
		outerLoop:
		for(ArrayList<PhasePaneWrapper> pane : subPhases.values()){
			for(int i=0;i<pane.size();i++){
				if(pane.get(i).equals(phase)){
					pane.remove(i);
					break outerLoop;
				}
			}
		}
		
		// clear the sub phases hash table if necessary
		Enumeration<PhasePaneWrapper> enumKey = subPhases.keys();
		while(enumKey.hasMoreElements()){
			PhasePaneWrapper key = enumKey.nextElement();
			if(subPhases.get(key).isEmpty())
				subPhases.remove(key);
		}
		
		this.checkMainPhases();
	}
	
	
	/**
	 * This method updates the skill combo boxes of the phases
	 */
	public void updatePhases(){
		for(PhasePaneWrapper main : mainPhases){
			// update the main phases
			main.updateSkillDropDown();
			
			try{
				for(PhasePaneWrapper sub : subPhases.get(main)){
					// update the sub phases
					sub.updateSkillDropDown();
				}
			}catch (Exception e){}
		}
	}
	
	
	
	
	private class AddPhaseWrapper extends TitledPane{
		
		private PhasePaneWrapper parentPhase;
		
		private Button btnAddPhase;
		private PhaseTab parent;
		
		/**
		 * The constructor
		 * @param parent the accordion parent
		 * @param title the title of the pane
		 */
		public AddPhaseWrapper(PhaseTab parent, String title){
			this(parent, title, null);
		}
		
		/**
		 * The constructor
		 * @param parent  the accordion parent
		 * @param title the title of the pane
		 * @param parentPhase the parent phase wrapper pane
		 */
		public AddPhaseWrapper(PhaseTab parent, String title, PhasePaneWrapper parentPhase){
			this.parent = parent;
			
			// set the title
			setText(title);
			
			this.parentPhase = parentPhase;
			// if it is a sub phase indent the titled pane
			if(parentPhase != null)
				setPadding(new Insets(0d, 0d, 0d, 50d));
			
			// prevent it from being opened
			setCollapsible(false);
			
			// build the add button
			btnAddPhase = new Button("+");
			btnAddPhase.setOnAction(this::btnAddPhaseClick);
			
			// add the add button to the title
			setGraphic(btnAddPhase);
		}
		
		
		// this method is called when the add phase button is clicked
		private void btnAddPhaseClick(ActionEvent event){
			if(parentPhase != null)
				parent.addSubPhase(this, parentPhase);
			else
				parent.addMainPhase(this);
		}
	}
}
