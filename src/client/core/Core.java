package client.core;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import model.project.Phase;
import model.project.Project;
import model.project.Resource;
import model.project.Skill;
import global.ASingelton;
import global.IServerService;
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
	
	
	/**
	 * The constructor
	 */
	public Core(){
		// connect to the server
		try {
			server = (IServerService) Naming.lookup("rmi://localhost:4711/PROJECT");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
//			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public void startGui() {
		view = new View(this);
		view.showFrame();
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
	public void calculateProject(){
		Project result = null;
		
		buildProject();
		try {
			result = server.calculateProject(project);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
