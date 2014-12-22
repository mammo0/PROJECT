package XMLService;

public class TestWrite {
	public static void main(String[] args) {
	
	    StaXWriter ProjectFile = new StaXWriter();
	    String Projektname = "Name des Projekt";
	    ProjectFile.setFile(""+Projektname+".xml");
	    try {
	      ProjectFile.saveConfig();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }

}
