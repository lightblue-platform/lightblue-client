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

public enum ExpressionOperation {
    EQ, NEQ, GT, LT, GTE, LTE, EQUALS, NOT_EQUALS, LESS_THAN, GREATER_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN_OR_EQUAL;

    @Override
    public String toString() {
        switch (this) {
            case EQ:
                return "$eq";
            case NEQ:
                return "$neq";
            case GT:
                return "$gt";
            case LT:
                return "$lt";
            case GTE:
                return "$gte";
            case LTE:
                return "$lte";
            case EQUALS:
                return "=";
            case NOT_EQUALS:
                return "!=";
            case LESS_THAN:
                return "<";
            case GREATER_THAN:
                return ">";
            case LESS_THAN_OR_EQUAL:
                return "<=";
            case GREATER_THAN_OR_EQUAL:
                return ">=";
            default:
                throw new IllegalArgumentException();
        }
    }
}
