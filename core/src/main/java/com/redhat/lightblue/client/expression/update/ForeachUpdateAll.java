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

/**
 * created by Michael White 10/10/2014
 */
public class ForeachUpdateAll implements Update {
    private final String path;
    private final Update update;

    public ForeachUpdateAll(String path, Update updateExpression) {
        this.path = path;
        this.update = updateExpression;
    }

    @Override
    public String toJson() {
        // http://jewzaam.gitbooks.io/lightblue-specifications/content/language_specification/update.html#examples-of-foreach
        StringBuilder json = new StringBuilder("{");
        json.append("$foreach:{");
        json.append(this.path);
        json.append(": $all, $update :");
        json.append(this.update.toJson());
        json.append("} }");
        return json.toString();
    }

}
