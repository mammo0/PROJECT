package model.project;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents the project.
 * All inputs of the project are saved in this class and subclasses.
 * @author Ammon
 *
 */
public class Project implements Serializable {
	// the name of the project
	private String projectName;
	
	// the name of the project responsible
	private String projectResponsible;
	
	// a short description of the project
	private String description;
	
	// final saving of the project
	private boolean finished;
	
	// a list of the containing phases
	private ArrayList<Phase> phases;
	
	// a list of the containing skills
	private ArrayList<Skill> skills;
	
	// a list of the containing resources
	private ArrayList<Resource> resources;
	
	// the project starting date
	private LocalDate startDate;
	
	// the project end Date
	private LocalDate endDate;
	
	// the result of the project calculation without the risk factor
	private Result resultWRisk;
	
	//the time when the project was marked as finished
	private	LocalDateTime timestamp;
	
	
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}



	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}



	/**
	 * Constructor
	 */
	public Project(){
		phases = new ArrayList<Phase>();
		skills = new ArrayList<Skill>();
		resources = new ArrayList<Resource>();
	}

	

	/**
	 * Get the project name
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Get the responsible of this project
	 * @return the projectResponsible
	 */
	public String getProjectResponsible() {
		return projectResponsible;
	}
	
	/**
	 * Get the project description
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Get the final state of the project
	 * @return the finished
	 */
	public boolean isFinished(){
		return finished;
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
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * Get the ending date of the project
	 * @return the endDate
	 */
	public LocalDate getEndDate() {
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
	 * Set the project name
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * Set the responsible of this project
	 * @param projectResponsible the projectResponsible to set
	 */
	public void setProjectResponsible(String projectResponsible) {
		this.projectResponsible = projectResponsible;
	}
	
	/**
	 * Set the project description
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Mark the project as finished
	 * @param finished
	 */
	public void setFinished(boolean finished){
		this.finished = finished;
	}

	/**
	 * Set the starting date of the project
	 * @param startDate the startDate to set
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * Set the ending date of the project
	 * @param endDate the endDate to set
	 */
	public void setEndDate(LocalDate endDate) {
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
	 * Set the result calculated without the risk factor
	 * @param resultWRisk the resultWRisk to set
	 */
	public void setResultWRisk(Result resultWRisk) {
		this.resultWRisk = resultWRisk;
	}
}
