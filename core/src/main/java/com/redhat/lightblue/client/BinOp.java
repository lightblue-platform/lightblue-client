package com.redhat.lightblue.client;

public enum BinOp {
    eq("="),
    neq("!="),
    lt("<"),
    gt(">"),
    lte("<="),
    gte(">=");

    private String s;
    
    private BinOp(String s) {
        this.s=s;
    }

    public String toString() {
        return s;
    }
}
