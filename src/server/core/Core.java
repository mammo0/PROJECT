package server.core;

import global.ASingelton;
import global.IServerService;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.stream.XMLStreamException;

import client.view.components.PhasePaneWrapper;
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

	// the server object
	private IServerService server;
	private int serverPort;
	private String rmiUrl;
	private Project project;

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

	@Override
	public void startGui() {
		view = new View(this);
		view.showFrame();
	}

	@Override
	public void startServer() {
		if (serverPort == 0)
			return;

		// build the rmi url
		this.rmiUrl = "rmi://localhost:" + serverPort + "/PROJECT";

		try {
			// set up the server
			server = new ProjectCalculator(this);

			// start it
			LocateRegistry.createRegistry(serverPort);
			Naming.rebind(rmiUrl, server);
			System.out.println("Server läuft.");
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
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			// ignore the exception
			// e.printStackTrace();
		}

		// clean server object
		server = null;

		System.out.println("Server gestoppt.");
	}

	@Override
	public String[] getAllProjectNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Project calculateProject(Project project) {
		this.project = project;
		calculateLenght(project);
		calculateResultSkill(project);
		// calculateProjectDays(project);
		return project;
	}

	public Phase calculatePhase(Phase phase) {
		return null;
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

		for (int i = 0; i <numberofYears; i++) {
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
				deltaYears=deltaYears-1;
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
			
			
			int _skillID = 0;
			int _totalShould = 0;
			double _totalBe = 0;
			double _totalBeExt = 0;
			int _availability = 0;
			int _availabilityExt = 0;
			int _pdExt = 0;
			int _pdInt = 0;
			int _totalDif = 0;
			int _Puffer = 0;
			float _costInt = 0;
			float _costExt = 0;
			float _costTotal = 0;
			float _dayrateInt = skill.getDayRateInt();
			float _dayrateExt = skill.getDayRateExt();
			

			
			
			// calculate the total needed mandays per skill
			_skillID = skill.getSkillID();
			for (Phase phases : project.getPhases()) {
				Enumeration<Integer> enumKey = phases.getSkills().keys();
				while (enumKey.hasMoreElements()) {
					int key = enumKey.nextElement();
					if (key == _skillID) {
						_totalShould = _totalShould
								+ phases.getSkills().get(_skillID);
					}
				}

			}
			result.setPdTotalShould(_totalShould);

			// calculate the total available mandays per skill intern/extern
			for (Resource resource : project.getResources()) {
				if (resource.getSkillID() == skill.getSkillID()) {
					if (resource.isIntern()) {
						_availability = _availability
								+ resource.getAvailability();
					} else {
						_availabilityExt = _availabilityExt
								+ resource.getAvailability();
					}
				}

			}

			_totalBe = (_availability * 0.01) * calculateProjectDays(project);
			_totalBeExt = (_availabilityExt * 0.01)
					* calculateProjectDays(project);

			_totalBe = Math.round(_totalBe);
			_totalBeExt = Math.round(_totalBeExt);

			result.setPdTotalBe((int) _totalBe);
			result.setPdTotalBeExt((int) _totalBeExt);

			_availability = 0;
			_availabilityExt = 0;

			// calculate

			while (_totalShould > 0) {
				while (_totalBeExt > 0 && _totalShould > 0) {
					_pdExt = _pdExt + 1;
					_totalBeExt = _totalBeExt - 1;
					_totalShould = _totalShould - 1;
				}
				if (_totalBeExt > 0) {
					_Puffer = (int) Math.round(_totalBeExt);
				}
				if (_totalShould >= _totalBe) {
					_pdInt = (int) Math.round(_totalBe);
					_totalDif = _totalShould - (int) Math.round(_totalBe);
					_totalShould = 0;
				} else {
					_pdInt = (int) Math.round(_totalShould);
					_totalShould = 0;
				}

			}
			
			_costInt = _pdInt*_dayrateInt;
			_costExt = _pdExt*_dayrateExt;
			_costTotal = _costInt + _costExt;

			result.setPdInt(_pdInt);
			result.setPdExt(_pdExt);
			result.setPuffer(_Puffer);
			result.setPdTotalDiff(_totalDif);
			result.setCostExt(_costExt);
			result.setCostInt(_costInt);
			result.setCostTotal(_costTotal);

			skill.setResultWRisk(result);

		}

	}
	
	public void calculateResultProject(Project project){
		
		Result result = new Result();
		calculateYearsQuarters(project,result);
	}

	// calculate the total amount of projectdays
	public int calculateProjectDays(Project project) {
		int duration = 0;
		long finaldays = 0;
		double workingdays = 0;
		int years = 0;
		int startyear = 0;
		int endyear = 0;
		if (project.getEndDate().getYear() == project.getStartDate().getYear()) {
			duration = project.getEndDate().getDayOfYear()
					- project.getStartDate().getDayOfYear() + 1;
			while (duration >= 30) {
				workingdays = workingdays + 19;
				duration = duration - 30;
			}
			workingdays = workingdays + (0.55835) * duration;
		} else {
			years = project.getEndDate().getYear()
					- project.getStartDate().getYear();
			while (years > 1) {
				workingdays = workingdays + 228;
				years = years - 1;
			}

			startyear = 366 - project.getStartDate().getDayOfYear();
			while (startyear >= 30) {
				workingdays = workingdays + 19;
				startyear = startyear - 30;
			}
			workingdays = workingdays + (0.55835) * startyear;

			endyear = project.getEndDate().getDayOfYear();
			while (endyear >= 30) {
				workingdays = workingdays + 19;
				endyear = endyear - 30;
			}
			workingdays = workingdays + (0.55835) * endyear;
		}

		finaldays = Math.round(workingdays);
		return (int) finaldays;

	}

	public void writeProject(Project project) throws FileNotFoundException,
			XMLStreamException {

		createxml.setFile("" + project.getProjectName() + ".xml");
		try {
			createxml.saveConfig(project);
			createxml.startTagPhasen();
			createxml.endTagPhasen();
			createxml.closeXML();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveProject(Project project) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public Project loadProject(String projectName) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
}
