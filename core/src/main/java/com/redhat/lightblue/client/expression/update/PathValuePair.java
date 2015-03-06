package com.redhat.lightblue.client.expression.update;

import com.redhat.lightblue.client.util.JSON;

/**
 * created by Michael White 10/10/2014
 */
public class PathValuePair {

    public final String path;
    public final RValue value;

    public PathValuePair( String path, RValue value ) {
        this.path = path;

        if(value == null){
            this.value = new NullRValue();
        }
        else{
            this.value = value;
        }
    }

    public String toJson() {
        StringBuffer builder = new StringBuffer();

        String valueJson = value.toJson();
        if(valueJson == null || valueJson.equalsIgnoreCase("null")){
            valueJson = NullRValue.getNullValueAsJson();
        }

        builder.append(JSON.toJson(path)).append(":").append(valueJson);

        return builder.toString();
    }

}