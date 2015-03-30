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
