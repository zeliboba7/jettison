package org.jam;


public class Employee {
	private String firstName;
	private String lastName;
	private String employeeId;
	private String address;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	public int hashCode() {
		int result = 17;
		result = result*37 + (employeeId != null ? employeeId.hashCode() : 0);
		return result;
	}
	
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		if(!(obj instanceof Employee))
			return false;
		
		Employee e = (Employee) obj;
		
		return employeeId != null ? employeeId.equals(e.employeeId) : e.employeeId == null;
	}
	
	public String toString() {
		return "Name: " + firstName + " " + lastName + ". Id: " + employeeId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
