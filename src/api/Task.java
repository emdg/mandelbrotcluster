package api;

import java.io.Serializable;

public interface Task extends Serializable {
	//public int priority = Priority.VERY_LOW;
	Result execute();
	public int priority();
}
