import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

public class GutscheinClient {

	public static void main(String[] args) {
		try {
			System.out.println("Abfragen von Gutscheinen beim Gutschein-Server.");
			System.out.println();
			
			// Schleife
			while(true){
				Socket s = new Socket("localhost", 12345);
				
				// Streams
				DataOutputStream dataAus = new DataOutputStream(s.getOutputStream());
				ObjectInputStream oIn = new ObjectInputStream(s.getInputStream());
				BufferedReader ein = new BufferedReader(new InputStreamReader(System.in));
				
				System.out.print("Kunden-Nummer (-1 fuer Abbruch): ");
				int kdnr = Integer.parseInt(ein.readLine());
				
				// Kundennummer an den Server schicken
				dataAus.writeInt(kdnr);
				
				if(kdnr == -1){
					s.close();
					return;
				}
				
				System.out.println("Alle Gutscheine für Kundennummer "+kdnr);
				while(true){
					try{
						System.out.println(((Gutschein) oIn.readObject()).toString());
					}catch (Exception e) {
						s.close();
						break;
					}
				}
				System.out.println();
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}