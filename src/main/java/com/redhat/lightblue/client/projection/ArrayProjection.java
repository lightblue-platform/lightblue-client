package com.redhat.lightblue.client.projection;

import com.redhat.lightblue.client.expression.Expression;

public class ArrayProjection implements Projection {

    private String field;
    private Boolean isIncluded;
    private Expression expression;
    private Projection projection;

    ArrayProjection( String field, Boolean isIncluded, Expression expression, Projection projection ) {
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
        sb.append(",\"project\":");
        sb.append(projection.toJson());
        sb.append("}");
        return sb.toString();
    }
}
