package system;


import java.rmi.RemoteException;
import java.util.concurrent.PriorityBlockingQueue;

import api.Result;
import api.Space;
import api.Task;
import api.RemoteComputer;

public class ComputerProxy implements Runnable{
	private RemoteComputer computer;
	private PriorityBlockingQueue<Task> taskList;
	private Space space;
	
	public ComputerProxy(RemoteComputer computer, PriorityBlockingQueue<Task> taskList, Space space){
		this.computer =  computer;
		this.taskList = taskList;
		this.space = space;
	}

	public void run(){
		while(true){
			if (!taskList.isEmpty()){
				Task task = null;	
					try {
						task = taskList.poll();
						if (task != null){
							Result res = computer.handleTask(task);
							space.sendResult(res);
						}
					}
					catch(RemoteException e){
						taskList.add(task);
						System.out.println(e);
						return;
					}
			}
		}
	}

}