import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GutscheinLieferant extends UnicastRemoteObject implements GutscheinService {

	protected GutscheinLieferant() throws RemoteException {
		super();
	}

	@Override
	public List<Gutschein> herMitdenGutscheinen(int kdnr) throws RemoteException {
		List<Gutschein> liste = new ArrayList<Gutschein>();
		
		Gutschein[] scheineAlle = GutscheinRead.liesGutscheineAus("Gutscheine.dat");
		
		for(Gutschein schein : scheineAlle)
			if(schein.getKdNr() == kdnr)
				liste.add(schein);
		
		return liste;
	}

}
