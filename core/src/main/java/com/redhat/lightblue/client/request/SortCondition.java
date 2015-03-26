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
package com.redhat.lightblue.client.request;

import com.redhat.lightblue.client.enums.SortDirection;

/**
 * Created by bmiller and vkumar on 10/10/14.
 */
public class SortCondition {
    public final String field;
    public final SortDirection direction;

    public SortCondition(String field, SortDirection direction) {
        this.field = field;
        this.direction = direction;
    }

    public String toJson() {
        StringBuilder json = new StringBuilder("{");
        json.append("\"").append(field).append(("\":"));
        json.append("\"").append(direction.toString()).append(("\""));
        json.append("}");

        return json.toString();
    }
}
