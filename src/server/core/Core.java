package server.core;

import global.ASingelton;
import global.IServerService;

import java.io.File;
import java.io.FileNotFoundException;
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

	private File projectFilesDir;

	public Core() {
		this.projectPath = System.getProperty("user.dir");
	}

	@Override
	public String getProjectDirectory() {
		if (projectFilesDir == null)
			return projectPath + "/";
		else
			return projectFilesDir.getAbsolutePath() + "/";
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
		//if(project.getEndDate()!= null){
		calculateLenght(project);
		calculateResultSkill(project);
		calculateProjectDays(project);
		calculateQuarterResults(project);
//		}
//		else{
//			
//		}
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

			// set all temp variables
			int _skillID = 0;
			int _totalShould = 0;
			int _totalShouldRisk = 0;
			double _totalBe = 0;
			double _totalBeExt = 0;
			int _availability = 0;
			int _availabilityExt = 0;
			int _pdExt = 0;
			int _pdInt = 0;
			int _totalDif = 0;
			int _totalDifRisk = 0;
			int _Puffer = 0;
			float _costIntRisk = 0;
			float _costInt = 0;
			float _costExt = 0;
			float _costExtRisk = 0;
			float _costTotal = 0;
			float _dayrateInt = skill.getDayRateInt();
			float _dayrateExt = skill.getDayRateExt();
			int _pdTotalBe = 0;

			// calculate the total needed mandays per skill
			_skillID = skill.getSkillID();
			for (Phase phases : project.getPhases()) {
				Enumeration<Integer> enumKey = phases.getSkills().keys();
				while (enumKey.hasMoreElements()) {
					int tempRisk = 0;
					int key = enumKey.nextElement();
					if (key == _skillID) {
						_totalShould = _totalShould
								+ phases.getSkills().get(_skillID);
						tempRisk = (int) Math.round(phases.getRiskFactor()
								* 0.01 * phases.getSkills().get(_skillID));
						_totalShouldRisk = _totalShouldRisk
								+ (tempRisk + phases.getSkills().get(_skillID));

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

			_availability = 0;
			_availabilityExt = 0;

			// calculate the Difference between the should and be mandays
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
					_totalDifRisk = _totalShouldRisk
							- (int) Math.round(_totalBe);
					_totalShould = 0;
				} else {
					_pdInt = (int) Math.round(_totalShould);
					_totalShould = 0;
				}

			}

			// calculate costs
			_costIntRisk = _totalShouldRisk * _dayrateInt;
			_costExt = _pdExt * _dayrateExt;
			_costInt = _pdInt * _dayrateInt;
			_costExt = _pdExt * _dayrateExt;
			_costTotal = _costInt + _costExt;
			_pdTotalBe = _pdInt + _pdExt;

			// set the result variables
			result.setPdIntBe(_pdInt);
			result.setPdExtBe(_pdExt);
			result.setPdTotalBe(_pdTotalBe);
			result.setPuffer(_Puffer);
			result.setPdTotalDiff(_totalDif);
			result.setCostExt(_costExt);
			result.setCostInt(_costInt);
			result.setCostTotal(_costTotal);
			result.setPdTotalShouldRisk(_totalShouldRisk);

			skill.setResultWRisk(result);

		}

	}

	public void calculateQuarterResults(Project project) {

		for (Phase phases : project.getPhases()) {

			LocalDate startdate = phases.getStartDate();
			LocalDate enddate = phases.getEndDate();

			// Gesamte Tage der Phase gilt nur wenn alles in eniem jahr ist!!
			int _diffdate = enddate.getDayOfYear() - startdate.getDayOfYear();

			int _skillID = 0;
			int _startyear = phases.getStartDate().getYear();
			int _endyear = phases.getEndDate().getYear();
			int _startquarter = (phases.getStartDate().getMonthValue() - 1) / 3 + 1;
			int _endquarter = (phases.getEndDate().getMonthValue() - 1) / 3 + 1;
			int _numberQuarters = 0;
			float _availIntern = 0;
			float _availExtern = 0;
			int daysInQ1 = 0;
			int daysInQ2 = 0;
			int daysInQ3 = 0;
			int daysInQ4 = 0;
			int daysInYStart = 0;
			int daysInYEnd = 0;

			for (Skill skill : project.getSkills()) {
				_skillID = skill.getSkillID();
				Enumeration<Integer> enumKey = phases.getSkills().keys();
				while (enumKey.hasMoreElements()) {
					int key = enumKey.nextElement();
					if (key == _skillID) {

						if (_startyear == _endyear) {
							if (_startquarter == _endquarter) {

								if (_startquarter == 1 && _endquarter == 1) {

									for (int i = 0; i < project.getResources()
											.size(); i++) {
										if (project.getResources().get(i)
												.getSkillID() == _skillID) {
											if (project.getResources().get(i)
													.isIntern() == true) {
												_availIntern = _availIntern
														+ project
																.getResources()
																.get(i)
																.getAvailability();
											} else {
												_availExtern = _availExtern
														+ project
																.getResources()
																.get(i)
																.getAvailability();
											}
										}
									}
									// Summe der Verfügbarkeiten in Faktor
									// umrechnen, mit welchem dann zuerest die
									// externen Tage berechnet werden

									double faktor = _availIntern / _availExtern;
									faktor = faktor + 1;

									// Benötigte Tage pro Skill und Phase
									// auslesen

									int neededDaysPerSkillAndPhase = phases
											.getSkills().get(_skillID);

									// Externe Tage berechen
									float extDaysPerPhaseAndSkill = 0;
									extDaysPerPhaseAndSkill = (float) (neededDaysPerSkillAndPhase / faktor);

									// interne Tage berechnen
									float intDaysPerPhaseAndSkill = neededDaysPerSkillAndPhase
											- extDaysPerPhaseAndSkill;
									// Kosten berechnen
									float CostsPerPhaseAndSkillIntern = 0;
									float CostsPerPhaseAndSkillExtern = 0;

									CostsPerPhaseAndSkillIntern = intDaysPerPhaseAndSkill
											* skill.getDayRateInt();
									CostsPerPhaseAndSkillExtern = extDaysPerPhaseAndSkill
											* skill.getDayRateExt();
									// extDaysPerPhaseAndSkill und
									// intDaysPerPhaseAndSkill ins Result (q1)
									// schreiben und auch die kosten

								} else if (_startquarter == 2
										&& _endquarter == 2) {

									for (int i = 0; i < project.getResources()
											.size(); i++) {
										if (project.getResources().get(i)
												.getSkillID() == _skillID) {
											if (project.getResources().get(i)
													.isIntern() == true) {
												_availIntern = _availIntern
														+ project
																.getResources()
																.get(i)
																.getAvailability();
											} else {
												_availExtern = _availExtern
														+ project
																.getResources()
																.get(i)
																.getAvailability();
											}
										}
									}
									// Summe der Verfügbarkeiten in Faktor
									// umrechnen, mit welchem dann zuerest die
									// externen Tage berechnet werden

									double faktor = _availIntern / _availExtern;
									faktor = faktor + 1;

									// Benötigte Tage pro Skill und Phase
									// auslesen

									int neededDaysPerSkillAndPhase = phases
											.getSkills().get(_skillID);

									// Externe Tage berechen
									float extDaysPerPhaseAndSkill = 0;
									extDaysPerPhaseAndSkill = (float) (neededDaysPerSkillAndPhase / faktor);

									// interne Tage berechnen
									float intDaysPerPhaseAndSkill = neededDaysPerSkillAndPhase
											- extDaysPerPhaseAndSkill;
									// Kosten berechnen
									float CostsPerPhaseAndSkillIntern = 0;
									float CostsPerPhaseAndSkillExtern = 0;

									CostsPerPhaseAndSkillIntern = intDaysPerPhaseAndSkill
											* skill.getDayRateInt();
									CostsPerPhaseAndSkillExtern = extDaysPerPhaseAndSkill
											* skill.getDayRateExt();
									// extDaysPerPhaseAndSkill und
									// intDaysPerPhaseAndSkill ins Result (q2)
									// schreiben und auch die kosten

								} else if (_startquarter == 3
										&& _endquarter == 3) {

									for (int i = 0; i < project.getResources()
											.size(); i++) {
										if (project.getResources().get(i)
												.getSkillID() == _skillID) {
											if (project.getResources().get(i)
													.isIntern() == true) {
												_availIntern = _availIntern
														+ project
																.getResources()
																.get(i)
																.getAvailability();
											} else {
												_availExtern = _availExtern
														+ project
																.getResources()
																.get(i)
																.getAvailability();
											}
										}
									}
									// Summe der Verfügbarkeiten in Faktor
									// umrechnen, mit welchem dann zuerest die
									// externen Tage berechnet werden

									double faktor = _availIntern / _availExtern;
									faktor = faktor + 1;

									// Benötigte Tage pro Skill und Phase
									// auslesen

									int neededDaysPerSkillAndPhase = phases
											.getSkills().get(_skillID);

									// Externe Tage berechen
									float extDaysPerPhaseAndSkill = 0;
									extDaysPerPhaseAndSkill = (float) (neededDaysPerSkillAndPhase / faktor);

									// interne Tage berechnen
									float intDaysPerPhaseAndSkill = neededDaysPerSkillAndPhase
											- extDaysPerPhaseAndSkill;
									// Kosten berechnen
									float CostsPerPhaseAndSkillIntern = 0;
									float CostsPerPhaseAndSkillExtern = 0;

									CostsPerPhaseAndSkillIntern = intDaysPerPhaseAndSkill
											* skill.getDayRateInt();
									CostsPerPhaseAndSkillExtern = extDaysPerPhaseAndSkill
											* skill.getDayRateExt();
									// extDaysPerPhaseAndSkill und
									// intDaysPerPhaseAndSkill ins Result (q3)
									// schreiben und auch die kosten

								} else if (_startquarter == 4
										&& _endquarter == 4) {

									for (int i = 0; i < project.getResources()
											.size(); i++) {
										if (project.getResources().get(i)
												.getSkillID() == _skillID) {
											if (project.getResources().get(i)
													.isIntern() == true) {
												_availIntern = _availIntern
														+ project
																.getResources()
																.get(i)
																.getAvailability();
											} else {
												_availExtern = _availExtern
														+ project
																.getResources()
																.get(i)
																.getAvailability();
											}
										}
									}
									// Summe der Verfügbarkeiten in Faktor
									// umrechnen, mit welchem dann zuerest die
									// externen Tage berechnet werden

									double faktor = _availIntern / _availExtern;
									faktor = faktor + 1;

									// Benötigte Tage pro Skill und Phase
									// auslesen

									int neededDaysPerSkillAndPhase = phases
											.getSkills().get(_skillID);

									// Externe Tage berechen
									float extDaysPerPhaseAndSkill = 0;
									extDaysPerPhaseAndSkill = (float) (neededDaysPerSkillAndPhase / faktor);

									// interne Tage berechnen
									float intDaysPerPhaseAndSkill = neededDaysPerSkillAndPhase
											- extDaysPerPhaseAndSkill;
									// Kosten berechnen
									float CostsPerPhaseAndSkillIntern = 0;
									float CostsPerPhaseAndSkillExtern = 0;

									CostsPerPhaseAndSkillIntern = intDaysPerPhaseAndSkill
											* skill.getDayRateInt();
									CostsPerPhaseAndSkillExtern = extDaysPerPhaseAndSkill
											* skill.getDayRateExt();
									// extDaysPerPhaseAndSkill und
									// intDaysPerPhaseAndSkill ins Result (q4)
									// schreiben und auch die kosten

								}
							} else {
								// Phasen, die länger als ein Quartal dauern,
								// bzw die Quartalsgrenzen überschreiten

								// _endquarter - _startquarter = 1 besagt, dass
								// die Phase in Qn beginnt und in Qn+1 endet
								if (_endquarter - _startquarter == 1) {
									// Q1
									if (startdate.getMonthValue() == 1
											|| startdate.getMonthValue() == 2
											|| startdate.getMonthValue() == 3) {

										LocalDate enddatequarter = LocalDate
												.of(_endyear, 3, 31);
										// Tage die im ersten Quartal liegen
										daysInQ1 = enddatequarter
												.getDayOfYear()
												- startdate.getDayOfYear();
										// Restliche tage folglich im 2 quartal
										daysInQ2 = _diffdate - daysInQ1;

										for (int i = 0; i < project
												.getResources().size(); i++) {
											if (project.getResources().get(i)
													.getSkillID() == _skillID) {
												if (project.getResources()
														.get(i).isIntern() == true) {

													_availIntern = _availIntern
															+ project
																	.getResources()
																	.get(i)
																	.getAvailability();

												} else {
													_availExtern = _availExtern
															+ project
																	.getResources()
																	.get(i)
																	.getAvailability();
												}
											}
										}

										// Summe der Verfügbarkeiten in Faktor
										// umrechnen, mit welchem dann zuerest
										// die externen Tage berechnet werden

										// faktor = _availIntern/_availExtern
										double faktor = _availIntern
												/ _availExtern;
										faktor = faktor + 1;

										// Benötigte Tage pro Skill und Phase
										// auslesen

										int neededDaysPerSkillAndPhase = phases
												.getSkills().get(_skillID);

										// Externe Tage berechen
										float extDaysPerPhaseAndSkill = 0;
										extDaysPerPhaseAndSkill = (float) (neededDaysPerSkillAndPhase / faktor);

										// interne Tage berechnen
										float intDaysPerPhaseAndSkill = neededDaysPerSkillAndPhase
												- extDaysPerPhaseAndSkill;

										// Tage auf die Quartale verteilen
										// Wie viele SkillTage verteilen sich
										// auf einen DateDiff Tag
										// Also wenn die Phase Datediff 90 tage
										// dauert und man einen internen mit 30
										// tagen verplanen muss
										// dann ist an einem Datedifftag 0,333
										// des internen verbraucht
										// diese Zahl dann mal die anzahl der
										// arbeitstage im Q1 udn in Q2

										// Faktor, also wie oben beschrieben zb
										// 0,33 brechenen
										// intern
										float dayfactorintern = 0;
										dayfactorintern = intDaysPerPhaseAndSkill
												/ _diffdate;
										float internalDaysPerSkillInQ1 = 0;
										float internalDaysPerSkillInQ2 = 0;
										internalDaysPerSkillInQ1 = daysInQ1
												* dayfactorintern;
										internalDaysPerSkillInQ2 = daysInQ2
												* dayfactorintern;

										// extern

										float dayfactorextern = 0;
										dayfactorextern = intDaysPerPhaseAndSkill
												/ _diffdate;
										float externalDaysPerSkillInQ1 = 0;
										float externalDaysPerSkillInQ2 = 0;
										externalDaysPerSkillInQ1 = daysInQ1
												* dayfactorextern;
										externalDaysPerSkillInQ2 = daysInQ2
												* dayfactorextern;

										// Kosten
										float internalCostsPerSkillInQ1 = 0;
										float internalCostsPerSkillInQ2 = 0;

										float externalCostsPerSkillInQ1 = 0;
										float externalCostsPerSkillInQ2 = 0;

										internalCostsPerSkillInQ1 = internalDaysPerSkillInQ1
												* skill.getDayRateInt();
										internalCostsPerSkillInQ2 = internalDaysPerSkillInQ2
												* skill.getDayRateInt();

										externalCostsPerSkillInQ1 = externalDaysPerSkillInQ1
												* skill.getDayRateExt();
										externalCostsPerSkillInQ2 = externalDaysPerSkillInQ2
												* skill.getDayRateExt();

										// Ergebnisse müssen hier noch in
										// Quartale geschrieben
										// werden(external/internalDaysPerSkillInQ1,
										// external/internalDaysPerSkillInQ2,
										// internal/externalCostsPerSkillInQ1,
										// internal/externalCostsPerSkillInQ2

									}
									// Q2
									else if (startdate.getMonthValue() == 4
											|| startdate.getMonthValue() == 5
											|| startdate.getMonthValue() == 6) {

										LocalDate enddatequarter = LocalDate
												.of(_endyear, 6, 30);
										// Tage die im zweiten Quartal liegen
										daysInQ2 = enddatequarter
												.getDayOfYear()
												- startdate.getDayOfYear();
										// Restliche tage folglich im 3 quartal
										daysInQ3 = _diffdate - daysInQ2;

										for (int i = 0; i < project
												.getResources().size(); i++) {
											if (project.getResources().get(i)
													.getSkillID() == _skillID) {
												if (project.getResources()
														.get(i).isIntern() == true) {

													_availIntern = _availIntern
															+ project
																	.getResources()
																	.get(i)
																	.getAvailability();

												} else {
													_availExtern = _availExtern
															+ project
																	.getResources()
																	.get(i)
																	.getAvailability();
												}
											}
										}

										// Summe der Verfügbarkeiten in Faktor
										// umrechnen, mit welchem dann zuerest
										// die externen Tage berechnet werden

										// faktor = _availIntern/_availExtern
										double faktor = _availIntern
												/ _availExtern;
										faktor = faktor + 1;

										// Benötigte Tage pro Skill und Phase
										// auslesen

										int neededDaysPerSkillAndPhase = phases
												.getSkills().get(_skillID);

										// Externe Tage berechen
										float extDaysPerPhaseAndSkill = 0;
										extDaysPerPhaseAndSkill = (float) (neededDaysPerSkillAndPhase / faktor);

										// interne Tage berechnen
										float intDaysPerPhaseAndSkill = neededDaysPerSkillAndPhase
												- extDaysPerPhaseAndSkill;

										// Tage auf die Quartale verteilen
										// Wie viele SkillTage verteilen sich
										// auf einen DateDiff Tag
										// Also wenn die Phase Datediff 90 tage
										// dauert und man einen internen mit 30
										// tagen verplanen muss
										// dann ist an einem Datedifftag 0,333
										// des internen verbraucht
										// diese Zahl dann mal die anzahl der
										// arbeitstage im Q1 udn in Q2

										// Faktor, also wie oben beschrieben zb
										// 0,33 brechenen
										// intern
										float dayfactorintern = 0;
										dayfactorintern = intDaysPerPhaseAndSkill
												/ _diffdate;
										float internalDaysPerSkillInQ2 = 0;
										float internalDaysPerSkillInQ3 = 0;
										internalDaysPerSkillInQ2 = daysInQ2
												* dayfactorintern;
										internalDaysPerSkillInQ3 = daysInQ3
												* dayfactorintern;

										// extern

										float dayfactorextern = 0;
										dayfactorextern = intDaysPerPhaseAndSkill
												/ _diffdate;
										float externalDaysPerSkillInQ2 = 0;
										float externalDaysPerSkillInQ3 = 0;
										externalDaysPerSkillInQ2 = daysInQ2
												* dayfactorextern;
										externalDaysPerSkillInQ3 = daysInQ3
												* dayfactorextern;
										// Ergebnisse müssen hier noch in
										// Quartale geschrieben werden

										// Kosten
										float internalCostsPerSkillInQ3 = 0;
										float internalCostsPerSkillInQ2 = 0;

										float externalCostsPerSkillInQ3 = 0;
										float externalCostsPerSkillInQ2 = 0;

										internalCostsPerSkillInQ2 = internalDaysPerSkillInQ2
												* skill.getDayRateInt();
										internalCostsPerSkillInQ3 = internalDaysPerSkillInQ3
												* skill.getDayRateInt();

										externalCostsPerSkillInQ2 = externalDaysPerSkillInQ2
												* skill.getDayRateExt();
										externalCostsPerSkillInQ3 = externalDaysPerSkillInQ3
												* skill.getDayRateExt();

									}
									// Q3
									else if (startdate.getMonthValue() == 7
											|| startdate.getMonthValue() == 8
											|| startdate.getMonthValue() == 9) {

										LocalDate enddatequarter = LocalDate
												.of(_endyear, 9, 30);
										// Tage die im dritten Quartal liegen
										daysInQ3 = enddatequarter
												.getDayOfYear()
												- startdate.getDayOfYear();
										// Restliche tage folglich im 4 quartal
										daysInQ4 = _diffdate - daysInQ3;

										for (int i = 0; i < project
												.getResources().size(); i++) {
											if (project.getResources().get(i)
													.getSkillID() == _skillID) {
												if (project.getResources()
														.get(i).isIntern() == true) {

													_availIntern = _availIntern
															+ project
																	.getResources()
																	.get(i)
																	.getAvailability();

												} else {
													_availExtern = _availExtern
															+ project
																	.getResources()
																	.get(i)
																	.getAvailability();
												}
											}
										}

										// Summe der Verfügbarkeiten in Faktor
										// umrechnen, mit welchem dann zuerest
										// die externen Tage berechnet werden

										// faktor = _availIntern/_availExtern
										double faktor = _availIntern
												/ _availExtern;
										faktor = faktor + 1;

										// Benötigte Tage pro Skill und Phase
										// auslesen

										int neededDaysPerSkillAndPhase = phases
												.getSkills().get(_skillID);

										// Externe Tage berechen
										float extDaysPerPhaseAndSkill = 0;
										extDaysPerPhaseAndSkill = (float) (neededDaysPerSkillAndPhase / faktor);

										// interne Tage berechnen
										float intDaysPerPhaseAndSkill = neededDaysPerSkillAndPhase
												- extDaysPerPhaseAndSkill;

										// Tage auf die Quartale verteilen
										// Wie viele SkillTage verteilen sich
										// auf einen DateDiff Tag
										// Also wenn die Phase Datediff 90 tage
										// dauert und man einen internen mit 30
										// tagen verplanen muss
										// dann ist an einem Datedifftag 0,333
										// des internen verbraucht
										// diese Zahl dann mal die anzahl der
										// arbeitstage im Q1 udn in Q2

										// Faktor, also wie oben beschrieben zb
										// 0,33 brechenen
										// intern
										float dayfactorintern = 0;
										dayfactorintern = intDaysPerPhaseAndSkill
												/ _diffdate;
										float internalDaysPerSkillInQ3 = 0;
										float internalDaysPerSkillInQ4 = 0;
										internalDaysPerSkillInQ3 = daysInQ3
												* dayfactorintern;
										internalDaysPerSkillInQ4 = daysInQ4
												* dayfactorintern;

										// extern

										float dayfactorextern = 0;
										dayfactorextern = intDaysPerPhaseAndSkill
												/ _diffdate;
										float externalDaysPerSkillInQ3 = 0;
										float externalDaysPerSkillInQ4 = 0;
										externalDaysPerSkillInQ3 = daysInQ3
												* dayfactorextern;
										externalDaysPerSkillInQ4 = daysInQ4
												* dayfactorextern;
										// Ergebnisse müssen hier noch in
										// Quartale geschrieben werden

										// Kosten
										float internalCostsPerSkillInQ3 = 0;
										float internalCostsPerSkillInQ4 = 0;

										float externalCostsPerSkillInQ3 = 0;
										float externalCostsPerSkillInQ4 = 0;

										internalCostsPerSkillInQ3 = internalDaysPerSkillInQ3
												* skill.getDayRateInt();
										internalCostsPerSkillInQ4 = internalDaysPerSkillInQ4
												* skill.getDayRateInt();

										externalCostsPerSkillInQ3 = externalDaysPerSkillInQ3
												* skill.getDayRateExt();
										externalCostsPerSkillInQ4 = externalDaysPerSkillInQ4
												* skill.getDayRateExt();

									}

								}
								// Wenn ein gesamtes Quartal dabei ist
								else if (_endquarter - _startquarter == 2) {
									// Q1
									if (startdate.getMonthValue() == 1
											|| startdate.getMonthValue() == 2
											|| startdate.getMonthValue() == 3) {

										LocalDate enddatequarter = LocalDate
												.of(_endyear, 3, 31);
										// Tage die im ersten Quartal liegen
										daysInQ1 = enddatequarter
												.getDayOfYear()
												- startdate.getDayOfYear();
										// Tage im gesamten 2. Quartal
										daysInQ2 = 91;
										// Tage im 3 Quartal
										daysInQ3 = _diffdate - daysInQ1
												- daysInQ2;

										for (int i = 0; i < project
												.getResources().size(); i++) {
											if (project.getResources().get(i)
													.getSkillID() == _skillID) {
												if (project.getResources()
														.get(i).isIntern() == true) {

													_availIntern = _availIntern
															+ project
																	.getResources()
																	.get(i)
																	.getAvailability();

												} else {
													_availExtern = _availExtern
															+ project
																	.getResources()
																	.get(i)
																	.getAvailability();
												}
											}
										}

										// Summe der Verfügbarkeiten in Faktor
										// umrechnen, mit welchem dann zuerest
										// die externen Tage berechnet werden

										// faktor = _availIntern/_availExtern
										double faktor = _availIntern
												/ _availExtern;
										faktor = faktor + 1;

										// Benötigte Tage pro Skill und Phase
										// auslesen

										int neededDaysPerSkillAndPhase = phases
												.getSkills().get(_skillID);

										// Externe Tage berechen
										float extDaysPerPhaseAndSkill = 0;
										extDaysPerPhaseAndSkill = (float) (neededDaysPerSkillAndPhase / faktor);

										// interne Tage berechnen
										float intDaysPerPhaseAndSkill = neededDaysPerSkillAndPhase
												- extDaysPerPhaseAndSkill;

										// Tage auf die Quartale verteilen
										// Wie viele SkillTage verteilen sich
										// auf einen DateDiff Tag
										// Also wenn die Phase Datediff 90 tage
										// dauert und man einen internen mit 30
										// tagen verplanen muss
										// dann ist an einem Datedifftag 0,333
										// des internen verbraucht
										// diese Zahl dann mal die anzahl der
										// arbeitstage im Q1 udn in Q2

										// Faktor, also wie oben beschrieben zb
										// 0,33 brechenen
										// intern
										float dayfactorintern = 0;
										dayfactorintern = intDaysPerPhaseAndSkill
												/ _diffdate;
										float internalDaysPerSkillInQ1 = 0;
										float internalDaysPerSkillInQ2 = 0;
										float internalDaysPerSkillInQ3 = 0;
										internalDaysPerSkillInQ1 = daysInQ1
												* dayfactorintern;
										internalDaysPerSkillInQ2 = daysInQ2
												* dayfactorintern;
										internalDaysPerSkillInQ3 = daysInQ3
												* dayfactorintern;

										// extern

										float dayfactorextern = 0;
										dayfactorextern = intDaysPerPhaseAndSkill
												/ _diffdate;
										float externalDaysPerSkillInQ1 = 0;
										float externalDaysPerSkillInQ2 = 0;
										float externalDaysPerSkillInQ3 = 0;
										externalDaysPerSkillInQ1 = daysInQ1
												* dayfactorextern;
										externalDaysPerSkillInQ2 = daysInQ2
												* dayfactorextern;
										externalDaysPerSkillInQ3 = daysInQ3
												* dayfactorextern;

										// Kosten
										float internalCostsPerSkillInQ1 = 0;
										float internalCostsPerSkillInQ2 = 0;
										float internalCostsPerSkillInQ3 = 0;

										float externalCostsPerSkillInQ1 = 0;
										float externalCostsPerSkillInQ2 = 0;
										float externalCostsPerSkillInQ3 = 0;

										internalCostsPerSkillInQ1 = internalDaysPerSkillInQ1
												* skill.getDayRateInt();
										internalCostsPerSkillInQ2 = internalDaysPerSkillInQ2
												* skill.getDayRateInt();
										internalCostsPerSkillInQ3 = internalDaysPerSkillInQ3
												* skill.getDayRateInt();

										externalCostsPerSkillInQ1 = externalDaysPerSkillInQ1
												* skill.getDayRateExt();
										externalCostsPerSkillInQ2 = externalDaysPerSkillInQ2
												* skill.getDayRateExt();
										externalCostsPerSkillInQ3 = externalDaysPerSkillInQ3
												* skill.getDayRateExt();

										// Ergebnisse müssen hier noch in
										// Quartale geschrieben
										// werden(external/internalDaysPerSkillInQ1,Q2,Q3
										// internal/externalCostsPerSkillInQ1,
										// Q2, Q3

									} else if (startdate.getMonthValue() == 4
											|| startdate.getMonthValue() == 5
											|| startdate.getMonthValue() == 6) {

										LocalDate enddatequarter = LocalDate
												.of(_endyear, 6, 30);
										// Tage die im ersten Quartal liegen
										daysInQ2 = enddatequarter
												.getDayOfYear()
												- startdate.getDayOfYear();
										// Tage im gesamten 2. Quartal
										daysInQ3 = 92;
										// Tage im 3 Quartal
										daysInQ4 = _diffdate - daysInQ2
												- daysInQ3;

										for (int i = 0; i < project
												.getResources().size(); i++) {
											if (project.getResources().get(i)
													.getSkillID() == _skillID) {
												if (project.getResources()
														.get(i).isIntern() == true) {

													_availIntern = _availIntern
															+ project
																	.getResources()
																	.get(i)
																	.getAvailability();

												} else {
													_availExtern = _availExtern
															+ project
																	.getResources()
																	.get(i)
																	.getAvailability();
												}
											}
										}

										// Summe der Verfügbarkeiten in Faktor
										// umrechnen, mit welchem dann zuerest
										// die externen Tage berechnet werden

										// faktor = _availIntern/_availExtern
										double faktor = _availIntern
												/ _availExtern;
										faktor = faktor + 1;

										// Benötigte Tage pro Skill und Phase
										// auslesen

										int neededDaysPerSkillAndPhase = phases
												.getSkills().get(_skillID);

										// Externe Tage berechen
										float extDaysPerPhaseAndSkill = 0;
										extDaysPerPhaseAndSkill = (float) (neededDaysPerSkillAndPhase / faktor);

										// interne Tage berechnen
										float intDaysPerPhaseAndSkill = neededDaysPerSkillAndPhase
												- extDaysPerPhaseAndSkill;

										// Tage auf die Quartale verteilen
										// Wie viele SkillTage verteilen sich
										// auf einen DateDiff Tag
										// Also wenn die Phase Datediff 90 tage
										// dauert und man einen internen mit 30
										// tagen verplanen muss
										// dann ist an einem Datedifftag 0,333
										// des internen verbraucht
										// diese Zahl dann mal die anzahl der
										// arbeitstage im Q1 udn in Q2

										// Faktor, also wie oben beschrieben zb
										// 0,33 brechenen
										// intern
										float dayfactorintern = 0;
										dayfactorintern = intDaysPerPhaseAndSkill
												/ _diffdate;
										float internalDaysPerSkillInQ2 = 0;
										float internalDaysPerSkillInQ3 = 0;
										float internalDaysPerSkillInQ4 = 0;
										internalDaysPerSkillInQ2 = daysInQ2
												* dayfactorintern;
										internalDaysPerSkillInQ3 = daysInQ3
												* dayfactorintern;
										internalDaysPerSkillInQ4 = daysInQ4
												* dayfactorintern;

										// extern

										float dayfactorextern = 0;
										dayfactorextern = intDaysPerPhaseAndSkill
												/ _diffdate;
										float externalDaysPerSkillInQ2 = 0;
										float externalDaysPerSkillInQ3 = 0;
										float externalDaysPerSkillInQ4 = 0;
										externalDaysPerSkillInQ2 = daysInQ2
												* dayfactorextern;
										externalDaysPerSkillInQ3 = daysInQ3
												* dayfactorextern;
										externalDaysPerSkillInQ4 = daysInQ4
												* dayfactorextern;

										// Kosten
										float internalCostsPerSkillInQ2 = 0;
										float internalCostsPerSkillInQ3 = 0;
										float internalCostsPerSkillInQ4 = 0;

										float externalCostsPerSkillInQ2 = 0;
										float externalCostsPerSkillInQ3 = 0;
										float externalCostsPerSkillInQ4 = 0;

										internalCostsPerSkillInQ2 = internalDaysPerSkillInQ2
												* skill.getDayRateInt();
										internalCostsPerSkillInQ3 = internalDaysPerSkillInQ3
												* skill.getDayRateInt();
										internalCostsPerSkillInQ4 = internalDaysPerSkillInQ4
												* skill.getDayRateInt();

										externalCostsPerSkillInQ2 = externalDaysPerSkillInQ2
												* skill.getDayRateExt();
										externalCostsPerSkillInQ3 = externalDaysPerSkillInQ3
												* skill.getDayRateExt();
										externalCostsPerSkillInQ4 = externalDaysPerSkillInQ4
												* skill.getDayRateExt();

										// Ergebnisse müssen hier noch in
										// Quartale geschrieben
										// werden(external/internalDaysPerSkillInQ2,Q3,Q4
										// internal/externalCostsPerSkillInQ2,
										// Q3, Q4

									}

								} else if (_endquarter - _startquarter == 3) {

									LocalDate enddatequarter = LocalDate.of(
											_endyear, 3, 31);
									// Tage die im ersten Quartal liegen
									daysInQ1 = enddatequarter.getDayOfYear()
											- startdate.getDayOfYear();
									// Tage im gesamten 2. Quartal
									daysInQ2 = 91;
									// Tage im 3 Quartal
									daysInQ3 = 92;
									// Tage im 4 Quartale
									daysInQ4 = _diffdate - daysInQ1 - daysInQ2
											- daysInQ3;

									for (int i = 0; i < project.getResources()
											.size(); i++) {
										if (project.getResources().get(i)
												.getSkillID() == _skillID) {
											if (project.getResources().get(i)
													.isIntern() == true) {

												_availIntern = _availIntern
														+ project
																.getResources()
																.get(i)
																.getAvailability();

											} else {
												_availExtern = _availExtern
														+ project
																.getResources()
																.get(i)
																.getAvailability();
											}
										}
									}

									// Summe der Verfügbarkeiten in Faktor
									// umrechnen, mit welchem dann zuerest die
									// externen Tage berechnet werden

									// faktor = _availIntern/_availExtern
									double faktor = _availIntern / _availExtern;
									faktor = faktor + 1;

									// Benötigte Tage pro Skill und Phase
									// auslesen

									int neededDaysPerSkillAndPhase = phases
											.getSkills().get(_skillID);

									// Externe Tage berechen
									float extDaysPerPhaseAndSkill = 0;
									extDaysPerPhaseAndSkill = (float) (neededDaysPerSkillAndPhase / faktor);

									// interne Tage berechnen
									float intDaysPerPhaseAndSkill = neededDaysPerSkillAndPhase
											- extDaysPerPhaseAndSkill;

									// Tage auf die Quartale verteilen
									// Wie viele SkillTage verteilen sich auf
									// einen DateDiff Tag
									// Also wenn die Phase Datediff 90 tage
									// dauert und man einen internen mit 30
									// tagen verplanen muss
									// dann ist an einem Datedifftag 0,333 des
									// internen verbraucht
									// diese Zahl dann mal die anzahl der
									// arbeitstage im Q1 udn in Q2

									// Faktor, also wie oben beschrieben zb 0,33
									// brechenen
									// intern
									float dayfactorintern = 0;
									dayfactorintern = intDaysPerPhaseAndSkill
											/ _diffdate;
									float internalDaysPerSkillInQ1 = 0;
									float internalDaysPerSkillInQ2 = 0;
									float internalDaysPerSkillInQ3 = 0;
									float internalDaysPerSkillInQ4 = 0;
									internalDaysPerSkillInQ1 = daysInQ1
											* dayfactorintern;
									internalDaysPerSkillInQ2 = daysInQ2
											* dayfactorintern;
									internalDaysPerSkillInQ3 = daysInQ3
											* dayfactorintern;
									internalDaysPerSkillInQ4 = daysInQ4
											* dayfactorintern;

									// extern

									float dayfactorextern = 0;
									dayfactorextern = intDaysPerPhaseAndSkill
											/ _diffdate;
									float externalDaysPerSkillInQ1 = 0;
									float externalDaysPerSkillInQ2 = 0;
									float externalDaysPerSkillInQ3 = 0;
									float externalDaysPerSkillInQ4 = 0;
									externalDaysPerSkillInQ1 = daysInQ1
											* dayfactorextern;
									externalDaysPerSkillInQ2 = daysInQ2
											* dayfactorextern;
									externalDaysPerSkillInQ3 = daysInQ3
											* dayfactorextern;
									externalDaysPerSkillInQ4 = daysInQ4
											* dayfactorextern;

									// Kosten
									float internalCostsPerSkillInQ1 = 0;
									float internalCostsPerSkillInQ2 = 0;
									float internalCostsPerSkillInQ3 = 0;
									float internalCostsPerSkillInQ4 = 0;

									float externalCostsPerSkillInQ1 = 0;
									float externalCostsPerSkillInQ2 = 0;
									float externalCostsPerSkillInQ3 = 0;
									float externalCostsPerSkillInQ4 = 0;

									internalCostsPerSkillInQ1 = internalDaysPerSkillInQ1
											* skill.getDayRateInt();
									internalCostsPerSkillInQ2 = internalDaysPerSkillInQ2
											* skill.getDayRateInt();
									internalCostsPerSkillInQ3 = internalDaysPerSkillInQ3
											* skill.getDayRateInt();
									internalCostsPerSkillInQ4 = internalDaysPerSkillInQ4
											* skill.getDayRateInt();

									externalCostsPerSkillInQ1 = externalDaysPerSkillInQ1
											* skill.getDayRateExt();
									externalCostsPerSkillInQ2 = externalDaysPerSkillInQ2
											* skill.getDayRateExt();
									externalCostsPerSkillInQ3 = externalDaysPerSkillInQ3
											* skill.getDayRateExt();
									externalCostsPerSkillInQ4 = externalDaysPerSkillInQ4
											* skill.getDayRateExt();

									// Ergebnisse müssen hier noch in Quartale
									// geschrieben
									// werden(external/internalDaysPerSkillInQ1,Q2,Q3,Q4
									// internal/externalCostsPerSkillInQ1, Q2,
									// Q3, Q4

								}

							}

						}
						// Abschluss der if Abfrage ob es sich um das gleiche
						// jahr handelt
						else if (enddate.getYear() - startdate.getYear() == 1) {

							LocalDate enddateyear = LocalDate.of(_endyear, 12,
									31);

							daysInYStart = 365 - startdate.getDayOfYear();
							daysInYEnd = enddate.getDayOfYear();

							for (int i = 0; i < project.getResources().size(); i++) {
								if (project.getResources().get(i).getSkillID() == _skillID) {
									if (project.getResources().get(i)
											.isIntern() == true) {

										_availIntern = _availIntern
												+ project.getResources().get(i)
														.getAvailability();

									} else {
										_availExtern = _availExtern
												+ project.getResources().get(i)
														.getAvailability();
									}
								}
							}

							// Benötigte Tage pro Skill und Phase auslesen

							int neededDaysPerSkillAndPhase = phases.getSkills()
									.get(_skillID);

							// Summe der Verfügbarkeiten in Faktor umrechnen,
							// mit welchem dann zuerest die externen Tage
							// berechnet werden

							// faktor = _availIntern/_availExtern
							double faktor = _availIntern / _availExtern;
							faktor = faktor + 1;

							// Externe Tage berechen
							float extDaysPerPhaseAndSkill = 0;
							extDaysPerPhaseAndSkill = (float) (neededDaysPerSkillAndPhase / faktor);

							// interne Tage berechnen
							float intDaysPerPhaseAndSkill = neededDaysPerSkillAndPhase
									- extDaysPerPhaseAndSkill;
							float dayfactorintern = 0;
							dayfactorintern = intDaysPerPhaseAndSkill
									/ _diffdate;

							float dayfactorextern = 0;
							dayfactorextern = intDaysPerPhaseAndSkill
									/ _diffdate;

							if (daysInYStart <= 92) {
								// Dann nur Q4
								daysInQ4 = daysInYStart;
								// Intern

								float internalDaysPerSkillInQ4 = 0;
								internalDaysPerSkillInQ4 = daysInQ4
										* dayfactorintern;
								// Extern

								float externalDaysPerSkillInQ4 = 0;
								externalDaysPerSkillInQ4 = daysInQ4
										* dayfactorextern;

								// Kosten berechnen
								float CostsPerPhaseAndSkillIntern = 0;
								float CostsPerPhaseAndSkillExtern = 0;

								CostsPerPhaseAndSkillIntern = intDaysPerPhaseAndSkill
										* skill.getDayRateInt();
								CostsPerPhaseAndSkillExtern = extDaysPerPhaseAndSkill
										* skill.getDayRateExt();
								// extDaysPerPhaseAndSkill und
								// intDaysPerPhaseAndSkill ins Result (q4)
								// schreiben und auch die kosten

							} else if (daysInYStart <= 184) {
								// Q4 und Q3
								daysInQ4 = 92;
								daysInQ3 = daysInYStart - daysInQ4;

								// Tage

								float internalDaysPerSkillInQ3 = 0;
								float internalDaysPerSkillInQ4 = 0;
								internalDaysPerSkillInQ3 = daysInQ3
										* dayfactorintern;
								internalDaysPerSkillInQ4 = daysInQ4
										* dayfactorintern;

								float externalDaysPerSkillInQ3 = 0;
								float externalDaysPerSkillInQ4 = 0;
								externalDaysPerSkillInQ3 = daysInQ3
										* dayfactorextern;
								externalDaysPerSkillInQ4 = daysInQ4
										* dayfactorextern;

								// Kosten

								float internalCostsPerSkillInQ3 = 0;
								float internalCostsPerSkillInQ4 = 0;
								float externalCostsPerSkillInQ3 = 0;
								float externalCostsPerSkillInQ4 = 0;

								internalCostsPerSkillInQ3 = internalDaysPerSkillInQ3
										* skill.getDayRateInt();
								internalCostsPerSkillInQ4 = internalDaysPerSkillInQ4
										* skill.getDayRateInt();
								externalCostsPerSkillInQ3 = externalDaysPerSkillInQ3
										* skill.getDayRateExt();
								externalCostsPerSkillInQ4 = externalDaysPerSkillInQ4
										* skill.getDayRateExt();

							} else if (daysInYStart <= 275) {
								// Q4, Q3, Q2

								daysInQ4 = 92;
								daysInQ3 = 92;
								daysInQ2 = daysInYStart - daysInQ4 - daysInQ3;

								// Tage

								float internalDaysPerSkillInQ2 = 0;
								float internalDaysPerSkillInQ3 = 0;
								float internalDaysPerSkillInQ4 = 0;

								internalDaysPerSkillInQ2 = daysInQ2
										* dayfactorintern;
								internalDaysPerSkillInQ3 = daysInQ3
										* dayfactorintern;
								internalDaysPerSkillInQ4 = daysInQ4
										* dayfactorintern;

								float externalDaysPerSkillInQ2 = 0;
								float externalDaysPerSkillInQ3 = 0;
								float externalDaysPerSkillInQ4 = 0;

								externalDaysPerSkillInQ2 = daysInQ2
										* dayfactorextern;
								externalDaysPerSkillInQ3 = daysInQ3
										* dayfactorextern;
								externalDaysPerSkillInQ4 = daysInQ4
										* dayfactorextern;

								// Kosten

								float internalCostsPerSkillInQ2 = 0;
								float internalCostsPerSkillInQ3 = 0;
								float internalCostsPerSkillInQ4 = 0;

								float externalCostsPerSkillInQ2 = 0;
								float externalCostsPerSkillInQ3 = 0;
								float externalCostsPerSkillInQ4 = 0;

								internalCostsPerSkillInQ2 = internalDaysPerSkillInQ2
										* skill.getDayRateInt();
								internalCostsPerSkillInQ3 = internalDaysPerSkillInQ3
										* skill.getDayRateInt();
								internalCostsPerSkillInQ4 = internalDaysPerSkillInQ4
										* skill.getDayRateInt();

								externalCostsPerSkillInQ2 = externalDaysPerSkillInQ2
										* skill.getDayRateExt();
								externalCostsPerSkillInQ3 = externalDaysPerSkillInQ3
										* skill.getDayRateExt();
								externalCostsPerSkillInQ4 = externalDaysPerSkillInQ4
										* skill.getDayRateExt();

							} else if (daysInYStart <= 366) {
								// Q4, Q3, Q2, Q1
								daysInQ4 = 92;
								daysInQ3 = 92;
								daysInQ2 = 91;
								daysInQ1 = daysInYStart - daysInQ4 - daysInQ3
										- daysInQ2;

								// Tage
								float internalDaysPerSkillInQ1 = 0;
								float internalDaysPerSkillInQ2 = 0;
								float internalDaysPerSkillInQ3 = 0;
								float internalDaysPerSkillInQ4 = 0;
								internalDaysPerSkillInQ1 = daysInQ1
										* dayfactorintern;
								internalDaysPerSkillInQ2 = daysInQ2
										* dayfactorintern;
								internalDaysPerSkillInQ3 = daysInQ3
										* dayfactorintern;
								internalDaysPerSkillInQ4 = daysInQ4
										* dayfactorintern;

								float externalDaysPerSkillInQ1 = 0;
								float externalDaysPerSkillInQ2 = 0;
								float externalDaysPerSkillInQ3 = 0;
								float externalDaysPerSkillInQ4 = 0;
								externalDaysPerSkillInQ1 = daysInQ1
										* dayfactorextern;
								externalDaysPerSkillInQ2 = daysInQ2
										* dayfactorextern;
								externalDaysPerSkillInQ3 = daysInQ3
										* dayfactorextern;
								externalDaysPerSkillInQ4 = daysInQ4
										* dayfactorextern;

								// Kosten
								float internalCostsPerSkillInQ1 = 0;
								float internalCostsPerSkillInQ2 = 0;
								float internalCostsPerSkillInQ3 = 0;
								float internalCostsPerSkillInQ4 = 0;

								float externalCostsPerSkillInQ1 = 0;
								float externalCostsPerSkillInQ2 = 0;
								float externalCostsPerSkillInQ3 = 0;
								float externalCostsPerSkillInQ4 = 0;

								internalCostsPerSkillInQ1 = internalDaysPerSkillInQ1
										* skill.getDayRateInt();
								internalCostsPerSkillInQ2 = internalDaysPerSkillInQ2
										* skill.getDayRateInt();
								internalCostsPerSkillInQ3 = internalDaysPerSkillInQ3
										* skill.getDayRateInt();
								internalCostsPerSkillInQ4 = internalDaysPerSkillInQ4
										* skill.getDayRateInt();

								externalCostsPerSkillInQ1 = externalDaysPerSkillInQ1
										* skill.getDayRateExt();
								externalCostsPerSkillInQ2 = externalDaysPerSkillInQ2
										* skill.getDayRateExt();
								externalCostsPerSkillInQ3 = externalDaysPerSkillInQ3
										* skill.getDayRateExt();
								externalCostsPerSkillInQ4 = externalDaysPerSkillInQ4
										* skill.getDayRateExt();
							}

							if (daysInYEnd <= 90) {
								// Nur Q1
								daysInQ1 = daysInYEnd;

								float internalDaysPerSkillInQ1 = 0;
								internalDaysPerSkillInQ1 = daysInQ1
										* dayfactorintern;
								// Extern

								float externalDaysPerSkillInQ1 = 0;
								externalDaysPerSkillInQ1 = daysInQ1
										* dayfactorextern;

								// Kosten berechnen
								float CostsPerPhaseAndSkillIntern = 0;
								float CostsPerPhaseAndSkillExtern = 0;

								CostsPerPhaseAndSkillIntern = intDaysPerPhaseAndSkill
										* skill.getDayRateInt();
								CostsPerPhaseAndSkillExtern = extDaysPerPhaseAndSkill
										* skill.getDayRateExt();

							} else if (daysInYEnd <= 181) {
								// Q1, Q2
								daysInQ1 = 90;
								daysInQ2 = daysInYEnd - daysInQ1;

								float internalDaysPerSkillInQ1 = 0;
								float internalDaysPerSkillInQ2 = 0;
								internalDaysPerSkillInQ1 = daysInQ1
										* dayfactorintern;
								internalDaysPerSkillInQ2 = daysInQ2
										* dayfactorintern;

								float externalDaysPerSkillInQ1 = 0;
								float externalDaysPerSkillInQ2 = 0;
								externalDaysPerSkillInQ1 = daysInQ1
										* dayfactorextern;
								externalDaysPerSkillInQ2 = daysInQ2
										* dayfactorextern;

								// Kosten

								float internalCostsPerSkillInQ1 = 0;
								float internalCostsPerSkillInQ2 = 0;
								float externalCostsPerSkillInQ1 = 0;
								float externalCostsPerSkillInQ2 = 0;

								internalCostsPerSkillInQ1 = internalDaysPerSkillInQ1
										* skill.getDayRateInt();
								internalCostsPerSkillInQ2 = internalDaysPerSkillInQ2
										* skill.getDayRateInt();
								externalCostsPerSkillInQ1 = externalDaysPerSkillInQ1
										* skill.getDayRateExt();
								externalCostsPerSkillInQ2 = externalDaysPerSkillInQ2
										* skill.getDayRateExt();
							}

							else if (daysInYEnd <= 273) {
								// Q1, Q2, Q3
								daysInQ1 = 90;
								daysInQ2 = 91;
								daysInQ3 = daysInYEnd - daysInQ1 - daysInQ2;

								float internalDaysPerSkillInQ1 = 0;
								float internalDaysPerSkillInQ2 = 0;
								float internalDaysPerSkillInQ3 = 0;
								internalDaysPerSkillInQ1 = daysInQ1
										* dayfactorintern;
								internalDaysPerSkillInQ2 = daysInQ2
										* dayfactorintern;
								internalDaysPerSkillInQ3 = daysInQ3
										* dayfactorintern;

								float externalDaysPerSkillInQ1 = 0;
								float externalDaysPerSkillInQ2 = 0;
								float externalDaysPerSkillInQ3 = 0;
								externalDaysPerSkillInQ1 = daysInQ1
										* dayfactorextern;
								externalDaysPerSkillInQ2 = daysInQ2
										* dayfactorextern;
								externalDaysPerSkillInQ3 = daysInQ3
										* dayfactorextern;

								// Kosten

								float internalCostsPerSkillInQ1 = 0;
								float internalCostsPerSkillInQ2 = 0;
								float externalCostsPerSkillInQ1 = 0;
								float externalCostsPerSkillInQ2 = 0;

								internalCostsPerSkillInQ1 = internalDaysPerSkillInQ1
										* skill.getDayRateInt();
								internalCostsPerSkillInQ2 = internalDaysPerSkillInQ2
										* skill.getDayRateInt();
								externalCostsPerSkillInQ1 = externalDaysPerSkillInQ1
										* skill.getDayRateExt();
								externalCostsPerSkillInQ2 = externalDaysPerSkillInQ2
										* skill.getDayRateExt();

							}

							else {
								// Q1, Q2, Q3, Q4
								daysInQ1 = 90;
								daysInQ2 = 91;
								daysInQ3 = 92;
								daysInQ4 = daysInYEnd - daysInQ1 - daysInQ2
										- daysInQ3;

							}
						}

					}

				}
			}
		}

	}

	public void calculateResultProject(Project project) {

		Result result = new Result();
		calculateYearsQuarters(project, result);
	}

	// calculate the total amount of projectdays
	public int calculateProjectDays(Project project) {
		int duration = 0;
		long finaldays = 0;
		double workingdays = 0;
		int years = 0;
		int startyear = 0;
		int endyear = 0;
//		if(project.getEndDate() != null){
		if (project.getEndDate().getYear() == project.getStartDate().getYear()) {
			duration = project.getEndDate().getDayOfYear()
					- project.getStartDate().getDayOfYear() + 1;
			while (duration >= 30) {
				workingdays = workingdays + 17;
				duration = duration - 30;
			}
			workingdays = workingdays + (0.55835) * duration;
		} else {
			years = project.getEndDate().getYear()
					- project.getStartDate().getYear();
			while (years > 1) {
				workingdays = workingdays + 204;
				years = years - 1;
			}

			startyear = 366 - project.getStartDate().getDayOfYear();
			while (startyear >= 30) {
				workingdays = workingdays + 17;
				startyear = startyear - 30;
			}
			workingdays = workingdays + (0.55835) * startyear;

			endyear = project.getEndDate().getDayOfYear();
			while (endyear >= 30) {
				workingdays = workingdays + 17;
				endyear = endyear - 30;
			}
			
			
			
		}
		workingdays = workingdays + (0.55835) * endyear;
		finaldays = Math.round(workingdays);
		return (int) finaldays;
	}
//		else{
//			
//		}


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
		project = new Project();
		project = xml.read();
		calculateProject(project);
		return project;
	}

	@Override
	public void deleteProject(String projectName) throws RemoteException {
		File file = new File(getProjectDirectory()+projectName+".xml");
		System.gc();
		file.delete();

	}
}
