package com.redhat.lightblue.client.enums;

import java.util.HashSet;
import java.util.Set;

@Deprecated
public enum ArrayOperation {
    NONE("$none"),
    ALL("$all"),
    ANY("$any");

    private final String operator;

    public String getOperator() {
        return operator;
    }

    private ArrayOperation(String expression) {
        this.operator = expression;
    }

    public static Set<String> getOperators() {
        Set<String> set = new HashSet<>();
        for (ArrayOperation exp : values()) {
            set.add(exp.getOperator());
        }
        return set;
    }

    public static boolean contains(String operator) {
        for (ArrayOperation e : values()) {
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
