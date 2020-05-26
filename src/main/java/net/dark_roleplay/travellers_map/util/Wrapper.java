package net.dark_roleplay.travellers_map.util;

public class Wrapper<T> {

	private T value;

	public Wrapper(T value){
		this.value = value;
	}

	public T get(){
		return this.value;
	}

	public void set(T value){
		this.value = value;
	}
}
