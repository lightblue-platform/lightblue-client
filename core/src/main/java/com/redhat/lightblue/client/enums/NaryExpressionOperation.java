package com.redhat.lightblue.client.enums;

import java.util.HashSet;
import java.util.Set;

public enum NaryExpressionOperation {
    NOT_IN("$not_in"),
    IN("$in"),
    NIN("$nin");

    private final String operator;

    public String getOperator() {
        return operator;
    }

    private NaryExpressionOperation(String operator) {
        this.operator = operator;
    }

    public static Set<String> getOperators() {
        Set<String> set = new HashSet<>();
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
