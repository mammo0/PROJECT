package client.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

public class InputTester implements ChangeListener<Object> {
	
	private ITester tester;
	private Node node;
	private IViewClient view;
	
	public InputTester(ITester tester, Node node) {
		this.tester = tester;
		this.node = node;
		this.view = View.getInstance(View.class);
	}
	
	@Override
	public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
		if(oldValue == null || !oldValue.equals(newValue)){
			if(tester.checkInput(node)){
				view.getViewRootPane().setMouseTransparent(false);
				node.setStyle("{-fx-background-color: white; -fx-accent: #0093ff; -fx-focus-color: #0093ff}");
				
			}else{
				view.getViewRootPane().setMouseTransparent(true);
				node.setStyle("{-fx-background-color: red; -fx-focus-color: red}");
			}
	    }
	}
}
