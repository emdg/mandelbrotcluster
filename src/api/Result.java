package api;

import java.io.Serializable;

public abstract class Result<T>  implements Serializable {
	T value;
	private int seqNr;
	
	public Result(int seqNr, T value){
		this.seqNr = seqNr;
		this.value = value;
	}
	
	public T getValue(){
		return this.value;
	}
	public int getSeqNr(){
		return this.seqNr;
	}
	
}
