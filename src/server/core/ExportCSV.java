package server.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import model.project.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * This class implements all methods needed for the CSV-export.
 * 
 * @author Mekic
 *
 */
public class ExportCSV {

	private Project prjToEx;
	private ArrayList<Phase> phaseList;
	private int _size;

	/**
	 * Constructor
	 */
	public ExportCSV(Project prj) {
		this.prjToEx = prj;
		this.phaseList = prj.getPhases();
		_size = phaseList.size();
	}

	/**
	 * Sorts the phases by start date
	 */
	public void sortPhases() {
		Phase phaseChanger = new Phase();

		for (int i = phaseList.size(); i > 1; i--) {
			for (int j = 0; j < i - 1; j++) {
				if (phaseList.get(j).getStartDate()
						.isAfter(phaseList.get(j + 1).getStartDate())) {
					phaseChanger = phaseList.get(j + 1);
					phaseList.add(j + 1, phaseList.get(j));
					phaseList.add(j, phaseChanger);
				}
			}
		}
	}

	/**
	 * Combines all main-phases with the sub-phases
	 * 
	 * @return the phaseGroup
	 */
	public ArrayList<Phase> combinePhases() {
		ArrayList<Phase> phaseGroup = new ArrayList<>();
		phaseGroup.add(0, null);

		for (int i = 0; i < phaseList.size(); i++) {
			if (phaseList.get(i).getParent() == null) {
				if (phaseGroup.get(0) != null) {
					phaseGroup.add(phaseList.get(i));
					phaseList.remove(i);
				} else {
					continue;
				}
			} else if (phaseList.get(i).getParent().equals(phaseGroup.get(0))) {
				phaseGroup.add(phaseList.get(i));
				phaseList.remove(i);
			}
		}

		return phaseGroup;

	}

	/**
	 * Creates the CSV-file and fills it with information
	 */
	public File ExportFile() {
		sortPhases();
		String sep = ";";
		ArrayList<Phase> phaseGroup;
		Enumeration<Integer> skillIDs;
		Integer nextElement;
		String skillName = "";
		PrintWriter pWriter = null;

		try {
			pWriter = new PrintWriter(new BufferedWriter(new FileWriter(
					prjToEx.getProjectName()
							+ ".csv")));

		} catch (IOException e) {
			e.printStackTrace();
		}

		while (_size > 0) {
			--_size;
			phaseGroup = phaseList;

			try {
				for (int i = 0; i <phaseGroup.size(); i++) {
					pWriter.println(phaseGroup.get(i).getPhaseName());
					pWriter.println(phaseGroup.get(i).getStartDate() + sep
							+ "-" + sep + phaseGroup.get(i).getEndDate());

					skillIDs = phaseGroup.get(i).getSkills().keys();

					try {
						nextElement = skillIDs.nextElement();
						for (int j = 0; j < prjToEx.getSkills().size(); j++) {
							if (nextElement == prjToEx.getSkills().get(j)
									.getSkillID()) {
								skillName = prjToEx.getSkills().get(j)
										.getSkillName();

							}
						}
						pWriter.println(skillName
								+ ":"
								+ sep
								+ phaseGroup.get(i).getSkills()
										.get(nextElement).toString());
					} catch (NoSuchElementException NSEe) {
						continue;
					} catch (NullPointerException e) {
						continue;
					}

				}

			} catch (Exception ioe) {
				ioe.printStackTrace();
			}

			try {
				pWriter.println("Manntage und Kosten Intern:" + sep
						+ prjToEx.getResult().getPdInt() + sep
						+ prjToEx.getResult().getCostInt());
				pWriter.println("Manntage und Kosten Extern:" + sep
						+ prjToEx.getResult().getPdExt() + sep
						+ prjToEx.getResult().getCostExt());
				pWriter.println("Manntage und Kosten Gesamt:" + sep
						+ prjToEx.getResult().getPdTotalShould() + sep
						+ prjToEx.getResult().getCostTotal());

				if (prjToEx.getResult().getPdTotalDiff() > 0) {
					pWriter.println("Info:"
							+ sep
							+ "Um das Projektziel zu erreichen werden noch mindestens"
							+ prjToEx.getResult().getPdTotalDiff()
							+ " Manntage benötigt.");
				}

			} catch (Exception e) {
				e.printStackTrace();

			}

			finally {
				if (pWriter != null) {
					pWriter.flush();
					pWriter.close();
				}
			}

		}
		
		File file = new File(prjToEx.getProjectName()
							+ ".csv");
		return file;
	}
}
