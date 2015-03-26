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

import com.redhat.lightblue.client.util.JSON;

/**
 * created by Michael White 10/10/2014
 */
public class SetUpdate implements Update {
    private final PathValuePair[] pathValuePairs;

    public SetUpdate(PathValuePair... statements) {
        pathValuePairs = statements;
    }

    public SetUpdate(Collection<PathValuePair> statements) {
        if (statements != null) {
            pathValuePairs = statements.toArray(new PathValuePair[statements.size()]);
        } else {
            pathValuePairs = null;
        }
    }

    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder("{");
        json.append(JSON.toJson("$set")).append(":{");
        for (int index = 0; index < pathValuePairs.length; index++) {
            json.append(pathValuePairs[index].toJson());
            if ((this.pathValuePairs.length - index) > 1) {
                json.append(", ");
            }
        }
        json.append("}}");
        return json.toString();
    }
}
