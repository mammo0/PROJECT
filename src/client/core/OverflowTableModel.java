package client.core;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This class is the model class for the overflow result table
 * @author Ammon
 *
 */
public class OverflowTableModel {
	public SimpleStringProperty skillName = new SimpleStringProperty();
	public SimpleIntegerProperty pdOverflow = new SimpleIntegerProperty();
	
	public String getSkillName() {
		return skillName.get();
	}
	public Integer getPdOverflow() {
		return pdOverflow.get();
	}
}
