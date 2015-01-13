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
		private void buildProject(){
			project = new Project();
			
			// first screen
			project.setProjectName(view.getProjectName());
			project.setProjectResponsible(view.getProjectResponsible());
			project.setDescription(view.getProjectDescription());
			
			// second screen
			for(SkillPane pane : view.getSkillPanes()){
				Skill skill = new Skill();
				skill.setSkillName(pane.getSkillName());
				skill.setDayRateInt(pane.getDayRateInt());
				skill.setDayRateExt(pane.getDayRateExt());
				
				project.addSkill(skill);
			}
			
			// third screen
			for(ResourcePaneWrapper paneWrapper : view.getResourcePanes()){
				for(ResourcePane pane : paneWrapper.getResourcePanes()){
					Resource resource = new Resource();
					resource.setResourceName(pane.getResourceName());
					resource.setSkill(paneWrapper.getSkill().getSkillID());
					resource.setAvailability(pane.getAvailability());
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
				mainPhase.setPhaseName(main.getPhaseName());
				if(phases.get(main).isEmpty()){
					mainPhase.setStartDate(main.getPhaseBegin());
					mainPhase.setEndDate(main.getPhaseEnd());
					mainPhase.setRiskFactor(main.getRiskFactor());
					
					for(PhasePane phasePane : main.getPhasePanes()){
						mainPhase.addSkill(phasePane.getPhaseSkillId(), phasePane.getPhaseDuration());
					}
					
					project.addPhase(mainPhase);
				}else{
					for(PhasePaneWrapper sub : phases.get(main)){
						Phase subPhase = new Phase();
						subPhase.setPhaseName(sub.getPhaseName());
						subPhase.setParent(mainPhase);
						subPhase.setStartDate(sub.getPhaseBegin());
						subPhase.setEndDate(sub.getPhaseEnd());
						subPhase.setRiskFactor(sub.getRiskFactor());
						
						for(PhasePane phasePane : sub.getPhasePanes()){
							subPhase.addSkill(phasePane.getPhaseSkillId(), phasePane.getPhaseDuration());
						}
						
						project.addPhase(subPhase);
					}
				}
			}
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
			if(pane.getSkillName().isEmpty() || Float.isNaN(pane.getDayRateInt())|| Float.isNaN(pane.getDayRateExt())){
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
	public void calculateProject(){
		Project result = null;
		
		buildProject();
		try {
			result = server.calculateProject(project);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		project = result;
	}
}
