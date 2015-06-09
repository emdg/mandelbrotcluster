package api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteComputer extends Remote {
	public Result handleTask(Task task) throws RemoteException;
}
