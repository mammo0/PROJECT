package model.project;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Hashtable;

/**
 * This class represents a phase of the project
 * 
 * @author Ammon
 *
 */
public class Phase implements Serializable {

	// the name of the phase
	private String phaseName;

	// set a parent phase if this phase is a sub phase (default is null)
	private Phase parent = null;

	// the start date of the phase
	private LocalDate startDate;

	// the end date of the phase
	private LocalDate endDate;

	// the risk factor for the phase
	private int riskFactor;

	// the required skills of the phase with the required duration
	private Hashtable<Integer, Integer> skills;

	/**
	 * Constructor
	 */
	public Phase() {
		skills = new Hashtable<Integer, Integer>();
	}

	/**
	 * Get the name of the phase
	 * 
	 * @return the phaseName
	 */
	public String getPhaseName() {
		return phaseName;
	}

	/**
	 * Get the parent of a sub phase (null if it is a main phase)
	 * 
	 * @return the parent
	 */
	public Phase getParent() {
		return parent;
	}

	/**
	 * Get the starting date of the phase
	 * 
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * Get the ending date of the phase
	 * 
	 * @return the endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * Get the risk factor of the phase
	 * 
	 * @return the riskFactor
	 */
	public int getRiskFactor() {
		return riskFactor;
	}

	/**
	 * Get the skills required by this phase with their duration
	 * 
	 * @return the skills
	 */
	public Hashtable<Integer, Integer> getSkills() {
		return skills;
	}

	/**
	 * Set the name of the phase
	 * 
	 * @param phaseName
	 *            the phaseName to set
	 */
	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	/**
	 * Set the parent of the sub phase
	 * 
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Phase parent) {
		this.parent = parent;
	}

	/**
	 * Set the starting date of the phase
	 * 
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * Set the ending date of the phase
	 * 
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * Set the risk factor of the phase
	 * 
	 * @param riskFactor
	 *            the riskFactor to set
	 */
	public void setRiskFactor(int riskFactor) {
		this.riskFactor = riskFactor;
	}

	/**
	 * Add a new skill to the phase with its duration
	 * 
	 * @param amount
	 *            the amount of that skill
	 * @param skill
	 *            the skill
	 */
	public void addSkill(int skillID, int amount) {
		this.skills.put(skillID, amount);
	}
}
