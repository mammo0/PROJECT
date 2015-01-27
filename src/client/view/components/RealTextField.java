package client.view.components;

import java.util.ArrayList;

import client.view.ITester;
import client.view.InputTester;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 * This class 
 * @author Ammon
 *
 */
public class RealTextField extends TextField implements ITester{
	
	private boolean summarize;
	
	public static ArrayList<RealTextField> textFields;
	
	static{
		textFields = new ArrayList<RealTextField>();
	}
	
	
	
	public RealTextField(){
		this(false);
	}
	
	public RealTextField(boolean summarize) {
		super();
		this.summarize = summarize;
		
		this.textProperty().addListener(new InputTester(this, this));
		
		if(!summarize){
			this.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					try{
						int sum = 0;
						RealTextField summarize = null;
						for(RealTextField field : textFields){
							if(!field.summarize){
								if(field.getText().isEmpty())
									sum += 0;
								else
									sum += Integer.parseInt(field.getText());
							}else
								summarize = field;
						}
						if(summarize != null){
							summarize.setText(String.valueOf(sum));
							summarize.setMouseTransparent(true);
							summarize.setStyle("-fx-background-color: lightgrey; -fx-focus-color: transparent;");
						}
					}catch (Exception e){}
				}
			});
		}else{
			setText("0");
			setMouseTransparent(true);
			setStyle("-fx-background-color: lightgrey; -fx-focus-color: transparent;");
		}
		
		
		if(!textFields.contains(this))
			textFields.add(this);
	}
	
	@Override
	public boolean checkInput(Node node) {
		if(((RealTextField) node).getText().isEmpty())
			return true;
		
		try{
			Integer.parseInt(((RealTextField) node).getText());
			return true;
		}catch (Exception e){
			return false;
		}
	}
}
