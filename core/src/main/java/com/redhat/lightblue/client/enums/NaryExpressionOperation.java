package com.redhat.lightblue.client.enums;

public enum NaryExpressionOperation {
    NOT_IN, IN, NIN;

    @Override
    public String toString() {
        switch (this) {
            case NOT_IN:
                return "$not_in";
            case IN:
                return "$in";
            case NIN:
                return "$nin";
            default:
                throw new IllegalArgumentException();
        }
    }

}
