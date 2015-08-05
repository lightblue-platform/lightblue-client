package com.redhat.lightblue.client;

public enum NaryOp {
    in("$in"),
    nin("$nin");

    private String s;
    
    private NaryOp(String s) {
        this.s=s;
    }

    public String toString() {
        return s;
    }
}
