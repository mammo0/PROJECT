import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class GutscheinRMIServer {
	 public static void main(String[] args) {
		 try {
			GutscheinLieferant gl = new GutscheinLieferant();
			
			LocateRegistry.createRegistry(12345);
			
			Naming.rebind("rmi://localhost:12345/Lieferant", gl);
			
			System.out.println("Server läuft.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
}