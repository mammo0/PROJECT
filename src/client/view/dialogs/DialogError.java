package client.view.dialogs;

public class DialogError extends Dialog {
	
	private Exception exception;
	
	public DialogError(String headerText, String messageText, Exception exception){
		super("Fehler", headerText, messageText);
		
		this.exception = exception;
	}

	
	
	/**
	 * Get the exception
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}
}
