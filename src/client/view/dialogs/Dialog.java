package client.view.dialogs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class Dialog {
	
	private String dialogTile;
	private String headerText;
	private String messageText;
	
	// no public instances of this class
	protected Dialog(String dialogTile, String headerText, String messageText){
		this.dialogTile = dialogTile;
		this.headerText = headerText;
		this.messageText = messageText;
	}
	
	
	// this method shows a confirmation dialog and returns true if "yes" is clicked
	private static boolean showConfirmationDialog(DialogConfirmation dialog){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(dialog.getDialogTile());
		alert.setHeaderText(dialog.getHeaderText());
		alert.setContentText(dialog.getMessageText());
		
		// build custom yes/no buttons
		ButtonType buttonYes = new ButtonType("Ja", ButtonData.NO);
		ButtonType buttonNo = new ButtonType("Nein", ButtonData.YES);
		alert.getButtonTypes().setAll(buttonYes, buttonNo);

		Optional<ButtonType> result = alert.showAndWait();
		if(result.get().equals(buttonYes))
			return true;
		else
			return false;
	}
	
	// this method shows a error dialog
	private static void showErrorDialog(DialogError dialog){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(dialog.getDialogTile());
		alert.setHeaderText(dialog.getHeaderText());
		alert.setContentText(dialog.getMessageText());

		Exception ex = dialog.getException();

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("Detailierte Fehlermeldung:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
	
	// this method shows a warning dialog
	private static void showWarningDialog(DialogWarning dialog){
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(dialog.getDialogTile());
		alert.setHeaderText(dialog.getHeaderText());
		alert.setContentText(dialog.getMessageText());

		alert.showAndWait();
	}
	
	// this method shows an information dialog
	private static void showInformationDialog(DialogInformation dialog){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(dialog.getDialogTile());
		alert.setHeaderText(dialog.getHeaderText());
		alert.setContentText(dialog.getMessageText());

		alert.showAndWait();
	}
	
	
	
	
	/**
	 * This method shows a dialog
	 * @param dialogType the type of the dialog
	 * @return (only for confirmation dialog) true if "yes" is clicked
	 */
	public static boolean showDialog(Dialog dialogType){
		if(dialogType instanceof DialogConfirmation)
			return showConfirmationDialog((DialogConfirmation) dialogType);
		else if(dialogType instanceof DialogError)
			showErrorDialog((DialogError) dialogType);
		else if(dialogType instanceof DialogWarning)
			showWarningDialog((DialogWarning) dialogType);
		else if(dialogType instanceof DialogInformation)
			showInformationDialog((DialogInformation) dialogType);
		
		return false;
	}


	
	
	/**
	 * Get the dialog title
	 * @return the dialogTile
	 */
	public String getDialogTile() {
		return dialogTile;
	}

	/**
	 * Get the header text
	 * @return the headerText
	 */
	public String getHeaderText() {
		return headerText;
	}

	/**
	 * Get the message text
	 * @return the messageText
	 */
	public String getMessageText() {
		return messageText;
	}
}
