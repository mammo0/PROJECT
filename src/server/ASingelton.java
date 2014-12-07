package server;

import java.util.ArrayList;
import java.util.List;

/*
 * Abstract class implementing the singleton pattern
 */
public abstract class ASingelton {
	// the List that contains all instances of the child classes
	private static List<ASingelton> classList = new ArrayList<ASingelton>();

	/*
	 * This constructor is automatically called if a child object is initialized.
	 * It ensures that only one instance can be created.
	 */
    protected ASingelton() {
    	if(classList.isEmpty())
    		classList.add(this);
    	else{
    		boolean found = false;
	    	for(int i=0;i<classList.size();i++){
	    		if(classList.get(i).getClass().equals(this.getClass())){
	    			found = false;
	    			break;
	    		}else
	    			found = true;
	    	}
	    	if(found)
    			classList.add(this);
    	}
    }

    
    
    /*
     * This method returns the only instance of the class
     */
    @SuppressWarnings("unchecked")
	public static <T extends ASingelton>T getInstance(Class<T> c) {
    	for(ASingelton single : classList)
    		if(single.getClass().equals(c))
    			return (T) classList.get(classList.indexOf(single));
    	
    	// shouldn't happen
    	return null;
    }
}
