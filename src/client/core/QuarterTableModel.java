package client.core;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This class is the model class for the quarter result table
 * @author Ammon
 *
 */
public class QuarterTableModel {
	public SimpleStringProperty skillName = new SimpleStringProperty();
	public SimpleFloatProperty costInt = new SimpleFloatProperty();
	public SimpleFloatProperty costExt = new SimpleFloatProperty();
	public SimpleIntegerProperty pdInt = new SimpleIntegerProperty();
	public SimpleIntegerProperty pdExt = new SimpleIntegerProperty();
	
	
	public String getSkillName() {
		return skillName.get();
	}
	public Float getCostInt() {
		return costInt.get();
	}
	public Float getCostExt() {
		return costExt.get();
	}
	public Integer getPdInt() {
		return pdInt.get();
	}
	public Integer getPdExt() {
		return pdExt.get();
	}
}
