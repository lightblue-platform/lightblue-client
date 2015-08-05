package com.redhat.lightblue.client;

public enum LogOp {
    and("$and"),
    or("$or");

    private String s;
    
    private LogOp(String s) {
        this.s=s;
    }

    public String toString() {
        return s;
    }
}
