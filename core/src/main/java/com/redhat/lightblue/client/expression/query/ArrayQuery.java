package com.redhat.lightblue.client.expression.query;

import com.redhat.lightblue.client.enums.ArrayOperation;

public class ArrayQuery implements Query {

	   String fieldName;
	   ArrayOperation arrayOperation;
	   String[] array;
	   Query queryExpression;
	    public ArrayQuery(String fieldName, Query queryExpression)
	    {
	     this.fieldName = fieldName;
	     this.queryExpression = queryExpression;
	     
	    }  
	  
	    ArrayQuery(String fieldName, ArrayOperation operation, String... values) {
	      this.fieldName = fieldName;
	      this.arrayOperation = operation;
	      this.array = values;
	    }
	
	@Override
	public String toJson() {
		StringBuilder builder = new StringBuilder("{");
		if(queryExpression !=null) {
			builder.append("\"array\":\""+fieldName+"\",");
			builder.append("\"elemMatch\":");
			builder.append(queryExpression.toJson());
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

    public static ArrayQuery withSubfield(String fieldName, Query queryExpression){
        return new ArrayQuery(fieldName, queryExpression);
    }

}
