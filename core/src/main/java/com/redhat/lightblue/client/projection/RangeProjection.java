package com.redhat.lightblue.client.projection;

public class RangeProjection implements Projection {
    private final String field;
    private final Boolean isIncluded;
    private final Integer rangeFrom;
    private final Integer rangeTo;
    private final Projection projection;

    public RangeProjection(String field, Boolean isIncluded, Integer rangeFrom, Integer rangeTo, Projection projection) {
        this.field = field;
        this.isIncluded = isIncluded;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
        this.projection = projection;
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"field\":\"");
        sb.append(field);
        sb.append("\",");
        if (isIncluded != null) {
            sb.append("\"include\":");
            sb.append(isIncluded.toString());
            sb.append(",");
        }
        sb.append("\"range\":[");
        sb.append(rangeFrom);
        sb.append(",");
        sb.append(rangeTo);
        sb.append("],\"project\":");
        sb.append(projection.toJson());
        sb.append("}");
        return sb.toString();
    }
}
