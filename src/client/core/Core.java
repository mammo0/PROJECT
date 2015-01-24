package client.core;

import global.ASingelton;
import global.IServerService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.project.Phase;
import model.project.Project;
import model.project.Quarter;
import model.project.Resource;
import model.project.Result;
import model.project.Skill;
import model.project.Year;
import client.view.IViewClient;
import client.view.View;
import client.view.components.PhasePane;
import client.view.components.PhasePaneWrapper;
import client.view.components.ResourcePane;
import client.view.components.ResourcePaneWrapper;
import client.view.components.SkillPane;

/**
 * This class is the core for the client application
 * @author Ammon
 *
 */
public class Core extends ASingelton implements ICoreClient {
	
	// the server object
	private IServerService server;
	
	private IViewClient view;
	
	private Project project;
	
	private String serverAddress;
	private int serverPort;
	
	private File configFile;
	
	
	/**
	 * The constructor
	 */
	public Core(){
		configFile = new File("client.conf");
		
		loadSettings();
	}
	
	
	// this method builds the rmi url
	private String buildRmiurl(String serverAddress){
		return "rmi://"+serverAddress+":"+serverPort+"/PROJECT";
	}
	
	// this method opens a new connection to the server
	private void openNewConnection(){
		// close the old connection
		server = null;
		
		// open a new one
		try {
			server = (IServerService) Naming.lookup(buildRmiurl(serverAddress));
			if(view != null)
				view.displayProjects(server.getAllProjectNames());
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			if(view != null)
				view.setStatus("Keine Verbindung zum Server möglich.", 0);
		}
	}
	
	// this method loads the connection settings from a configuration file
	private void loadSettings(){
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(configFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if(line.startsWith("PORT="))
					this.serverPort = Integer.valueOf(line.split("=")[1]);
				else if(line.startsWith("ADDR="))
					this.serverAddress = line.split("=")[1];
				else{
					reader.close();
					throw new IOException();
				}
			}
			reader.close();
			
			openNewConnection();
		} catch (IOException e) {
			if(view != null)
				view.setStatus("Bitte die Servereinstellungen überprüfen.", 0);
		}
	}
	
	
	// build the project object
	// returns true if it was successful
	private boolean buildProject(){
		return buildProject(false, false);
	}
	private boolean buildProject(boolean ignoreErrors, boolean dontMark){
		if(!dontMark)
			project = new Project();
		
		// first screen
		if(view.getProjectName().replaceAll(" ", "").isEmpty()){
			if(!dontMark){
				view.markNode(null, "txtProjectName");
				view.setStatus("Bitte einen Projektnamen angeben.", 10);
			}
			return false;
		}
		if(!dontMark)
			project.setProjectName(view.getProjectName());
		if(!ignoreErrors && view.getProjectResponsible().replaceAll(" ", "").isEmpty()){
			if(!dontMark){
				view.markNode(null, "txtProjectResponsible");
				view.setStatus("Bitte einen Projektverantwortlichen angeben.", 10);
			}
			return false;
		}
		if(!dontMark){
			project.setProjectResponsible(view.getProjectResponsible());
			project.setDescription(view.getProjectDescription());
		}
		
		// second screen
		for(SkillPane pane : view.getSkillPanes()){
			if(pane.isEmpty())
				continue;
			Skill skill = new Skill();
			if(!ignoreErrors && pane.getSkillName().replaceAll(" ", "").isEmpty()){
				if(!dontMark){
					view.markNode(pane, "txtSkillName");
					view.setStatus("Bitte einen Kompetenznamen angeben.", 10);
				}
				return false;
			}
			skill.setSkillName(pane.getSkillName());
			skill.setDayRateInt(pane.getDayRateInt());
			skill.setDayRateExt(pane.getDayRateExt());
			
			if(!dontMark)
				project.addSkill(skill);
		}
		
		// third screen
		for(ResourcePaneWrapper paneWrapper : view.getResourcePanes()){
			for(ResourcePane pane : paneWrapper.getResourcePanes()){
				Resource resource = new Resource();
				if(!ignoreErrors && pane.getResourceName().replaceAll(" ", "").isEmpty()){
					if(!dontMark){
						view.markNode(pane, "txtResourceName");
						view.setStatus("Bitte einen Resourcennamen angeben.", 10);
					}
					return false;
				}
				resource.setResourceName(pane.getResourceName());
				resource.setSkill(paneWrapper.getSkill().getSkillID());
				if(!ignoreErrors && pane.getAvailability() == -1){
					if(!dontMark){
						view.markNode(pane, "txtAvailability");
						view.setStatus("Bitte eine Verfügbarkeit angeben.", 10);
					}
					return false;
				}
				resource.setAvailability(pane.getAvailability());
				if(!ignoreErrors && pane.getSkillAmount() == -1){
					if(!dontMark){
						view.markNode(pane, "txtSkillAmount");
						view.setStatus("Bitte eine Anzahl angeben.", 10);
					}
					return false;
				}
				resource.setSkillAmount(pane.getSkillAmount());
				resource.setIntern(pane.isIntern());
				
				if(!dontMark)
					project.addResource(resource);
			}
		}
		
		// fourth screen
		Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> phases = view.getPhasePanes();
		if(phases.isEmpty() && !ignoreErrors){
			view.addPhasePaneWrapper("", null);
		}
		Enumeration<PhasePaneWrapper> enumKey = phases.keys();
		while(enumKey.hasMoreElements()){
			PhasePaneWrapper main = enumKey.nextElement();
			
			Phase mainPhase = new Phase();
			if(!ignoreErrors && main.getPhaseName().replaceAll(" ", "").isEmpty()){
				if(!dontMark){
					view.markNode(main, "txtPhaseName");
					view.setStatus("Bitte einen Phasennamen angeben.", 10);
				}
				return false;
			}
			mainPhase.setPhaseName(main.getPhaseName());
			if(phases.get(main).isEmpty()){
				if(!ignoreErrors && main.getPhaseBegin() == null){
					if(!dontMark){
						view.markNode(main, "datPhaseBegin");
						view.setStatus("Bitte einen Phasenbeginn angeben.", 10);
					}
					return false;
				}
				mainPhase.setStartDate(main.getPhaseBegin());
				if(!ignoreErrors && main.getPhaseEnd() == null){
					if(!dontMark){
						view.markNode(main, "datPhaseEnd");
						view.setStatus("Bitte ein Phasenende angeben.", 10);
					}
					return false;
				}
				mainPhase.setEndDate(main.getPhaseEnd());
				mainPhase.setRiskFactor(main.getRiskFactor());
				
				for(PhasePane phasePane : main.getPhasePanes()){
					if(!ignoreErrors && phasePane.getPhaseSkillId() == -1){
						if(!dontMark){
							view.markNode(phasePane, "cmbSkills");
							view.setStatus("Bitte einen Skill auswählen.", 10);
						}
						return false;
					}
					if(!ignoreErrors && phasePane.getPhaseDuration() == -1){
						if(!dontMark){
							view.markNode(phasePane, "txtDuration");
							view.setStatus("Bitte eine Dauer angeben.", 10);
						}
						return false;
					}
					mainPhase.addSkill(phasePane.getPhaseSkillId(), phasePane.getPhaseDuration());
				}
				
				if(!dontMark)
					project.addPhase(mainPhase);
			}else{
				for(PhasePaneWrapper sub : phases.get(main)){
					Phase subPhase = new Phase();
					if(!ignoreErrors && sub.getPhaseName().replaceAll(" ", "").isEmpty()){
						if(!dontMark){
							view.markNode(sub, "txtPhaseName");
							view.setStatus("Bitte einen Phasennamen angeben.", 10);
						}
						return false;
					}
					subPhase.setPhaseName(sub.getPhaseName());
					subPhase.setParent(mainPhase);
					if(!ignoreErrors && sub.getPhaseBegin() == null){
						if(!dontMark){
							view.markNode(sub, "datPhaseBegin");
							view.setStatus("Bitte einen Phasenbeginn angeben.", 10);
						}
						return false;
					}
					subPhase.setStartDate(sub.getPhaseBegin());
					if(!ignoreErrors && sub.getPhaseEnd() == null){
						if(!dontMark){
							view.markNode(sub, "datPhaseEnd");
							view.setStatus("Bitte ein Phasenende angeben.", 10);
						}
						return false;
					}
					subPhase.setEndDate(sub.getPhaseEnd());
					subPhase.setRiskFactor(sub.getRiskFactor());
					
					for(PhasePane phasePane : sub.getPhasePanes()){
						if(!ignoreErrors && phasePane.getPhaseSkillId() == -1){
							if(!dontMark){
								view.markNode(phasePane, "cmbSkills");
								view.setStatus("Bitte einen Skill auswählen.", 10);
							}
							return false;
						}
						if(!ignoreErrors && phasePane.getPhaseDuration() == -1){
							if(!dontMark){
								view.markNode(phasePane, "txtDuration");
								view.setStatus("Bitte eine Dauer angeben.", 10);
							}
							return false;
						}
						subPhase.addSkill(phasePane.getPhaseSkillId(), phasePane.getPhaseDuration());
					}
					
					if(!dontMark)
						project.addPhase(subPhase);
				}
			}
		}
		
		return true;
	}
	
	
	
	// this method shows the project object on the view
	private void showProject(){
		// first screen
		view.setProjectName(project.getProjectName());
		view.setProjectResponsible(project.getProjectResponsible());
		view.setProjectDescription(project.getDescription());
		
		view.setProjectTimeStamp(project.getTimestamp());
		
		// second screen
		for(Skill skill : project.getSkills()){
			SkillPane pane = view.addSkillPane();
			pane.setSkillName(skill.getSkillName());
			pane.setDayRateInt(skill.getDayRateInt());
			pane.setDayRateExt(skill.getDayRateExt());
		}
		
		// third screen
		for(Skill skill : project.getSkills()){
			for(Resource resource : project.getResources()){
				if(resource.getSkillID() == skill.getSkillID()){
					ResourcePane pane = view.addResourcePane(skill.getSkillID());
					pane.setResourceName(resource.getResourceName());
					pane.setIntern(resource.isIntern());
					pane.setAvailability(resource.getAvailability());
					pane.setSkillAmount(resource.getSkillAmount());
				}
			}
		}
		
		// forth screen
		// sort the phases
		ArrayList<Phase> phases = sortPhases(project.getPhases());
		boolean firstSub = true;
		for(int i=0;i<phases.size();i++){
			Phase phase = phases.get(i);
			PhasePaneWrapper wrapper;
			if(phase.getParent() == null){
				wrapper = view.addPhasePaneWrapper(phase.getPhaseName(), null);
				firstSub = true;
			}else{
				if(firstSub){
					wrapper = view.addPhasePaneWrapper(phase.getParent().getPhaseName(), null);
					wrapper.setPhaseName(phase.getParent().getPhaseName());
					phases.add(i, phase.getParent());
					firstSub = false;
					continue;
				}
				
				wrapper = view.addPhasePaneWrapper(phase.getPhaseName(), phase.getParent().getPhaseName());
			}
			wrapper.setPhaseName(phase.getPhaseName());
			wrapper.setPhaseBegin(phase.getStartDate());
			wrapper.setPhaseEnd(phase.getEndDate());
			wrapper.setRiskFactor(phase.getRiskFactor());
			Enumeration<Integer> enumKey = phase.getSkills().keys();
			while(enumKey.hasMoreElements()){
				Integer skillId = enumKey.nextElement();
				Integer skillDuration = phase.getSkills().get(skillId);
				
				PhasePane pane = view.addPhasePane(wrapper);
				pane.setPhaseSkillId(skillId);
				pane.setPhaseDuration(skillDuration);
			}
		}
		
		// fifth screen
		if(buildProject(false, true))
			view.displayResults();
		
		// disable write if the project is finished
		if(project.isFinished()){
			view.disableWrite(true);
		}
	}
	
	// sort the phases ascending (by their start date)
	private ArrayList<Phase> sortPhases(ArrayList<Phase> phases){
		ArrayList<Phase> sorted = new ArrayList<Phase>();
		
		int size = phases.size();
		for(int i=0;i<size;i++){
			Phase earliest = getEarliestPhase(phases);
			sorted.add(i, earliest);
			phases.remove(earliest);
		}
		
		return sorted;
	}
	
	// helper method for the phase sort method
	private Phase getEarliestPhase(ArrayList<Phase> phases){
		ArrayList<Phase> _phases = new ArrayList<>(phases);
		
		Phase earliest = _phases.get(0);
		_phases.remove(0);
		for(Phase phase : _phases){
			if(phase.getStartDate().isBefore(earliest.getStartDate())){
				earliest = phase;
			}
		}
		
		return earliest;
	}
	
	
	// calculate the pds
	private PDTableModel calculatePDs(String skillName, Result result, boolean withRisk){
		PDTableModel pdModel = new PDTableModel();
		pdModel.skillName.set(skillName);
		if(withRisk)
			pdModel.pdShould.set(result.getPdTotalShouldRisk());
		else
			pdModel.pdShould.set(result.getPdTotalShould());
		pdModel.pdIs.set(result.getPdTotalBe());
		pdModel.pdIsInt.set(result.getPdInt());
		pdModel.pdIsExt.set(result.getPdExt());
		
		return pdModel;
	}
	
	// calculate costs
	private CostTableModel calculateCosts(String skillName, Result result){
		CostTableModel costModel = new CostTableModel();
		costModel.skillName.set(skillName);
		costModel.costTotal.set(result.getCostTotal());
		costModel.costInt.set(result.getCostInt());
		costModel.costExt.set(result.getCostExt());
		
		return costModel;
	}
	
	// calculate the quarters
	private QuarterTableModel calculateQuarters(String skillName, Result result, int yearBegin, int quarterBegin, int yearEnd, int quarterEnd){
		QuarterTableModel quarterModel = new QuarterTableModel();
		
		ArrayList<Year> years = result.getYears();
		years.sort(new Comparator<Year>() {
			@Override
			public int compare(Year o1, Year o2) {
				return o1.getYearDate()<o2.getYearDate() ? -1 :
					o1.getYearDate()==o2.getYearDate() ? 0 : 1;
			}
		});
		
		int pdInt = 0;
		int pdExt = 0;
		float costInt = 0;
		float costExt = 0;
		for(Year year : years){
			if(year.getYearDate() == yearBegin && year.getYearDate() == yearEnd){
				for(int i=quarterBegin;i<=quarterEnd;i++){
					Quarter quarter = year.getQuarter(i);
					pdInt += quarter.getPdInt();
					pdExt += quarter.getPdExt();
					costInt += quarter.getCostInt();
					costExt += quarter.getCostExt();
				}
			}else if(year.getYearDate() == yearBegin){
				for(int i=quarterBegin;i<=4;i++){
					Quarter quarter = year.getQuarter(i);
					pdInt += quarter.getPdInt();
					pdExt += quarter.getPdExt();
					costInt += quarter.getCostInt();
					costExt += quarter.getCostExt();
				}
			}else if(year.getYearDate() < yearEnd){
				for(int i=1;i<=4;i++){
					Quarter quarter = year.getQuarter(i);
					pdInt += quarter.getPdInt();
					pdExt += quarter.getPdExt();
					costInt += quarter.getCostInt();
					costExt += quarter.getCostExt();
				}
			} else if(year.getYearDate() == yearEnd){
				for(int i=1;i<=quarterEnd;i++){
					Quarter quarter = year.getQuarter(i);
					pdInt += quarter.getPdInt();
					pdExt += quarter.getPdExt();
					costInt += quarter.getCostInt();
					costExt += quarter.getCostExt();
				}
			}
		}
		
		quarterModel.skillName.set(skillName);
		quarterModel.pdInt.set(pdInt);
		quarterModel.pdExt.set(pdExt);
		quarterModel.costInt.set(costInt);
		quarterModel.costExt.set(costExt);
		
		return quarterModel;
	}
	
	
	
	@Override
	public void saveSettings(String serverAddress, int serverPort) {
		// do something only on change
		if(!serverAddress.equals(this.serverAddress) || this.serverPort != serverPort){
			this.serverPort = serverPort;
			this.serverAddress = serverAddress;
			
			String firstLine = "ADDR="+serverAddress;
			String lastLine = "PORT="+serverPort;
			
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter(configFile));
				writer.write(firstLine);
				writer.newLine();
				writer.write(lastLine);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			openNewConnection();
		}
	}
	
	
	
	@Override
	public void startGui() {
		view = new View(this);
		view.showFrame();
	}
	
	
	@Override
	public void guiInitialized() {
		loadSettings();
	}
	
	
	@Override
	public String getServerAddress(){
		return serverAddress;
	}
	
	
	@Override
	public int getServerPort(){
		return serverPort;
	}
	
	
	@Override
	public Project getProject() {
		return project;
	}
	


	@Override
	public ArrayList<Skill> getSkills() {
		ArrayList<Skill> skills = new ArrayList<Skill>();
		for(SkillPane pane : view.getSkillPanes()){
			// check if all fields are filled out
			if(pane.getSkillName().isEmpty()){
				continue;
			}else{
				// build the new skill
				Skill skill = new Skill();
				skill.setSkillName(pane.getSkillName());
				skill.setDayRateInt(pane.getDayRateInt());
				skill.setDayRateExt(pane.getDayRateExt());
				skills.add(skill);
			}
		}
		
		return skills;
	}
	
	
	
	@Override
	public ObservableList<PDTableModel> getPDTable(boolean withRisk){
		ObservableList<PDTableModel> pdData = FXCollections.observableArrayList();
		
		for(Skill skill : project.getSkills()){
			PDTableModel pdModel = null;
			Result result;
			if(skill.getResult() != null){
				result = skill.getResult();
				pdModel = calculatePDs(skill.getSkillName(), result, withRisk);
			}
			pdData.add(pdModel);
		}
		
		// summary line
		PDTableModel pdModel = null;
		Result result = project.getResult();
		if(result != null){
			pdModel = calculatePDs("Gesamt", result, withRisk);
		}
		pdData.add(pdModel);
		
		return pdData;
	}
	
	@Override
	public ObservableList<CostTableModel> getCostTable(){
		ObservableList<CostTableModel> costData = FXCollections.observableArrayList();
		
		for(Skill skill : project.getSkills()){
			CostTableModel costModel = null;
			Result result;
			if(skill.getResult() != null){
				result = skill.getResult();
				costModel = calculateCosts(skill.getSkillName(), result);
			}
			costData.add(costModel);
		}
		
		// summary line
		CostTableModel costModel = null;
		Result result = project.getResult();
		if(result != null){
			costModel = calculateCosts("Gesamt", result);
		}
		costData.add(costModel);
		
		return costData;
	}
	
	@Override
	public ObservableList<QuarterTableModel> getQuarterTable(int yearBegin, int quarterBegin, int yearEnd, int quarterEnd){
		ObservableList<QuarterTableModel> quarterData = FXCollections.observableArrayList();
		
		if(yearBegin < 0 || quarterBegin < 0 || yearEnd < 0 || quarterEnd < 0)
			return quarterData;
		
		for(Skill skill : project.getSkills()){
			QuarterTableModel quarterModel = null;
			Result result;
			if(skill.getResult() != null){
				result = skill.getResult();
				quarterModel = calculateQuarters(skill.getSkillName(), result, yearBegin, quarterBegin, yearEnd, quarterEnd);
			}
			quarterData.add(quarterModel);
		}
		
		// summary line
		QuarterTableModel quarterModel = null;
		Result result = project.getResult();
		if(result != null){
			quarterModel = calculateQuarters("Gesamt", result, yearBegin, quarterBegin, yearEnd, quarterEnd);
		}
		quarterData.add(quarterModel);
		
		return quarterData;
	}
	
	
	
	@Override
	public boolean calculateProject(){
		Project result = null;
		
		if(!buildProject())
			return false;
		try {
			result = server.calculateProject(project);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		project = result;
		
		return true;
	}
	
	
	@Override
	public void saveProject(){
		if(buildProject(true, false)){
			try {
				server.saveProject(project);
				view.displayProjects(server.getAllProjectNames());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void loadProject(String projectName){
		view.clearAll();
		
		try {
			project = server.loadProject(projectName);
		} catch (RemoteException e) {
			e.printStackTrace();
			return;
		}
		
		showProject();
	}
	
	@Override
	public void deleteProject(String projectName){
		try {
			server.deleteProject(projectName);
			view.displayProjects(server.getAllProjectNames());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void finishProject(){
		project.setFinished(true);
		project.setTimestamp(LocalDateTime.now());
		
		try {
			server.saveProject(project);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		view.setProjectTimeStamp(project.getTimestamp());
		view.disableWrite(true);
	}
	
	@Override
	public boolean isProjectFinished(){
		return project.isFinished();
	}


	@Override
	public LocalDate getProjectStartDate() {
		return project.getStartDate();
	}


	@Override
	public LocalDate getProjectEndDate() {
		return project.getEndDate();
	}
}
