import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface GutscheinService extends Remote {
	public List<Gutschein> herMitdenGutscheinen(int kdnr) throws RemoteException;
}
