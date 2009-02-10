package org.jam.utils;

public class SetObjectInfo<T> {
	public DisjointInfo from;
	public T obj;
	
	public SetObjectInfo(DisjointInfo from, T obj) {
		this.from = from;
		this.obj = obj;
	}
}
