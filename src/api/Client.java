package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {	
	
	public void didReceiveResult(Result res) throws RemoteException;
	
	
	
}
