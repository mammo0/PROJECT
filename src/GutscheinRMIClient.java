import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.util.List;

public class GutscheinRMIClient {

	public static void main(String[] args) {
		try {
			GutscheinService gs = (GutscheinService) Naming.lookup("rmi://localhost:12345/Lieferant");
			
			System.out.println("Abfragen von Gutscheinen beim Gutschein-Server.");
			System.out.println();
			
			// Schleife
			while(true){
				
				// Stream
				BufferedReader ein = new BufferedReader(new InputStreamReader(System.in));
				
				System.out.print("Kunden-Nummer (-1 fuer Abbruch): ");
				int kdnr = Integer.parseInt(ein.readLine());
				
				if(kdnr == -1){
					return;
				}
				
				// 
				List<Gutschein> liste = gs.herMitdenGutscheinen(kdnr);
				
				System.out.println("Alle Gutscheine für Kundennummer "+kdnr);
				for(Gutschein schein : liste){
					System.out.println(schein.toString());
				}
				
				System.out.println();
				System.out.println();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}