package org.jam;

public class ChangeInfo {
	private Object parentLeft;
	private Object parentRight;
	
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

	public Object getParentLeft() {
		return parentLeft;
	}

	public void setParentLeft(Object parentLeft) {
		this.parentLeft = parentLeft;
	}

	public Object getParentRight() {
		return parentRight;
	}

	public void setParentRight(Object parentRight) {
		this.parentRight = parentRight;
	}



}
