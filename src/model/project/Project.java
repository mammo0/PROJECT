package model.project;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents the project.
 * All inputs of the project are saved in this class and subclasses.
 * @author Ammon
 *
 */
public class Project {
	// the name of the project
	private String projectName;
	
	// a short description of the project
	private String description;
	
	// a list of the containing phases
	private ArrayList<Phase> phases;
	
	// a list of the containing skills
	private ArrayList<Skill> skills;
	
	// a list of the containing resources
	private ArrayList<Resource> resources;
	
	// the project starting date
	private Date startDate;
	
	// the project end Date
	private Date endDate;
	
	// the result of the project calculation including the risk factor
	private Result resultRisk;
	
	// the result of the project calculation without the risk factor
	private Result resultWRisk;

	

	/**
	 * Get the project name
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	
	/**
	 * Get the project description
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get all phases of the project
	 * @return the phases
	 */
	public ArrayList<Phase> getPhases() {
		return phases;
	}

	/**
	 * Get all skills of the project
	 * @return the skills
	 */
	public ArrayList<Skill> getSkills() {
		return skills;
	}

	/**
	 * Get all resources of the project
	 * @return the resources
	 */
	public ArrayList<Resource> getResources() {
		return resources;
	}

	/**
	 * Get the starting date of the project
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Get the ending date of the project
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	/**
	 * Get the calculated result without the risk factor
	 * @return the resultWRisk
	 */
	public Result getResultWRisk() {
		return resultWRisk;
	}

	/**
	 * Get the calculated result including the risk factor
	 * @return the resultRisk
	 */
	public Result getResultRisk() {
		return resultRisk;
	}

	/**
	 * Set the project name
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	/**
	 * Set the project description
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Set the starting date of the project
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Set the ending date of the project
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * Add a phase to the project
	 * @param phase the phase to add
	 */
	public void addPhase(Phase phase){
		this.phases.add(phase);
	}
	
	/**
	 * Add a resource to the project
	 * @param resource the resource to add
	 */
	public void addResource(Resource resource) {
		this.resources.add(resource);
	}
	
	/**
	 * Add a skill to the project
	 * @param skill the skill to add
	 */
	public void addSkill(Skill skill){
		this.skills.add(skill);
	}

	/**
	 * Set the result calculated with the risk factor
	 * @param resultRisk the resultRisk to set
	 */
	public void setResultRisk(Result resultRisk) {
		this.resultRisk = resultRisk;
	}

	/**
	 * Set the result calculated without the risk factor
	 * @param resultWRisk the resultWRisk to set
	 */
	public void setResultWRisk(Result resultWRisk) {
		this.resultWRisk = resultWRisk;
	}
}
