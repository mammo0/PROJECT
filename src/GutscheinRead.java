import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class GutscheinRead {

	public static void main(String[] args) {
		String dateiname = "bin/vertSys/uebung2/Gutscheine.dat";
		
		Gutschein[] scheine = liesGutscheineAus(dateiname);
		
		System.out.println("Die Datei "+dateiname+" enthaelt "+scheine.length+" Gutschein(e).");
		
		for(Gutschein schein : scheine)
			System.out.println(schein.toString());
	}
	
	
	public static Gutschein[] liesGutscheineAus(String dateiname){
		try {
			// InputStreams aufbauen
			FileInputStream datIn = new FileInputStream(dateiname);
			ObjectInputStream oIn = new ObjectInputStream(datIn);
			
			// Array mit Gutscheinen initialisieren
			Gutschein[] scheine = new Gutschein[oIn.readInt()];
			
			// Gutscheine einlesen
			for(int i=0;i<scheine.length;i++)
				scheine[i] = (Gutschein) oIn.readObject();
			
			// Streams schließen
			oIn.close();
			datIn.close();
			
			// Gutscheine zurückgeben
			return scheine;
		} catch (Exception e) {
			System.out.println("I/O-Error!");
			e.printStackTrace();
			
			return null;
		}
	}
}