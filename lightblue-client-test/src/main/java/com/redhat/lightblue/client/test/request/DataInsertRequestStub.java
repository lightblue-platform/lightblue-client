package com.redhat.lightblue.client.test.request;

import com.redhat.lightblue.client.request.data.DataInsertRequest;

/**
 * Created by jblashka on 10/20/14.
 */
public class DataInsertRequestStub extends DataInsertRequest {
    private final String body;

    public DataInsertRequestStub(String entityName, String entityVersion, String body) {
        super(entityName, entityVersion);
        this.body = body;
    }

    @Override
    public String getBody() {
        return body;
    }

}
