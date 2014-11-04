package com.redhat.lightblue.client.enums;

public enum ExpressionOperation {
	EQ, NEQ, GT, LT, GTE, LTE, EQUALS, NOT_EQUALS, LESS_THAN, GREATER_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN_OR_EQUAL;
	
	
	@Override
	public String toString(){
		switch(this) {
			case EQ: return "$eq";
			case NEQ: return "$neq";
			case GT: return "$gt";
			case LT: return "$lt";
			case GTE: return "$gte";
			case LTE: return "$lte";
			case EQUALS: return "=";
			case NOT_EQUALS: return "!=";
			case LESS_THAN: return "<";
			case GREATER_THAN: return ">";
			case LESS_THAN_OR_EQUAL: return "<=";
			case GREATER_THAN_OR_EQUAL: return ">=";
			default: throw new IllegalArgumentException(); 
		}
	}	
}
