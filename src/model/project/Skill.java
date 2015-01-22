package model.project;

import java.io.Serializable;

/**
 * This class describes an available skill
 * 
 * @author Ammon
 *
 */
public class Skill implements Serializable {
	// the name of the skill
	private String skillName;

	// the skillID calculated by the hash value of the skill name
	private int skillID;

	// the internal daily rate
	private float dayRateInt;

	// the external daily rate
	private float dayRateExt;

	// the result of the calculation per skill including the risk factor
	private Result result;

	/**
	 * Get the name of the skill
	 * 
	 * @return the skillName
	 */
	public String getSkillName() {
		return skillName;
	}

	/**
	 * Get the skill ID. It is based on the hash value of the skill name.
	 * Returns -1 if no name is given.
	 * 
	 * @return the skillID
	 */
	public int getSkillID() {
		if (skillName == null)
			return -1;
		else {
			skillID = skillName.hashCode();
			return skillID;
		}
	}

	/**
	 * Get the internal daily rate of the skill
	 * 
	 * @return the dayRateInt
	 */
	public float getDayRateInt() {
		return dayRateInt;
	}

	/**
	 * Get the external daily rate of the skill
	 * 
	 * @return the dayRateExt
	 */
	public float getDayRateExt() {
		return dayRateExt;
	}

	/**
	 * Get the calculated result including the risk factor
	 * 
	 * @return the resultRisk
	 */
	public Result getResult() {
		return result;
	}

	public void setSkillID(int skillID) {
		this.skillID = skillID;
	}

	/**
	 * Set the name of the Skill
	 * 
	 * @param skillName
	 *            the skillName to set
	 */
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	/**
	 * Set the internal daily rate of the skill
	 * 
	 * @param dayRateInt
	 *            the dayRate to set
	 */
	public void setDayRateInt(float dayRateInt) {
		this.dayRateInt = dayRateInt;
	}

	/**
	 * Set the external daily rate of the skill
	 * 
	 * @param dayRateExt
	 *            the dayRateExt to set
	 */
	public void setDayRateExt(float dayRateExt) {
		this.dayRateExt = dayRateExt;
	}

	/**
	 * Set the result calculated with the risk factor
	 * 
	 * @param resultRisk
	 *            the resultRisk to set
	 */
	public void setResult(Result result) {
		this.result = result;
	}
}
