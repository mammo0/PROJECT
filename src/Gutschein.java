import java.io.*;
import java.util.*;

public class Gutschein implements Serializable {
	private long nr; // Nummer des Gutscheins
	private String kdName; // Name des Gutschein-Besitzers
	private int kdNr; // Kundennummer des Gutschein-Besitzers
	private double wert; // Wert des Gutscheins in EUR
	private Calendar verfall; // Verfalls-Datum

	public Gutschein(long n, String kNa, int kNu, double w) {
		nr = n;
		kdName = kNa;
		wert = w;
		kdNr = kNu;
		verfall = Calendar.getInstance();
		if (wert > 100)
			verfall.add(Calendar.YEAR, 2);
		else if (wert > 10)
			verfall.add(Calendar.YEAR, 1);
		else
			verfall.add(Calendar.MONTH, 6);
	}

	public int getKdNr() {
		return kdNr;
	}

	public String toString() {
		return "Gutschein fuer " + kdName + " (Kd-Nr. " + kdNr + ")\n"
				+ "\tin Hoehe von " + wert + " EUR" + " gueltig bis "
				+ verfall.get(Calendar.DAY_OF_MONTH) + "."
				+ verfall.get(Calendar.MONTH) + "."
				+ verfall.get(Calendar.YEAR);
	}
}