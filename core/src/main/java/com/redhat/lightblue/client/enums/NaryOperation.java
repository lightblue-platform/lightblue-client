package com.redhat.lightblue.client.enums;

import java.util.HashSet;
import java.util.Set;

@Deprecated
public enum NaryOperation {
    AND("$and"),
    OR("$or"),
    ALL("$all"),
    ANY("$any");

    private final String operator;

    public String getOperator() {
        return operator;
    }

    private NaryOperation(String expression) {
        this.operator = expression;
    }

    public static Set<String> getOperators() {
        Set<String> set = new HashSet<>();
        for (NaryOperation exp : values()) {
            set.add(exp.getOperator());
        }
        return set;
    }

    public static boolean contains(String operator) {
        for (NaryOperation e : values()) {
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
