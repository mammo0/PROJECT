package client.core;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This class is the model class for the cost result table
 * @author Ammon
 *
 */
public class costTableModel {
	public SimpleStringProperty skillName = new SimpleStringProperty();
	public SimpleFloatProperty costTotal = new SimpleFloatProperty();
	public SimpleFloatProperty costInt = new SimpleFloatProperty();
	public SimpleFloatProperty costExt = new SimpleFloatProperty();
	
	
	public String getSkillName() {
		return skillName.get();
	}
	public Float getCostTotal() {
		return costTotal.get();
	}
	public Float getCostInt() {
		return costInt.get();
	}
	public Float getCostExt() {
		return costExt.get();
	}
}
