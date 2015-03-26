package com.redhat.lightblue.client.enums;

public enum UnaryOperation {
    NOT;

    @Override
    public String toString() {
        switch (this) {
            case NOT:
                return "$not";
            default:
                throw new IllegalArgumentException();
        }
    }

}
