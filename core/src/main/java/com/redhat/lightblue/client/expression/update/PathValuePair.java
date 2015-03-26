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

import com.redhat.lightblue.client.util.JSON;

/**
 * created by Michael White 10/10/2014
 */
public class PathValuePair {
    public final String path;
    public final RValue value;

    public PathValuePair(String path, RValue value) {
        this.path = path;

        if (value == null) {
            this.value = new NullRValue();
        } else {
            this.value = value;
        }
    }

    public String toJson() {
        StringBuilder builder = new StringBuilder();

        String valueJson = value.toJson();
        if (valueJson == null || valueJson.equalsIgnoreCase("null")) {
            valueJson = NullRValue.getNullValueAsJson();
        }

        builder.append(JSON.toJson(path)).append(":").append(valueJson);

        return builder.toString();
    }
}
