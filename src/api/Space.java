package api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

public interface Space extends Remote {
	
	
	public static int PORT = 8001;
	public static String SERVICE_NAME = "Space";
	public PriorityBlockingQueue<Task> taskQueue = new PriorityBlockingQueue<Task>();
	
	
	
	public void put(Task t) throws RemoteException;
	public void putAll(List<Task> taskList) throws RemoteException;
	
	public void sendResult(Result res) throws RemoteException;
	
	
	public void registerComputer(RemoteComputer computer) throws RemoteException;

	
	public void registerClient(Client client) throws RemoteException;
	
	
	
}