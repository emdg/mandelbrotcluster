package system;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import api.RemoteComputer;
import api.Result;
import api.Space;
import api.Task;

public class Computer extends UnicastRemoteObject implements RemoteComputer {
	private Space space;
	public Computer(String spaceDomain) throws RemoteException, NotBoundException, MalformedURLException{
		String url = "rmi://" + spaceDomain + ":" + Space.PORT + "/" + Space.SERVICE_NAME;
		space = (Space) Naming.lookup( url );
		space.registerComputer(this);
	}
	@Override
	public Result handleTask(Task task) throws RemoteException {
		System.out.println(task);
		return task.execute();
	}
	
	
	
	
	
	

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException{
		
		System.setSecurityManager( new SecurityManager() );
        
        String domain;

        if(args.length == 0){
        	domain = "localhost";
        }
        else {
        	domain = args[0];
        }
        
        
        final Computer client = new Computer(domain);	
    }

	
	
	
}
