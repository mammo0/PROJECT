package client.view.components;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.sun.org.apache.bcel.internal.generic.IFNE;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import client.view.IComponents;
import client.view.dialogs.Dialog;
import client.view.dialogs.DialogConfirmation;

public class PhaseTab extends ScrollPane implements IComponents {
	
	@FXML
	private Accordion accPhaseList;
	
	private ArrayList<PhasePaneWrapper> mainPhases;
	private Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> subPhases;
	
	private AddPhaseWrapper addMain;
	
	private static boolean changeAbove;
	private static boolean changeBelow;
	
	private boolean ignoreDateDialog;
	
	
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
	
	
	// this method waits until all date checks are performed
	// then it sets the ignoreDateDialog boolean to false
	private void endIgnoreDateDialog(){
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				new Thread(){
					public void run(){
						while(PhasePaneWrapper.getRunnableStackSize() != 0)
							try {
								Thread.sleep(1);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						ignoreDateDialog = false;
					}
				}.start();
			}
		});
	}
	
	
	/**
	 * Check if the phases are overlapping. If so change the other phase dates.
	 * @param phase the phase to be checked
	 * @return true if values have changed
	 */
	public boolean checkPhaseDates(PhasePaneWrapper phase){
		return checkPhaseDates(phase, phase.getPhaseBegin(), phase.getPhaseEnd());
	}
	
	
	/**
	 * Check if the phases are overlapping. If so change the other phase dates.
	 * @param phase the phase to be checked
	 * @param phaseStart start date of the phase
	 * @param phaseEnd end date of the phase
	 * @return true if values have changed
	 */
	public boolean checkPhaseDates(PhasePaneWrapper phase, LocalDate phaseStart, LocalDate phaseEnd){
		boolean changed = false;
		
		// check phase itself
		if((phase.getPhaseEnd() == null && phaseStart != null) ||
				(phase.getPhaseBegin() != null && phase.getPhaseEnd() != null && phase.getPhaseBegin().isAfter(phase.getPhaseEnd()))){
			phase.setPhaseEnd(phase.getPhaseBegin(), true);
			changed = true;
		}
		if(phase.getPhaseBegin() == null && phaseEnd != null){
			phase.setPhaseBegin(phaseEnd, true);
			changed = true;
		}
		
		// check above
		LocalDate aboveEnd = getAboveEndDate(phase);
		if(phase.getPhaseBegin() == null && aboveEnd != null){
			phase.setPhaseBegin(aboveEnd.plusDays(1));
		}
		else if(aboveEnd != null && phaseStart != null){
			if(aboveEnd.isAfter(phaseStart) || aboveEnd.isEqual(phaseStart)){
				PhasePaneWrapper above = getPhaseAbove(phase);
				if(above.getPhaseBegin() != null && above.getPhaseEnd() != null){
					String message = "Das eigegebene Datum liegt vor oder auf dem Ende der vorherigen Phase: "
							+ above.getPhaseName() +"\n"
							+ "Möchten Sie diese nach vorne verlegen?";
					DialogConfirmation confirmation = new DialogConfirmation("Phasenverschiebung", message);
					if(!ignoreDateDialog && PhasePaneWrapper.getRunnableStackSize() == 1 &&
							!Dialog.showDialog(confirmation))
						return false;
					
					changeAbove = true;
					if(!changeBelow){
						int aboveDuration = Period.between(above.getPhaseBegin(), above.getPhaseEnd()).getDays();
						above.setPhaseBegin(phaseStart.minusDays(aboveDuration+1));
						above.setPhaseEnd(phaseStart.minusDays(1));
						changed = true;
					}
					changeAbove = false;
				}
			}else if(!phaseStart.minusDays(1).isEqual(aboveEnd)){
				PhasePaneWrapper above = getPhaseAbove(phase);
				if(above.getPhaseBegin() != null && above.getPhaseEnd() != null){
					String message = "Das eigegebene Startdatum liegt nach dem Ende der vorherigen Phase: "
							+ above.getPhaseName() +"\n"
							+ "Möchten Sie diese nach hinten verlegen?";
					DialogConfirmation confirmation = new DialogConfirmation("Phasenverschiebung", message);
					if(!ignoreDateDialog && PhasePaneWrapper.getRunnableStackSize() == 1 &&
							!Dialog.showDialog(confirmation))
						return false;
					
					changeAbove = true;
					if(!changeBelow){
						int aboveDuration = Period.between(above.getPhaseBegin(), above.getPhaseEnd()).getDays();
						above.setPhaseEnd(phaseStart.minusDays(1));
						above.setPhaseBegin(phaseStart.minusDays(aboveDuration+1));
						changed = true;
					}
					changeAbove = false;
				}
			}
		}
		
		// check below
		LocalDate belowStart = getBelowStartDate(phase);
		if(phase.getPhaseEnd() == null && belowStart != null)
			phase.setPhaseEnd(belowStart.minusDays(1));
		else if(belowStart != null && phaseEnd != null){
			if(belowStart.isBefore(phaseEnd) || belowStart.isEqual(phaseEnd)){
				PhasePaneWrapper below = getPhaseBelow(phase);
				if(below.getPhaseBegin() != null && below.getPhaseEnd() != null){
					String message = "Das eigegebene Datum liegt nach oder auf dem Anfang der nächsten Phase: "
							+ below.getPhaseName() +"\n"
							+ "Möchten Sie diese nach hinten verlegen?";
					DialogConfirmation confirmation = new DialogConfirmation("Phasenverschiebung", message);
					if(!ignoreDateDialog && PhasePaneWrapper.getRunnableStackSize() == 1 &&
							!Dialog.showDialog(confirmation))
						return false;
					
					changeBelow = true;
					if(!changeAbove){
						int belowDuration = Period.between(below.getPhaseBegin(), below.getPhaseEnd()).getDays();
						below.setPhaseEnd(phaseEnd.plusDays(belowDuration+1));
						below.setPhaseBegin(phaseEnd.plusDays(1));
						changed = true;
					}
					changeBelow = false;
				}
			}else if(!phaseEnd.plusDays(1).isEqual(belowStart)){
				PhasePaneWrapper below = getPhaseBelow(phase);
				if(below.getPhaseBegin() != null && below.getPhaseEnd() != null){
					String message = "Das eigegebene Enddatum liegt vor dem Anfang der nächsten Phase: "
							+ below.getPhaseName() +"\n"
							+ "Möchten Sie diese nach vorne verlegen?";
					DialogConfirmation confirmation = new DialogConfirmation("Phasenverschiebung", message);
					if(!ignoreDateDialog && PhasePaneWrapper.getRunnableStackSize() == 1 &&
							!Dialog.showDialog(confirmation))
						return false;
					
					changeBelow = true;
					if(!changeAbove){
						int belowDuration = Period.between(below.getPhaseBegin(), below.getPhaseEnd()).getDays();
						below.setPhaseBegin(phaseEnd.plusDays(1));
						below.setPhaseEnd(phaseEnd.plusDays(belowDuration+1));
						changed = true;
					}
					changeBelow = false;
				}
			}
		}
		
		if(!changed && ((phaseStart == null && phaseEnd != null) || (phaseStart != null && phaseEnd == null))){
			changed = true;
		}
		
		return changed;
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
		}else if(phase.isSubPhase() && !below.isSubPhase() && subPhases.get(below) != null && !subPhases.get(below).isEmpty()){
			date = getBelowStartDate(below);
		}else{
			date = below.getPhaseBegin();
		}
		
		return date;
	}
	
	
	
	// add a main phase by its index
	private PhasePaneWrapper addMainPhase(){
		int index = accPhaseList.getPanes().size()-1;
		AddPhaseWrapper add = (AddPhaseWrapper) accPhaseList.getPanes().get(index);
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
	private PhasePaneWrapper addSubPhase(PhasePaneWrapper main){
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
		String message = "Möchten Sie die Phase: " + phase.getPhaseName() +"\n"
				+"wirklich löschen? Alle nachfolgenden Phasen werden nach vorne verschoben.";
		DialogConfirmation conformation = new DialogConfirmation("Phase löschen?", message);
		if(!Dialog.showDialog(conformation))
			return;
		
		int indexBeginn = accPhaseList.getPanes().indexOf(phase);
		
		PhasePaneWrapper surroundPhase = getPhaseAbove(phase);
		if(surroundPhase == null)
			surroundPhase = getPhaseBelow(phase);
		
		int indexEnd;
		try{
			indexEnd = subPhases.get(phase).size() + indexBeginn;
		}catch (Exception e){
			indexEnd = indexBeginn;
		}
		
		accPhaseList.getPanes().remove(indexBeginn, indexEnd+3);
		
		subPhases.remove(phase);
		mainPhases.remove(phase);
		
		if(surroundPhase != null){
			ignoreDateDialog = true;
			checkPhaseDates(surroundPhase);
			endIgnoreDateDialog();
		}	
	}
	
	
	/**
	 * This method removes the sub phase
	 * @param phase the sub phase to remove
	 */
	public void removeSubPhase(PhasePaneWrapper phase){
		String message = "Möchten Sie die Phase: " + phase.getPhaseName() +"\n"
				+"wirklich löschen? Alle nachfolgenden Phasen werden nach vorne verschoben.";
		DialogConfirmation conformation = new DialogConfirmation("Phase löschen?", message);
		if(!Dialog.showDialog(conformation))
			return;
		
		PhasePaneWrapper surroundPhase = getPhaseAbove(phase);
		if(surroundPhase == null)
			surroundPhase = getPhaseBelow(phase);
		
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
		
		if(surroundPhase != null){
			ignoreDateDialog = true;
			checkPhaseDates(surroundPhase);
			endIgnoreDateDialog();
		}
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
		for(int i=0;i<accPhaseList.getPanes().size();i++){
			TitledPane phase = accPhaseList.getPanes().get(i);
			accPhaseList.getPanes().remove(phase);
			i--;
		}
		accPhaseList.getPanes().add(addMain);
		
		mainPhases.clear();
		subPhases.clear();
	}
	
	@Override
	public void disableWrite(boolean disable){
		for(int i=0;i<accPhaseList.getPanes().size();i++){
			TitledPane phase = accPhaseList.getPanes().get(i);
			if(phase instanceof PhasePaneWrapper){
				((PhasePaneWrapper)phase).disableWrite(disable);
			}else if(phase instanceof AddPhaseWrapper && disable){
				accPhaseList.getPanes().remove(phase);
				i--;
			}
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
		
		return panes;
	}
	
	
	@Override
	public PhasePaneWrapper addPhasePaneWrapper(String phaseName, String parentName){
		if(parentName != null){
			for(PhasePaneWrapper mainPane : mainPhases){
				if(mainPane.getPhaseName().equals(parentName)){
					return addSubPhase(mainPane);
				}
			}
		}else{
				return addMainPhase();
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
	
	@Override
	public void setProjectTimeStamp(LocalDateTime timeStamp){}
}
