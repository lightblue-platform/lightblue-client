package com.redhat.lightblue.client.enums;

public enum NaryOperation {
	AND, OR, ALL, ANY;

	@Override
	public String toString(){
		switch(this) {
			case AND: return "$and";
			case OR: return "$or";
			case ALL: return "$all";
			case ANY: return "$any";
		    default: throw new IllegalArgumentException(); 
		}
	}	
	
	
}
