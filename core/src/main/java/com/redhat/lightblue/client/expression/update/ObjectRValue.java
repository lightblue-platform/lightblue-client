package com.redhat.lightblue.client.expression.update;

import com.redhat.lightblue.client.util.JSON;

@Deprecated
public class ObjectRValue implements RValue {

    private final String json;

    public ObjectRValue(Object o) {
        this.json = JSON.toJson(o);
    }

    @Override
    public String toJson() {
        return json;
    }
}
