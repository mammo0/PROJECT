package model.project;

/**
 * This class describes an available skill
 * @author Ammon
 *
 */
public class Skill {
	// the name of the skill
	private String skillName;
	
	// the daily rate
	private float dayRate;
	
	// flag if the skill is internal or not
	private boolean intern;

	
	
	/**
	 * Get the name of the skill
	 * @return the skillName
	 */
	public String getSkillName() {
		return skillName;
	}

	/**
	 * Get the daily rate of the skill
	 * @return the dayRate
	 */
	public float getDayRate() {
		return dayRate;
	}

	/**
	 * Return if the skill is internal or external
	 * @return the intern
	 */
	public boolean isIntern() {
		return intern;
	}

	/**
	 * Set the name of the Skill
	 * @param skillName the skillName to set
	 */
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	/**
	 * Set the daily rate of the skill
	 * @param dayRate the dayRate to set
	 */
	public void setDayRate(float dayRate) {
		this.dayRate = dayRate;
	}

	/**
	 * Define if the skill is internal or not
	 * @param intern the intern to set
	 */
	public void setIntern(boolean intern) {
		this.intern = intern;
	}
}
