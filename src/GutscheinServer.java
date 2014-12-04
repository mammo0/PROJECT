import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GutscheinServer {
	
	
	public static void main(String[] args) {
		try {
			ServerSocket sSocket = new ServerSocket(12345);
			
			System.out.println("Server laeuft.");
			
			while(true){
				Socket s = sSocket.accept();
				new Dienst(s).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	static class Dienst extends Thread{
		
		Socket s;
		
		DataInputStream dataIn;
		ObjectOutputStream oAus;
		
		
		public Dienst(Socket s){
			try {
				this.s = s;
				dataIn = new DataInputStream(s.getInputStream());
				oAus = new ObjectOutputStream(s.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		public void run(){
			try {
				int kdnr = dataIn.readInt();
				
				if(kdnr == -1){
					s.close();
					return;
				}
				
				Gutschein[] scheineAlle = GutscheinRead.liesGutscheineAus("src/Gutscheine.dat");
				
				for(Gutschein schein : scheineAlle)
					if(schein.getKdNr() == kdnr)
						oAus.writeObject(schein);
				
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}