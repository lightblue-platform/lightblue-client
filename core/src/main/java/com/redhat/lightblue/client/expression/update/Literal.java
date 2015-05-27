package com.redhat.lightblue.client.expression.update;

public class Literal implements RValue {
    private final Object value;

    public Literal(String value) {
        this.value = value;
    }

    public Literal(int value) {
        this.value = new Integer(value);
    }

    public Literal(double value) {
        this.value = new Double(value);
    }

    public Literal(float value) {
        this.value = new Float(value);
    }

    public Literal(long value) {
        this.value = new Long(value);
    }

    public Literal(Object value) {
        this.value = value;
    }

    @Override
    public String toJson() {
        if(value==null)
            return null;
        else if(value instanceof String)
            return "\""+value+"\"";
        else
            return value.toString();
    }
}
