package client.view.components;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class BlockableButton extends Button {
	private static boolean blocked;
	
	private static ArrayList<BlockableButton> buttons;
	
	static{
		buttons = new ArrayList<BlockableButton>();
	}
	
	public BlockableButton(){
		this("");
	}
	
	public BlockableButton(String text){
		this(text, null);
	}
	
	public BlockableButton(String text, Node graphic){
		super(text, graphic);
		buttons.add(this);
	}
	
	
	// this method removes all blockable buttons from the list, which are not shown on the screen any more
	private static void checkButtonList(){
		for(int i=0;i<buttons.size();i++){
			BlockableButton btn = buttons.get(i);
			
			// check if the property has a parent parent
			if(btn.parentProperty().get().parentProperty().get() == null){
				buttons.remove(btn);
				i--;
			}
		}
	}
	
	
	/**
	 * @return the blocked
	 */
	public static boolean isBlocked() {
		return blocked;
	}

	/**
	 * @param blocked the blocked to set
	 */
	public static void setBlocked(boolean blocked) {
		// check if some buttons has been removed from the screen
		checkButtonList();
		
		for(BlockableButton btn : buttons){
			btn.setMouseTransparent(blocked);
		}
		BlockableButton.blocked = blocked;
	}
}
