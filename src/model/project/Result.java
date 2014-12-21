package model.project;

import java.util.ArrayList;

/**
 * This class and it subclasses contain all results of the project calculation
 * @author Ammon
 *
 */
public class Result {
	// the internal people days
	private int pdInt;
	// the external people days
	private int pdExt;
	// the total people days
	private int pdTotal;
	// the total people days that should be there
	private int pdTotalShould;
	// the total people days that are there
	private int pdTotalBe;
	
	// the internal costs
	private float costInt;
	// the external costs
	private float costExt;
	// the total costs
	private float costTotal;
	
	// the results for every single year of the project duration
	private ArrayList<Year> years;
	
	

	/**
	 * Get the internal people days
	 * @return the pdInt
	 */
	public int getPdInt() {
		return pdInt;
	}
	
	/**
	 * Get the external people days
	 * @return the pdExt
	 */
	public int getPdExt() {
		return pdExt;
	}
	
	/**
	 * Get the total people days
	 * @return the pdTotal
	 */
	public int getPdTotal() {
		return pdTotal;
	}
	
	/**
	 * Get the total people days that should be there
	 * @return the pdTotalShould
	 */
	public int getPdTotalShould() {
		return pdTotalShould;
	}
	
	/**
	 * Get the total people days that are there
	 * @return the pdTotalBe
	 */
	public int getPdTotalBe() {
		return pdTotalBe;
	}
	
	/**
	 * Get the internal costs per quarter
	 * @return the costInt
	 */
	public float getCostInt() {
		return costInt;
	}
	
	/**
	 * Get the external costs per quarter
	 * @return the costExt
	 */
	public float getCostExt() {
		return costExt;
	}
	
	/**
	 * Get the total costs per quarter
	 * @return the costTotal
	 */
	public float getCostTotal() {
		return costTotal;
	}
	
	/**
	 * Get the years of the project with their quarter results
	 * @return the years
	 */
	public ArrayList<Year> getYears() {
		return years;
	}
	
	
	
	/**
	 * Set the internal people days
	 * @param pdInt the pdInt to set
	 */
	public void setPdInt(int pdInt) {
		this.pdInt = pdInt;
	}
	
	/**
	 * Set the external people days
	 * @param pdExt the pdExt to set
	 */
	public void setPdExt(int pdExt) {
		this.pdExt = pdExt;
	}
	
	/**
	 * Set the total people days
	 * @param pdTotal the pdTotal to set
	 */
	public void setPdTotal(int pdTotal) {
		this.pdTotal = pdTotal;
	}
	
	/**
	 * Set the total people days that should be there
	 * @param pdTotalShould the pdTotalShould to set
	 */
	public void setPdTotalShould(int pdTotalShould) {
		this.pdTotalShould = pdTotalShould;
	}
	
	/**
	 * Set the total people days that are there
	 * @param pdTotalBe the pdTotalBe to set
	 */
	public void setPdTotalBe(int pdTotalBe) {
		this.pdTotalBe = pdTotalBe;
	}
	
	/**
	 * Set the internal costs per quarter
	 * @param costInt the costInt to set
	 */
	public void setCostInt(float costInt) {
		this.costInt = costInt;
	}
	
	/**
	 * Set the external costs per quarter
	 * @param costExt the costExt to set
	 */
	public void setCostExt(float costExt) {
		this.costExt = costExt;
	}
	
	/**
	 * Set the total costs per quarter
	 * @param costTotal the costTotal to set
	 */
	public void setCostTotal(float costTotal) {
		this.costTotal = costTotal;
	}

	/**
	 * Add a year with the quarter splitted results to the project result
	 * @param years the years to set
	 */
	public void addYear(Year year) {
		this.years.add(year);
	}
}
