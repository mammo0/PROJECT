package client.core;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import model.project.Skill;
import global.ASingelton;
import global.IServerService;
import client.view.IViewClient;
import client.view.View;
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
	
	
	/**
	 * The constructor
	 */
	public Core(){
		// connect to the server
		try {
			server = (IServerService) Naming.lookup("rmi://localhost:12345/PROJECT");
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
}
