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
package com.redhat.lightblue.client.enums;

import java.util.HashSet;
import java.util.Set;

public enum NaryExpressionOperation {
    NOT_IN("$not_in"),
    IN("$in"),
    NIN("$nin");

    private String operator;

    public String getOperator() {
        return operator;
    }

    private NaryExpressionOperation(String operator) {
        this.operator = operator;
    }

    public static Set<String> getOperators() {
        Set<String> set = new HashSet<String>();
        for (NaryExpressionOperation exp : values()) {
            set.add(exp.getOperator());
        }
        return set;
    }

    public static boolean contains(String operator) {
        for (NaryExpressionOperation e : values()) {
            if (e.getOperator().equalsIgnoreCase(operator)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return getOperator();
    }

}
