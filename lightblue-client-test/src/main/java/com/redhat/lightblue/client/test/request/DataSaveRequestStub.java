package com.redhat.lightblue.client.test.request;

import com.redhat.lightblue.client.request.data.DataSaveRequest;

/**
 * Created by jblashka on 11/3/14.
 */
public class DataSaveRequestStub extends DataSaveRequest {
    private final String body;

    public DataSaveRequestStub(String entityName, String entityVersion, String body) {
        super(entityName, entityVersion);
        this.body = body;
    }

    @Override
    public String getBody() {
        return body;
    }

}
