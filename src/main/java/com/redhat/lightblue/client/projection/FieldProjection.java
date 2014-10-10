package com.redhat.lightblue.client.projection;

public class FieldProjection implements Projection {

    private String field;
    private Boolean isIncluded;
    private Boolean isRecursive;

    FieldProjection(String field, Boolean isIncluded, Boolean isRecursive) {
        this.field = field;
        this.isIncluded = isIncluded;
        this.isRecursive = isRecursive;

    }
    public String toJson() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"field\":\"");
        sb.append(field);
        sb.append("\"");
        if (isIncluded != null) {
            sb.append(",\"include\":");
            sb.append(isIncluded.toString());
        }
        if (isRecursive != null) {
            sb.append(",\"recursive\":");
            sb.append(isRecursive.toString());
        }
        sb.append("}");
        return sb.toString();
    }
}
