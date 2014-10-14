package com.redhat.lightblue.client.projection;

import com.redhat.lightblue.client.expression.Expression;

public class ArrayProjection implements Projection {

    private String field;
    private Boolean isIncluded;
    private Expression expression;
    private Projection[] projection;

    public ArrayProjection( String field, Boolean isIncluded, Expression expression, Projection... projection ) {
        this.field = field;
        this.isIncluded = isIncluded;
        this.expression = expression;
        this.projection = projection;
    }

    public String toJson() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"field\":\"");
        sb.append(field);
        sb.append("\",");
        if (isIncluded!=null) {
            sb.append("\"include\":");
            sb.append(isIncluded.toString());
            sb.append(",");
        }
        sb.append("\"match\":");
        sb.append(expression.toJson());
        sb.append(",\"project\":[");
        for (int i=0;i<projection.length;i++){
            sb.append(projection[i].toJson());
            if (i<projection.length-1) {
                sb.append(",");
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    public static ArrayProjection includeSubfield(String field, Expression expression, Projection... projection){
        return new ArrayProjection(field, true, expression, projection);
    }
}
