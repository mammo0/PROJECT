package server.core;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import model.project.Phase;
import model.project.Project;
import model.project.Skill;

/**
 * This class implements all methods needed for the CSV-export.
 * 
 * @author Mekic
 *
 */
public class ExportCSV {

	private Project prjToEx;
	private ArrayList<Phase> phaseList;
	private StringBuilder builder;

	/**
	 * Constructor
	 */
	public ExportCSV(Project prj) {
		this.prjToEx = prj;
		this.phaseList = prj.getPhases();
		builder = new StringBuilder();
	}

	/**
	 * Sorts the phases by start date
	 */
	public void sortPhases() {
		phaseList.sort(new Comparator<Phase>() {
			@Override
			public int compare(Phase o1, Phase o2) {
				if(o1.getStartDate() == null || o2.getStartDate() == null)
					return -1;
				
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

//		while (_size > 0) {
//			--_size;
			phaseGroup = phaseList;
			
			builder.append("Projektname: "+prjToEx.getProjectName()+"\n");
			
			try {
				for (int i = 0; i <phaseGroup.size(); i++) {
					if(phaseGroup.get(i).getParent()==null){
						if(phaseGroup.get(i).getStartDate()!=null){
							builder.append("Projektphase: " +sep+ phaseGroup.get(i).getPhaseName()+ sep);
							builder.append(phaseGroup.get(i).getStartDate() + sep
									+ "-" + sep + phaseGroup.get(i).getEndDate()+ "\n");
						}else
							builder.append("Projektphase: " +sep+ phaseGroup.get(i).getPhaseName()+ "\n");
					}else{
//						builder.append("Projektphase: " +sep+ phaseGroup.get(i).getParent().getPhaseName()+ "\n");
						if(phaseGroup.get(i).getStartDate()!=null){
							builder.append("Subphase: "+sep+phaseGroup.get(i).getPhaseName()+ sep);
							builder.append(phaseGroup.get(i).getStartDate() + sep
									+ "-" + sep + phaseGroup.get(i).getEndDate()+ "\n");
						}else
							builder.append("Subphase: "+sep+phaseGroup.get(i).getPhaseName()+ "\n");
					}
					
					
//					pWriter.println(phaseGroup.get(i).getPhaseName());
//					pWriter.println(phaseGroup.get(i).getStartDate() + sep
//							+ "-" + sep + phaseGroup.get(i).getEndDate());

					skillIDs = phaseGroup.get(i).getSkills().keys();

					try {
						
						boolean resources = false;
						if(skillIDs.hasMoreElements()){
							builder.append("Benötigte Ressourcen: "+sep);
							resources = true;
						}
						while(skillIDs.hasMoreElements()){
							nextElement = skillIDs.nextElement();
							
							for(Skill skill : prjToEx.getSkills()){
								if(skill.getSkillID() == nextElement){
									builder.append(skill.getSkillName()+":"+sep+String.valueOf(phaseGroup.get(i).getSkills().get(nextElement)+"\n"));
									builder.append(sep);
								}
							}
						}
						if(resources){
							builder.setCharAt(builder.length()-1, '\0');
							builder.append("\n");
						}
						
//						builder.append("Benötigte Ressourcen: "+sep+skillName
//								+ ":"
//								+ sep
//								+ phaseGroup.get(i).getSkills()
//										.get(nextElement).toString()+ "PT\n");
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
						+ prjToEx.getResult().getPdInt()+" PT" + sep
						+ prjToEx.getResult().getCostInt()+ "\n");
				
//				pWriter.println("Manntage und Kosten Intern:" + sep
//						+ prjToEx.getResult().getPdInt() + sep
//						+ prjToEx.getResult().getCostInt());
//				
				builder.append("Manntage und Kosten Extern:" + sep
						+ prjToEx.getResult().getPdExt()+" PT" + sep
						+ prjToEx.getResult().getCostExt()+ "\n");
				
//				pWriter.println("Manntage und Kosten Extern:" + sep
//						+ prjToEx.getResult().getPdExt() + sep
//						+ prjToEx.getResult().getCostExt());
//				
				builder.append("Manntage und Kosten Gesamt:" + sep
						+ prjToEx.getResult().getPdTotalShould()+" PT" + sep
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
//							+ " Manntage benötigt.");
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

//		}
		return builder;
	}
}
