package com.redhat.lightblue.client.enums;

import java.util.HashSet;
import java.util.Set;

public enum ExpressionOperation {
    EQ("$eq"),
    NEQ("$neq"),
    GT("$gt"),
    LT("$lt"),
    GTE("$gte"),
    LTE("$lte"),
    EQUALS("="),
    NOT_EQUALS("!="),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    LESS_THAN_OR_EQUAL("<="),
    GREATER_THAN_OR_EQUAL(">=");

    private String operator;

    public String getOperator() {
        return operator;
    }

    private ExpressionOperation(String expression) {
        this.operator = expression;
    }

    public static Set<String> getOperators() {
        Set<String> set = new HashSet<String>();
        for (ExpressionOperation exp : values()) {
            set.add(exp.getOperator());
        }
        return set;
    }

    public static boolean contains(String operator) {
        for (ExpressionOperation e : values()) {
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
