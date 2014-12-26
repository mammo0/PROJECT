package XMLService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Benennung {
	
	private ArrayList BenoetigteSkillsPhase = new ArrayList();
	private ArrayList BenoetigtePTproSkillPhase = new ArrayList();
	private String projektname = "Name für das Projekt aus BenennungKLasse";
	private ArrayList Hauptphasen = new ArrayList();
	private String Hauptphasenname = "Name der Hauptphase aus Benennungsklasse";

   
	private Calendar Startdatum = new GregorianCalendar();
	private Calendar Enddatum = new GregorianCalendar();
	
	public void ArrayMitHauptphasenfuellen(){
		Hauptphasen.add("Phase0");
		Hauptphasen.add("Phase1");
		Hauptphasen.add("Phase2");
		Hauptphasen.add("Phase3");
		
	}
	
	
	
	public void datumgesamtprojektsetzen(){
	   Startdatum.set( 1997, Calendar.MARCH, 1, 0, 0, 0 );                      // erster Zeitpunkt
	   Enddatum.set( 1998, Calendar.APRIL, 2, 0, 0, 0 );                      // zweiter Zeitpunkt
	}
	
	public void BenoetigteSkillsPhasefuellen(){
		BenoetigteSkillsPhase.add("Javaentwickler");
		BenoetigteSkillsPhase.add("Cobolentwickler");
		System.out.println("Array gefüllt");
		
	}

	
	
	public void BenoetigtePTproSkillPhasefuellen(){
		BenoetigtePTproSkillPhase.add("3");
		BenoetigtePTproSkillPhase.add("4");
		System.out.println("Array BenoetigtePTproSkillPhase gefüllt");
		
	}

	public Calendar getStartdatum() {
		return Startdatum;
	}

	public void setStartdatum(Calendar startdatum) {
		Startdatum = startdatum;
	}

	public Calendar getEnddatum() {
		return Enddatum;
	}

	public void setEnddatum(Calendar enddatum) {
		Enddatum = enddatum;
	}

	public ArrayList getBenoetigtePTproSkillPhase() {
		return BenoetigtePTproSkillPhase;
	}

	public void setBenoetigtePTproSkillPhase(ArrayList benoetigtePTproSkillPhase) {
		BenoetigtePTproSkillPhase = benoetigtePTproSkillPhase;
	}

	public String getProjektname() {
		return projektname;
	}

	public void setProjektname(String projektname) {
		this.projektname = projektname;
	}

	public String getHauptphasenname() {
		return Hauptphasenname;
	}

	public void setHauptphasenname(String hauptphasenname) {
		Hauptphasenname = hauptphasenname;
	}

	public ArrayList getBenoetigteSkillsPhase() {
		return BenoetigteSkillsPhase;
	}

	public void setBenoetigteSkillsPhase(ArrayList BenoetigteSkillsPhase) {
		BenoetigteSkillsPhase = BenoetigteSkillsPhase;
	}

	public ArrayList getHauptphasen() {
		return Hauptphasen;
	}

	public void setHauptphasen(ArrayList hauptphasen) {
		Hauptphasen = hauptphasen;
	}

}
