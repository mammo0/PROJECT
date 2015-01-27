package client.core;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;

/**
 * This class is the model class for the pd result table
 * @author Ammon
 *
 */
public class PDTableModel {
	public SimpleStringProperty skillName = new SimpleStringProperty();
	public SimpleIntegerProperty pdShould = new SimpleIntegerProperty();
	public SimpleIntegerProperty pdIs = new SimpleIntegerProperty();
	public SimpleIntegerProperty pdIsInt = new SimpleIntegerProperty();
	public SimpleIntegerProperty pdIsExt = new SimpleIntegerProperty();
	public SimpleObjectProperty<TextField> pdReal = new SimpleObjectProperty<TextField>();
	
	
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
	public Object getPdReal() {
		return pdReal.get();
	}
}
