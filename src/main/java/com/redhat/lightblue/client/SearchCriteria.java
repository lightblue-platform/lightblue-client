package com.redhat.lightblue.client;

public class SearchCriteria {
	private String field;
	private SearchOperation operation;
	private Object criterion;
	
	public SearchCriteria(String field, SearchOperation operation, Object criterion) {
		this.field = field;
		this.operation = operation;
		this.criterion = criterion;
	}
	
	public String getField() {
		return this.field;
	}
	
	public SearchOperation getOperation() {
		return this.getOperation();
	}
	
	public Object getCriterion() {
		return this.criterion;
	}

}
