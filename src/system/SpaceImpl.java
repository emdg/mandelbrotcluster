package system;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import api.Client;
import api.RemoteComputer;
import api.Result;
import api.Space;
import api.Task;

public class SpaceImpl extends UnicastRemoteObject implements Space {
	private Client c;
	public SpaceImpl() throws RemoteException {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void put(Task t) throws RemoteException {
		taskQueue.add(t);
	}

	@Override
	public void putAll(List<Task> taskList) throws RemoteException {
		taskQueue.addAll(taskList);
	}
	
	
	@Override
	public void registerComputer(RemoteComputer computer) {
		System.out.println("got computer");
		new Thread(new ComputerProxy(computer, taskQueue, this)).run();
	}

	
	@Override
	public void sendResult(Result res) {
		try {
			c.didReceiveResult(res);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
    public static void main( final String[] args ) throws Exception
    {
    	System.out.println("running space");
        System.setSecurityManager( new SecurityManager() );
        LocateRegistry.createRegistry( Space.PORT )
                      .rebind(Space.SERVICE_NAME, new SpaceImpl() );
    }

	@Override
	public void registerClient(Client client) {
		System.out.println("received Client");
		c = client;
	}


}
