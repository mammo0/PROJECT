package model.project;

import java.io.Serializable;

/**
 * This class represents a year
 * 
 * @author Ammon
 *
 */
public class Year implements Serializable {

	// the date
	private int yearDate;
	// the first quarter
	private Quarter q1;
	// the second quarter
	private Quarter q2;
	// the third quarter
	private Quarter q3;
	// the fourth quarter
	private Quarter q4;

	/**
	 * Get the number of the year
	 * 
	 * @return the yearDate
	 */
	public int getYearDate() {
		return yearDate;
	}

	/**
	 * Get the first quarter of the year
	 * 
	 * @return the q1
	 */
	public Quarter getQ1() {
		return q1;
	}

	/**
	 * Get the second quarter of the year
	 * 
	 * @return the q2
	 */
	public Quarter getQ2() {
		return q2;
	}

	/**
	 * Get the third quarter of the year
	 * 
	 * @return the q3
	 */
	public Quarter getQ3() {
		return q3;
	}

	/**
	 * Get the fourth quarter of the year
	 * 
	 * @return the q4
	 */
	public Quarter getQ4() {
		return q4;
	}
	
	/**
	 * Get the quarter by its number
	 * @param quarter the quarter number
	 * @return the quarter
	 */
	public Quarter getQuarter(int quarter){
		switch (quarter) {
		case 1:
			return q1;
		case 2:
			return q2;
		case 3:
			return q3;
		case 4:
			return q4;
		default:
			return null;
		}
	}

	
	/**
	 * Set the number of the year
	 * 
	 * @param yearDate
	 *            the yearDate to set
	 */
	public void setYearDate(int yearDate) {
		this.yearDate = yearDate;
	}

	/**
	 * Set the first quarter of the year
	 * 
	 * @param q1
	 *            the q1 to set
	 */
	public void setQ1(Quarter q1) {
		this.q1 = q1;
	}

	/**
	 * Set the second quarter of the year
	 * 
	 * @param q2
	 *            the q2 to set
	 */
	public void setQ2(Quarter q2) {
		this.q2 = q2;
	}

	/**
	 * Set the third quarter of the year
	 * 
	 * @param q3
	 *            the q3 to set
	 */
	public void setQ3(Quarter q3) {
		this.q3 = q3;
	}

	/**
	 * Set the fourth quarter of the year
	 * 
	 * @param q4
	 *            the q4 to set
	 */
	public void setQ4(Quarter q4) {
		this.q4 = q4;
	}
}
