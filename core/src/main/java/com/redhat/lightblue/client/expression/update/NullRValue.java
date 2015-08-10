package com.redhat.lightblue.client.expression.update;

import com.redhat.lightblue.client.util.JSON;

/**
 * created by Michael White 10/10/2014
 */
@Deprecated
public class NullRValue implements RValue {
    public final static String NULL = "$null";

    public static String getNullValueAsJson() {
        return JSON.toJson(NULL);
    }

    @Override
    public String toJson() {
        return getNullValueAsJson();
    }
}
