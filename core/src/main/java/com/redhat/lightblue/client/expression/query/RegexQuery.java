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
package com.redhat.lightblue.client.expression.query;

/**
 * Created by bmiller on 10/10/14.
 */
public class RegexQuery implements Query {
    private final String fieldName;
    private final String pattern;
    private final Boolean isCaseInsensitive;
    private final Boolean isExtended;
    private final Boolean isMultiline;
    private final Boolean isDotAll;

    public RegexQuery(String fieldName, String pattern, Boolean isCaseInsensitive, Boolean isExtended, Boolean isMultiline, Boolean isDotAll) {
        this.fieldName = fieldName;
        this.pattern = pattern;
        this.isCaseInsensitive = isCaseInsensitive;
        this.isExtended = isExtended;
        this.isMultiline = isMultiline;
        this.isDotAll = isDotAll;
    }

    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder("{");
        json.append("\"field\":");
        json.append("\"").append(fieldName).append("\",");
        json.append("\"regex\":");
        json.append("\"").append(pattern).append("\",");
        json.append("\"caseInsensitive\":");
        json.append("\"").append(isCaseInsensitive).append("\",");
        json.append("\"extended\":");
        json.append("\"").append(isExtended).append("\",");
        json.append("\"multiline\":");
        json.append("\"").append(isMultiline).append("\",");
        json.append("\"dotall\":");
        json.append("\"").append(isDotAll).append("\"");
        json.append("}");

        return json.toString();
    }

    @Override
    public String toString() {
        return this.toJson();
    }
}
