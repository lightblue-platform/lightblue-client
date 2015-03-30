/*
 Copyright 2015 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
