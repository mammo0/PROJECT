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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.project.Phase;
import model.project.Project;
import model.project.Resource;
import model.project.Result;
import model.project.Skill;
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
		project = new Project();
		
		// first screen
		if(view.getProjectName().replaceAll(" ", "").isEmpty()){
			view.markNode(null, "txtProjectName");
			view.setStatus("Bitte einen Projektnamen angeben.", 10);
			return false;
		}
		project.setProjectName(view.getProjectName());
		if(view.getProjectResponsible().replaceAll(" ", "").isEmpty()){
			view.markNode(null, "txtProjectResponsible");
			view.setStatus("Bitte einen Projektverantwortlichen angeben.", 10);
			return false;
		}
		project.setProjectResponsible(view.getProjectResponsible());
		project.setDescription(view.getProjectDescription());
		
		// second screen
		for(SkillPane pane : view.getSkillPanes()){
			Skill skill = new Skill();
			if(pane.getSkillName().replaceAll(" ", "").isEmpty()){
				view.markNode(pane, "txtSkillName");
				view.setStatus("Bitte einen Kompetenznamen angeben.", 10);
				return false;
			}
			skill.setSkillName(pane.getSkillName());
			skill.setDayRateInt(pane.getDayRateInt());
			skill.setDayRateExt(pane.getDayRateExt());
			
			project.addSkill(skill);
		}
		
		// third screen
		for(ResourcePaneWrapper paneWrapper : view.getResourcePanes()){
			for(ResourcePane pane : paneWrapper.getResourcePanes()){
				Resource resource = new Resource();
				if(pane.getResourceName().replaceAll(" ", "").isEmpty()){
					view.markNode(pane, "txtResourceName");
					view.setStatus("Bitte einen Resourcennamen angeben.", 10);
					return false;
				}
				resource.setResourceName(pane.getResourceName());
				resource.setSkill(paneWrapper.getSkill().getSkillID());
				if(pane.getAvailability() == -1){
					view.markNode(pane, "txtAvailability");
					view.setStatus("Bitte eine Verfügbarkeit angeben.", 10);
					return false;
				}
				resource.setAvailability(pane.getAvailability());
				if(pane.getSkillAmount() == -1){
					view.markNode(pane, "txtSkillAmount");
					view.setStatus("Bitte eine Anzahl angeben.", 10);
					return false;
				}
				resource.setSkillAmount(pane.getSkillAmount());
				resource.setIntern(pane.isIntern());
				
				project.addResource(resource);
			}
		}
		
		// fourth screen
		Hashtable<PhasePaneWrapper, ArrayList<PhasePaneWrapper>> phases = view.getPhasePanes();
		Enumeration<PhasePaneWrapper> enumKey = phases.keys();
		while(enumKey.hasMoreElements()){
			PhasePaneWrapper main = enumKey.nextElement();
			
			Phase mainPhase = new Phase();
			if(main.getPhaseName().replaceAll(" ", "").isEmpty()){
				view.markNode(main, "txtPhaseName");
				view.setStatus("Bitte einen Phasennamen angeben.", 10);
				return false;
			}
			mainPhase.setPhaseName(main.getPhaseName());
			if(phases.get(main).isEmpty()){
				if(main.getPhaseBegin() == null){
					view.markNode(main, "datPhaseBegin");
					view.setStatus("Bitte einen Phasenbeginn angeben.", 10);
					return false;
				}
				mainPhase.setStartDate(main.getPhaseBegin());
				if(main.getPhaseEnd() == null){
					view.markNode(main, "datPhaseEnd");
					view.setStatus("Bitte ein Phasenende angeben.", 10);
					return false;
				}
				mainPhase.setEndDate(main.getPhaseEnd());
				mainPhase.setRiskFactor(main.getRiskFactor());
				
				for(PhasePane phasePane : main.getPhasePanes()){
					if(phasePane.getPhaseSkillId() == -1){
						view.markNode(phasePane, "cmbSkills");
						view.setStatus("Bitte einen Skill auswählen.", 10);
						return false;
					}
					if(phasePane.getPhaseDuration() == -1){
						view.markNode(phasePane, "txtDuration");
						view.setStatus("Bitte eine Dauer angeben.", 10);
						return false;
					}
					mainPhase.addSkill(phasePane.getPhaseSkillId(), phasePane.getPhaseDuration());
				}
				
				project.addPhase(mainPhase);
			}else{
				for(PhasePaneWrapper sub : phases.get(main)){
					Phase subPhase = new Phase();
					if(sub.getPhaseName().replaceAll(" ", "").isEmpty()){
						view.markNode(sub, "txtPhaseName");
						view.setStatus("Bitte einen Phasennamen angeben.", 10);
						return false;
					}
					subPhase.setPhaseName(sub.getPhaseName());
					subPhase.setParent(mainPhase);
					if(sub.getPhaseBegin() == null){
						view.markNode(sub, "datPhaseBegin");
						view.setStatus("Bitte einen Phasenbeginn angeben.", 10);
						return false;
					}
					subPhase.setStartDate(sub.getPhaseBegin());
					if(sub.getPhaseEnd() == null){
						view.markNode(sub, "datPhaseEnd");
						view.setStatus("Bitte ein Phasenende angeben.", 10);
						return false;
					}
					subPhase.setEndDate(sub.getPhaseEnd());
					subPhase.setRiskFactor(sub.getRiskFactor());
					
					for(PhasePane phasePane : sub.getPhasePanes()){
						if(phasePane.getPhaseSkillId() == -1){
							view.markNode(phasePane, "cmbSkills");
							view.setStatus("Bitte einen Skill auswählen.", 10);
							return false;
						}
						if(phasePane.getPhaseDuration() == -1){
							view.markNode(phasePane, "txtDuration");
							view.setStatus("Bitte eine Dauer angeben.", 10);
							return false;
						}
						subPhase.addSkill(phasePane.getPhaseSkillId(), phasePane.getPhaseDuration());
					}
					
					project.addPhase(subPhase);
				}
			}
		}
		
		return true;
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
	public ObservableList<pdTableModel> getPDTable(boolean withRisk){
		ObservableList<pdTableModel> pdData = FXCollections.observableArrayList();
		
		for(Skill skill : project.getSkills()){
			pdTableModel pdModel = new pdTableModel();
			Result result = new Result();
			if(withRisk){
				if(skill.getResultRisk() != null){
					// not implemented yet
				}
			}else{
				if(skill.getResultWRisk() != null){
					result = skill.getResultWRisk();
					pdModel.skillName.set(skill.getSkillName());
					pdModel.pdShould.set(result.getPdTotalShould());
					pdModel.pdIs.set(result.getPdTotalBe());
					pdModel.pdIsInt.set(result.getPdInt());
					pdModel.pdIsExt.set(result.getPdExt());
				}
			}
			pdData.add(pdModel);
		}
		
		return pdData;
	}
	
	@Override
	public ObservableList<costTableModel> getCostTable(boolean withRisk){
		ObservableList<costTableModel> costData = FXCollections.observableArrayList();
		
		for(Skill skill : project.getSkills()){
			costTableModel costModel = new costTableModel();
			Result result = new Result();
			if(withRisk){
				if(skill.getResultRisk() != null){
					// not implemented yet
				}
			}else{
				if(skill.getResultWRisk() != null){
					result = skill.getResultWRisk();
					costModel.skillName.set(skill.getSkillName());
					costModel.costTotal.set(result.getCostTotal());
					costModel.costInt.set(result.getCostInt());
					costModel.costExt.set(result.getCostExt());
				}
			}
			costData.add(costModel);
		}
		
		return costData;
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
}
