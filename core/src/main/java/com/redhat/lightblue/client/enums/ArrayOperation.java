package com.redhat.lightblue.client.enums;

public enum ArrayOperation {
	NONE, ALL, ANY;
	
	@Override
	public String toString(){
		switch(this) {
			case NONE: return "$none";
			case ALL: return "$all";
			case ANY: return "$any";
		    default: throw new IllegalArgumentException(); 
		}
	}	
	
	
}
