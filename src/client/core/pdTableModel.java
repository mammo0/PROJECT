package client.core;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * This class is the model class for the pd result table
 * @author Ammon
 *
 */
public class pdTableModel {
	public SimpleStringProperty skillName = new SimpleStringProperty();
	public SimpleIntegerProperty pdShould = new SimpleIntegerProperty();
	public SimpleIntegerProperty pdIs = new SimpleIntegerProperty();
	public SimpleIntegerProperty pdIsInt = new SimpleIntegerProperty();
	public SimpleIntegerProperty pdIsExt = new SimpleIntegerProperty();
	
	
	public String getSkillName() {
		return skillName.get();
	}
	public Integer getPdShould() {
		return pdShould.get();
	}
	public Integer getPdIs() {
		return pdIs.get();
	}
	public Integer getPdIsInt() {
		return pdIsInt.get();
	}
	public Integer getPdIsExt() {
		return pdIsExt.get();
	}
}
