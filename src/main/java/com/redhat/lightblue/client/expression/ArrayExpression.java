package com.redhat.lightblue.client.expression;

import com.redhat.lightblue.client.enums.ArrayOperation;

public class ArrayExpression implements Expression {

	   String fieldName;
	   ArrayOperation arrayOperation;
	   String[] array;
	   Expression expression;
	    public ArrayExpression( String fieldName, Expression expression ) 
	    {
	     this.fieldName = fieldName;
	     this.expression = expression;
	     
	    }  
	  
	    ArrayExpression( String fieldName, ArrayOperation operation, String... values ) {
	      this.fieldName = fieldName;
	      this.arrayOperation = operation;
	      this.array = values;
	    }
	
	@Override
	public String toJson() {
		StringBuilder builder = new StringBuilder("{");
		if(expression!=null) {
			builder.append("\"array\":\""+fieldName+"\",");
			builder.append("\"elemMatch\":");
			builder.append(expression.toJson());
		}
		else {
			builder.append("\"array\":\""+fieldName+"\",");
			builder.append("\"contains\":\""+arrayOperation.toString()+"\",");
			builder.append("\"values\":[");
			builder.append("");
			
			builder.append("\""+array[0]+"\"");
			
			if (array.length > 1) {
				for (int i = 1; i < array.length; i++) {
					builder.append(",\""+array[i]+"\"");
				}
			}

			builder.append("]");
			
		}
		builder.append("}");
		return builder.toString();
	}

}
