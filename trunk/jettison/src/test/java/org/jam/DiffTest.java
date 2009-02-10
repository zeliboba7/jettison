package org.jam;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

import org.junit.Test;

import static org.junit.Assert.*;

public class DiffTest {
	
	private static final Random r = new Random(System.nanoTime());
	private static char[] alphaNums;
	
	static {
		alphaNums = new char[62];
		
		StringBuilder builder = new StringBuilder();
		for(char c = 'a'; c <= 'z'; ++c)
			builder.append(c);
		for(char c = 'A'; c <= 'Z'; ++c)
			builder.append(c);
		for(int i = 0; i < 10; ++i)
			builder.append(Integer.toString(i));
		alphaNums = builder.toString().toCharArray();
	}
	
	private static Manager newManager() {
		Manager m1 = new Manager();
		m1.setFirstName("John");
		m1.setLastName("Doe");
		m1.setEmployeeId(UUID.randomUUID().toString());
		m1.setAddress("my address");
		return m1;
	}
	
	private static Employee newEmployee() {
		Employee e = new Employee();
		e.setFirstName("Jane");
		e.setLastName("Doe");
		e.setEmployeeId(UUID.randomUUID().toString());
		e.setAddress("some address");
		return e;
	}
	
	private static String randomString(int len) {
		StringBuilder builder = new StringBuilder();
		while(--len >= 0) {
			builder.append(alphaNums[r.nextInt(alphaNums.length)]);
		}
		return builder.toString();
	}
	
	@Test
	public void testSimpleProperties() throws Exception {
		Manager m1 = newManager();
		Manager m2 = newManager();
		
		m2.setEmployeeId(m1.getEmployeeId());
		
		m1.setAddress(randomString(20));
		
		Diff4J differ = new Diff4J();
		Collection<ChangeInfo> changes = differ.diff(m1, m2);
		
		assertEquals(1, changes.size());
	}
	
	@Test
	public void testCollectionSimple() throws Exception {
		Manager m1 = newManager();
		Manager m2 = newManager();
		m2.setEmployeeId(m1.getEmployeeId());
		m2.setAddress("MY address");
		
		Employee e1 = newEmployee();
		Employee e2 = newEmployee();
		e2.setEmployeeId(e1.getEmployeeId());
		e2.setAddress(randomString(20));
		
		m1.addPerson(e1);
		m2.addPerson(e2);
		
		Diff4J differ = new Diff4J();
		Collection<ChangeInfo> changes = differ.diff(m1, m2);
		
		assertEquals(2, changes.size());
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
