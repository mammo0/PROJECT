package server.core;

import global.ASingelton;
import global.IServerService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.stream.XMLStreamException;

import model.project.Phase;
import model.project.Project;
import model.project.Quarter;
import model.project.Resource;
import model.project.Result;
import model.project.Skill;
import model.project.Year;
import server.view.View;

/**
 * This class is the core for the server application
 * 
 * @author Ammon
 *
 */
public class Core extends ASingelton implements ICoreServer {

	private View view;
	private CreateXML createxml;

	private String projectPath;

	// the server object
	private IServerService server;
	private Registry rmiRegistry;
	private int serverPort;
	private String rmiUrl;
	private Project project;
	Hashtable<String, Integer> phasesdays = new Hashtable();
	private Result resultProject;

	private File projectFilesDir;
	
	private File configFile;

	public Core() {
		configFile = new File("server.conf");
		
		loadSettings();
	}

	@Override
	public String getProjectDirectory() {
		if (projectFilesDir == null){
			if(projectPath.endsWith(File.separator))
				return projectPath;
			else
				return projectPath + File.separator;
		}else
			return projectFilesDir.getAbsolutePath() + File.separator;
	}

	@Override
	public void setProjectDirectory(String path) {
		this.projectPath = path;
	}

	@Override
	public int getServerPort() {
		return serverPort;
	}

	@Override
	public void setServerPort(int portNumber) {
		this.serverPort = portNumber;
	}

	@Override
	public boolean isServerRunning() {
		if (rmiUrl == null)
			return false;

		try {
			if (Naming.lookup(rmiUrl) != null)
				return true;
			else
				return false;
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			return false;
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
					setServerPort(Integer.valueOf(line.split("=")[1]));
				else if(line.startsWith("DIR="))
					setProjectDirectory(line.split("=")[1]);
				else{
					reader.close();
					throw new IOException();
				}
			}
			reader.close();
		} catch (IOException e) {
			setProjectDirectory(System.getProperty("user.dir"));
			setServerPort(4711);
		}
	}
	
	
	@Override
	public void saveSettings(String projectDir, int serverPort) {
		setServerPort(serverPort);
		setProjectDirectory(projectDir);
		
		String firstLine = "DIR="+projectDir;
		String lastLine = "PORT="+serverPort;
		
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(configFile));
			writer.write(firstLine);
			writer.newLine();
			writer.write(lastLine);
			writer.close();
		} catch (IOException e) {}
	}
	

	@Override
	public void startGui() {
		view = new View();
		view.showFrame();

	}

	@Override
	public void startServer() {
		if (serverPort == 0)
			return;

		// build the rmi url
		this.rmiUrl = "rmi://localhost:" + serverPort + "/PROJECT";

		// set the server project path
		this.projectFilesDir = new File(projectPath);

		try {
			// set up the server
			server = new ProjectCalculator(this);

			// start it
			rmiRegistry = LocateRegistry.createRegistry(serverPort);
			Naming.rebind(rmiUrl, server);
			System.out.println("Server lÃ¤uft.");
		} catch (RemoteException | MalformedURLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void stopServer() {
		if (rmiUrl == null)
			return;

		try {
			Naming.unbind(rmiUrl);
			UnicastRemoteObject.unexportObject(server, true);
			UnicastRemoteObject.unexportObject(rmiRegistry, true);
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			// ignore the exception
			// e.printStackTrace();
		}

		// clean server object
		server = null;

		System.out.println("Server gestoppt.");
	}

	@Override
	public Project calculateProject(Project project) {
		this.project = project;
		resultProject = new Result();
		project.setResult(resultProject);
		if(project.getPhases().size()!= 0){
		calculateLenght(project);
		calculateProjectDays(project);
		calculateResultSkill(project);
		calculateQuarterResults(project);
		}
		else{
			
		}
		return project;
	}
	

	// calculate the length of the complete Project
	public void calculateLenght(Project project) {

		LocalDate startdate = null;
		LocalDate enddate = null;
		for (Phase phases : project.getPhases()) {

			if (startdate == null
					|| startdate.compareTo(phases.getStartDate()) > 0) {

				startdate = phases.getStartDate();
			}

			if (enddate == null || enddate.compareTo(phases.getEndDate()) < 0) {
				enddate = phases.getEndDate();
			}
		}
		project.setStartDate(startdate);
		project.setEndDate(enddate);
		
		//Jahrobjekte ins Projekt schreiben
		
	}
	
	

	public void calculateYearsQuarters(Project project, Result result) {

		LocalDate startdate = project.getStartDate();
		LocalDate enddate = project.getEndDate();
		Year year;
		Quarter quarter;
		int startdateYear = project.getStartDate().getYear();
		int enddateYear = project.getEndDate().getYear();
		int startquarter = 0;
		int endquarter = 0;
		int deltaYears = 0;
		int numberQuarters = 0;

		int numberofYears = enddateYear - startdateYear + 1;
		
		for (int i = 0; i < numberofYears; i++) {
			year = new Year();
			year.setYearDate(startdateYear + i);
			result.addYear(year);
		}

		if (result.getYears().size() > 1) {
			startquarter = (startdate.getMonthValue() - 1) / 3 + 1;
			switch (startquarter) {
			case 1:
				result.getYears().get(0).setQ1(quarter = new Quarter());
				result.getYears().get(0).setQ2(quarter = new Quarter());
				result.getYears().get(0).setQ3(quarter = new Quarter());
				result.getYears().get(0).setQ4(quarter = new Quarter());
				break;
			case 2:
				result.getYears().get(0).setQ2(quarter = new Quarter());
				result.getYears().get(0).setQ3(quarter = new Quarter());
				result.getYears().get(0).setQ4(quarter = new Quarter());
				break;
			case 3:
				result.getYears().get(0).setQ3(quarter = new Quarter());
				result.getYears().get(0).setQ4(quarter = new Quarter());
				break;
			case 4:
				result.getYears().get(0).setQ4(quarter = new Quarter());
				break;
			}

			deltaYears = enddateYear - startdateYear - 1;
			while (deltaYears > 0) {
				result.getYears().get(deltaYears)
						.setQ1(quarter = new Quarter());
				result.getYears().get(deltaYears)
						.setQ2(quarter = new Quarter());
				result.getYears().get(deltaYears)
						.setQ3(quarter = new Quarter());
				result.getYears().get(deltaYears)
						.setQ4(quarter = new Quarter());
				deltaYears = deltaYears - 1;
			}

			endquarter = (enddate.getMonthValue() - 1) / 3 + 1;
			switch (endquarter) {
			case 1:
				result.getYears().get(result.getYears().size() - 1)
						.setQ1(quarter = new Quarter());
				break;
			case 2:
				result.getYears().get(result.getYears().size() - 1)
						.setQ1(quarter = new Quarter());
				result.getYears().get(result.getYears().size() - 1)
						.setQ2(quarter = new Quarter());
				break;
			case 3:
				result.getYears().get(result.getYears().size() - 1)
						.setQ1(quarter = new Quarter());
				result.getYears().get(result.getYears().size() - 1)
						.setQ2(quarter = new Quarter());
				result.getYears().get(result.getYears().size() - 1)
						.setQ3(quarter = new Quarter());
				break;
			case 4:
				result.getYears().get(result.getYears().size() - 1)
						.setQ1(quarter = new Quarter());
				result.getYears().get(result.getYears().size() - 1)
						.setQ2(quarter = new Quarter());
				result.getYears().get(result.getYears().size() - 1)
						.setQ3(quarter = new Quarter());
				result.getYears().get(result.getYears().size() - 1)
						.setQ4(quarter = new Quarter());
				break;
			}
		} else {

			startquarter = (startdate.getMonthValue() - 1) / 3 + 1;
			endquarter = (enddate.getMonthValue() - 1) / 3 + 1;
			numberQuarters = endquarter - startquarter + 1;

			if (startdate.getMonthValue() == 1
					|| startdate.getMonthValue() == 2
					|| startdate.getMonthValue() == 3) {
				switch (numberQuarters) {
				case 1:
					result.getYears().get(0).setQ1(quarter = new Quarter());
					break;
				case 2:
					result.getYears().get(0).setQ1(quarter = new Quarter());
					result.getYears().get(0).setQ2(quarter = new Quarter());
					break;
				case 3:
					result.getYears().get(0).setQ1(quarter = new Quarter());
					result.getYears().get(0).setQ2(quarter = new Quarter());
					result.getYears().get(0).setQ3(quarter = new Quarter());
					break;
				case 4:
					result.getYears().get(0).setQ1(quarter = new Quarter());
					result.getYears().get(0).setQ2(quarter = new Quarter());
					result.getYears().get(0).setQ3(quarter = new Quarter());
					result.getYears().get(0).setQ4(quarter = new Quarter());
					break;
				}
			} else if (startdate.getMonthValue() == 4
					|| startdate.getMonthValue() == 5
					|| startdate.getMonthValue() == 6) {
				switch (numberQuarters) {
				case 1:
					result.getYears().get(0).setQ2(quarter = new Quarter());
					break;
				case 2:
					result.getYears().get(0).setQ2(quarter = new Quarter());
					result.getYears().get(0).setQ3(quarter = new Quarter());
					break;
				case 3:
					result.getYears().get(0).setQ2(quarter = new Quarter());
					result.getYears().get(0).setQ3(quarter = new Quarter());
					result.getYears().get(0).setQ4(quarter = new Quarter());
					break;
				}

			} else if (startdate.getMonthValue() == 7
					|| startdate.getMonthValue() == 8
					|| startdate.getMonthValue() == 9) {
				switch (numberQuarters) {
				case 1:
					result.getYears().get(0).setQ3(quarter = new Quarter());
					break;
				case 2:
					result.getYears().get(0).setQ3(quarter = new Quarter());
					result.getYears().get(0).setQ4(quarter = new Quarter());
					break;
				}

			} else if (startdate.getMonthValue() == 10
					|| startdate.getMonthValue() == 11
					|| startdate.getMonthValue() == 12) {
				switch (numberQuarters) {
				case 1:
					result.getYears().get(0).setQ4(quarter = new Quarter());
					break;
				}

			}
		}
	}
	


	// calculate the skill result object
	public void calculateResultSkill(Project project) {

		for (Skill skill : project.getSkills()) {

			Result result = new Result();
			calculateYearsQuarters(project, result);

			skill.setResult(result);

		}
	}

	public void calculateQuarterResults(Project project) {
		Hashtable <Integer, Integer> Puffer = new Hashtable();
		for (Phase phases : project.getPhases()) {

			LocalDate startdate = phases.getStartDate();
			LocalDate enddate = phases.getEndDate();
			int _diffdate=0;
			int _endyear = phases.getEndDate().getYear();
			int _startquarter = (phases.getStartDate().getMonthValue() - 1) / 3 + 1;
			int _endquarter = (phases.getEndDate().getMonthValue() - 1) / 3 + 1;
			
			int daysInYStart=0;
			int daysInYEnd=0;
			float dayfactorintern = 0;
			float dayfactorextern = 0;
		
			
			int durationinworkingdays = 0;
			int neededdays =0;
			
			//Gesamte Tage der Phase
			if(enddate.getYear()-startdate.getYear()==0){
			_diffdate =enddate.getDayOfYear()-startdate.getDayOfYear()+1;
			}
			else {
				int yeardiff = enddate.getYear()-startdate.getYear();
				if(yeardiff == 1){
					_diffdate=365-startdate.getDayOfYear()+enddate.getDayOfYear();
				}
				if(yeardiff>1){
					yeardiff = yeardiff-1;
					for (int i = 0; i<yeardiff; i++){
						_diffdate= _diffdate+365;
					}
					_diffdate=_diffdate + 365-startdate.getDayOfYear()+enddate.getDayOfYear();
				}
			}
			
			//Dauer in Arbeitstagen
			durationinworkingdays = (int) Math.round(_diffdate * (0.55835));
			
			for (Skill skill : project.getSkills()) {
				Result result = skill.getResult();
				int daysInQ1=0;
				int daysInQ2=0;
				int daysInQ3=0;
				int daysInQ4=0;
				int availableInternalDays=0;
				int availableExternalDays=0;
				int externalPuffer = 0;
				
				int neededInternalDays=0;
				int neededExternalDays=0;
				
				//Benötigte Arbeitstage Tage des Skills in der Phase
				if(phases.getSkills().get(skill.getSkillID())!=null){
				neededdays = phases.getSkills().get(skill.getSkillID());
				}
				else{
					neededdays=0;
				}
				//Vorhandene Interne und externe Tage 
				for ( int i = 0; i<project.getResources().size(); i++){
					if(project.getResources().get(i).getSkillID()==skill.getSkillID()){
						if (project.getResources().get(i).isIntern()==true){
							double faktor = project.getResources().get(i).getAvailability()*project.getResources().get(i).getSkillAmount()*0.01;
							availableInternalDays = (int)Math.round(availableInternalDays + durationinworkingdays*faktor);
						}
						else {
							double faktor = project.getResources().get(i).getAvailability()*project.getResources().get(i).getSkillAmount()*0.01;
							availableExternalDays = (int)Math.round(availableExternalDays + durationinworkingdays*faktor);
						}
					}
				}
				
				//Tage verteilen auf intern und extern und Kosten berechnen und ins Result schreiben
				if (neededdays==0){
					neededInternalDays=0;
					neededExternalDays=0;
				}else if (neededdays<=availableExternalDays){
					neededInternalDays=0;
					neededExternalDays=neededdays;
					externalPuffer=availableExternalDays-neededdays;
				} else if(neededdays<=availableExternalDays+availableInternalDays){
					neededInternalDays=neededdays-availableExternalDays;
					neededExternalDays=availableExternalDays;
				} else if (neededdays>availableExternalDays+availableInternalDays){
					neededInternalDays=availableInternalDays;
					neededExternalDays=availableExternalDays;
				}
			 if (Puffer.containsKey(skill.getSkillID())){
				 int temppuffer= Puffer.get(skill.getSkillID())+externalPuffer;
				 
				 Puffer.replace(skill.getSkillID(), temppuffer);
				 
			 }
			 else{
				 Puffer.put(skill.getSkillID(), externalPuffer);
			 }
						 dayfactorintern = (float) neededInternalDays/_diffdate;
						 dayfactorextern = (float) neededExternalDays/_diffdate;
						 
						 
						 
						//Start und Ende in einem Quartal
						if (startdate.getYear()-enddate.getYear()==0){
								if( _startquarter == _endquarter) {

							if (_startquarter == 1 && _endquarter == 1) {
								daysInQ1=_diffdate;
								createQ1(daysInQ1, startdate.getYear(), dayfactorintern, 
										dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
								
							} else if (_startquarter == 2 && _endquarter == 2) {
								daysInQ2=_diffdate;
								createQ2(daysInQ2, startdate.getYear(), dayfactorintern, 
										dayfactorextern, skill, neededInternalDays, neededExternalDays, result);

							} else if (_startquarter == 3 && _endquarter == 3) {
								daysInQ3=_diffdate;
								createQ3(daysInQ3, startdate.getYear(), dayfactorintern, 
										dayfactorextern, skill, neededInternalDays, neededExternalDays, result);

							} else if (_startquarter == 4 && _endquarter == 4) {
								daysInQ4=_diffdate;
								createQ4(daysInQ4, startdate.getYear(), dayfactorintern, 
										dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
							}
						}
								else {
							if (_endquarter - _startquarter == 1) {
								//Q1
								if (startdate.getMonthValue() == 1
										|| startdate.getMonthValue() == 2
										|| startdate.getMonthValue() == 3){
								 LocalDate enddatequarter = LocalDate.of(_endyear, 3, 31);
								 daysInQ1 = enddatequarter.getDayOfYear()-startdate.getDayOfYear()+1;//Tage die im ersten Quartal liegen
								 daysInQ2 = _diffdate - daysInQ1;//Restliche tage folglich im 2 quartal	
								 
								 createQ1(daysInQ1, startdate.getYear(), dayfactorintern, 
											dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
								 createQ2(daysInQ2, startdate.getYear(), dayfactorintern, 
											dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
								}
								//Q2
								else if (startdate.getMonthValue() == 4
										|| startdate.getMonthValue() == 5
										|| startdate.getMonthValue() == 6){
								LocalDate enddatequarter = LocalDate.of(_endyear, 6, 30);
								daysInQ2 = enddatequarter.getDayOfYear()-startdate.getDayOfYear();//Tage die im zweiten Quartal liegen
								daysInQ3 = _diffdate - daysInQ2;//Restliche tage folglich im 3 quartal	
								
								createQ2(daysInQ2, startdate.getYear(), dayfactorintern, 
										dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
								createQ3(daysInQ3, startdate.getYear(), dayfactorintern, 
										dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
								}
								//Q3
								else if (startdate.getMonthValue() == 7
										|| startdate.getMonthValue() == 8
										|| startdate.getMonthValue() == 9){
								LocalDate enddatequarter = LocalDate.of(_endyear, 9, 30);
								daysInQ3 = enddatequarter.getDayOfYear()-startdate.getDayOfYear();//Tage die im dritten Quartal liegen
								daysInQ4 = _diffdate - daysInQ3;//Restliche tage folglich im 4 quartal
								 
								createQ3(daysInQ3, startdate.getYear(), dayfactorintern, 
											dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
								createQ4(daysInQ4, startdate.getYear(), dayfactorintern, 
											dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
							} 	
							}
							//Wenn ein gesamtes Quartal dabei ist					
							else if (_endquarter - _startquarter == 2) {
								//Q1
								if (startdate.getMonthValue() == 1
										|| startdate.getMonthValue() == 2
										|| startdate.getMonthValue() == 3){
								LocalDate enddatequarter = LocalDate.of(_endyear, 3, 31);
								 daysInQ1 = enddatequarter.getDayOfYear()-startdate.getDayOfYear();//Tage die im ersten Quartal liegen
								 daysInQ2 = 91;//Tage im gesamten 2. Quartal
								 daysInQ3=_diffdate - daysInQ1 - daysInQ2;// Tage im 3 Quartal
						
								 createQ1(daysInQ1, startdate.getYear(), dayfactorintern, 
											dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
								 createQ2(daysInQ2, startdate.getYear(), dayfactorintern, 
											dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
								 createQ3(daysInQ3, startdate.getYear(), dayfactorintern, 
											dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
							}
								else if (startdate.getMonthValue() == 4
										|| startdate.getMonthValue() == 5
										|| startdate.getMonthValue() == 6){
									LocalDate enddatequarter = LocalDate.of(_endyear, 6, 30);
										 daysInQ2 = enddatequarter.getDayOfYear()-startdate.getDayOfYear();//Tage im gesamten 2. Quartal
										 daysInQ3 = 92;// Tage im 3 Quartal
										 daysInQ4=_diffdate - daysInQ2 - daysInQ3;
										 
										 createQ2(daysInQ2, startdate.getYear(), dayfactorintern, 
													dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
										 createQ3(daysInQ3, startdate.getYear(), dayfactorintern, 
													dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
										 createQ4(daysInQ4, startdate.getYear(), dayfactorintern, 
													dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
								}
							}
							 else if (_endquarter - _startquarter == 3) {
								LocalDate enddatequarter = LocalDate.of(_endyear, 3, 31);
								//Tage die im ersten Quartal liegen
									 daysInQ1 = enddatequarter.getDayOfYear()-startdate.getDayOfYear();
									 daysInQ2 = 91;//Tage im gesamten 2. Quartal
									 daysInQ3=92;// Tage im 3 Quartal
									 daysInQ4= _diffdate-daysInQ1-daysInQ2-daysInQ3;// Tage im 4 Quartale
									 
									 createQ1(daysInQ1, startdate.getYear(), dayfactorintern, 
												dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
									 createQ2(daysInQ2, startdate.getYear(), dayfactorintern, 
												dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
									 createQ3(daysInQ3, startdate.getYear(), dayfactorintern, 
												dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
									 createQ4(daysInQ4, startdate.getYear(), dayfactorintern, 
												dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
							}
						}
				}//Abschluss der if Abfrage ob es sich um das gleiche jahr handelt
						else if(enddate.getYear()-startdate.getYear()==1){
							daysInYStart = 365-startdate.getDayOfYear();
							daysInYEnd = enddate.getDayOfYear();
							
							 //Startjahr
							//Gibt den Index zurï¿½ck an dem das Startjahr im Array steht
							if(startdate.getMonthValue()==10||startdate.getMonthValue()==11||startdate.getMonthValue()==12){
								daysInQ4=daysInYStart;
								createQ4(daysInQ4, startdate.getYear(), dayfactorintern, 
										dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
							
							}
							else if(startdate.getMonthValue()==7||startdate.getMonthValue()==8||startdate.getMonthValue()==9){
									//Q4 und Q3
									daysInQ4=92;
									daysInQ3=daysInYStart-daysInQ4;
									createQ3(daysInQ3, startdate.getYear(), dayfactorintern, 
											dayfactorextern, skill, neededInternalDays, neededExternalDays, result);	
									createQ4(daysInQ4, startdate.getYear(), dayfactorintern, 
												dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
						}
							else if(startdate.getMonthValue()==4||startdate.getMonthValue()==5||startdate.getMonthValue()==6){
									//Q4 und Q3 und Q2
									daysInQ4=92;
									daysInQ3=92;
									daysInQ2=daysInYStart-daysInQ4-daysInQ3;
									createQ2(daysInQ2, startdate.getYear(), dayfactorintern, 
											dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
									createQ3(daysInQ3, startdate.getYear(), dayfactorintern, 
											dayfactorextern, skill, neededInternalDays, neededExternalDays, result);	
									createQ4(daysInQ4, startdate.getYear(), dayfactorintern, 
												dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
						}
							else {
									//Q4 und Q3
									daysInQ4=92;
									daysInQ3=92;
									daysInQ2=91;
									daysInQ1=daysInYStart-daysInQ4-daysInQ3-daysInQ2;
									createQ1(daysInQ1, startdate.getYear(), dayfactorintern, 
											dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
									
									createQ2(daysInQ2, startdate.getYear(), dayfactorintern, 
											dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
									
									createQ3(daysInQ3, startdate.getYear(), dayfactorintern, 
											dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
									
									createQ4(daysInQ4, startdate.getYear(), dayfactorintern, 
												dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
										}
						
							if(enddate.getMonthValue()==1||enddate.getMonthValue()==2||enddate.getMonthValue()==3){
								//Nur Q1
								daysInQ1 = daysInYEnd;
								
								createQ1(daysInQ1, enddate.getYear(), dayfactorintern, 
										dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
								 
							}
							else if(enddate.getMonthValue()==4||enddate.getMonthValue()==5||enddate.getMonthValue()==6){
									//Q1, Q2
									daysInQ1= 90;
									daysInQ2 =daysInYEnd- daysInQ1;
										
									createQ1(daysInQ1, enddate.getYear(), dayfactorintern, 
												dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
									createQ2(daysInQ2, enddate.getYear(), dayfactorintern, 
												dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
								
							}
							
							else if(enddate.getMonthValue()==7||enddate.getMonthValue()==8||enddate.getMonthValue()==9){
									daysInQ1= 90;
									daysInQ2 = 91;
									daysInQ3 = daysInYEnd- daysInQ1-daysInQ2;
								
							createQ1(daysInQ1, enddate.getYear(), dayfactorintern, 
										dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
							createQ2(daysInQ2, enddate.getYear(), dayfactorintern, 
										dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
							createQ3(daysInQ3, enddate.getYear(), dayfactorintern, 
									dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
					
					}
							
							else{
								//Q1, Q2, Q3, Q4
								daysInQ1 = 90;
								daysInQ2 = 91;
								daysInQ3 = 92;
								daysInQ4 = daysInYEnd- daysInQ1-daysInQ2-daysInQ3;
								createQ1(daysInQ1, enddate.getYear(), dayfactorintern, 
										dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
								createQ2(daysInQ2, enddate.getYear(), dayfactorintern, 
										dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
								createQ3(daysInQ3, enddate.getYear(), dayfactorintern, 
									dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
								createQ4(daysInQ4, enddate.getYear(), dayfactorintern, 
									dayfactorextern, skill, neededInternalDays, neededExternalDays, result);
							}
						}
					}
				}
		float _costIntProject = 0;
		float _costExtProject=0;
		int _pdIntBeProject = 0;
		int _pdExtBeProject = 0;
		int _pdTotalShouldProject=0;
		int _pdTotalShouldRiskProject=0;
		int _PufferProject=0;
		
		
		for (Skill skill : project.getSkills()){
			float _costInt = 0;
			float _costExt = 0;
			int _pdInt = 0;
			int _pdExt=0;
			int _pdTotalShould = 0;
			int _pdTotalShouldRisk = 0;
					
			
			for (int i=0; i<skill.getResult().getYears().size(); i++){
				Year year = skill.getResult().getYears().get(i);
				for (int j = 1; j<=4; j++){
					Quarter quarter = year.getQuarter(j);
					if (quarter!=null){
					_costInt=_costInt+quarter.getCostInt();	
					_costExt=_costExt+quarter.getCostExt();
					_pdInt=_pdInt+quarter.getPdInt();
					_pdExt = _pdExt+quarter.getPdExt();
					_costIntProject=_costIntProject+quarter.getCostInt();	
					_costExtProject=_costExtProject+quarter.getCostExt();
					_pdIntBeProject=_pdIntBeProject+quarter.getPdInt();
					_pdExtBeProject=_pdExtBeProject+quarter.getPdExt();
					}
				}	
			}
			for (Phase phase : project.getPhases()){
				if(phase.getSkills().get(skill.getSkillID())!=null){
				_pdTotalShould = _pdTotalShould + phase.getSkills().get(skill.getSkillID());
				double risikofaktor= 0;
				risikofaktor = 1+phase.getRiskFactor()*0.01;
				_pdTotalShouldRisk = (int) (_pdTotalShouldRisk + phase.getSkills().get(skill.getSkillID())*risikofaktor);
				}
			}
			skill.getResult().setCostInt(_costInt);
			skill.getResult().setCostExt(_costExt);
			skill.getResult().setCostTotal(_costExt+_costInt);
			skill.getResult().setPdIntBe(_pdInt);
			skill.getResult().setPdExtBe(_pdExt);
			skill.getResult().setPdTotalBe(_pdExt+_pdInt);
			skill.getResult().setPdTotalShould(_pdTotalShould);
			skill.getResult().setPdTotalShouldRisk(_pdTotalShouldRisk);
			skill.getResult().setPdTotalDiff(_pdTotalShould-skill.getResult().getPdTotalBe());
			skill.getResult().setPuffer(Puffer.get(skill.getSkillID()));
			_PufferProject=_PufferProject+Puffer.get(skill.getSkillID());
			_pdTotalShouldProject=_pdTotalShouldProject+_pdTotalShould;
			_pdTotalShouldRiskProject=_pdTotalShouldRiskProject+_pdTotalShouldRisk;
		}
			project.getResult().setCostInt(_costIntProject);
			project.getResult().setCostExt(_costExtProject);
			project.getResult().setCostTotal(_costIntProject+_costExtProject);
			project.getResult().setPdIntBe(_pdIntBeProject);
			project.getResult().setPdExtBe(_pdExtBeProject);
			project.getResult().setPdTotalBe(_pdExtBeProject+_pdIntBeProject);
			project.getResult().setPdTotalShould(_pdTotalShouldProject);
			project.getResult().setPdTotalShouldRisk(_pdTotalShouldRiskProject);
			project.getResult().setPdTotalDiff(_pdTotalShouldProject-project.getResult().getPdTotalBe());
			
			for (int a = 0; a<project.getResult().getYears().size(); a++){
				Year year= project.getResult().getYears().get(a);
				for (int b = 1; b<=4; b++){
					Quarter quarter = year.getQuarter(b);
					float _costExtQuarterProject =0;
					if (quarter!=null){
						
						for(Skill skill : project.getSkills()){
							_costExtQuarterProject=_costExtQuarterProject+
									skill.getResult().getYears().get(a).getQuarter(b).getCostExt();
							
						}
						
					}
					project.getResult().getYears().get(a).getQuarter(b).setCostExt(_costExtQuarterProject);	
				}
			}
			
			}
		
	
	public int calculateIndexOfStartYear(int year, Result result){
		
		int i;
		for (i=0; i<result.getYears().size(); i++){
			if(result.getYears().get(i).getYearDate()==year){
				return i;
			}
				
		}
		return -1;
		
	}
	
	public void createQ1(int daysInQ1, int year,float dayfactorintern, float dayfactorextern, Skill skill, 
			float intDaysPerPhaseAndSkill, float extDaysPerPhaseAndSkill, Result result){
		
		int yearindex = calculateIndexOfStartYear(year, result);
		
		float internalDaysPerSkillInQ1 = daysInQ1*dayfactorintern;
		float externalDaysPerSkillInQ1 = daysInQ1*dayfactorextern;
		 
		 //Kosten berechnen
		 float internalCostsPerSkillInQ1=internalDaysPerSkillInQ1*skill.getDayRateInt();
		 float externalCostsPerSkillInQ1=externalDaysPerSkillInQ1*skill.getDayRateExt();
		 //extDaysPerPhaseAndSkill und intDaysPerPhaseAndSkill ins Result (q1) schreiben und auch die kosten
		float tempIntDays = result.getYears().get(yearindex).getQ1().getPdInt();
		float tempExtDays = result.getYears().get(yearindex).getQ1().getPdExt();
		float tempIntCosts = result.getYears().get(yearindex).getQ1().getCostInt();
		float tempExtCosts = result.getYears().get(yearindex).getQ1().getCostExt();
		
		result.getYears().get(yearindex).getQ1().setPdInt((int) Math.round(tempIntDays + internalDaysPerSkillInQ1));
		result.getYears().get(yearindex).getQ1().setPdExt((int) Math.round(tempExtDays + externalDaysPerSkillInQ1));
		result.getYears().get(yearindex).getQ1().setCostInt(tempIntCosts + internalCostsPerSkillInQ1);
		result.getYears().get(yearindex).getQ1().setCostExt(tempExtCosts + externalCostsPerSkillInQ1);
		
		//Totals setzen
		
		result.getYears().get(yearindex).getQ1().setCostTotal(result.getYears().
				get(yearindex).getQ1().getCostExt()+result.getYears().get(yearindex).getQ1().getCostInt());
		result.getYears().get(yearindex).getQ1().setPdTotal(result.getYears().
				get(yearindex).getQ1().getPdExt()+result.getYears().get(yearindex).getQ1().getPdInt());
		
	}
	public void createQ2(int daysInQ2, int year,float dayfactorintern, float dayfactorextern, Skill skill, 
			float intDaysPerPhaseAndSkill, float extDaysPerPhaseAndSkill, Result result){
		
		int yearindex = calculateIndexOfStartYear(year, result);
		
		float internalDaysPerSkillInQ2 = daysInQ2*dayfactorintern;
		float externalDaysPerSkillInQ2 = daysInQ2*dayfactorextern;
		 
		 //Kosten berechnen
		 float internalCostsPerSkillInQ2=internalDaysPerSkillInQ2*skill.getDayRateInt();
		 float externalCostsPerSkillInQ2=externalDaysPerSkillInQ2*skill.getDayRateExt();
		 //extDaysPerPhaseAndSkill und intDaysPerPhaseAndSkill ins Result (q2) schreiben und auch die kosten
		float tempIntDays = result.getYears().get(yearindex).getQ2().getPdInt();
		float tempExtDays = result.getYears().get(yearindex).getQ2().getPdExt();
		float tempIntCosts = result.getYears().get(yearindex).getQ2().getCostInt();
		float tempExtCosts = result.getYears().get(yearindex).getQ2().getCostExt();
		
		result.getYears().get(yearindex).getQ2().setPdInt((int) Math.round(tempIntDays + internalDaysPerSkillInQ2));
		result.getYears().get(yearindex).getQ2().setPdExt((int) Math.round(tempExtDays + externalDaysPerSkillInQ2));
		result.getYears().get(yearindex).getQ2().setCostInt(tempIntCosts + internalCostsPerSkillInQ2);
		result.getYears().get(yearindex).getQ2().setCostExt(tempExtCosts + externalCostsPerSkillInQ2);
		
		//Totals setzen
		
				result.getYears().get(yearindex).getQ2().setCostTotal(result.getYears().
						get(yearindex).getQ2().getCostExt()+result.getYears().get(yearindex).getQ2().getCostInt());
				result.getYears().get(yearindex).getQ2().setPdTotal(result.getYears().
						get(yearindex).getQ2().getPdExt()+result.getYears().get(yearindex).getQ2().getPdInt());
		
		
	}
	
	public void createQ3(int daysInQ3, int year,float dayfactorintern, float dayfactorextern, Skill skill, 
			float intDaysPerPhaseAndSkill, float extDaysPerPhaseAndSkill, Result result){
		
		int yearindex = calculateIndexOfStartYear(year, result);
		
		float internalDaysPerSkillInQ3 = daysInQ3*dayfactorintern;
		float externalDaysPerSkillInQ3 = daysInQ3*dayfactorextern;
		 
		 //Kosten berechnen
		 float internalCostsPerSkillInQ3=internalDaysPerSkillInQ3*skill.getDayRateInt();
		 float externalCostsPerSkillInQ3=externalDaysPerSkillInQ3*skill.getDayRateExt();
		 //extDaysPerPhaseAndSkill und intDaysPerPhaseAndSkill ins Result (q3) schreiben und auch die kosten
		float tempIntDays = result.getYears().get(yearindex).getQ3().getPdInt();
		float tempExtDays = result.getYears().get(yearindex).getQ3().getPdExt();
		float tempIntCosts = result.getYears().get(yearindex).getQ3().getCostInt();
		float tempExtCosts = result.getYears().get(yearindex).getQ3().getCostExt();
		
		result.getYears().get(yearindex).getQ3().setPdInt((int) Math.round(tempIntDays + internalDaysPerSkillInQ3));
		result.getYears().get(yearindex).getQ3().setPdExt((int) Math.round(tempExtDays + externalDaysPerSkillInQ3));
		result.getYears().get(yearindex).getQ3().setCostInt(tempIntCosts + internalCostsPerSkillInQ3);
		result.getYears().get(yearindex).getQ3().setCostExt(tempExtCosts + externalCostsPerSkillInQ3);
		
		//Totals setzen
		
				result.getYears().get(yearindex).getQ3().setCostTotal(result.getYears().
						get(yearindex).getQ3().getCostExt()+result.getYears().get(yearindex).getQ3().getCostInt());
				result.getYears().get(yearindex).getQ3().setPdTotal(result.getYears().
						get(yearindex).getQ3().getPdExt()+result.getYears().get(yearindex).getQ3().getPdInt());
	}
	
	
	public void createQ4(int daysInQ4, int year,float dayfactorintern, float dayfactorextern, Skill skill, 
			float intDaysPerPhaseAndSkill, float extDaysPerPhaseAndSkill, Result result){
		
		int yearindex = calculateIndexOfStartYear(year, result);
		
		float internalDaysPerSkillInQ4 = daysInQ4*dayfactorintern;
		float externalDaysPerSkillInQ4 = daysInQ4*dayfactorextern;
		 
		 //Kosten berechnen
		 float internalCostsPerSkillInQ4=internalDaysPerSkillInQ4*skill.getDayRateInt();
		 float externalCostsPerSkillInQ4=externalDaysPerSkillInQ4*skill.getDayRateExt();
		 //extDaysPerPhaseAndSkill und intDaysPerPhaseAndSkill ins Result (q4) schreiben und auch die kosten
		float tempIntDays = result.getYears().get(yearindex).getQ4().getPdInt();
		float tempExtDays = result.getYears().get(yearindex).getQ4().getPdExt();
		float tempIntCosts = result.getYears().get(yearindex).getQ4().getCostInt();
		float tempExtCosts = result.getYears().get(yearindex).getQ4().getCostExt();
		
		result.getYears().get(yearindex).getQ4().setPdInt((int) Math.round(tempIntDays + internalDaysPerSkillInQ4));
		result.getYears().get(yearindex).getQ4().setPdExt((int) Math.round(tempExtDays + externalDaysPerSkillInQ4));
		result.getYears().get(yearindex).getQ4().setCostInt(tempIntCosts + internalCostsPerSkillInQ4);
		result.getYears().get(yearindex).getQ4().setCostExt(tempExtCosts + externalCostsPerSkillInQ4);
		
		//Totals setzen
		
				result.getYears().get(yearindex).getQ4().setCostTotal(result.getYears().
						get(yearindex).getQ4().getCostExt()+result.getYears().get(yearindex).getQ4().getCostInt());
				result.getYears().get(yearindex).getQ4().setPdTotal(result.getYears().
						get(yearindex).getQ4().getPdExt()+result.getYears().get(yearindex).getQ4().getPdInt());
	}

	// calculate the total amount 3f projectdays
	public int calculateProjectDays(Project project) {
		
		long finaldays = 0;
		double workingdays = 0;
		int years = 0;
		int startyear = 0;
		int endyear = 0;
	
		
		for(Phase Phase : project.getPhases()){
			int duration = 0;
			workingdays = 0;
			if (Phase.getEndDate().getYear() == Phase.getStartDate().getYear()) {
				duration = Phase.getEndDate().getDayOfYear()
						- Phase.getStartDate().getDayOfYear() + 1;
				while (duration >= 30) {
					workingdays = workingdays + 17;
					duration = duration - 30;
				}
				workingdays = workingdays + (0.55835) * duration;
			}else {
				years = Phase.getEndDate().getYear()
						- Phase.getStartDate().getYear();
				while (years > 1) {
					workingdays = workingdays + 204;
					years = years - 1;
				}

				startyear = 366 - Phase.getStartDate().getDayOfYear();
				while (startyear >= 30) {
					workingdays = workingdays + 17;
					startyear = startyear - 30;
				}
				workingdays = workingdays + (0.55835) * startyear;

				endyear = Phase.getEndDate().getDayOfYear();
				while (endyear >= 30) {
					workingdays = workingdays + 17;
					endyear = endyear - 30;
				}
				workingdays = workingdays + (0.55835) * endyear;
			}
			
			phasesdays.put(Phase.getPhaseName(), (int) workingdays);
		}
		
		Enumeration<String> enumKey = phasesdays.keys();
		while (enumKey.hasMoreElements()) {
			String key = enumKey.nextElement();
			finaldays = finaldays + phasesdays.get(key);
		}
		
		finaldays = Math.round(workingdays);
		return (int) finaldays;

	}

	public void writeProject(Project project) throws FileNotFoundException,
			XMLStreamException, NullPointerException {
		createxml = new CreateXML();
		createxml.setFile(getProjectDirectory() + project.getProjectName()
				+ ".xml");
		try {
			createxml.saveConfig(project);
			// createxml.startTagSkills();
			// createxml.endTagSkills();
			createxml.startTagPhasen();
			for (int i = 0; i < project.getPhases().size(); i++) {
				createxml.insertPhase(project, i);
			}
			createxml.endTagPhasen();
			createxml.startTagSkills();
			for (int j = 0; j < project.getSkills().size(); j++) {
				createxml.insertSkill(project, j);
			}
			createxml.endTagSkills();
			createxml.startTagResource();
			for (int k = 0; k < project.getResources().size(); k++) {
				createxml.insertResource(project, k);
			}
			createxml.endTagResource();
			createxml.closeXML();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<String> getAllProjectNames() throws RemoteException {
		File dir = projectFilesDir;
		ArrayList<String> Names = new ArrayList();
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().endsWith("xml") == true) {
					String _fileName = files[i].getName();
					String fileName = _fileName.substring(0,
							_fileName.indexOf('.'));
					Names.add(fileName);

				}
			}
		}

		return Names;
	}

	@Override
	public void saveProject(Project project) throws RemoteException {

		calculateLenght(project);
		try {
			writeProject(project);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Project loadProject(String projectName) throws RemoteException {
		ReadXML xml = new ReadXML(projectName);
		project = xml.read();
		
	
		return calculateProject(project);
	}

	@Override
	public void deleteProject(String projectName) throws RemoteException {
		File file = new File(getProjectDirectory()+projectName+".xml");
		System.gc();
		file.delete();
	}

	@Override
	public StringBuilder getProjectCSV(Project project) throws RemoteException {
		ExportCSV csv = new ExportCSV(calculateProject(project));
		csv.ExportFile();
		
		return csv.ExportFile();
	}
}
