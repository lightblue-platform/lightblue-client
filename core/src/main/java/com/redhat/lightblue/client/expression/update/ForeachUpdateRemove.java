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
package com.redhat.lightblue.client.expression.update;

import com.redhat.lightblue.client.expression.query.Query;

/**
 * created by Michael White 10/10/2014
 */
public class ForeachUpdateRemove implements Update {
    private final String path;
    private final Query queryExpression;

    public ForeachUpdateRemove(String path, Query queryExpression) {
        this.path = path;
        this.queryExpression = queryExpression;
    }

    @Override
    public String toJson() {
        // http://jewzaam.gitbooks.io/lightblue-specifications/content/language_specification/update.html#examples-of-foreach
        StringBuilder json = new StringBuilder("{");
        json.append("\"$foreach\":{");
        json.append("\"").append(this.path).append("\"");
        json.append(":");
        json.append(this.queryExpression.toJson());
        json.append(", \"$update\" : \"$remove\" } }");
        return json.toString();
    }

}
