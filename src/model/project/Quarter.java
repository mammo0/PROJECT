package model.project;

import java.io.Serializable;

/**
 * This class represents a quarter in a year
 * 
 * @author Ammon
 *
 */
public class Quarter implements Serializable {

	// the internal costs
	private float costInt;
	// the external costs
	private float costExt;
	// the total Costs
	private float costTotal;

	// the internal people days
	private int pdInt;
	// the external people days
	private int pdExt;
	// the total people days
	private int pdTotal;

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
	 * Get the internal people days per quarter
	 * 
	 * @return the pdInt
	 */
	public int getPdInt() {
		return pdInt;
	}

	/**
	 * Get the external people days per quarter
	 * 
	 * @return the pdExt
	 */
	public int getPdExt() {
		return pdExt;
	}

	/**
	 * Get the total people days per quarter
	 * 
	 * @return the pdTotal
	 */
	public int getPdTotal() {
		return pdTotal;
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
	 * Set the internal people days per quarter
	 * 
	 * @param pdInt
	 *            the pdInt to set
	 */
	public void setPdInt(int pdInt) {
		this.pdInt = pdInt;
	}

	/**
	 * Set the external people days per quarter
	 * 
	 * @param pdExt
	 *            the pdExt to set
	 */
	public void setPdExt(int pdExt) {
		this.pdExt = pdExt;
	}

	/**
	 * Set the total people days per quarter
	 * 
	 * @param pdTotal
	 *            the pdTotal to set
	 */
	public void setPdTotal(int pdTotal) {
		this.pdTotal = pdTotal;
	}
}
