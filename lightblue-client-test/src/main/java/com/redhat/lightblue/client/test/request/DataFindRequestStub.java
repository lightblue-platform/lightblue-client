package com.redhat.lightblue.client.test.request;

import com.redhat.lightblue.client.request.data.DataFindRequest;

/**
 * Created by jblashka on 11/3/14.
 */
public class DataFindRequestStub extends DataFindRequest {
    private final String body;

    public DataFindRequestStub(String entityName, String entityVersion, String body) {
        super(entityName, entityVersion);
        this.body = body;
    }

    @Override
    public String getBody() {
        return body;
    }

}
