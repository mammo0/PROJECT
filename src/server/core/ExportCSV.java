package server.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import model.project.*;

import java.util.ArrayList;
import java.util.Comparator;
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
	private StringBuilder builder;

	/**
	 * Constructor
	 */
	public ExportCSV(Project prj) {
		this.prjToEx = prj;
		this.phaseList = prj.getPhases();
		_size = phaseList.size();
		builder = new StringBuilder();
	}

	/**
	 * Sorts the phases by start date
	 */
	public void sortPhases() {
//		Phase phaseChanger = new Phase();
//
//		for (int i = phaseList.size(); i > 1; i--) {
//			for (int j = 0; j < i - 1; j++) {
//				if (phaseList.get(j).getStartDate()
//						.isAfter(phaseList.get(j + 1).getStartDate())) {
//					phaseChanger = phaseList.get(j + 1);
//					phaseList.add(j + 1, phaseList.get(j));
//					phaseList.add(j, phaseChanger);
//				}
//			}
//		}
		phaseList.sort(new Comparator<Phase>() {
			@Override
			public int compare(Phase o1, Phase o2) {
				return o1.getStartDate().isBefore(o2.getStartDate()) ? -1 :
					o1.getStartDate().isEqual(o2.getStartDate()) ? 0 : 1;
			}
		});
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
	public StringBuilder ExportFile() {
		sortPhases();
		String sep = ";";
		ArrayList<Phase> phaseGroup;
		Enumeration<Integer> skillIDs;
		Integer nextElement;
		String skillName = "";
		PrintWriter pWriter = null;

//		try {
//			pWriter = new PrintWriter(new BufferedWriter(new FileWriter(
//					prjToEx.getProjectName()
//							+ ".csv")));

//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		while (_size > 0) {
			--_size;
			phaseGroup = phaseList;

			try {
				for (int i = 0; i <phaseGroup.size(); i++) {
					builder.append(phaseGroup.get(i).getPhaseName()+ "\n");
					builder.append(phaseGroup.get(i).getStartDate() + sep
							+ "-" + sep + phaseGroup.get(i).getEndDate()+ "\n");
					
					
//					pWriter.println(phaseGroup.get(i).getPhaseName());
//					pWriter.println(phaseGroup.get(i).getStartDate() + sep
//							+ "-" + sep + phaseGroup.get(i).getEndDate());

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
						
						builder.append(skillName
								+ ":"
								+ sep
								+ phaseGroup.get(i).getSkills()
										.get(nextElement).toString()+ "\n");
//						pWriter.println(skillName
//								+ ":"
//								+ sep
//								+ phaseGroup.get(i).getSkills()
//										.get(nextElement).toString());
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
				builder.append("Manntage und Kosten Intern:" + sep
						+ prjToEx.getResult().getPdInt() + sep
						+ prjToEx.getResult().getCostInt()+ "\n");
				
//				pWriter.println("Manntage und Kosten Intern:" + sep
//						+ prjToEx.getResult().getPdInt() + sep
//						+ prjToEx.getResult().getCostInt());
//				
				builder.append("Manntage und Kosten Extern:" + sep
						+ prjToEx.getResult().getPdExt() + sep
						+ prjToEx.getResult().getCostExt()+ "\n");
				
//				pWriter.println("Manntage und Kosten Extern:" + sep
//						+ prjToEx.getResult().getPdExt() + sep
//						+ prjToEx.getResult().getCostExt());
//				
				builder.append("Manntage und Kosten Gesamt:" + sep
						+ prjToEx.getResult().getPdTotalShould() + sep
						+ prjToEx.getResult().getCostTotal()+ "\n");
				
//				pWriter.println("Manntage und Kosten Gesamt:" + sep
//						+ prjToEx.getResult().getPdTotalShould() + sep
//						+ prjToEx.getResult().getCostTotal());
//
//				if (prjToEx.getResult().getPdTotalDiff() > 0) {
//					pWriter.println("Info:"
//							+ sep
//							+ "Um das Projektziel zu erreichen werden noch mindestens"
//							+ prjToEx.getResult().getPdTotalDiff()
//							+ " Manntage ben�tigt.");
//				}

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
		return builder;
	}
}
