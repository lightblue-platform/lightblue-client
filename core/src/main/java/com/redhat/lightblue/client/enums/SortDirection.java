package com.redhat.lightblue.client.enums;

import java.util.HashSet;
import java.util.Set;

public enum SortDirection {
    ASC("$asc"),
    DESC("$desc"),
    ASCENDING("$asc"),
    DESCENDING("$desc");

    private final String operator;

    public String getOperator() {
        return operator;
    }

    private SortDirection(String expression) {
        this.operator = expression;
    }

    public static Set<String> getOperators() {
        Set<String> set = new HashSet<>();
        for (SortDirection exp : values()) {
            set.add(exp.getOperator());
        }
        return set;
    }

    public static boolean contains(String operator) {
        for (SortDirection e : values()) {
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
