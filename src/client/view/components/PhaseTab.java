package client.view.components;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
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
import client.view.IComponents;

public class PhaseTab extends ScrollPane implements IComponents {
	
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
			if(subPhases.containsKey(main)){
				main.setExpanded(false);
				main.setCollapsible(false);
			}
			else if(!main.isCollapsible())
				main.setCollapsible(true);
		}
	}
	
	
	/**
	 * Check if the phases are overlapping. If so change the other phase dates.
	 * @param phase the phase to be checked
	 */
	public void checkPhaseDates(PhasePaneWrapper phase){
		checkPhaseDates(phase, phase.getPhaseBegin(), phase.getPhaseEnd());
	}
	
	
	/**
	 * Check if the phases are overlapping. If so change the other phase dates.
	 * @param phase the phase to be checked
	 * @param phaseStart start date of the phase
	 * @param phaseEnd end date of the phase
	 */
	public void checkPhaseDates(PhasePaneWrapper phase, LocalDate phaseStart, LocalDate phaseEnd){
		// check above
		LocalDate aboveEnd = getAboveEndDate(phase);
		if(phaseStart == null && aboveEnd != null){
			phase.setPhaseBegin(aboveEnd.plusDays(1));
		}
		else if(aboveEnd != null && phaseStart != null){
			if(aboveEnd.isAfter(phaseStart) || aboveEnd.isEqual(phaseStart)){
				PhasePaneWrapper above = getPhaseAbove(phase);
				if(above.getPhaseBegin() != null && above.getPhaseEnd() != null){
					// TODO dialog confirmation
					
					int aboveDuration = Period.between(above.getPhaseBegin(), above.getPhaseEnd()).getDays();
					above.setPhaseBegin(phaseStart.minusDays(aboveDuration+1));
					above.setPhaseEnd(phaseStart.minusDays(1));
				}
			}else if(!phaseStart.minusDays(1).isEqual(aboveEnd)){
				PhasePaneWrapper above = getPhaseAbove(phase);
				if(above.getPhaseBegin() != null && above.getPhaseEnd() != null){
					// TODO dialog confirmation
					
					int aboveDuration = Period.between(above.getPhaseBegin(), above.getPhaseEnd()).getDays();
					above.setPhaseEnd(phaseStart.minusDays(1));
					above.setPhaseBegin(above.getPhaseEnd().minusDays(aboveDuration));
				}
			}
		}
		
		// check below
		LocalDate belowStart = getBelowStartDate(phase);
		if(phaseEnd == null && belowStart != null)
			phase.setPhaseEnd(belowStart.minusDays(1));
		else if(belowStart != null && phaseEnd != null){
			if(belowStart.isBefore(phaseEnd) || belowStart.isEqual(phaseEnd)){
				PhasePaneWrapper below = getPhaseBelow(phase);
				if(below.getPhaseBegin() != null && below.getPhaseEnd() != null){
					// TODO dialog confirmation
					
					int belowDuration = Period.between(below.getPhaseBegin(), below.getPhaseEnd()).getDays();
					below.setPhaseEnd(phaseEnd.plusDays(belowDuration+1));
					below.setPhaseBegin(phaseEnd.plusDays(1));
				}
			}else if(!phaseEnd.plusDays(1).isEqual(belowStart)){
				PhasePaneWrapper below = getPhaseBelow(phase);
				if(below.getPhaseBegin() != null && below.getPhaseEnd() != null){
					// TODO dialog confirmation
					
					int belowDuration = Period.between(below.getPhaseBegin(), below.getPhaseEnd()).getDays();
					below.setPhaseBegin(phaseEnd.plusDays(1));
					below.setPhaseEnd(phaseEnd.plusDays(belowDuration+1));
				}
			}
				
		}
	}
	
	
	
	// this method gets the phase below another
	private PhasePaneWrapper getPhaseBelow(PhasePaneWrapper phase){
		int index = accPhaseList.getPanes().indexOf(phase)+1;
		while(index < accPhaseList.getPanes().size() && !(accPhaseList.getPanes().get(index) instanceof PhasePaneWrapper)){
			index++;
		}
		if(index >= accPhaseList.getPanes().size())
			return null;
		
		return (PhasePaneWrapper) accPhaseList.getPanes().get(index);
	}
	
	// this method gets the phase above another
	private PhasePaneWrapper getPhaseAbove(PhasePaneWrapper phase){
		int index = accPhaseList.getPanes().indexOf(phase)-1;
		while(index >=0 && !(accPhaseList.getPanes().get(index) instanceof PhasePaneWrapper)){
			index--;
		}
		if(index < 0)
			return null;
		
		return (PhasePaneWrapper) accPhaseList.getPanes().get(index);
	}
	
	// this method returns the end date of the phase above
	private LocalDate getAboveEndDate(PhasePaneWrapper phase){
		LocalDate date = null;
		PhasePaneWrapper above = getPhaseAbove(phase);
		if(above == null)
			return null;
		
		if(phase.isSubPhase() && !above.isSubPhase()){
			date = getAboveEndDate(above);
		}else{
			date = above.getPhaseEnd();
		}
		
		return date;
	}
	
	// this method returns the start date of the phase below
	private LocalDate getBelowStartDate(PhasePaneWrapper phase){
		LocalDate date = null;
		PhasePaneWrapper below = getPhaseBelow(phase);
		if(below == null)
			return null;
		
		if(!phase.isSubPhase() && below.isSubPhase()){
			date = getBelowStartDate(below);
		}else if(phase.isSubPhase() && !below.isSubPhase() && !subPhases.get(below).isEmpty()){
			date = getBelowStartDate(below);
		}else{
			date = below.getPhaseBegin();
		}
		
		return date;
	}
	
	
	
	// add a main phase by its index
	private PhasePaneWrapper addMainPhase(int index){
		AddPhaseWrapper add = (AddPhaseWrapper) accPhaseList.getPanes().get(accPhaseList.getPanes().size()-1);
		return addMainPhase(add);
	}
	
	/**
	 * This method adds a new main phase
	 * @param from the add pane wrapper calling this method
	 */
	public PhasePaneWrapper addMainPhase(AddPhaseWrapper from){
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
		
		checkPhaseDates(main);;
		
		mainPhases.add(main);
		
		return main;
	}

	
	// add a sub phase by its index
	private PhasePaneWrapper addSubPhase(int index, PhasePaneWrapper main){
		AddPhaseWrapper add = (AddPhaseWrapper) accPhaseList.getPanes().get(accPhaseList.getPanes().size()-2);
		return addSubPhase(add, main);
	}
	
	/**
	 * This method adds a new sub phase
	 * @param from the add pane wrapper calling this method
	 * @param main the main phase belonging to this sub phase
	 */
	public PhasePaneWrapper addSubPhase(AddPhaseWrapper from, PhasePaneWrapper main){
		// create the new sub phase
		PhasePaneWrapper sub = new PhasePaneWrapper(this, true);
		
		int index = accPhaseList.getPanes().indexOf(from);
		
		accPhaseList.getPanes().add(index, sub);
		
		checkPhaseDates(sub);
		
		ArrayList<PhasePaneWrapper> tempSubPhases = subPhases.get(mainPhases.get(mainPhases.indexOf(main)));
		if(tempSubPhases == null)
			tempSubPhases = new ArrayList<PhasePaneWrapper>();
		tempSubPhases.add(sub);
		
		subPhases.put(mainPhases.get(mainPhases.indexOf(main)), tempSubPhases);
		
		this.checkMainPhases();
		
		return sub;
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
	
	
	
	/**
	 * This class is a place holder to add new phases
	 * @author ammon
	 *
	 */
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



	@Override
	public void clearAll(){
		for(int i=0;i<mainPhases.size();i++){
			PhasePaneWrapper mainPhase = mainPhases.get(i);
			removeMainPhase(mainPhase);
			ArrayList<PhasePaneWrapper> subPanes;
			if((subPanes = subPhases.get(mainPhase)) != null){
				for(int j=0;j< subPanes.size();j++){
					removeSubPhase(subPanes.get(j));
					j--;
				}
			}
			i--;
		}
	}
	
	
	
	@Override
	public ArrayList<SkillPane> getSkillPanes() {
		return  null;
	}
	
	@Override
	public SkillPane addSkillPane(){
		return null;
	}
	
	@Override
	public Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> getPhasePanes() {
		Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> panes = new Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>>();
		for(PhasePaneWrapper pane : mainPhases){
			if(!subPhases.containsKey(pane))
				panes.put(pane, new ArrayList<PhasePaneWrapper>());
		}
		
		Enumeration<PhasePaneWrapper> enumKey = subPhases.keys();
		while(enumKey.hasMoreElements()){
			PhasePaneWrapper key = enumKey.nextElement();
			ArrayList<PhasePaneWrapper> subPanes = new ArrayList<PhasePaneWrapper>();
			subPanes.addAll(subPhases.get(key));
			
			panes.put(key, subPanes);
		}
		
		if(panes.isEmpty()){
			addMainPhase(addMain);
			return getPhasePanes();
		}else
			return panes;
	}
	
	
	@Override
	public PhasePaneWrapper addPhasePaneWrapper(String phaseName, int index, String parentName){
		if(parentName != null){
			for(PhasePaneWrapper mainPane : mainPhases){
				if(mainPane.getPhaseName().equals(parentName)){
					return addSubPhase(index, mainPane);
				}
			}
		}else{
				return addMainPhase(index);
		}
		return null;
	}
	
	@Override
	public PhasePane addPhasePane(PhasePaneWrapper wrapper){
		for(PhasePaneWrapper mainPane : mainPhases){
			if(mainPane.equals(wrapper)){
				return mainPane.addPhasePane();
			}
			ArrayList<PhasePaneWrapper> subPanes = subPhases.get(mainPane);
			if(subPanes != null){
				for(PhasePaneWrapper subPane : subPanes){
					if(subPane.equals(wrapper)){
						return subPane.addPhasePane();
					}
				}
			}
		}
		
		return null;
	}

	@Override
	public ArrayList<ResourcePaneWrapper> getResourcePanes() {
		return null;
	}
	
	@Override
	public ResourcePane addResourcePane(int parentSkillID){
		return null;
	}
	


	@Override
	public String getProjectName() {
		return null;
	}
	
	@Override
	public void setProjectName(String projectName){}

	@Override
	public String getProjectResponsible() {
		return null;
	}
	
	@Override
	public void setProjectResponsible(String projectResponsible){}

	@Override
	public String getProjectDescription() {
		return null;
	}
	
	@Override
	public void setProjectDescription(String projectDescription){}
}
