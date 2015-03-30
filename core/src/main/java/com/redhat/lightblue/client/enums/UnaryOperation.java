package com.redhat.lightblue.client.enums;

import java.util.HashSet;
import java.util.Set;

public enum UnaryOperation {
    NOT("$not");

    private String operator;

    public String getOperator() {
        return operator;
    }

    private UnaryOperation(String expression) {
        this.operator = expression;
    }

    public static Set<String> getOperators() {
        Set<String> set = new HashSet<String>();
        for (UnaryOperation exp : values()) {
            set.add(exp.getOperator());
        }
        return set;
    }

    public static boolean contains(String operator) {
        for (UnaryOperation e : values()) {
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
