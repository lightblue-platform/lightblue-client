package com.redhat.lightblue.client.expression.update;

import java.util.Date;

import com.redhat.lightblue.client.util.ClientConstants;

@Deprecated
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
        else if(value instanceof Date)
            return "\""+ClientConstants.getDateFormat().format((Date)value)+"\"";
        else
            return value.toString();
    }
}
