package com.redhat.lightblue.client;

public enum ArrOp {
    any("$any"),
    all("$all"),
    none("$none");

    private String s;
    
    private ArrOp(String s) {
        this.s=s;
    }

    public String toString() {
        return s;
    }
}
