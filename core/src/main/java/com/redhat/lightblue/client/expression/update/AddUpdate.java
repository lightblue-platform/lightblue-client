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
public class AddUpdate implements Update {
    private final PathValuePair[] statements;

    public AddUpdate(PathValuePair... statements) {
        this.statements = statements;
    }

    public AddUpdate(Collection<PathValuePair> statements) {
        this.statements = statements.toArray(new PathValuePair[statements.size()]);
    }

    @Override
    public String toJson() {
        StringBuilder builder = new StringBuilder();

        builder.append("{\"$add\":");
        for (int index = 0; index < this.statements.length; index++) {
            builder.append(this.statements[index].toJson());
            // if there's more than one element left...
            if ((this.statements.length - index) > 1) {
                builder.append(","); // append a comma
            }
        }
        builder.append("}");

        return builder.toString();
    }

}
