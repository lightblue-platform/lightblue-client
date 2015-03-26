package com.redhat.lightblue.client.expression.update;

/**
 * created by Michael White 10/10/2014
 */
public class PathRValue implements RValue {
    private final String path;

    public PathRValue(String path) {
        this.path = path;
    }

    @Override
    public String toJson() {
        return "{$valueof:" + this.path + "}";
    }
}
