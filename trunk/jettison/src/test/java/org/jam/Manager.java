package org.jam;

import java.util.Collection;
import java.util.LinkedList;

public class Manager extends Employee {
	private Collection<Employee> people = new LinkedList<Employee>();
	
	public void addPerson(Employee p) {
		people.add(p);
	}
}
