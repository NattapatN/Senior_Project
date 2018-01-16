package testLive;

import java.io.Serializable;

public class Data implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int index;
	byte[] pice;
	Data(byte[] pice,int index){
		this.pice = pice;
		this.index = index;
	}
	public void print(){
		System.out.println(index);
	}

}
