package com.redhat.lightblue.client.projection;

public class FieldProjection implements Projection {
    private final String field;
    private final Boolean isIncluded;
    private final Boolean isRecursive;

    public FieldProjection(String field, Boolean isIncluded, Boolean isRecursive) {
        this.field = field;
        this.isIncluded = isIncluded;
        this.isRecursive = isRecursive;
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
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

    public static FieldProjection includeField(String field) {
        return new FieldProjection(field, true, false);
    }

    public static FieldProjection includeFieldRecursively(String field) {
        return new FieldProjection(field, true, true);
    }

    public static FieldProjection excludeField(String field) {
        return new FieldProjection(field, false, false);
    }
}
