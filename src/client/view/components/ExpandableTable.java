package client.view.components;

import global.IExpandableNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * This class is a scroll pane with an expandable table in it.
 * The content of the table is set with the constructor.
 * 
 * @author Ammon
 *
 * @param <Content>
 */
public class ExpandableTable<Content extends Node> extends ScrollPane {
	
	private Class<Content> contentPane;
	
	private GridPane contentList;
	private Collection<ColumnConstraints> columnConstraints;
	private int contentListRowCount;
	
	private Button btnContentAdd;
	private Hashtable<Integer, Content> contentPanes;
	private Hashtable<Button, Integer> btnContentRemoves;
	
	private Node parent;
	
	
	
	/**
	 * The constructor
	 * @param contentPane the content class
	 */
	public ExpandableTable(Class<Content> contentPane, Node parent){
		this.contentPane = contentPane;
		this.parent = parent;
		
		this.setFitToWidth(true);
		
		contentList = new GridPane();
		
		// set the column constraints
		columnConstraints = new ArrayList<ColumnConstraints>();
		ColumnConstraints col1 = new ColumnConstraints();
	    col1.setPrefWidth(25d);
	    col1.setHgrow(Priority.NEVER);
	    col1.setHalignment(HPos.CENTER);
	    columnConstraints.add(col1);
	    ColumnConstraints col2 = new ColumnConstraints();
	    col2.setHgrow(Priority.ALWAYS);
	    col2.setHalignment(HPos.CENTER);
	    columnConstraints.add(col2);
	    ColumnConstraints col3 = new ColumnConstraints();
	    col3.setPrefWidth(25d);
	    col3.setHgrow(Priority.NEVER);
	    col3.setHalignment(HPos.CENTER);
	    columnConstraints.add(col3);
	    contentList.getColumnConstraints().addAll(columnConstraints);
		
		contentListRowCount = 0;
		
		contentPanes = new Hashtable<Integer, Content>();
		btnContentRemoves = new Hashtable<Button, Integer>();
		btnContentAdd = new Button("+");
		btnContentAdd.setOnAction(this::btnContentAddClick);
		contentListAddRow();
		
		this.setContent(contentList);
	}
	
	
	// this methods adds a row to the content view
	private void contentListAddRow(){
		Content newContent = null;
		try {
			newContent = contentPane.newInstance();
			((IExpandableNode) newContent).setParentNode(parent);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		contentPanes.put(contentListRowCount, newContent);
		
		Button btnContentRemove = new Button("-");
		btnContentRemove.setOnAction(this::btnContentRemoveClick);
		btnContentRemoves.put(btnContentRemove, contentListRowCount);
		
		// move the content add button one row down
		if(contentListRowCount > 0)
			contentList.getChildren().remove(btnContentAdd);
		contentList.add(btnContentAdd, 0, contentListRowCount);
		
		// add the content pane and the remove button
		contentList.add(newContent, 1, contentListRowCount);
		contentList.add(btnContentRemove, 2, contentListRowCount);
		
		// increase the row counter
		contentListRowCount++;
	}
		
	// this method is called by the add button
	private void btnContentAddClick(ActionEvent event){
		contentListAddRow();
	}
	
	// this method is called by the remove button
	@SuppressWarnings("unchecked")
	private void btnContentRemoveClick(ActionEvent event){
		int deleteRowIndex = btnContentRemoves.get(event.getSource());
		
		// build a new row only if one row is left
		if(contentListRowCount == 1){
			for(Node child : contentList.getChildren()){
				try{
					// call the remove method in the child
					((IExpandableNode) child).removeNode();
				}catch (Exception e){}
			}
			contentList.getChildren().clear();
			contentListRowCount--;
			contentListAddRow();
			return;
		}
		
		// initialize a new grid pane
		GridPane tempGrid = new GridPane();
		tempGrid.getColumnConstraints().addAll(columnConstraints);
		
		// clear the hashtables (will be rebuilt)
		btnContentRemoves.remove(event.getSource());
		contentPanes.clear();
		
		// special case on deleting the first line
		boolean deleteFirstRow = false;
		if (deleteRowIndex == 0)
			deleteFirstRow = true;
		
		// iterate over the old pane
		int childCounter = 0;
		int rowIndex = 0;
		int temp = 0;
		IExpandableNode removeChild = null;
		while(contentList.getChildren().size() > childCounter){
			Node child = contentList.getChildren().get(childCounter);
			
			if(deleteFirstRow){
				deleteFirstRow = false;
				rowIndex--;
			}
			
			// check if this row should be ignored
			if(GridPane.getRowIndex(child) != deleteRowIndex){
				if(temp != GridPane.getRowIndex(child)){
					temp = GridPane.getRowIndex(child);
					rowIndex++;
				}
				
				// add the child to the temporary grid pane
				tempGrid.add(child, GridPane.getColumnIndex(child), rowIndex);
				
				// rebuild the hashtables
				if(child.getClass().equals(contentPane)){
					contentPanes.put(rowIndex, (Content) child);
				}
				if(btnContentRemoves.containsKey(child)){
					btnContentRemoves.replace((Button) child, rowIndex);
				}
			}else{
				try{
					// call the remove method in the child
					removeChild = ((IExpandableNode) child);
				}catch (Exception e){}
				childCounter++;
			}
		}
		
		if(removeChild != null)
			removeChild.removeNode();
		
		// check if the last row should be removed
		if(deleteRowIndex == contentListRowCount-1){
			tempGrid.add(btnContentAdd, 0, deleteRowIndex-1);
		}
		
		// apply the temporary grid pane
		contentList = tempGrid;
		this.setContent(contentList);
		
		// decrease the row counter
		contentListRowCount--;
	}
	
	
	
	/**
	 * This method returns all entered contents
	 * @return the contents
	 */
	public ArrayList<Content> getContents(){
		return new ArrayList<Content>(contentPanes.values());
	}
}
