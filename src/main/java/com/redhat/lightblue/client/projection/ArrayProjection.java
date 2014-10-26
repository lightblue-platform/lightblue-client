package com.redhat.lightblue.client.projection;

import com.redhat.lightblue.client.query.QueryExpression;

public class ArrayProjection implements Projection {

    private String field;
    private Boolean isIncluded;
    private QueryExpression queryExpression;
    private Projection[] projection;

    public ArrayProjection( String field, Boolean isIncluded, QueryExpression queryExpression, Projection... projection ) {
        this.field = field;
        this.isIncluded = isIncluded;
        this.queryExpression = queryExpression;
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
        sb.append(queryExpression.toJson());
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

    public static ArrayProjection includeSubfield(String field, QueryExpression queryExpression, Projection... projection){
        return new ArrayProjection(field, true, queryExpression, projection);
    }
}
