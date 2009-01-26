package org.jam;

public class ChangeInfo {
	private Class<?> enclosingType;
	
	private Object from;
	private Object to;
	
	private ChangeType changeType;

	private String fieldName;

	public Object getFrom() {
		return from;
	}

	public void setFrom(Object from) {
		this.from = from;
	}

	public Object getTo() {
		return to;
	}

	public void setTo(Object to) {
		this.to = to;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	public void setFieldName(String name) {
		this.fieldName = name;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Class<?> getEnclosingType() {
		return enclosingType;
	}

	public void setEnclosingType(Class<?> enclosingType) {
		this.enclosingType = enclosingType;
	}

}
