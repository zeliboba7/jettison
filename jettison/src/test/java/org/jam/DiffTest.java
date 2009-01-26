package org.jam;

import java.util.Collection;
import java.util.UUID;

import org.junit.Test;

public class DiffTest {
	@Test
	public void testTraversal() throws Exception {
		Manager m1 = new Manager();
		m1.setFirstName("John");
		m1.setLastName("Doe");
		m1.setEmployeeId(UUID.randomUUID().toString());
		m1.setAddress("my address");
		
		String managerId = m1.getEmployeeId();
		
		Manager m2 = new Manager();
		m2.setFirstName("John");
		m2.setLastName("Doe");
		m2.setEmployeeId(managerId);
		m2.setAddress("MY address");
		
		Employee e = new Employee();
		e.setFirstName("Jane");
		e.setLastName("Doe");
		e.setEmployeeId(UUID.randomUUID().toString());
		e.setAddress("some address");
		
		m1.addPerson(e);
		
		String id = e.getEmployeeId();
		
		e = new Employee();
		e.setFirstName("Jane");
		e.setLastName("Doe");
		e.setEmployeeId(id);
		e.setAddress("some different address");
		
		m2.addPerson(e);
		
		Diff4J differ = new Diff4J();
		Collection<ChangeInfo> changes = differ.diff(m1, m2);
		
		printChanges(changes);
	}
	
	protected static void printChanges(Collection<ChangeInfo> changes) {
		for(ChangeInfo change : changes) {
			StringBuilder builder = new StringBuilder();
			switch(change.getChangeType()) {
			case ADD:
				builder.append("New value for field ")
					.append(change.getFieldName())
					.append(": ")
					.append(change.getTo().toString());
				break;
			case CHANGE:
				builder.append("Value changed for field ")
					.append(change.getFieldName())
					.append(" from ")
					.append(change.getFrom().toString())
					.append(" to ")
					.append(change.getTo().toString());
				break;
			case REMOVE:
				builder.append("Value removed for field ")
					.append(change.getFieldName())
					.append(": ")
					.append(change.getFrom().toString());
				break;
			}
			
			System.out.println(builder.toString());
		}
	}
}
