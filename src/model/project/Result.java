package model.project;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class and it subclasses contain all results of the project calculation
 * 
 * @author Ammon
 *
 */
public class Result implements Serializable {

	// the total people days
	private int pdIntBe;
	// the total people Ext days
	private int pdExtBe;
	// the total people days that are there
	private int pdTotalBe;
	// the total people days that should be there
	private int pdTotalShould;
	// the total people days that should be there calculated with Risk
	private int pdTotalShouldRisk;
	// the difference between the should and be people days
	private int pdTotalDiff;
	// the difference between the should and the ext people days
	private int Puffer;
	// the internal cost;
	private float costInt;
	// the external costs
	private float costExt;
	// the total costs
	private float costTotal;
	// the results for every single year of the project duration
	private ArrayList<Year> years;

	public Result() {
		years = new ArrayList<Year>();
	}

	/**
	 * Get the total mandays with Risk
	 * 
	 * @return the pdTotalShouldRisk
	 */
	public int getPdTotalShouldRisk() {
		return pdTotalShouldRisk;
	}

	/**
	 * Get the Extern Overtime
	 * 
	 * @return the Puffer
	 */
	public int getPuffer() {
		return Puffer;
	}

	/**
	 * Get the mandays
	 * 
	 * @return the pdIntBe
	 */
	public int getPdInt() {
		return pdIntBe;
	}

	/**
	 * Get the extern mandays
	 * 
	 * @return the pdExt
	 */
	public int getPdExt() {
		return pdExtBe;
	}

	/**
	 * Get the difference of the should and be people days
	 * 
	 * @return the pdTotalDiff
	 */
	public int getPdTotalDiff() {
		return pdTotalDiff;
	}

	/**
	 * Get the total people days that should be there
	 * 
	 * @return the pdTotalShould
	 */
	public int getPdTotalShould() {
		return pdTotalShould;
	}

	/**
	 * Get the total people days that are there
	 * 
	 * @return the pdTotalBe
	 */
	public int getPdTotalBe() {
		return pdTotalBe;
	}

	/**
	 * Get the internal costs per quarter
	 * 
	 * @return the costInt
	 */
	public float getCostInt() {
		return costInt;
	}

	/**
	 * Get the external costs per quarter
	 * 
	 * @return the costExt
	 */
	public float getCostExt() {
		return costExt;
	}

	/**
	 * Get the total costs per quarter
	 * 
	 * @return the costTotal
	 */
	public float getCostTotal() {
		return costTotal;
	}

	/**
	 * Get the years of the project with their quarter results
	 * 
	 * @return the years
	 */
	public ArrayList<Year> getYears() {
		return years;
	}

	/**
	 * Set the total Should Days with Risk
	 * 
	 * @param pdTotalDiff
	 *            the pdTotal to set
	 */

	public void setPdTotalShouldRisk(int pdTotalShouldRisk) {
		this.pdTotalShouldRisk = pdTotalShouldRisk;
	}

	/**
	 * Set the extern Puffer
	 * 
	 * @param pdTotalDiff
	 *            the pdTotal to set
	 */

	public void setPuffer(int puffer) {
		Puffer = puffer;
	}

	/**
	 * Set the intern mandays
	 * 
	 * @param pdTotalDiff
	 *            the pdTotal to set
	 */

	public void setPdIntBe(int pdInt) {
		this.pdIntBe = pdInt;
	}

	/**
	 * Set the extern mandays
	 * 
	 * @param pdTotalDiff
	 *            the pdTotal to set
	 */

	public void setPdExtBe(int pdExt) {
		this.pdExtBe = pdExt;
	}

	/**
	 * Set the difference of the should and be people days
	 * 
	 * @param pdTotalDiff
	 *            the pdTotal to set
	 */
	public void setPdTotalDiff(int pdTotalDiff) {
		this.pdTotalDiff = pdTotalDiff;
	}

	/**
	 * Set the total people days that should be there
	 * 
	 * @param pdTotalShould
	 *            the pdTotalShould to set
	 */
	public void setPdTotalShould(int pdTotalShould) {
		this.pdTotalShould = pdTotalShould;
	}

	/**
	 * Set the total people days that are there
	 * 
	 * @param pdTotalBe
	 *            the pdTotalBe to set
	 */
	public void setPdTotalBe(int pdTotalBe) {
		this.pdTotalBe = pdTotalBe;
	}

	/**
	 * Set the internal costs per quarter
	 * 
	 * @param costInt
	 *            the costInt to set
	 */
	public void setCostInt(float costInt) {
		this.costInt = costInt;
	}

	/**
	 * Set the external costs per quarter
	 * 
	 * @param costExt
	 *            the costExt to set
	 */
	public void setCostExt(float costExt) {
		this.costExt = costExt;
	}

	/**
	 * Set the total costs per quarter
	 * 
	 * @param costTotal
	 *            the costTotal to set
	 */
	public void setCostTotal(float costTotal) {
		this.costTotal = costTotal;
	}

	/**
	 * Add a year with the quarter splitted results to the project result
	 * 
	 * @param years
	 *            the years to set
	 */
	public void addYear(Year year) {
		this.years.add(year);
	}
}
