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

import java.util.Collection;

/**
 * created by Michael White 10/10/2014
 */
public class InsertUpdate implements Update {

    private final String path;
    private final Integer index;
    private final RValue[] expressions;

    public InsertUpdate(String path, Integer index, RValue... expressions) {
        this.path = path;
        this.index = index;
        this.expressions = expressions;
    }

    public InsertUpdate(String path, Integer index, Collection<RValue> expressions) {
        this.path = path;
        this.index = index;
        this.expressions = expressions.toArray(new RValue[expressions.size()]);
    }

    @Override
    public String toJson() {
        // http://jewzaam.gitbooks.io/lightblue-specifications/content/language_specification/update.html#array-updates
        StringBuilder json = new StringBuilder("{");
        json.append("$insert:{");
        json.append(this.path).append(".").append(this.index.toString()).append(":");
        if (expressions.length > 1) {
            json.append("[");
        }
        for (int i = 0; i < expressions.length; i++) {
            json.append(expressions[i].toJson());
            if ((this.expressions.length - i) > 1) {
                json.append(","); // append a comma
            }
        }
        if (expressions.length > 1) {
            json.append("]");
        }
        json.append("}");
        return json.toString();
    }

}
